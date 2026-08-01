# V3_8_4__invalidate_refresh_tokens_on_password_change

- **Version**: V3.8.4
- **Ngày tạo**: 2026-08-02
- **Loại thay đổi**: Cập nhật
- **API bị ảnh hưởng**: Đổi mật khẩu
- **Breaking change**: Có

## Chi tiết thay đổi
- Hệ thống sẽ tự động xóa tất cả các refresh tokens của user khi user thực hiện đổi mật khẩu thành công.
- Các thiết bị khác (hoặc chính thiết bị hiện tại) đang giữ token cũ sẽ không thể lấy access token mới khi access token hiện tại hết hạn.

## Hướng dẫn FE
- Xử lý lỗi `401 Unauthorized`: Khi gọi bất kỳ API nào bị trả về 401 do token không còn hợp lệ, FE cần clear local state (xóa token, thông tin user) và đẩy user ra màn hình đăng nhập.
- **Tại màn hình đổi mật khẩu**: Sau khi đổi mật khẩu thành công, FE BẮT BUỘC phải thực hiện logout cục bộ và chuyển hướng người dùng đến màn hình Đăng nhập để yêu cầu đăng nhập lại bằng mật khẩu mới.
