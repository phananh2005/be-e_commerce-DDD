# API Reference (Frontend) — Tổng hợp endpoints theo module

> File này dành cho team frontend: liệt kê endpoint thực tế, yêu cầu auth, query params phổ biến, body mẫu và response mẫu.

Base URL (local):

```text
http://localhost:8080/e-commerce
```

Lưu ý chung:
- Backend trả wrapper chung `ApiResponse<T>` với cấu trúc: `{ code, message, result? }` trừ một vài endpoint trả `204 No Content` hoặc plain text.
- Authorization header: `Authorization: Bearer <accessToken>` khi endpoint cần auth.
- Với các endpoint phân trang, backend thường trả `Page<T>` với trường `content`, `totalPages`, `totalElements`, `number` (page index).

---

## Mẫu `ApiResponse<T>`

```json
{
  "code": 1000,
  "message": "Success",
  "result": {}
}
```

## Kiểu dữ liệu (DTO) - định nghĩa nhanh

Để frontend rõ ràng khi implement, dưới đây là các DTO mẫu (không nhất thiết 1:1 với backend nhưng đủ để mapping và tạo interface TypeScript):

- ProductDto

```json
{
  "id": 1,
  "name": "Product A",
  "price": 100000,
  "image": "https://.../img.jpg",
  "status": "ACTIVE",
  "stock": 20,
  "category": { "id": 2, "name": "Shoes" },
  "brand": { "id": 3, "name": "BrandX" }
}
```

- ProductDetailDto (mở rộng ProductDto)

```json
{
  "id": 1,
  "name": "Product A",
  "price": 100000,
  "images": ["https://.../img1.jpg","https://.../img2.jpg"],
  "description": "Chi tiết sản phẩm",
  "status": "ACTIVE",
  "stock": 20,
  "variants": [ { "id": 11, "sku": "PA-11", "price": 100000, "stock": 5 } ],
  "category": { "id": 2, "name": "Shoes" },
  "brand": { "id": 3, "name": "BrandX" }
}
```

- CategoryDto / BrandDto

```json
{ "id": 2, "name": "Shoes", "status": "ACTIVE" }
```

- UserDto

```json
{
  "id": 12,
  "username": "user01",
  "email": "user01@gmail.com",
  "fullName": "User 01",
  "phone": "0909123456",
  "address": "Hanoi",
  "roles": ["ROLE_USER"],
  "status": "ACTIVE"
}
```

- CartDto / CartItemDto

```json
{
  "items": [
    { "id": 123, "productId": 1, "variantId": 11, "name": "Product A", "price": 100000, "quantity": 2, "lineTotal": 200000 }
  ],
  "totalAmount": 200000
}
```

- OrderDto / OrderDetailDto

```json
{
  "orderCode": "ORD-20260527-001",
  "customerName": "Nguyen Van A",
  "status": "PENDING",
  "paymentStatus": "UNPAID",
  "totalAmount": 250000,
  "createdAt": "2026-05-27T10:00:00Z",
  "items": [ { "productId":1, "name":"Product A", "price":100000, "quantity":2 } ]
}
```

- VariantDto

```json
{ "id": 11, "sku": "PA-11", "price": 100000, "stock": 5, "attributes": { "size": "42", "color": "Black" } }
```

- AuthTokenResponse

```json
{ "accessToken":"...","refreshToken":"...","tokenType":"Bearer","expiresIn":3600,"refreshExpiresIn":604800 }
```

- IntrospectResponse

```json
{ "active": true, "username": "user01", "tokenType": "Bearer", "expiresAt": "2026-05-27T11:00:00Z" }
```

---

## Module: Authentication

1) POST /auth/login
  - Full: POST http://localhost:8080/e-commerce/auth/login
  - Auth: public
  - Request body (application/json):
    ```json
    { "username": "admin", "password": "Admin@12345" }
    ```
  - Response (200, ApiResponse<AuthTokenResponse>):
    ```json
    {
      "code": 1000,
      "message": "Login successful",
      "result": {
        "accessToken": "...",
        "refreshToken": "...",
        "tokenType": "Bearer",
        "expiresIn": 3600,
        "refreshExpiresIn": 604800
      }
    }
    ```

2) POST /auth/register
  - Full: POST http://localhost:8080/e-commerce/auth/register
  - Auth: public (xác nhận lại hành vi backend khi tích hợp)
  - Request body (application/json):
    ```json
    { "username":"user01","password":"12345678","email":"user01@gmail.com","fullName":"User 01" }
    ```
  - Response (201/200, ApiResponse<void> hoặc message):
    ```json
    { "code": 1000, "message": "Register successful" }
    ```

3) POST /auth/refresh
  - Full: POST http://localhost:8080/e-commerce/auth/refresh
  - Auth: public
  - Request body:
    ```json
    { "refreshToken": "..." }
    ```
  - Response: new AuthTokenResponse inside `result`.

4) POST /auth/logout
  - Full: POST http://localhost:8080/e-commerce/auth/logout
  - Auth: may require token
  - Request body:
    ```json
    { "token": "..." }
    ```
  - Response: `{ "code": 1000, "message": "Logout successful" }` or `LogoutResponse`.

5) POST /auth/introspect
  - Full: POST http://localhost:8080/e-commerce/auth/introspect
  - Request body: `{ "token": "..." }`
  - Response: `ApiResponse<IntrospectResponse>`

---

## Module: Products (Customer)

1) GET /search
  - Full: GET http://localhost:8080/e-commerce/search
  - Auth: public
  - Query params (common): `q`, `categoryId`, `brandId`, `minPrice`, `maxPrice`, `page`, `size`, `sort`
  - Response: `ApiResponse<Page<ProductDto>>` where `ProductDto` includes `id, name, price, image, status, stock, description, category, brand`.
  - Example response:
    ```json
    {
      "code": 1000,
      "message": "Success",
      "result": {
        "content": [{ "id": 1, "name": "Product A", "price": 100000 }],
        "totalPages": 5,
        "totalElements": 45,
        "number": 0
      }
    }
    ```

2) GET /product/{id}
  - Full: GET http://localhost:8080/e-commerce/product/{id}
  - Auth: public
  - Path param: `id`
  - Response: `ApiResponse<ProductDetailDto>`

---

## Module: Category / Brand (Customer)

1) GET /categories
  - Full: GET http://localhost:8080/e-commerce/categories
  - Auth: public
  - Response: `ApiResponse<List<CategoryDto>>` (chỉ các category đang bật)

2) GET /brands
  - Full: GET http://localhost:8080/e-commerce/brands
  - Auth: public
  - Response: `ApiResponse<List<BrandDto>>`

---

## Module: Users (Customer)

1) GET /users/my-info
  - Full: GET http://localhost:8080/e-commerce/users/my-info
  - Auth: required
  - Response: `ApiResponse<UserDto>` (id, username, email, fullName, phone, address, roles, status)

2) PATCH /users/update-info
  - Full: PATCH http://localhost:8080/e-commerce/users/update-info
  - Auth: required
  - Request body (partial update):
    ```json
    { "fullName": "Nguyen Van A", "phone": "0909123456", "address": "Hanoi" }
    ```
  - Response: `ApiResponse<void>` or updated `UserDto`.

3) PATCH /users/change-password
  - Full: PATCH http://localhost:8080/e-commerce/users/change-password
  - Auth: required
  - Body:
    ```json
    { "oldPassword": "old","newPassword": "New@12345" }
    ```

---

## Module: Cart

1) GET /cart-item/my-cart
  - Full: GET http://localhost:8080/e-commerce/cart-item/my-cart
  - Auth: required
  - Response: `ApiResponse<CartDto>` (items, totalAmount)

2) POST /cart-item/add
  - Full: POST http://localhost:8080/e-commerce/cart-item/add
  - Auth: required
  - Body:
    ```json
    { "productId": 10, "variantId": 5, "quantity": 2 }
    ```
  - Response: `ApiResponse<CartDto>` or message

3) DELETE /cart-item/remove/{ids}
  - Full: DELETE http://localhost:8080/e-commerce/cart-item/remove/{ids}
  - Auth: required
  - Path param: `ids` (comma-separated ids e.g. `1,2,3`)
  - Response: `204 No Content` or `ApiResponse` message

4) PATCH /cart-item/update
  - Full: PATCH http://localhost:8080/e-commerce/cart-item/update
  - Auth: required
  - Body:
    ```json
    { "id": 123, "quantity": 3 }
    ```

Notes: Một vài endpoint trả plain text message hoặc `204 No Content`. Frontend nên hỗ trợ cả 3 kiểu response.

---

## Module: Orders (Customer)

1) GET /orders/preview
  - Full: GET http://localhost:8080/e-commerce/orders/preview
  - Auth: required
  - Response: preview thông tin đơn hàng (cart -> fee -> summary)

2) POST /orders/checkout
  - Full: POST http://localhost:8080/e-commerce/orders/checkout
  - Auth: required
  - Body sample:
    ```json
    {
      "shippingAddress": { "fullName":"...","phone":"...","address":"..." },
      "paymentMethod": "COD",
      "notes":"Giao sau 2h"
    }
    ```
  - Response: `ApiResponse<OrderDto>` or message containing order id/code

3) GET /orders/my-orders
  - Full: GET http://localhost:8080/e-commerce/orders/my-orders
  - Auth: required
  - Query: `page,size,sort`
  - Response: `ApiResponse<Page<OrderDto>>`

4) GET /orders/my-orders/{orderId}
  - Full: GET http://localhost:8080/e-commerce/orders/my-orders/{orderId}
  - Auth: required
  - Response: `ApiResponse<OrderDetailDto>`

---

## Module: Products (Staff/Admin)

Lưu ý: các endpoint staff/admin yêu cầu role tương ứng theo `SecurityConfig`.

1) GET /staff/product/search
  - Full: GET http://localhost:8080/e-commerce/staff/product/search
  - Auth: staff/admin
  - Query: `q,page,size,sort,status`
  - Response: `ApiResponse<Page<ProductDto>>`

2) GET /staff/product/{id}
  - Get product detail for staff/admin

3) GET /staff/product/{productId}/variants
  - Get variants list

4) POST /staff/product/{productId}/variants
  - Create variant (body depends on backend model)

5) POST /staff/product/create
  - Create product (multipart or json depending on upload flow)

6) PUT /staff/product/update
  - Update product

7) PATCH /staff/product/{productId}/{status}
  - Update product status (enable/disable)

8) PATCH /staff/variant/{variantId}/{stockQuantity}
  - Update variant stock

---

## Module: Orders (Staff/Admin)

1) GET /orders/search
  - Full: GET http://localhost:8080/e-commerce/orders/search
  - Auth: staff/admin
  - Query: `q,status,page,size,sort,dateFrom,dateTo`

2) GET /orders/{orderId}
  - Get order detail for staff/admin

3) PATCH /orders/{orderId}/{status}
  - Update order status

---

## Module: Categories (Admin)

1) GET /admin/categories/search
  - Auth: admin
  - Query: `q,page,size,sort`

2) POST /admin/categories
  - Create category

3) PUT /admin/categories
  - Update category

4) PATCH /admin/categories/{categoryId}/{status}
  - Update category status

## Module: Brands (Admin)

1) GET /admin/brands/search
2) POST /admin/brands
3) PATCH /admin/brands/update
4) PATCH /admin/brands/{brandId}/{status}

---

## Module: Users (Admin)

1) GET /admin/users
  - List users (query: `q,role,page,size`)

2) GET /admin/users/customer/info/{id}
  - View customer info

3) PATCH /admin/users/update-role
  - Body sample:
    ```json
    { "userId": 12, "roles": ["ROLE_ADMIN"] }
    ```

4) PATCH /admin/users/{id}/{status}
  - Update user active/inactive

---

## Module: Dashboard / Statistics (Admin)

1) GET /admin/statistics/overview
  - Summary metrics: `totalUsers, totalProducts, totalOrders, revenue`

2) POST /admin/statistics/orders
  - Body: filters (dateFrom/dateTo, groupBy)

3) POST /admin/statistics/revenue
  - Body: filters

---

## Module: Cloudinary / Upload

1) GET /cloudinary/signature?folder=...
  - Full: GET http://localhost:8080/e-commerce/cloudinary/signature?folder=products
  - Auth: staff/admin
  - Response: signature payload (timestamp, api_key, signature, folder, uploadUrl etc.) to allow direct upload from client to Cloudinary

---

## Error handling guide (frontend)

- 401: redirect to `/login` or call refresh token flow
- 403: show `unauthorized` page or toast
- 404: show resource not found UI
- 409: show conflict message
- 500: show generic server error

---

## Tips khi implement frontend service layer

- Tạo một HTTP client wrapper (Axios/fetch) để:
  - tự động gắn `Authorization` header
  - map `ApiResponse<T>` -> T hoặc throw lỗi khi code khác không phải success
  - handle 204/empty body

- Tách service theo domain: `authService`, `productService`, `cartService`, `orderService`, `adminService`, `cloudinaryService`.

---

## Next steps (optional)

Nếu bạn muốn, mình có thể:
- chuyển mỗi endpoint thành bảng chi tiết (path, params, request example, response example) để xuất ra OpenAPI stub hoặc Postman collection.
- hoặc generate TypeScript interfaces và service functions mẫu dựa trên các DTO được liệt kê.
