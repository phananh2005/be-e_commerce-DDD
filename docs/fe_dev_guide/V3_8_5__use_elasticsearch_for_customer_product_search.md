---
version: V3_8_5
date: 2026-08-02
breaking_change: false
---

# Use Elasticsearch for Customer Product Search

## APIs Affected
- `GET /api/v1/customer/products` (CustomerProductController)

## Summary of Changes
- Đã thay đổi core xử lý tìm kiếm trong `CustomerProductServiceImpl.java` hàm `getProductsActiveBySearch`.
- Chuyển từ query database truyền thống sang sử dụng Elasticsearch với `ProductDocument`.
- Request và Response contract hoàn toàn giữ nguyên, không có breaking change.
- Kết quả tìm kiếm cho `keyword` sẽ tận dụng được analyzer của Elasticsearch (`vi_analyzer`, `vi_smart_unaccent`).

## Frontend Action Required
- **KHÔNG CẦN THAY ĐỔI CODE.**
- Không cần điều chỉnh gọi API hay xử lý response. Việc cập nhật hoàn toàn trong suốt với Frontend.
