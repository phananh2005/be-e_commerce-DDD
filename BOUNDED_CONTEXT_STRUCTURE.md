# Bounded Context Structure

Tài liệu này mô tả cấu trúc bounded context hiện tại của dự án theo package thật trong `src/main/java/com/phananh/e_commerce`.

## Các context chính
- `core` — exception, util, cấu hình dùng chung
- `productcatalog` — quản lý danh mục, thương hiệu
- `product` — quản lý sản phẩm, biến thể, thuộc tính
- `order` — đơn hàng, giỏ hàng, thanh toán
- `usermanagement` — người dùng, vai trò, tài khoản
- `authentication` — xác thực, token, security
- `dashboard` — thống kê, báo cáo

## Quy ước lớp
- `domain` — entity, repository contract, enum nghiệp vụ
- `application` — service, DTO, mapper, orchestrator
- `infrastructure` — Spring Data, config, persistence
- `presentation` — REST controller, request/response DTO

## Cấu trúc thực tế
```text
com.phananh.e_commerce/
├── core/
├── productcatalog/
├── product/
├── order/
├── usermanagement/
├── authentication/
└── dashboard/