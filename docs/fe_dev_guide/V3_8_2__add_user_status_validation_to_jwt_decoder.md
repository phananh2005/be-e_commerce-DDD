# Migration V3_8_2: Thêm kiểm tra trạng thái người dùng khi validate token

**Version**: V3.8.2
**Ngày tạo**: 2026-08-01
**Breaking Change**: Không

## Chi tiết thay đổi
- Trong quá trình giải mã và xác thực JWT token (sử dụng cho các API yêu cầu đăng nhập, refresh token, logout, introspect), hệ thống sẽ tự động kiểm tra xem tài khoản có tồn tại và đang ở trạng thái `ACTIVE` (enabled) hay không.
- Nếu tài khoản đã bị vô hiệu hóa hoặc xóa, token dù chưa hết hạn cũng sẽ bị coi là không hợp lệ và trả về lỗi `ACCOUNT_DISABLED` hoặc `USER_NOT_FOUND`.

## Hướng dẫn FE cần làm gì để cập nhật
- Bổ sung hoặc đảm bảo logic xử lý lỗi global (interceptor) có khả năng bắt các mã lỗi liên quan đến tài khoản bị khóa (`ACCOUNT_DISABLED`, `USER_NOT_FOUND`).
- Khi gặp các lỗi này từ bất kỳ API nào, frontend nên tự động đăng xuất người dùng (xóa token local) và điều hướng về trang đăng nhập, tương tự như khi nhận lỗi 401 Unauthorized do token hết hạn.
