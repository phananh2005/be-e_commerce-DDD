# Migration: V4.1.3 - Thêm Idempotency-Key cho API đăng ký và giỏ hàng

## Thông tin
- **Version:** V4.1.3
- **Ngày tạo:** 2026-08-13
- **Loại thay đổi:** Cập nhật validation (thêm header bắt buộc)
- **Breaking Change:** Có

## Chi tiết thay đổi
Để chặn spam click và lỗi race condition, các API sau đã bị ép buộc gửi header `Idempotency-Key`:

- `POST /auth/register` (Đăng ký tài khoản)
- `POST /cart-item/add` (Thêm vào giỏ hàng)

## Ảnh hưởng Frontend
- Thiếu header = `400 Bad Request` (`INVALID_REQUEST`).
- Trùng header = Bị chặn (`409 Conflict`) hoặc trả kết quả cũ (nếu Request trước đã chạy xong 100%).

## Hướng dẫn cập nhật
Các hàm gọi đăng ký hoặc thêm giỏ hàng bắt buộc sinh một UUID v4 nhét vào header `Idempotency-Key`.
