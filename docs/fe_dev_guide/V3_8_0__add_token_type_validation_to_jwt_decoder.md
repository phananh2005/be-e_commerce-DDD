# V3.8.0 - Bổ sung kiểm tra token type trong JWT Decoder

- **Version**: V3.8.0
- **Ngày tạo**: 2026-07-31
- **Loại thay đổi**: Cập nhật Auth/Validation
- **Breaking Change**: Có (nếu FE truyền sai loại token)

## Chi tiết thay đổi

- Trong `CustomJwtDecoder`, đã bổ sung thêm kiểm tra claim `type` của JWT token.
- Token hợp lệ để truy cập các API được bảo vệ bắt buộc phải có claim `type` với giá trị là `access`.
- Nếu token có `type` khác (ví dụ: `refresh`), hệ thống sẽ throw `BadJwtException` ("Invalid token type"), dẫn đến lỗi 401 Unauthorized.

## Ảnh hưởng tới Frontend

- **Hành động cần thiết**: Kiểm tra lại toàn bộ quy trình gọi API có sử dụng Bearer token. Đảm bảo rằng Frontend đang gửi đúng Access Token trong header `Authorization: Bearer <token>`.
- **Breaking change**: Nếu trước đây Frontend vô tình gửi nhầm Refresh Token để truy cập API và vẫn được chấp nhận, thì hiện tại request đó sẽ bị từ chối với mã lỗi HTTP 401. Đảm bảo phân biệt rõ ràng 2 loại token và chỉ dùng Access Token cho các endpoint thông thường, và Refresh Token cho endpoint cấp lại token.
