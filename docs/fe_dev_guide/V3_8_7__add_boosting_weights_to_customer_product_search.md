# V3.8.7 - Thêm trọng số (boosting) cho tìm kiếm sản phẩm

**Ngày tạo:** 2026-08-04
**API thay đổi:** `GET /api/v1/customer/products/search`
**Mức độ thay đổi:** Nhỏ (Non-breaking change)

## Chi tiết thay đổi

- **Cải thiện thuật toán tìm kiếm (Backend):** 
  - Đã thêm trọng số (boosting) cho các trường khi tìm kiếm theo `keyword`.
  - Thứ tự ưu tiên điểm số (relevance score) từ cao xuống thấp: 
    1. Tên sản phẩm tiếng Việt có dấu (`name^6`)
    2. Tên sản phẩm không dấu (`name.unaccent^5`)
    3. Tên sản phẩm tiếng Anh (`name.english^4`)
    4. Mô tả tiếng Việt có dấu (`description^3`)
    5. Mô tả không dấu (`description.unaccent^2`)
    6. Mô tả tiếng Anh (`description.english^1`)
  
## Hướng dẫn cho Frontend

- **Không thay đổi Contract**: Request và Response không thay đổi.
- Khi user tìm kiếm và không sort theo trường cụ thể (sử dụng sort mặc định theo điểm relevance của Elasticsearch), kết quả trả về sẽ chính xác với nhu cầu hơn (sản phẩm khớp tên có dấu sẽ lên đầu).
