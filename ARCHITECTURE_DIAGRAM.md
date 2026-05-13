# E-Commerce Architecture - Complete Package Structure Diagram

```
com.phananh.e_commerce/
│
├── 📦 ECommerceApplication.java
│
└── modules/
    │
    ├── 🔧 core/                                 [SHARED INFRASTRUCTURE]
    │   ├── exception/
    │   │   ├── AppException.java
    │   │   ├── ErrorCode.java
    │   │   ├── GlobalExceptionHandler.java
    │   │   └── package-info.java
    │   │
    │   ├── infrastructure/
    │   │   ├── ApplicationInitConfig.java
    │   │   ├── JpaAuditingConfig.java
    │   │   ├── CloudinaryConfig.java
    │   │   ├── OpenApiConfig.java
    │   │   └── package-info.java
    │   │
    │   ├── util/
    │   │   ├── SecurityUtils.java
    │   │   └── package-info.java
    │   │
    │   └── package-info.java
    │
    │
    ├── 🏷️  productcatalog/                     [PRODUCT CATALOG CONTEXT]
    │   ├── package-info.java
    │   │
    │   ├── domain/
    │   │   ├── model/
    │   │   │   ├── Product.java
    │   │   │   ├── Category.java
    │   │   │   ├── Brand.java
    │   │   │   ├── ProductVariant.java
    │   │   │   ├── ProductAttribute.java
    │   │   │   ├── AttributeValue.java
    │   │   │   ├── VariantImage.java
    │   │   │   ├── BaseEntity.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── enums/
    │   │   │   ├── ProductStatus.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── ProductRepository.java (interface)
    │   │   │   ├── CategoryRepository.java (interface)
    │   │   │   ├── BrandRepository.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (no service interfaces in domain - move to application)
    │   │
    │   ├── application/
    │   │   ├── service/
    │   │   │   ├── ProductService.java (interface)
    │   │   │   ├── CategoryService.java (interface)
    │   │   │   ├── BrandService.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── service/impl/
    │   │   │   ├── ProductServiceImpl.java
    │   │   │   ├── CategoryServiceImpl.java
    │   │   │   ├── BrandServiceImpl.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/request/
    │   │   │   ├── CreateProductRequest.java
    │   │   │   ├── UpdateProductRequest.java
    │   │   │   ├── CreateCategoryRequest.java
    │   │   │   ├── UpdateCategoryRequest.java
    │   │   │   ├── CreateBrandRequest.java
    │   │   │   ├── UpdateBrandRequest.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/response/
    │   │   │   ├── ProductResponse.java
    │   │   │   ├── ProductDetailResponse.java
    │   │   │   ├── CategoryResponse.java
    │   │   │   ├── BrandResponse.java
    │   │   │   ├── ProductVariantResponse.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── mapper/
    │   │   │   ├── ProductMapper.java
    │   │   │   ├── CategoryMapper.java
    │   │   │   ├── BrandMapper.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (application layer orchestrates business logic)
    │   │
    │   ├── infrastructure/
    │   │   ├── persistence/repository/
    │   │   │   ├── ProductRepository.java (extends Spring Data)
    │   │   │   ├── CategoryRepository.java
    │   │   │   ├── BrandRepository.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── persistence/specification/
    │   │   │   ├── ProductSpecification.java
    │   │   │   ├── CategorySpecification.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (infrastructure handles technical details)
    │   │
    │   └── presentation/
    │       ├── controller/
    │       │   ├── ProductController.java (@RestController)
    │       │   ├── CategoryController.java
    │       │   ├── BrandController.java
    │       │   └── package-info.java
    │       │
    │       └── (presentation handles HTTP requests/responses)
    │
    │
    ├── 📦 order/                              [ORDER MANAGEMENT CONTEXT]
    │   ├── package-info.java
    │   │
    │   ├── domain/
    │   │   ├── model/
    │   │   │   ├── Order.java
    │   │   │   ├── OrderItem.java
    │   │   │   ├── CartItem.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── enums/
    │   │   │   ├── OrderStatus.java
    │   │   │   ├── PaymentStatus.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── OrderRepository.java (interface)
    │   │   │   ├── CartItemRepository.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (domain contains business rules)
    │   │
    │   ├── application/
    │   │   ├── service/
    │   │   │   ├── OrderService.java (interface)
    │   │   │   ├── CartItemService.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── service/impl/
    │   │   │   ├── OrderServiceImpl.java
    │   │   │   ├── CartItemServiceImpl.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/request/
    │   │   │   ├── CreateOrderRequest.java
    │   │   │   ├── AddToCartRequest.java
    │   │   │   ├── UpdateCartItemRequest.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/response/
    │   │   │   ├── OrderResponse.java
    │   │   │   ├── CartResponse.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── mapper/
    │   │   │   ├── OrderMapper.java
    │   │   │   ├── CartItemMapper.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (application coordinates operations)
    │   │
    │   ├── infrastructure/
    │   │   ├── persistence/repository/
    │   │   │   ├── OrderRepository.java (Spring Data)
    │   │   │   ├── OrderItemRepository.java
    │   │   │   ├── CartItemRepository.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── persistence/specification/
    │   │   │   ├── OrderSpecification.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (infrastructure implements technical details)
    │   │
    │   └── presentation/
    │       ├── controller/
    │       │   ├── OrderController.java
    │       │   ├── CartController.java
    │       │   └── package-info.java
    │       │
    │       └── (presentation exposes REST APIs)
    │
    │
    ├── 👤 usermanagement/                     [USER MANAGEMENT CONTEXT]
    │   ├── package-info.java
    │   │
    │   ├── domain/
    │   │   ├── model/
    │   │   │   ├── User.java
    │   │   │   ├── Role.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java (interface)
    │   │   │   ├── RoleRepository.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (domain contains user business rules)
    │   │
    │   ├── application/
    │   │   ├── service/
    │   │   │   ├── UserService.java (interface)
    │   │   │   ├── RoleService.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── service/impl/
    │   │   │   ├── UserServiceImpl.java
    │   │   │   ├── RoleServiceImpl.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/request/
    │   │   │   ├── RegisterUserRequest.java
    │   │   │   ├── UpdateProfileRequest.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/response/
    │   │   │   ├── UserResponse.java
    │   │   │   ├── RoleResponse.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── mapper/
    │   │   │   ├── UserMapper.java
    │   │   │   ├── RoleMapper.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (application orchestrates user workflows)
    │   │
    │   ├── infrastructure/
    │   │   ├── persistence/repository/
    │   │   │   ├── UserRepository.java (Spring Data)
    │   │   │   ├── RoleRepository.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── persistence/specification/
    │   │   │   ├── UserSpecification.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (infrastructure implements persistence)
    │   │
    │   └── presentation/
    │       ├── controller/
    │       │   ├── UserController.java
    │       │   └── package-info.java
    │       │
    │       └── (presentation exposes user APIs)
    │
    │
    ├── 🔐 authentication/                     [AUTHENTICATION CONTEXT]
    │   ├── package-info.java
    │   │
    │   ├── domain/
    │   │   ├── model/
    │   │   │   ├── RefreshToken.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (minimal domain, mostly configuration)
    │   │
    │   ├── application/
    │   │   ├── service/
    │   │   │   ├── AuthenticationService.java (interface)
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── service/impl/
    │   │   │   ├── AuthenticationServiceImpl.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/request/
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── RefreshTokenRequest.java
    │   │   │   ├── IntrospectRequest.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── dto/response/
    │   │   │   ├── LoginResponse.java
    │   │   │   ├── TokenResponse.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   ├── mapper/
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (application handles auth service)
    │   │
    │   ├── infrastructure/
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java
    │   │   │   ├── CustomJwtDecoder.java
    │   │   │   ├── JwtProvider.java
    │   │   │   └── package-info.java
    │   │   │
    │   │   └── (infrastructure handles security)
    │   │
    │   └── presentation/
    │       ├── controller/
    │       │   ├── AuthenticationController.java
    │       │   └── package-info.java
    │       │
    │       └── (presentation exposes auth endpoints)
    │
    │
    └── 📊 dashboard/                          [DASHBOARD & ANALYTICS CONTEXT]
        ├── package-info.java
        │
        ├── application/
        │   ├── service/
        │   │   ├── StatisticsService.java (interface)
        │   │   └── package-info.java
        │   │
        │   ├── service/impl/
        │   │   ├── StatisticsServiceImpl.java
        │   │   └── package-info.java
        │   │
        │   ├── dto/response/
        │   │   ├── StatisticsResponse.java
        │   │   ├── DashboardResponse.java
        │   │   ├── SalesStatsResponse.java
        │   │   └── package-info.java
        │   │
        │   └── (application aggregates statistics)
        │
        └── presentation/
            ├── controller/
            │   ├── DashboardController.java
            │   └── package-info.java
            │
            └── (presentation exposes analytics endpoints)
```

## 📊 Layer Communication Flow

```
Request
  ↓
[Presentation Layer] ← REST Controllers
  ↓
[Application Layer] ← Services, DTOs, Mappers
  ↓
[Domain Layer] ← Entities, Business Rules
  ↓
[Infrastructure Layer] ← Database, External Services
  ↓
Response
```

## 🔄 Cross-Context Communication Pattern

```
Order Context                           Product Context
      ↓                                        ↑
  [OrderService]  ────requests──────→  [ProductService]
      ↓                                        ↑
  Uses Product DTO                   Returns Product DTO
  (NOT Product Entity)                     
```

## 📈 Dependency Hierarchy

```
Level 0 (Lowest): Domain Layer
  ├── No dependencies on other layers
  ├── Pure business logic
  └── Highly testable

Level 1: Infrastructure Layer
  ├── Depends on: Domain
  ├── Technical implementation
  └── Database access

Level 2: Application Layer
  ├── Depends on: Domain, Infrastructure
  ├── Business workflows
  └── DTO transformations

Level 3 (Highest): Presentation Layer
  ├── Depends on: Application, Domain
  ├── HTTP handling
  └── Request validation
```

## 🎯 Entity Ownership Matrix

```
┌─────────────────────┬──────────────────────────────────┐
│   Bounded Context   │      Owned Entities              │
├─────────────────────┼──────────────────────────────────┤
│ Product Catalog     │ Product, Category, Brand         │
│                     │ ProductVariant, ProductAttribute │
│                     │ AttributeValue, VariantImage     │
├─────────────────────┼──────────────────────────────────┤
│ Order Management    │ Order, OrderItem, CartItem       │
├─────────────────────┼──────────────────────────────────┤
│ User Management     │ User, Role                       │
├─────────────────────┼──────────────────────────────────┤
│ Authentication      │ (Uses User from User Management) │
├─────────────────────┼──────────────────────────────────┤
│ Dashboard           │ (Aggregates from all contexts)   │
└─────────────────────┴──────────────────────────────────┘
```

## 🚀 Service Discovery Map

```
Product Catalog Services:
  ├── ProductService
  ├── CategoryService
  └── BrandService

Order Management Services:
  ├── OrderService
  └── CartItemService

User Management Services:
  ├── UserService
  └── RoleService

Authentication Services:
  └── AuthenticationService

Dashboard Services:
  └── StatisticsService

Cloud Services:
  └── CloudinaryService (in core.infrastructure or authentication)
```

---

**Last Updated:** 2026-05-13
**Status:** Ready for implementation ✅

