# V4.0.1 - Refactor Internal Authentication

**Ngày tạo**: 2026-08-12
**Người tạo**: AI Agent

## Thay đổi
Refactor nội bộ cơ chế xác thực đăng nhập (Login) sử dụng `AuthenticationManager` và `UserDetailsService` chuẩn của Spring Security thay cho việc tra cứu user và verify password thủ công.

## Breaking Change
**Không có Breaking Change**.
- API Endpoint (`POST /auth/login`) không đổi.
- Request và Response format hoàn toàn giống như trước.
- Các Error Codes (`INVALID_USERNAME_OR_PASSWORD`, `ACCOUNT_DISABLED`, `USER_NOT_FOUND`) vẫn được giữ nguyên và trả về tương ứng.

## Hướng dẫn cho Frontend
- Frontend không cần phải thay đổi gì đối với thay đổi này.
