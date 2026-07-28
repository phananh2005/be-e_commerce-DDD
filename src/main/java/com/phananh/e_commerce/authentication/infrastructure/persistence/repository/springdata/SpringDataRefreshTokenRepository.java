package com.phananh.e_commerce.authentication.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.authentication.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);
    void deleteByUser_Id(Long userId);
}
