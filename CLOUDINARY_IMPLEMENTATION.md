# Cloudinary Implementation

Tài liệu mô tả tích hợp Cloudinary.

## Cấu hình
- Bean Cloudinary được khởi tạo trong `core/infrastructure/config`.
- Service xử lý upload nằm tại `application/service`.

## Properties
```properties
cloudinary.cloud_name=...
cloudinary.api_key=...
cloudinary.api_secret=...
```

## Quy ước thư mục
- `products/` — ảnh sản phẩm
- `users/` — avatar người dùng
- `categories/` — ảnh danh mục