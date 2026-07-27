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


## Temp upload & rename workflow

1. Client tải ảnh trực tiếp lên Cloudinary vào thư mục `temp_uploads/` và nhận được `url`/`public_id`.
2. Backend lưu thực thể (product, user, …) với `imageUrl` = URL tạm và trong **cùng transaction** tạo bản ghi `rename_tasks` chứa `public_id` tạm, thư mục đích (`products/`, `users/`...), kiểu thực thể và `entityId`.
3. Trả về `200 OK` cho client.
4. `RenameTaskProcessor` (được schedule mỗi phút) thực hiện:
   - Đổi tên tài nguyên Cloudinary (`renameResource`).
   - Cập nhật URL thực thể qua `EntityUrlUpdater`.
   - Đánh dấu task `SUCCESS` hoặc `FAILED` (sau max retries).
5. `TempUploadCleanupJob` chạy hàng ngày, xóa các tài nguyên trong `temp_uploads/` có `created_at` > 24h và không có task hoặc thực thể tham chiếu.

## Rename task queue
- Bảng `rename_tasks` (Flyway migration `V10__create_rename_tasks_table.sql`).
- Entity `RenameTask` và repository `RenameTaskRepository`.
- Service `RenameTaskCreator` để tạo task khi lưu thực thể.
- Processor `RenameTaskProcessor` xử lý queue.
- `EntityUrlUpdater` cập nhật URL cho các thực thể (`Product`, `User`).

## Cleanup job
- `TempUploadCleanupJob` thực hiện hai bước: (a) Cloudinary side cleanup, (b) DB side: đánh dấu các task `PENDING` cũ > retention thành `FAILED`.
