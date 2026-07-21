# Frontend Development Guide Index

Quản lý các phiên bản hướng dẫn phát triển Frontend theo kiểu Flyway migration.

## Các phiên bản

| Version | Ngày tạo | Thay đổi | Breaking Change |
|---------|----------|---------|-----------------|
| V1_1_6 | 2026-07-22 | Gộp migration response identifiers sang UUID | Có |
| V1_1_5 | 2026-07-21 | Cập nhật request/response management product search | Có |
| V1_1_4 | 2026-07-21 | Thêm field `cancellationReason` vào OrderDetailResponse | Không |
| V1_1_3 | 2026-07-21 | Bắt buộc parameter `folder` trong cloudinary signature | Có |
| V1_1_2 | 2026-07-21 | Đổi tên trường `keyword` → `name` trong brand/category search | Có |
| V1.1.1 | 2026-07-21 | Thêm bộ lọc `userId` vào management order search | Không |
| V1.1.0 | 2026-07-21 | Refactor OrderDetailResponse với addressInfo inner class | Có |
| V1.0.9 | 2026-07-21 | Tách hàm `getOrderDetailForManagement` không kiểm tra ownership | Không |
| V1.0.8 | 2026-07-21 | Thêm xác thực ownership cho customer order detail | Không |
| V1.0.6 | 2026-07-20 | Thêm fields `email` và `createdAt` vào user info response | Không |
| V1.0.5 | 2026-07-20 | Thêm xác thực quyền role-based cho get user info | Không |
| V1.0.4 | 2026-07-20 | Thêm trường `status` và `updatedAt` vào order search response | Không |
| V1.0.3 | 2026-07-19 | Simplify management order search và thêm product items | Có |
| V1.0.2 | 2026-07-19 | Thêm order creation date range filter | Không |
| V1.0.1 | 2026-07-19 | Thêm management order filters | Không |
| V1.0.0 | 2026-07-22 | Initial FE development guide | Không |

## Hướng dẫn

Mỗi file là một phiên bản migration. Frontend phải cập nhật theo thứ tự.

### Cách cập nhật

1. Kiểm tra phiên bản hiện tại của FE project
2. Tìm file migration tiếp theo chưa áp dụng
3. Đọc file migration và thực hiện các thay đổi
4. Test lại toàn bộ flow liên quan
5. Commit và push

### File naming convention

```
V{major}_{minor}_{patch}__{short_summary}.md
```

Ví dụ: `V1_0_0__add_uuid_public_id.md`
