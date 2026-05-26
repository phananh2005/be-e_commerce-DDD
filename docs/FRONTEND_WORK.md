# Frontend Work Documentation

Tài liệu này mô tả phạm vi công việc frontend cho dự án e-commerce backend hiện tại. Mục tiêu là dùng làm bản mô tả nghiệp vụ, cấu trúc màn hình và cách tích hợp API cho team frontend.

## 1. Mục tiêu frontend
- Xây dựng giao diện người dùng cho hệ thống thương mại điện tử.
- Kết nối với API backend qua REST.
- Đảm bảo trải nghiệm tốt cho cả khách hàng và nhân viên quản trị.
- Tái sử dụng component, chuẩn hóa form, validation và xử lý trạng thái.

## 2. Nhóm chức năng chính

### 2.1. Authentication
- Đăng nhập
- Đăng ký tài khoản
- Đăng xuất
- Làm mới token
- Kiểm tra phiên đăng nhập

### 2.2. Customer
- Xem danh sách sản phẩm
- Xem chi tiết sản phẩm
- Lọc / tìm kiếm / phân trang
- Quản lý giỏ hàng
- Tạo đơn hàng
- Xem lịch sử đơn hàng
- Cập nhật thông tin cá nhân

### 2.3. Staff / Admin
- Dashboard thống kê
- Danh sách đơn hàng có phân trang
- Xem chi tiết đơn hàng
- Quản lý sản phẩm
- Quản lý danh mục / brand
- Quản lý người dùng và phân quyền

## 3. Màn hình đề xuất
- `/login`
- `/register`
- `/products`
- `/products/:id`
- `/cart`
- `/checkout`
- `/orders`
- `/profile`
- `/admin/dashboard`
- `/admin/orders`
- `/admin/products`
- `/admin/users`

## 4. Cấu trúc frontend đề xuất

### 4.1. Layer logic
- `pages/` hoặc `routes/`: các màn hình chính
- `components/`: component dùng lại
- `features/`: logic theo nghiệp vụ
- `services/`: gọi API backend
- `hooks/`: custom hooks
- `store/`: state management nếu có
- `utils/`: helper dùng chung
- `assets/`: ảnh, icon, font

### 4.2. Phân tách theo domain
- `auth`
- `product`
- `cart`
- `order`
- `user`
- `dashboard`
- `admin`

## 5. Tích hợp API

### 5.1. Nguyên tắc chung
- Frontend không xử lý nghiệp vụ nặng, chỉ gọi backend API.
- Luôn đọc `ApiResponse<T>` từ backend.
- Kiểm tra HTTP status và message trả về.
- Token JWT nên lưu an toàn theo chiến lược của frontend.

### 5.2. API cần chú ý
- Authentication endpoints
- Product listing/detail
- Cart endpoints
- Order creation/detail
- User profile endpoints
- Dashboard statistics

### 5.3. Xử lý lỗi
- `401`: chuyển về trang login hoặc refresh token
- `403`: hiển thị không đủ quyền
- `404`: hiển thị không tìm thấy dữ liệu
- `409`: xung đột dữ liệu
- `500`: thông báo lỗi hệ thống

## 6. Dữ liệu hiển thị từ backend
- Danh sách sản phẩm: tên, giá, ảnh, trạng thái, tồn kho
- Danh sách đơn hàng: mã đơn, người mua, trạng thái, thanh toán, tổng tiền
- Dashboard: tổng users, tổng products, thống kê đơn hàng, doanh thu
- Thông tin user: username, email, họ tên, số điện thoại, địa chỉ, roles

## 7. Quy ước UI/UX
- Form có validation rõ ràng trước khi submit
- Table hỗ trợ phân trang, sorting, filter nếu cần
- Loading state cho mọi request bất đồng bộ
- Empty state khi không có dữ liệu
- Toast / snackbar cho thông báo thành công hoặc lỗi
- Responsive cho mobile, tablet, desktop

## 8. Gợi ý công nghệ frontend
- React + TypeScript, hoặc framework tương đương
- Axios / fetch wrapper cho HTTP client
- React Router / router tương đương
- State management: Redux Toolkit, Zustand hoặc Context tùy quy mô
- UI library: Ant Design, MUI, Tailwind CSS, hoặc kết hợp

## 9. Quy trình triển khai
1. Thiết kế route và layout tổng thể.
2. Tạo service layer gọi API.
3. Dựng màn hình auth trước.
4. Làm màn hình catalog và cart.
5. Làm checkout và order history.
6. Làm dashboard và màn hình admin.
7. Kiểm thử luồng đăng nhập, đặt hàng, xem đơn, xem thống kê.

## 10. Ghi chú cho team frontend
- Backend đã tách theo bounded context, nên frontend nên tách feature tương ứng.
- Các API trả về DTO, tránh map trực tiếp từ entity backend.
- Nếu cần thêm tài liệu API chi tiết, nên bổ sung OpenAPI/Swagger hoặc file contract riêng.

