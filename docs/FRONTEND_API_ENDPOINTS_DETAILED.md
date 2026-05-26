# Detailed API Endpoints (Frontend) — Bảng chi tiết cho Postman / OpenAPI

Base URL: `http://localhost:8080/e-commerce`

Hướng dẫn: mỗi bảng gồm Method, Path, Auth, Query/Path params, Request body mẫu, Response mẫu (ApiResponse wrapper). Tập trung các endpoint quan trọng để frontend bắt tay vào code nhanh.

---

## Authentication

### POST /auth/login
- Method: POST
- Path: `/auth/login`
- Auth: public
- Request (application/json):

```json
{ "username": "admin", "password": "Admin@12345" }
```

- Response (200, ApiResponse<AuthTokenResponse>):

```json
{
  "code": 1000,
  "message": "Login successful",
  "result": { "accessToken": "...", "refreshToken": "...", "tokenType": "Bearer", "expiresIn": 3600, "refreshExpiresIn": 604800 }
}
```

Notes: store `accessToken` (short-lived) and `refreshToken` (secure storage) according to chosen strategy.

---

### POST /auth/refresh
- Method: POST
- Path: `/auth/refresh`
- Auth: public
- Request:

```json
{ "refreshToken": "..." }
```

- Response (200, ApiResponse<AuthTokenResponse>):

```json
{ "code": 1000, "message": "Refreshed", "result": { "accessToken":"...","refreshToken":"...","expiresIn":3600 } }
```

Notes: frontend should call this when receiving 401 and attempt one refresh before redirecting to login.

---

### POST /auth/logout
- Method: POST
- Path: `/auth/logout`
- Auth: Bearer
- Request:

```json
{ "token": "<refreshToken>" }
```

- Response (200):

```json
{ "code": 1000, "message": "Logout successful" }
```

---

## Products (Customer)

### GET /search
- Method: GET
- Path: `/search`
- Auth: public
- Query params:
  - `q` (string)
  - `categoryId` (number)
  - `brandId` (number)
  - `minPrice` (number)
  - `maxPrice` (number)
  - `page` (0-based)
  - `size`
  - `sort` (e.g. `createdAt,desc`)

- Response (200, ApiResponse<Page<ProductDto>>):

```json
{
  "code": 1000,
  "message": "Success",
  "result": {
    "content": [ { "id": 1, "name": "Product A", "price": 100000 } ],
    "totalPages": 5,
    "totalElements": 45,
    "number": 0
  }
}
```

Notes: frontend maps `result.content` to list and pagination fields for UI.

---

### GET /product/{id}
- Method: GET
- Path: `/product/{id}`
- Auth: public
- Path params: `id` (number)
- Response (200, ApiResponse<ProductDetailDto>)

```json
{ "code":1000, "message":"Success", "result": { "id":1, "name":"Product A", "description":"..." } }
```

---

## Cart

### GET /cart-item/my-cart
- Method: GET
- Path: `/cart-item/my-cart`
- Auth: Bearer
- Request: none
- Response (200, ApiResponse<CartDto>):

```json
{ "code":1000, "message":"Success", "result": { "items": [ { "id":123, "productId":1, "quantity":2 } ], "totalAmount":200000 } }
```

---

### POST /cart-item/add
- Method: POST
- Path: `/cart-item/add`
- Auth: Bearer
- Body:

```json
{ "productId": 10, "variantId": 5, "quantity": 2 }
```

- Response (200 or 201): ApiResponse<CartDto> or message

---

### DELETE /cart-item/remove/{ids}
- Method: DELETE
- Path: `/cart-item/remove/{ids}` (ids comma-separated)
- Auth: Bearer
- Response: `204 No Content` or ApiResponse message

---

### PATCH /cart-item/update
- Method: PATCH
- Path: `/cart-item/update`
- Auth: Bearer
- Body:

```json
{ "id": 123, "quantity": 3 }
```

- Response: ApiResponse<CartDto>

---

## Orders (Customer)

### GET /orders/preview
- Method: GET
- Path: `/orders/preview`
- Auth: Bearer
- Response: ApiResponse<OrderPreviewDto>

### POST /orders/checkout
- Method: POST
- Path: `/orders/checkout`
- Auth: Bearer
- Body sample:

```json
{
  "shippingAddress": { "fullName":"Nguyen Van A","phone":"0909123456","address":"Hanoi" },
  "paymentMethod": "COD",
  "notes": "Giao sau 2h"
}
```

- Response (200/201): ApiResponse<OrderDto> or message with orderCode

---

## Admin / Staff (selected endpoints)

### GET /admin/statistics/overview
- Method: GET
- Path: `/admin/statistics/overview`
- Auth: Bearer (ROLE_ADMIN)
- Response: ApiResponse<OverviewDto> e.g.

```json
{ "code":1000, "message":"OK", "result": { "totalUsers":100, "totalProducts":250, "totalOrders":500, "revenue": 12345678 } }
```

### GET /admin/categories/search
- Method: GET
- Path: `/admin/categories/search`
- Auth: Bearer (ROLE_ADMIN)
- Query: `q,page,size,sort`
- Response: ApiResponse<Page<CategoryDto>>

---

## Cloudinary

### GET /cloudinary/signature?folder=...
- Method: GET
- Path: `/cloudinary/signature`
- Auth: Bearer (ROLE_STAFF/ROLE_ADMIN)
- Query: `folder`
- Response: ApiResponse<CloudinarySignatureDto> e.g.

```json
{ "code":1000, "message":"OK", "result": { "timestamp": 165..., "api_key":"...", "signature":"...", "folder":"products", "uploadUrl":"https://api.cloudinary.com/v1_1/.../upload" } }
```

---

## Notes for exporter
- These tables cover the main endpoints. Nếu muốn Postman/OpenAPI đầy đủ, mình có thể generate một Postman collection JSON hoặc OpenAPI YAML từ cùng dữ liệu này — báo mình để mình xuất file tương ứng.

