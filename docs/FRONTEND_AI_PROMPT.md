# Frontend AI Prompt Brief

Tài liệu này được viết để bạn copy-paste vào AI coding assistant khi muốn AI tạo hoặc tiếp tục phát triển frontend cho hệ thống e-commerce này.

---

## 1. Mục tiêu

Xây dựng frontend cho hệ thống thương mại điện tử dựa trên backend Spring Boot hiện có.
Frontend cần:
- Có cấu trúc rõ ràng, dễ mở rộng
- Tách theo feature/domain
- Kết nối backend qua REST API
- Hỗ trợ luồng khách hàng và quản trị
- Ưu tiên code sạch, component tái sử dụng, dễ bảo trì

---

## 2. Bối cảnh dự án

Backend hiện là hệ thống e-commerce tổ chức theo bounded context:
- `authentication`
- `productcatalog`
- `order`
- `usermanagement`
- `dashboard`
- `core`

Frontend nên bám theo các domain này để tách module hợp lý.

Nếu chưa có frontend stack cố định, mặc định đề xuất:
- **React + TypeScript**
- **Vite**
- **React Router**
- **Axios** hoặc fetch wrapper
- **TanStack Query** cho server-state
- **Zustand** hoặc Redux Toolkit cho state cục bộ/toàn cục
- **Tailwind CSS** hoặc UI library tương đương

---

## 3. Phạm vi màn hình

### 3.1. Auth
- Đăng ký
- Đăng nhập
- Đăng xuất
- Làm mới phiên / token
- Bảo vệ route theo role

### 3.2. Customer
- Trang danh sách sản phẩm
- Trang chi tiết sản phẩm
- Tìm kiếm, lọc, sắp xếp, phân trang
- Giỏ hàng
- Checkout / đặt hàng
- Lịch sử đơn hàng
- Hồ sơ cá nhân

### 3.3. Admin / Staff
- Dashboard thống kê
- Danh sách đơn hàng
- Chi tiết đơn hàng
- Quản lý sản phẩm
- Quản lý danh mục / brand
- Quản lý người dùng và phân quyền

---

## 4. Danh sách route gợi ý

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

Nếu cần, AI có thể thêm:
- `/admin/categories`
- `/admin/brands`
- `/404`
- `/unauthorized`

---

## 5. Cấu trúc thư mục đề xuất

```text
src/
  app/
  assets/
  components/
  features/
    auth/
    cart/
    checkout/
    dashboard/
    orders/
    products/
    profile/
    users/
  hooks/
  layouts/
  pages/
  routes/
  services/
  store/
  types/
  utils/
  constants/
```

### Nguyên tắc tách module
- `pages/`: trang theo route
- `features/`: logic theo nghiệp vụ
- `components/`: component tái sử dụng
- `services/`: gọi API
- `store/`: state management
- `types/`: type/interface dùng chung
- `utils/`: helper

---

## 6. Quy ước UI/UX

AI phải đảm bảo:
- Có loading state cho mọi request bất đồng bộ
- Có empty state khi không có dữ liệu
- Có error state rõ ràng
- Form validation trước khi submit
- Toast/snackbar cho thông báo thành công hoặc thất bại
- Responsive cho mobile/tablet/desktop
- Có protected routes cho màn hình admin
- Tách layout khách hàng và layout quản trị nếu phù hợp

---

## 7. Dữ liệu hiển thị chính

### 7.1. Product
- id
- name
- price
- image
- status
- stock
- description
- category
- brand

### 7.2. Order
- orderCode
- customerName
- status
- paymentStatus
- totalAmount
- createdAt
- items

### 7.3. User
- id
- username
- email
- fullName
- phone
- address
- roles
- status

### 7.4. Dashboard
- totalUsers
- totalProducts
- totalOrders
- revenue
- orderByStatus statistics

---

## 8. Nguyên tắc tích hợp API

- Frontend chỉ gọi backend API, không tự xử lý nghiệp vụ phức tạp
- Ưu tiên tạo lớp service riêng cho từng domain
- Tất cả API nên đi qua một HTTP client wrapper chung
- Có xử lý lỗi chuẩn cho các HTTP status phổ biến
- Nếu backend có `ApiResponse<T>`, frontend cần map nhất quán

### Xử lý lỗi
- `401`: chuyển về login hoặc refresh token
- `403`: hiển thị không đủ quyền
- `404`: hiển thị không tìm thấy dữ liệu
- `409`: thông báo xung đột dữ liệu
- `500`: thông báo lỗi hệ thống

### Token
- Nếu dùng JWT, lưu trữ theo chiến lược an toàn phù hợp với frontend
- Tự động gắn token vào request khi cần
- Có cơ chế refresh token nếu backend hỗ trợ

---

## 9. Yêu cầu kỹ thuật khi AI code

AI cần:
- Viết code theo TypeScript nếu có thể
- Tạo component nhỏ, tái sử dụng
- Không hard-code dữ liệu nghiệp vụ nếu có thể gọi API
- Không nhồi toàn bộ logic vào một file
- Có type/interface rõ ràng
- Có reusable hooks cho data fetching nếu phù hợp
- Tách service, component, page, type rõ ràng
- Ưu tiên code dễ đọc hơn là quá tối ưu sớm

---

## 10. Checklist code nên tạo

Khi bắt đầu frontend, AI nên ưu tiên:
1. Khởi tạo project và base config
2. Tạo layout tổng thể
3. Tạo router và route guard
4. Tạo HTTP client + service layer
5. Tạo auth flow
6. Tạo product listing + detail
7. Tạo cart + checkout
8. Tạo order history
9. Tạo admin dashboard
10. Tạo admin CRUD screens
11. Thêm validation, loading, empty state, error state
12. Rà soát responsive và accessibility cơ bản

---

## 11. Tiêu chí hoàn thành

Frontend được xem là ổn khi:
- Người dùng đăng nhập/đăng ký thành công
- Xem danh sách và chi tiết sản phẩm
- Thêm/xóa/sửa giỏ hàng
- Tạo đơn hàng thành công
- Xem lịch sử đơn hàng
- Admin xem dashboard và quản lý dữ liệu chính
- Không có lỗi lớn về route, state, hoặc API integration

---

## 12. Prompt mẫu để đưa cho AI

Bạn có thể copy nguyên khối dưới đây:

```text
Bạn là một senior frontend engineer. Hãy xây dựng frontend cho dự án e-commerce dựa trên backend Spring Boot hiện có.

Yêu cầu chung:
- Dùng kiến trúc rõ ràng, tách theo feature/domain
- Ưu tiên React + TypeScript + Vite nếu không có ràng buộc khác
- Có router, HTTP client, service layer, state management phù hợp
- Tách component tái sử dụng, layout riêng cho customer/admin
- Có loading, empty state, error state, validation, toast notifications
- Responsive cho mobile/tablet/desktop
- Có protected routes theo role
- Code phải sạch, dễ bảo trì, có type/interface rõ ràng

Phạm vi chức năng:
- Auth: login, register, logout, refresh session/token
- Customer: product listing, product detail, cart, checkout, order history, profile
- Admin: dashboard, orders, products, users, categories/brands nếu cần

Route gợi ý:
- /login
- /register
- /products
- /products/:id
- /cart
- /checkout
- /orders
- /profile
- /admin/dashboard
- /admin/orders
- /admin/products
- /admin/users

Nguyên tắc API:
- Tạo lớp service riêng cho từng domain
- Dùng một HTTP client chung
- Xử lý 401/403/404/409/500 rõ ràng
- Nếu backend trả ApiResponse<T>, hãy map dữ liệu thống nhất

Đầu ra mong muốn:
- Đề xuất cấu trúc thư mục trước
- Sau đó generate code từng phần theo thứ tự ưu tiên
- Nếu thiếu thông tin endpoint, hãy tạo mock interface/service placeholders để dễ thay thế
- Tránh viết code một cục quá lớn
```

---

## 13. Ghi chú cho người dùng

Nếu bạn muốn AI code frontend nhanh hơn, hãy cung cấp thêm:
- Base URL backend
- Danh sách endpoint thực tế
- Format response JSON
- Cấu hình auth/token
- UI library muốn dùng
- Màu sắc / brand guideline nếu có

Tham khảo trực tiếp tài liệu đã chuẩn bị sẵn:
- `docs/FRONTEND_BACKEND_REFERENCE.md`

---

## 14. Kết luận

Tài liệu này đóng vai trò như một “frontend brief” để AI hiểu:
- đang code cho hệ thống gì
- cần làm những màn hình nào
- phải tổ chức code ra sao
- tích hợp backend theo nguyên tắc nào

Bạn có thể mở rộng tài liệu này bằng cách thêm contract API chi tiết khi backend hoàn thiện.

