package com.phananh.e_commerce.authentication.application.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.concurrent.TimeUnit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.phananh.e_commerce.authentication.presentation.dto.request.AuthenticationRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.IntrospectRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.LogoutRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RefreshTokenRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RegisterRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.ResendOtpRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.VerifySmsRequest;
import com.phananh.e_commerce.authentication.application.dto.response.AuthTokenResponse;
import com.phananh.e_commerce.authentication.application.dto.response.IntrospectResponse;
import com.phananh.e_commerce.authentication.application.dto.response.LogoutResponse;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PasswordUtils;
import com.phananh.e_commerce.usermanagement.domain.model.Role;
import com.phananh.e_commerce.usermanagement.domain.model.UserCredentials;
import com.phananh.e_commerce.usermanagement.domain.model.User;
import com.phananh.e_commerce.usermanagement.domain.model.UserInfo;
import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataRoleRepository;
import com.phananh.e_commerce.usermanagement.infrastructure.persistence.repository.springdata.SpringDataUserRepository;
import com.phananh.e_commerce.authentication.application.service.AuthenticationService;
import com.phananh.e_commerce.authentication.domain.model.RefreshToken;
import com.phananh.e_commerce.authentication.infrastructure.persistence.repository.springdata.SpringDataRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String TOKEN_TYPE_BEARER = "Bearer";
	private static final String ACCESS_TYPE = "access";
	private static final String REFRESH_TYPE = "refresh";

	private final SpringDataUserRepository springDataUserRepository;
	private final SpringDataRoleRepository springDataRoleRepository;
	private final SpringDataRefreshTokenRepository springDataRefreshTokenRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final byte[] jwtSecret;
	private final long accessTokenExpirationSeconds;
	private final long refreshTokenExpirationSeconds;

	public AuthenticationServiceImpl(
			SpringDataUserRepository springDataUserRepository,
			SpringDataRoleRepository springDataRoleRepository,
			SpringDataRefreshTokenRepository springDataRefreshTokenRepository,
			RedisTemplate<String, Object> redisTemplate,
			@Value("${application.security.jwt.secret-key}") String jwtSecret,
			@Value("${application.security.jwt.expiration}") long accessTokenExpirationSeconds,
			@Value("${application.security.jwt.refresh-expiration}") long refreshTokenExpirationSeconds
	) {
		this.springDataUserRepository = springDataUserRepository;
		this.springDataRoleRepository = springDataRoleRepository;
		this.springDataRefreshTokenRepository = springDataRefreshTokenRepository;
		this.redisTemplate = redisTemplate;
		this.jwtSecret = jwtSecret.getBytes(StandardCharsets.UTF_8);
		this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
		this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
	}

	@Override
	@Transactional
	public AuthTokenResponse login(AuthenticationRequest request) {
		User user = springDataUserRepository.findByCredentials_Username(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD));

		if (user.getCredentials() == null || !Boolean.TRUE.equals(user.getCredentials().isEnabled())) {
			throw new AppException(ErrorCode.ACCOUNT_DISABLED);
		}

		if (!PasswordUtils.matches(request.getPassword(), user.getCredentials().password())) {
			throw new AppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
		}

		return issueTokenPair(user);
	}

    @Override
    @Transactional(readOnly = true)
    public void register(RegisterRequest request) {
		if (springDataUserRepository.existsByCredentialsUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
		}

		if (request.getEmail() != null && !request.getEmail().isBlank() && springDataUserRepository.existsByInfoEmail(request.getEmail())) {
			throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}

		if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank() && springDataUserRepository.existsByInfoPhoneNumber(request.getPhoneNumber())) {
			throw new AppException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS);
		}

		redisTemplate.opsForValue().set("register:user:" + request.getPhoneNumber(), request, 30, TimeUnit.MINUTES);
    }

    @Override
    @Transactional
    public void verifySms(VerifySmsRequest request) {
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(request.getIdToken());
            String firebasePhone = (String) decodedToken.getClaims().get("phone_number");
            if (firebasePhone == null || !firebasePhone.equals(request.getPhoneNumber())) {
                throw new AppException(ErrorCode.PHONE_NUMBER_MISMATCH);
            }

            Object pendingReqObj = redisTemplate.opsForValue().get("register:user:" + request.getPhoneNumber());
            if (pendingReqObj != null) {
                RegisterRequest pendingReq = objectMapper.convertValue(pendingReqObj, RegisterRequest.class);

                Role role = springDataRoleRepository.findByName(RoleName.ROLE_CUSTOMER)
                        .orElseGet(() -> springDataRoleRepository.save(Role.builder().name(RoleName.ROLE_CUSTOMER).build()));

                UserInfo userInfo = UserInfo.builder()
                        .email(pendingReq.getEmail())
                        .address(pendingReq.getAddress())
                        .fullName(pendingReq.getFullName())
                        .phoneNumber(pendingReq.getPhoneNumber())
                        .isPhoneVerified(true)
                        .build();

                User user = User.builder()
                        .credentials(new UserCredentials(
                                pendingReq.getUsername(),
                                PasswordUtils.encode(pendingReq.getPassword()),
                                true))
                        .info(userInfo)
                        .roles(new HashSet<>(Set.of(role)))
                        .build();

                springDataUserRepository.save(user);
                redisTemplate.delete("register:user:" + request.getPhoneNumber());
            } else {
                User user = springDataUserRepository.findByInfoPhoneNumber(request.getPhoneNumber())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

                if (!user.getInfo().isPhoneVerified()) {
                    user.verifyPhone();
                    springDataUserRepository.save(user);
                }
            }
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(ErrorCode.INVALID_FIREBASE_TOKEN);
        }
    }

    @Override
    public void resendOtp(ResendOtpRequest request) {
        String key = "register:user:" + request.getPhoneNumber();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        } else {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
    }

//    private void registerWithRole(RegisterRequest request, RoleName roleName) {
//        if (springDataUserRepository.existsByCredentialsUsername(request.getUsername())) {
//            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
//        }
//
//        if (request.getEmail() != null && !request.getEmail().isBlank() && springDataUserRepository.existsByInfoEmail(request.getEmail())) {
//            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
//        }
//
//        redisTemplate.opsForValue().set("register:user:" + request.getPhoneNumber(), request, 30, TimeUnit.MINUTES);
//    }

	@Override
	@Transactional
	public AuthTokenResponse refreshToken(RefreshTokenRequest request) {
		RefreshToken rt = springDataRefreshTokenRepository.findByToken(request.getRefreshToken())
				.orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

		TokenClaims claims = parseAndValidateToken(request.getRefreshToken(), REFRESH_TYPE, false);
		
		springDataRefreshTokenRepository.delete(rt);

		User user = springDataUserRepository.findByCredentials_Username(claims.username())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

		if (user.getCredentials() == null || !Boolean.TRUE.equals(user.getCredentials().isEnabled())) {
			throw new AppException(ErrorCode.ACCOUNT_DISABLED);
		}

		return issueTokenPair(user);
	}

	@Override
	@Transactional
	public LogoutResponse logout(LogoutRequest request) {
		try {
			TokenClaims claims = parseAndValidateToken(request.getAccessToken(), ACCESS_TYPE);
			long ttl = claims.expiresAt() - Instant.now().getEpochSecond();
			if (ttl > 0) {
				redisTemplate.opsForValue().set("jwt:revoked:" + request.getAccessToken(), "revoked", ttl, TimeUnit.SECONDS);
			}
		} catch (AppException e) {
			// Token is already invalid or expired, no need to do anything
		}

		springDataRefreshTokenRepository.findByToken(request.getRefreshToken())
				.ifPresent(springDataRefreshTokenRepository::delete);

		return LogoutResponse.builder().success(true).build();
	}

	@Override
	public IntrospectResponse introspect(IntrospectRequest request) {
		try {
			TokenClaims claims = parseAndValidateToken(request.getToken(), null);
			return IntrospectResponse.builder()
					.active(true)
					.username(claims.username())
					.tokenType(claims.tokenType())
					.expiresAt(claims.expiresAt())
					.build();
		} catch (AppException ex) {
			return IntrospectResponse.builder().active(false).build();
		}
	}

//	@Override
//	public IntrospectResponse introspect(IntrospectRequest request) {
//		if (invalidatedTokens.contains(request.getToken())) {
//			return IntrospectResponse.builder().active(false).build();
//		}
//
//		try {
//			TokenClaims claims = parseAndValidateToken(request.getToken(), null, true);
//			return IntrospectResponse.builder()
//					.active(true)
//					.username(claims.username())
//					.tokenType(claims.tokenType())
//					.expiresAt(claims.expiresAt())
//					.build();
//		} catch (ResponseStatusException ex) {
//			return IntrospectResponse.builder().active(false).build();
//		}
//	}

	private AuthTokenResponse issueTokenPair(User user) {
		String username = user.getCredentials().username();
		String roles = user.getRoles().stream()
				.map(this::extractRoleName)
				.reduce((left, right) -> left + " " + right)
				.orElse("");

		String accessToken = generateToken(username, roles, ACCESS_TYPE, accessTokenExpirationSeconds);
		String refreshToken = generateToken(username, roles, REFRESH_TYPE, refreshTokenExpirationSeconds);

		RefreshToken rt = RefreshToken.builder()
				.token(refreshToken)
				.user(user)
				.expiresAt(Instant.now().plusSeconds(refreshTokenExpirationSeconds))
				.build();
		springDataRefreshTokenRepository.save(rt);

		return AuthTokenResponse.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.tokenType(TOKEN_TYPE_BEARER)
				.expiresIn(accessTokenExpirationSeconds)
				.refreshExpiresIn(refreshTokenExpirationSeconds)
				.build();
	}

	private String generateToken(String username, String roles, String tokenType, long ttlSeconds) {
		long now = Instant.now().getEpochSecond();
		long exp = now + ttlSeconds;

		Map<String, Object> header = Map.of(
				"alg", "HS256",
				"typ", "JWT"
		);

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("sub", username);
		payload.put("roles", roles);
		payload.put("type", tokenType);
		payload.put("iat", now);
		payload.put("exp", exp);
		payload.put("jti", UUID.randomUUID().toString());

		try {
			String encodedHeader = encodeBase64Url(objectMapper.writeValueAsBytes(header));
			String encodedPayload = encodeBase64Url(objectMapper.writeValueAsBytes(payload));
			String signingInput = encodedHeader + "." + encodedPayload;
			String signature = encodeBase64Url(sign(signingInput));
			return signingInput + "." + signature;
		} catch (Exception ex) {
			throw new AppException(ErrorCode.TOKEN_GENERATION_ERROR);
		}
	}

	private TokenClaims parseAndValidateToken(String token, String expectedType) {
		return parseAndValidateToken(token, expectedType, true);
	}

	private TokenClaims parseAndValidateToken(String token, String expectedType, boolean checkRedis) {
		if (token == null || token.isBlank()) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}

		if (checkRedis && Boolean.TRUE.equals(redisTemplate.hasKey("jwt:revoked:" + token))) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}

		String[] parts = token.split("\\.");
		if (parts.length != 3) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}

		String signingInput = parts[0] + "." + parts[1];
		byte[] actualSignature = decodeBase64Url(parts[2]);
		byte[] expectedSignature = sign(signingInput);
		if (!MessageDigest.isEqual(actualSignature, expectedSignature)) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}

		try {
			Map<String, Object> payload = objectMapper.readValue(
					decodeBase64Url(parts[1]),
					new TypeReference<>() {
					}
			);

			String username = Optional.ofNullable(payload.get("sub")).map(Object::toString).orElse("");
			String tokenType = Optional.ofNullable(payload.get("type")).map(Object::toString).orElse("");
			long exp = asLong(payload.get("exp"));

			if (expectedType != null && !expectedType.equals(tokenType)) {
				throw new AppException(ErrorCode.INVALID_TOKEN);
			}

			if (Instant.now().getEpochSecond() >= exp) {
				throw new AppException(ErrorCode.TOKEN_EXPIRED);
			}

			if (username.isBlank()) {
				throw new AppException(ErrorCode.INVALID_TOKEN);
			}

			return new TokenClaims(username, tokenType, exp);
		} catch (AppException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}
	}

	private byte[] sign(String data) {
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(jwtSecret, "HmacSHA256"));
			return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
		} catch (Exception ex) {
			throw new AppException(ErrorCode.TOKEN_SIGNING_ERROR);
		}
	}

	private String encodeBase64Url(byte[] bytes) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private byte[] decodeBase64Url(String value) {
		try {
			return Base64.getUrlDecoder().decode(value);
		} catch (IllegalArgumentException ex) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}
	}

	private long asLong(Object value) {
		if (value instanceof Number number) {
			return number.longValue();
		}
		if (value == null) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}
		try {
			return Long.parseLong(value.toString());
		} catch (NumberFormatException ex) {
			throw new AppException(ErrorCode.INVALID_TOKEN);
		}
	}

    private String extractRoleName(Role role) {
		try {
			Field field = role.getClass().getDeclaredField("name");
			field.setAccessible(true);
			Object value = field.get(role);
			return value == null ? "" : value.toString();
		} catch (ReflectiveOperationException ex) {
			throw new AppException(ErrorCode.ROLE_READ_ERROR);
		}
	}

	private record TokenClaims(String username, String tokenType, long expiresAt) {
	}
}

