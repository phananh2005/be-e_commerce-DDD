# V1.1.6: Migrate Public IDs to UUID

**Ngày tạo:** 2026-07-22

**Phạm vi:** API responses cho User, Product, Order

**Loại thay đổi:** Breaking Change (đổi structure response)

## Tóm tắt thay đổi

Thêm trường `uuid` (UUID string) làm public identifier cho 3 entities chính:
- User
- Product
- Order

Trường `id` (Long) vẫn tồn tại nhưng không được expose trong API response nữa.

## Chi tiết API thay đổi

### 1. User API Responses

**Trước (V0):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "fullName": "John Doe"
}
```

**Sau (V1.1.6):**
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "username": "john_doe",
  "email": "john@example.com",
  "fullName": "John Doe"
}
```

**Thay đổi:**
- Xóa field `id`
- Thêm field `uuid` (String format)

### 2. Product API Responses

**Trước (V0):**
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Description",
  "status": "ACTIVE",
  "avatarUrl": "http://...",
  "variants": [
    {
      "id": 10,
      "skuCode": "SKU123",
      "price": 100.00
    }
  ]
}
```

**Sau (V1.1.6):**
```json
{
  "uuid": "660e8400-e29b-41d4-a716-446655440001",
  "name": "Product Name",
  "description": "Description",
  "status": "ACTIVE",
  "avatarUrl": "http://...",
  "variants": [
    {
      "uuid": "770e8400-e29b-41d4-a716-446655440002",
      "skuCode": "SKU123",
      "price": 100.00
    }
  ]
}
```

**Thay đổi:**
- Xóa field `id`
- Thêm field `uuid` cho Product
- Thêm field `uuid` cho ProductVariant (trong array variants)

### 3. Order API Responses

**Trước (V0):**
```json
{
  "id": 1,
  "userId": 1,
  "totalPrice": 500.00,
  "status": "PENDING",
  "orderItems": [
    {
      "id": 5,
      "variantId": 10,
      "quantity": 2,
      "price": 250.00
    }
  ]
}
```

**Sau (V1.1.6):**
```json
{
  "uuid": "880e8400-e29b-41d4-a716-446655440003",
  "userUuid": "550e8400-e29b-41d4-a716-446655440000",
  "totalPrice": 500.00,
  "status": "PENDING",
  "orderItems": [
    {
      "uuid": "990e8400-e29b-41d4-a716-446655440004",
      "variantUuid": "770e8400-e29b-41d4-a716-446655440002",
      "quantity": 2,
      "price": 250.00
    }
  ]
}
```

**Thay đổi:**
- Xóa field `id` từ Order
- Thêm field `uuid` cho Order
- Xóa field `userId` và thay bằng `userUuid`
- Xóa field `id` từ OrderItem
- Thêm field `uuid` cho OrderItem
- Xóa field `variantId` và thay bằng `variantUuid`

## Hướng dẫn cập nhật Frontend

### 1. Cập nhật API Service/Client

Mọi endpoint trả về User, Product, Order cần update:

```javascript
// Trước
const user = await getUser(1);

// Sau
const user = await getUser('550e8400-e29b-41d4-a716-446655440000');
```

### 2. Cập nhật Data Models/Types

```typescript
interface User {
  uuid: string;
  username: string;
  email: string;
  fullName: string;
}

interface Product {
  uuid: string;
  name: string;
  variants: ProductVariant[];
}

interface ProductVariant {
  uuid: string;
  skuCode: string;
  price: number;
}

interface Order {
  uuid: string;
  userUuid: string;
  totalPrice: number;
  orderItems: OrderItem[];
}

interface OrderItem {
  uuid: string;
  variantUuid: string;
  quantity: number;
  price: number;
}
```

### 3. Cập nhật component/page state

- Tất cả reference đến entity ID phải chuyển sang UUID
- Nếu lưu trữ user/product/order ID trong localStorage, Redux, Context - cần update
- Nếu dùng ID để routing, cần update URL params

### 4. Cập nhật API calls

```javascript
axios.get(`/api/users/${userUuid}`)
axios.post(`/api/orders/${orderUuid}/cancel`, data)
```

### 5. Cập nhật UI display

Nếu FE hiển thị ID ở bất kỳ đâu, xóa hoặc che dấu nó. UUID không thân thiện với user.

## Ảnh hưởng tới các màn hình/flow

- **User Profile:** Tất cả reference đến user ID → userUuid
- **Product Catalog:** Tất cả reference đến product ID → productUuid
- **Product Details:** Variants ID → variantUuid
- **Shopping Cart:** Chứa product/variant UUIDs
- **Order History:** Order ID, OrderItem ID → UUIDs
- **Checkout:** Send order với variant UUIDs
- **Admin Dashboard:** Tất cả reports/lists dùng UUIDs

## Breaking Change

✅ **Đây là breaking change.** Nếu FE không cập nhật, tất cả API calls dùng ID sẽ fail hoặc nhận sai data.

## Phiên bản Backend

Backend version: >= 1.0.0 (với UUID support)

Hãy đảm bảo backend đã deployed trước khi cập nhật FE.

## Kiểm tra

Sau khi cập nhật, test các flow sau:

- [ ] User login/profile page hiển thị đúng
- [ ] Product list/detail page hoạt động
- [ ] Add product to cart
- [ ] Checkout và tạo order
- [ ] View order history
- [ ] Admin view products/orders/users

---

**Ghi chú:** File này được generate tự động. Nếu cần clarify, hãy check Backend API documentation.
