<h1 align="center">🛒 E-Commerce Backend (Domain-Driven Design)</h1>

<p align="center">
  <i>Hệ thống Backend Thương Mại Điện Tử hiệu năng cao, được thiết kế theo chuẩn Domain-Driven Design (DDD) và Clean Architecture với Spring Boot 3 & Java 21.</i>
</p>

---

## 🎯 Giới thiệu Dự án

Dự án này được xây dựng với mục tiêu cung cấp một giải pháp backend toàn diện, mở rộng và dễ bảo trì cho nền tảng thương mại điện tử. Điểm nổi bật của dự án là việc áp dụng khắt khe các triết lý thiết kế phần mềm hiện đại như **Domain-Driven Design (DDD)** và **Clean Architecture**, giúp tách biệt hoàn toàn logic nghiệp vụ cốt lõi (Core Business) khỏi các chi tiết công nghệ (Infrastructure).

Đây không chỉ là một dự án hoàn chỉnh về mặt tính năng (từ quản lý sản phẩm, giỏ hàng, đơn hàng đến xác thực người dùng), mà còn là một minh chứng về khả năng áp dụng các mẫu thiết kế (Design Patterns), tối ưu hóa hiệu năng và quản lý kiến trúc phần mềm trong các hệ thống doanh nghiệp phức tạp.

## 🌟 Điểm nhấn Kỹ thuật (Technical Highlights)

- **Domain-Driven Design (DDD):** Hệ thống được chia nhỏ thành các **Bounded Contexts** độc lập (Product, Order, User, Authentication...), giúp chia nhỏ độ phức tạp nghiệp vụ và tạo tiền đề dễ dàng nâng cấp lên Microservices trong tương lai.
- **Clean Architecture:** Mỗi Bounded Context tuân thủ kiến trúc đa tầng (Presentation -> Application -> Domain -> Infrastructure). Logic nghiệp vụ (Domain) không phụ thuộc vào bất kỳ framework bên ngoài nào.
- **Tối ưu Hiệu năng & Cache:** Sử dụng **Redis** để caching các dữ liệu thường xuyên truy cập, giảm tải cho Database.
- **Giao tiếp Bất đồng bộ (Async Messaging):** Tích hợp **RabbitMQ** để xử lý các tác vụ nền, đảm bảo hệ thống phản hồi nhanh gọn và chịu tải tốt.
- **Tìm kiếm Nâng cao:** Sử dụng **Elasticsearch** cho các chức năng tìm kiếm sản phẩm phức tạp, đảm bảo tốc độ truy vấn text cực nhanh.
- **Lưu trữ Đám mây:** Tích hợp **Cloudinary** để upload và quản lý tài nguyên (hình ảnh) tối ưu, tích hợp CDN tự động.

## 🛠 Tech Stack

| Thành phần | Công nghệ sử dụng |
| :--- | :--- |
| **Core** | Java 21, Spring Boot 3.x |
| **Database** | MySQL 8.0, Spring Data JPA |
| **Caching** | Redis |
| **Message Broker** | RabbitMQ |
| **Search Engine** | Elasticsearch |
| **Bảo mật (Security)** | Spring Security, JWT (JSON Web Token) |
| **Lưu trữ (Storage)** | Cloudinary |
| **Hạ tầng (Infra)** | Docker, Docker Compose |

## 📦 Kiến trúc Bounded Contexts

Dự án được phân rã thành các module chính:

- `productcatalog` & `product`: Quản lý toàn bộ vòng đời sản phẩm, thương hiệu, danh mục, biến thể và thuộc tính phức tạp.
- `order`: Xử lý nghiệp vụ giỏ hàng, đặt hàng, thanh toán và quản lý luồng trạng thái đơn hàng.
- `usermanagement`: Xử lý hồ sơ, địa chỉ, lịch sử của người dùng.
- `authentication`: Xử lý xác thực người dùng, sinh/xác thực JWT và phân quyền (RBAC).
- `dashboard`: Các API phục vụ thống kê, báo cáo và phân tích dữ liệu cho Admin.

## 📚 Tài liệu Thiết kế

Để tìm hiểu sâu hơn về cách hệ thống được thiết kế, vui lòng tham khảo các tài liệu chuyên sâu:
- 🏗 [Thiết kế Bounded Contexts](BOUNDED_CONTEXT_STRUCTURE.md) - Cách phân rã nghiệp vụ.
- 📊 [Sơ đồ Luồng & Package](ARCHITECTURE_DIAGRAM.md) - Cấu trúc Clean Architecture.
- 📖 [Tài liệu API (Swagger)](SWAGGER_DOCUMENTATION_SUMMARY.md) - Tổng hợp các Endpoint.
- ☁️ [Tích hợp Cloudinary](CLOUDINARY_IMPLEMENTATION.md)
- 📁 Các tài liệu kỹ thuật khác xem tại thư mục [`docs/`](docs/README.md).

## 🚀 Hướng dẫn Chạy (Getting Started)

### 1. Yêu cầu hệ thống
- **Java 21**
- **Docker** và **Docker Compose**

### 2. Khởi động Hạ tầng
Hệ thống sử dụng file `docker-compose.yml` để khởi tạo toàn bộ hạ tầng (MySQL, Redis, RabbitMQ, Elasticsearch).

```powershell
# Bật Docker và chạy:
docker volume create db-mysql_db_data
docker-compose up -d
```
*(Các service sẽ chạy ở background. Đảm bảo các port 3306, 6379, 5672, 9200 trống).*

### 3. Khởi động Ứng dụng
Bạn có thể mở dự án bằng IntelliJ IDEA hoặc chạy trực tiếp bằng Maven Wrapper:

```powershell
# Chạy ứng dụng Spring Boot
.\mvnw.cmd spring-boot:run
```

---
*Dự án được thiết kế để dễ dàng đọc, mở rộng và bảo trì - phản ánh chuẩn mực mã nguồn của một Kỹ sư Phần mềm.*
