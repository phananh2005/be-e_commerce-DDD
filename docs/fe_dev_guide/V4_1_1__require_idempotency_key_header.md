# Migration: V4.1.1 - Yêu cầu bắt buộc header Idempotency-Key

## Thông tin
- **Version:** V4.1.1
- **Ngày tạo:** 2026-08-13
- **Loại thay đổi:** Cập nhật validation
- **Breaking Change:** Có

## Chi tiết thay đổi
Trước đây, nếu không truyền header `Idempotency-Key` vào các API có yêu cầu tính idempotency (như Checkout), hệ thống sẽ bỏ qua và xử lý bình thường.

Bây giờ, việc truyền header này là **bắt buộc**. Nếu không truyền hoặc giá trị bị rỗng, hệ thống sẽ trả về lỗi `400 Bad Request` (Mã lỗi: `INVALID_REQUEST`).

## Ảnh hưởng Frontend
- **Tất cả các API yêu cầu idempotency (ví dụ: POST /api/v1/customer/orders/checkout)**: FE bắt buộc phải gửi kèm header `Idempotency-Key` (hoặc tên header tương ứng tùy API) với giá trị UUID duy nhất cho mỗi yêu cầu.
- Nếu không, API sẽ trả về `400 Bad Request` và không xử lý yêu cầu.

## Hướng dẫn cập nhật
Đảm bảo sinh UUID v4 hoặc chuỗi ngẫu nhiên duy nhất trên FE mỗi khi gọi các API tạo mới dữ liệu quan trọng, và gán vào header yêu cầu.
