# Migration V3_8_1: Xóa trường roles khỏi Refresh Token payload

**Version**: V3.8.1
**Ngày tạo**: 2026-08-01
**Breaking Change**: Không

## Chi tiết thay đổi
- Không đính kèm trường `roles` vào trong payload của JWT `refresh_token` khi hệ thống tạo ra nó (trong API đăng nhập và refresh token).
- Cấu trúc `refresh_token` bây giờ chỉ chứa các thông tin cơ bản: `sub` (username), `type`, `iat`, `exp`, `jti`.
- API bị ảnh hưởng:
  - `POST /auth/login`
  - `POST /auth/refresh`

## Hướng dẫn FE cần làm gì để cập nhật
- **Không có ảnh hưởng trực tiếp đến frontend** vì frontend không nên (hoặc không cần) giải mã `refresh_token` để lấy thông tin roles.
- `access_token` vẫn chứa trường `roles` như bình thường.
