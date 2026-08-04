# V3.8.6 - Cải thiện tìm kiếm sản phẩm với Fuzziness và đa ngôn ngữ

**Ngày tạo:** 2026-08-04
**API thay đổi:** `GET /api/v1/customer/products/search`
**Mức độ thay đổi:** Nhỏ (Non-breaking change)

## Chi tiết thay đổi

- **Cải thiện thuật toán tìm kiếm (Backend):** 
  - Đã cập nhật logic xử lý `keyword` cho API tìm kiếm sản phẩm của Customer.
  - Hỗ trợ tìm kiếm từ khóa trên cả 2 trường: `name` và `description`.
  - Hỗ trợ tìm kiếm tiếng Việt có dấu, tiếng Việt không dấu (unaccent) và tiếng Anh (english) sử dụng Elasticsearch `multi_match`.
  - Bật tính năng `fuzziness` (tự động sửa lỗi chính tả nhẹ).
  
## Hướng dẫn cho Frontend

- **Không có thay đổi về Contract (Request/Response)**: Cấu trúc gửi lên và nhận về giữ nguyên.
- Frontend không cần sửa đổi mã nguồn.
- QA/Tester có thể thử nghiệm gõ sai lỗi chính tả nhẹ hoặc gõ không dấu để thấy kết quả trả về chính xác hơn so với trước đây.
