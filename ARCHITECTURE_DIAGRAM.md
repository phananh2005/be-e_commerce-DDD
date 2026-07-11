# Architecture Diagram

Sơ đồ cấu trúc package hiện tại.

```text
com.phananh.e_commerce/
├── ECommerceApplication.java
├── core/
├── productcatalog/
├── product/
├── order/
├── usermanagement/
├── authentication/
└── dashboard/
```

## Luồng xử lý
`Controller` -> `Service/Application` -> `Domain` -> `Infrastructure` -> `Database`

## Ghi chú
- Package gốc: `com.phananh.e_commerce`
- Mỗi context tuân thủ kiến trúc phân lớp (Layered Architecture)