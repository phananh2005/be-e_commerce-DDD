# V4.1.0: Thêm yêu cầu header Idempotency-Key cho API Thanh toán

- **Ngày tạo**: 2026-08-11
- **API thay đổi**: `POST /orders/checkout`
- **Loại thay đổi**: Cập nhật
- **Breaking change**: Có (Tùy thuộc vào policy, ở đây thêm header để bảo vệ tránh duplicate order, khuyến khích bắt buộc)

## 1. Chi tiết thay đổi

- Backend đã thêm tính năng **Idempotency Key** sử dụng Redis và AOP để ngăn chặn người dùng thanh toán nhiều lần cho cùng một request (ví dụ: bấm nút "Thanh toán" nhiều lần do lỗi mạng hoặc lag).
- **API bị ảnh hưởng**:
  - `POST /orders/checkout`

## 2. Yêu cầu HTTP Header mới

Khi gọi API `/orders/checkout`, Frontend **CẦN** (hoặc bắt buộc theo business logic) truyền thêm một header tên là `Idempotency-Key`.

```http
POST /orders/checkout HTTP/1.1
Content-Type: application/json
Authorization: Bearer <token>
Idempotency-Key: <unique-uuid>
```

- **`Idempotency-Key`**: Là một chuỗi định danh duy nhất (UUID) cho mỗi hành động thanh toán. 
- Nếu gửi lại cùng một `Idempotency-Key` trong khoảng thời gian Redis lưu trữ, Backend sẽ:
  - Báo lỗi `409 Conflict` nếu request đầu tiên vẫn đang được xử lý.
  - Trả về nguyên trạng (kết quả cũ của HTTP) nếu request trước đã hoàn thành.

## 3. Hướng dẫn FE cập nhật

- Trước khi gọi API `/orders/checkout`, Frontend sử dụng thư viện `uuid` (như `uuidv4`) để sinh ra một chuỗi UUID mới duy nhất.
- Gắn chuỗi này vào HTTP request headers dưới tên `Idempotency-Key`.
- Nếu request thất bại do mạng và frontend retry tự động, hãy giữ nguyên `Idempotency-Key` đó.
- Chỉ tạo `Idempotency-Key` mới khi người dùng thực hiện một luồng đặt hàng MỚI (chẳng hạn, load lại trang giỏ hàng và ấn thanh toán lần khác sau khi lần trước thất bại).

**Xử lý mã lỗi trả về (HTTP Status Codes mới):**
- **409 Conflict**: Xảy ra khi Frontend gửi 2 request với cùng một `Idempotency-Key` cùng lúc (request 1 chưa xử lý xong mà request 2 đã tới). Frontend nên hiện thông báo: "Đơn hàng đang được xử lý, vui lòng đợi..." thay vì báo lỗi đỏ.
