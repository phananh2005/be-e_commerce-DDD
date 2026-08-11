# Thay đổi cấu trúc JSON phân trang toàn cục

- **Version**: V4.0.0
- **Ngày tạo**: 2026-08-11
- **Loại thay đổi**: Cập nhật (Breaking Change)
- **API ảnh hưởng**: TẤT CẢ các API GET trả về danh sách phân trang (sử dụng Spring `Page`).

## Chi tiết thay đổi

Cập nhật cấu trúc JSON trả về của đối tượng phân trang để khắc phục lỗi warning của Spring Boot 3.3.

**Trước đây (PageImpl directly):**
```json
{
  "content": [ ... ],
  "pageable": { ... },
  "last": true,
  "totalPages": 1,
  "totalElements": 5,
  "size": 10,
  "number": 0,
  "sort": { ... },
  "first": true,
  "numberOfElements": 5,
  "empty": false
}
```

**Hiện tại (PagedModel VIA_DTO):**
```json
{
  "content": [ ... ],
  "page": {
    "size": 10,
    "number": 0,
    "totalElements": 5,
    "totalPages": 1
  }
}
```

## Hướng dẫn FE cần làm gì

Tất cả các thành phần UI (table, danh sách, pagination component) đang đọc các trường metadata ở root của response phải cập nhật để đọc từ object `page` con:
- `response.totalPages` -> `response.page.totalPages`
- `response.totalElements` -> `response.page.totalElements`
- `response.size` -> `response.page.size`
- `response.number` -> `response.page.number`

## Breaking change
**CÓ**. FE phải cập nhật đường dẫn parse response nếu không UI phân trang sẽ lỗi.
