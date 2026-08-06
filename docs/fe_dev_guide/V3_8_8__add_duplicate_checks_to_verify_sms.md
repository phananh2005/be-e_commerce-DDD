---
version: V3.8.8
date: 2026-08-06
description: Thêm kiểm tra trùng lặp thông tin vào API xác thực SMS OTP
---

# V3.8.8 - Thêm kiểm tra trùng lặp thông tin vào API xác thực SMS OTP

## 1. Thông tin thay đổi
- **API bị ảnh hưởng**: `POST /auth/verify-sms`
- **Loại thay đổi**: Cập nhật logic (Thêm mã lỗi mới)
- **Breaking Change**: Có (frontend cần xử lý thêm các mã lỗi mới)

## 2. Chi tiết thay đổi
Trước đây API đăng ký `/auth/register` đã kiểm tra trùng lặp (username, email, phone). Tuy nhiên, vì có khoảng delay (lưu Redis 30 phút chờ OTP), hệ thống bổ sung thêm một bước kiểm tra trùng lặp lần cuối trong hàm `verifySms` trước khi thực sự lưu user vào database.

Nếu phát hiện trùng lặp, API sẽ trả về các mã lỗi sau:
- `USERNAME_ALREADY_EXISTS`: Tên đăng nhập đã được sử dụng.
- `EMAIL_ALREADY_EXISTS`: Email đã được sử dụng.
- `PHONE_NUMBER_ALREADY_EXISTS`: Số điện thoại đã được sử dụng.

## 3. Hướng dẫn cập nhật Frontend
- **Cập nhật màn hình nhập OTP**: Khi gọi API `/auth/verify-sms`, Frontend cần bắt các mã lỗi trên và hiển thị thông báo.
- Nếu gặp lỗi này, thông báo cho người dùng quay lại bước đăng ký để đổi thông tin, do thông tin đó đã bị đăng ký bởi người khác trong lúc chờ OTP.
