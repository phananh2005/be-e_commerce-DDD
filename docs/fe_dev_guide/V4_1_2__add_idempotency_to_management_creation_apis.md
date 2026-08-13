# Migration: V4.1.2 - Thêm Idempotency-Key cho các API tạo dữ liệu Quản trị

## Thông tin
- **Version:** V4.1.2
- **Ngày tạo:** 2026-08-13
- **Loại thay đổi:** Cập nhật validation (thêm header bắt buộc)
- **Breaking Change:** Có

## Chi tiết thay đổi
Để tránh việc tạo trùng lặp dữ liệu do gửi request nhiều lần (ví dụ như double-click), các API tạo mới (CREATE) sau đây trong phần quản trị đã được gắn thêm ràng buộc Idempotency:

- `POST /management/product/create` (Tạo sản phẩm)
- `POST /management/brands` (Tạo thương hiệu)
- `POST /management/categories` (Tạo danh mục)

Việc truyền header `Idempotency-Key` với giá trị duy nhất (UUID v4) cho mỗi yêu cầu hiện tại là **bắt buộc**. 

## Ảnh hưởng Frontend
- Nếu không gửi `Idempotency-Key` khi gọi các API trên, hệ thống sẽ trả về lỗi `400 Bad Request` (Mã lỗi: `INVALID_REQUEST`).
- Nếu gửi cùng một `Idempotency-Key` nhiều lần, các yêu cầu sau sẽ bị chặn (trả về lỗi `409 Conflict` nếu đang xử lý, hoặc trả về nguyên kết quả cũ nếu đã xong).

## Hướng dẫn cập nhật
Cập nhật các hàm gọi API tạo mới sản phẩm, thương hiệu, danh mục trên ứng dụng quản trị để đảm bảo luôn tạo một chuỗi ngẫu nhiên duy nhất và gửi kèm trong header `Idempotency-Key`.
