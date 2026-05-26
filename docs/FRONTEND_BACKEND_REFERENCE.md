# Frontend Backend Reference

Tài liệu này tổng hợp các thông tin backend thực tế cần cho frontend: base URL, endpoint, format response JSON và cấu hình auth/token.

---

## 1. Base URL backend

### Môi trường local hiện tại
```text
http://localhost:8080/e-commerce
```

### Ghi chú
- `application.properties` đang cấu hình `server.servlet.context-path=/e-commerce`.
- Backend chưa khai báo `server.port`, nên port mặc định của Spring Boot là `8080` nếu không đổi ở môi trường khác.
- Công thức đầy đủ: `http://<host>:<port>/e-commerce`

---

## 2. Format response JSON

### 2.1. Response chuẩn
Backend dùng wrapper chung `ApiResponse<T>`:

```json
{
  "code": 1000,
  "message": "Login successful",
  "result": {
    "accessToken": "...",
    "refreshToken": "..."
  }
}
```

### 2.2. Cấu trúc `ApiResponse<T>`
- `code`: mặc định `1000`
- `message`: thông điệp từ backend
- `result`: dữ liệu chính, có thể `null` hoặc không có nếu endpoint không trả dữ liệu

### 2.3. Ví dụ response không có `result`
```json
{
  "code": 1000,
  "message": "Logout successful"
}
```

### 2.4. Lưu ý không đồng nhất
Một số endpoint không dùng `ApiResponse<T>` hoàn toàn giống nhau:
- Nhiều API CRUD trả `204 No Content`
- `PATCH /cart-item/update` đang trả `200 OK` với body là chuỗi message
- Một số API nếu dữ liệu rỗng chỉ trả `message` mà không có `result`

Frontend nên xử lý cả 3 kiểu:
- `ApiResponse<T>`
- `204 No Content`
- Body chuỗi/plain text ở một vài endpoint đặc biệt

---

## 3. Cấu hình auth/token

### 3.1. Cơ chế chung
Backend dùng JWT và Spring Security resource server.

Frontend cần:
- Gửi access token qua header:
  ```text
  Authorization: Bearer <accessToken>
  ```
- Lưu `refreshToken` để làm mới phiên khi cần
- Xử lý 401/403 theo role và trạng thái token

### 3.2. Token response
`AuthTokenResponse`:
- `accessToken`
- `refreshToken`
- `tokenType`
- `expiresIn`
- `refreshExpiresIn`

### 3.3. Introspect response
`IntrospectResponse`:
- `active`
- `username`
- `tokenType`
- `expiresAt`

### 3.4. Logout response
`LogoutResponse`:
- `success`

### 3.5. Request body cho auth endpoints
#### Login
`POST /auth/login`
```json
{
  "username": "admin",
  "password": "Admin@12345"
}
```

#### Register
`POST /auth/register`
```json
{
  "username": "user01",
  "password": "12345678",
  "email": "user01@gmail.com",
  "fullName": "User 01"
}
```

#### Refresh token
`POST /auth/refresh`
```json
{
  "refreshToken": "..."
}
```

#### Logout
`POST /auth/logout`
```json
{
  "token": "..."
}
```

#### Introspect
`POST /auth/introspect`
```json
{
  "token": "..."
}
```

### 3.6. Phân quyền hiện tại từ `SecurityConfig`
- `/admin/**` → `ROLE_ADMIN`
- `/staff/**` → `ROLE_STAFF`
- `/cloudinary/**` → `ROLE_STAFF` hoặc `ROLE_ADMIN`
- Các request còn lại hiện được chặn theo rule role hiện tại của backend

> Ghi chú: trong `SecurityConfig` hiện chưa thấy rule `permitAll()` riêng cho `/auth/**`, vì vậy frontend nên xác nhận lại hành vi thực tế của login/register/refresh/logout khi tích hợp.

---

## 4. Danh sách endpoint thực tế

### 4.1. Authentication
| Method | Path | Mục đích |
|---|---|---|
| POST | `/auth/login` | Đăng nhập |
| POST | `/auth/register` | Đăng ký tài khoản |
| POST | `/auth/refresh` | Làm mới token |
| POST | `/auth/logout` | Đăng xuất |
| POST | `/auth/introspect` | Kiểm tra token |

### 4.2. Product - customer
> Lưu ý: controller hiện tại không có `@RequestMapping` cấp class, nên path là đúng như bên dưới theo code hiện tại.

| Method | Path | Mục đích |
|---|---|---|
| GET | `/search` | Danh sách sản phẩm customer, hỗ trợ search/filter/paging qua query params |
| GET | `/product/{id}` | Xem chi tiết sản phẩm |

### 4.3. Category / Brand - customer
| Method | Path | Mục đích |
|---|---|---|
| GET | `/categories` | Lấy danh sách danh mục đang bật |
| GET | `/brands` | Lấy danh sách thương hiệu |

### 4.4. User - customer
| Method | Path | Mục đích |
|---|---|---|
| GET | `/users/my-info` | Lấy thông tin cá nhân |
| PATCH | `/users/update-info` | Cập nhật thông tin cá nhân |
| PATCH | `/users/change-password` | Đổi mật khẩu |

### 4.5. Cart
| Method | Path | Mục đích |
|---|---|---|
| GET | `/cart-item/my-cart` | Lấy giỏ hàng hiện tại |
| POST | `/cart-item/add` | Thêm sản phẩm vào giỏ |
| DELETE | `/cart-item/remove/{ids}` | Xóa item khỏi giỏ, `ids` là danh sách id trong path |
| PATCH | `/cart-item/update` | Cập nhật item trong giỏ |

### 4.6. Order - customer
| Method | Path | Mục đích |
|---|---|---|
| GET | `/orders/preview` | Xem preview đơn hàng |
| POST | `/orders/checkout` | Tạo đơn hàng / checkout |
| GET | `/orders/my-orders` | Lịch sử đơn hàng của tôi |
| GET | `/orders/my-orders/{orderId}` | Chi tiết đơn hàng của tôi |

### 4.7. Product - staff
| Method | Path | Mục đích |
|---|---|---|
| GET | `/staff/product/search` | Tìm kiếm sản phẩm cho staff |
| GET | `/staff/product/{id}` | Xem chi tiết sản phẩm |
| GET | `/staff/product/{productId}/variants` | Lấy danh sách variant |
| POST | `/staff/product/{productId}/variants` | Tạo variant |
| POST | `/staff/product/create` | Tạo product |
| PUT | `/staff/product/update` | Cập nhật product |
| PATCH | `/staff/product/{productId}/{status}` | Cập nhật trạng thái product |
| PATCH | `/staff/variant/{variantId}/{stockQuantity}` | Cập nhật tồn kho variant |

### 4.8. Order - staff
| Method | Path | Mục đích |
|---|---|---|
| GET | `/orders/search` | Danh sách đơn hàng staff, có paging/sort qua query params |
| GET | `/orders/{orderId}` | Chi tiết đơn hàng staff |
| PATCH | `/orders/{orderId}/{status}` | Cập nhật trạng thái đơn hàng |

### 4.9. Category - admin
| Method | Path | Mục đích |
|---|---|---|
| GET | `/admin/categories/search` | Tìm kiếm / phân trang danh mục |
| POST | `/admin/categories` | Tạo danh mục |
| PUT | `/admin/categories` | Cập nhật danh mục |
| PATCH | `/admin/categories/{categoryId}/{status}` | Cập nhật trạng thái danh mục |

### 4.10. Brand - admin
| Method | Path | Mục đích |
|---|---|---|
| GET | `/admin/brands/search` | Tìm kiếm / phân trang thương hiệu |
| POST | `/admin/brands` | Tạo brand |
| PATCH | `/admin/brands/update` | Cập nhật brand |
| PATCH | `/admin/brands/{brandId}/{status}` | Cập nhật trạng thái brand |

### 4.11. User - admin
| Method | Path | Mục đích |
|---|---|---|
| GET | `/admin/users` | Danh sách người dùng |
| GET | `/admin/users/customer/info/{id}` | Xem thông tin customer |
| PATCH | `/admin/users/update-role` | Cập nhật role user |
| PATCH | `/admin/users/{id}/{status}` | Cập nhật trạng thái user |

### 4.12. User - staff
| Method | Path | Mục đích |
|---|---|---|
| GET | `/staff/users/customer/info/{id}` | Xem thông tin customer cho staff |

### 4.13. Dashboard - admin
| Method | Path | Mục đích |
|---|---|---|
| GET | `/admin/statistics/overview` | Tổng quan dashboard |
| POST | `/admin/statistics/orders` | Thống kê đơn hàng |
| POST | `/admin/statistics/revenue` | Báo cáo doanh thu |

### 4.14. Cloudinary
| Method | Path | Mục đích |
|---|---|---|
| GET | `/cloudinary/signature?folder=...` | Lấy signature upload ảnh trực tiếp |

---

## 5. Gợi ý cho frontend khi tích hợp

- Tạo một HTTP client chung để tự gắn `Authorization: Bearer <accessToken>`
- Bọc response về một format chuẩn phía frontend để xử lý nhất quán
- Tách service theo domain: `auth`, `products`, `orders`, `cart`, `users`, `dashboard`, `categories`, `brands`
- Với endpoint search/paging, gửi query params bằng `GET` hoặc `@ModelAttribute`
- Với API `204 No Content`, chỉ cần đọc status code thành công
- Với API trả `Page<T>`, frontend nên map `content`, `totalPages`, `totalElements`, `pageNumber`

---

## 6. Kết luận nhanh

Nếu bạn đưa tài liệu này cho AI, AI sẽ biết ngay:
- backend đang chạy ở đâu
- endpoint nào là thật
- format response thế nào
- token truyền ra sao
- route nào thuộc customer/staff/admin


