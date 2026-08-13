# Migration: V4.1.4 - Chuyển Idempotency-Key từ Đăng ký sang Xác thực SMS

## Thông tin
- **Version:** V4.1.4
- **Ngày tạo:** 2026-08-13
- **Loại thay đổi:** Cập nhật validation (thay đổi luồng gửi header)
- **Breaking Change:** Có

## Chi tiết thay đổi
Trước đây (V4.1.3), API `POST /auth/register` (Đăng ký) yêu cầu header `Idempotency-Key`.
Tuy nhiên, logic tạo tài khoản thực tế (lưu DB) nằm ở bước xác thực OTP. Do đó, yêu cầu Idempotency đã được chuyển sang API Verify SMS.

- **Bỏ yêu cầu Idempotency-Key** tại: `POST /auth/register`
- **Bắt buộc thêm Idempotency-Key** tại: `POST /auth/verify-sms`

## Ảnh hưởng Frontend
- Có thể xóa logic sinh và gửi `Idempotency-Key` khi gọi API `/auth/register`.
- **Bắt buộc** sinh UUID v4 và truyền vào header `Idempotency-Key` khi gọi API `/auth/verify-sms`.

## Hướng dẫn cập nhật
Sửa lại code trên FE: không cần gửi `Idempotency-Key` lúc submit form đăng ký nữa. Chỉ gửi khi user nhập xong OTP và bấm xác nhận (gọi API `verify-sms`).
