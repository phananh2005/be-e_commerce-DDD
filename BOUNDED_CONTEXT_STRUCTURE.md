# E-Commerce Backend - Bounded Context Architecture

## Overview

This project uses **Domain-Driven Design (DDD)** principles with **Bounded Contexts** to organize the codebase. Each bounded context is a separate, cohesive business domain with clear responsibilities and minimal dependencies on other contexts.

## Directory Structure

```
src/main/java/com/phananh/e_commerce/
├── modules/
│   ├── core/                          # Shared infrastructure across all contexts
│   │   ├── exception/                 # Global exception handling
│   │   ├── infrastructure/            # Shared infrastructure config
│   │   └── util/                      # Shared utilities
│   │
│   ├── productcatalog/                # 🏷️  Product Catalog Bounded Context
│   │   ├── domain/
│   │   │   ├── model/                 # Domain entities (Product, Category, Brand, etc.)
│   │   │   ├── repository/            # Repository interfaces
│   │   │   └── enums/                 # Domain enums (ProductStatus, etc.)
│   │   ├── application/
│   │   │   ├── dto/
│   │   │   │   ├── request/           # Request DTOs (CreateProductRequest, etc.)
│   │   │   │   └── response/          # Response DTOs (ProductResponse, etc.)
│   │   │   ├── service/               # Service interfaces
│   │   │   ├── service/impl/          # Service implementations
│   │   │   └── mapper/                # MapStruct mappers
│   │   ├── infrastructure/
│   │   │   └── persistence/
│   │   │       ├── repository/        # Spring Data JPA repositories
│   │   │       └── specification/     # JPA Specifications for queries
│   │   └── presentation/
│   │       └── controller/            # REST controllers
│   │
│   ├── order/                         # 📦 Order Management Bounded Context
│   │   ├── domain/
│   │   │   ├── model/                 # Domain entities (Order, OrderItem, CartItem)
│   │   │   ├── repository/            # Repository interfaces
│   │   │   └── enums/                 # Domain enums (OrderStatus, etc.)
│   │   ├── application/
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── service/
│   │   │   ├── service/impl/
│   │   │   └── mapper/
│   │   ├── infrastructure/
│   │   │   └── persistence/
│   │   │       ├── repository/
│   │   │       └── specification/
│   │   └── presentation/
│   │       └── controller/
│   │
│   ├── usermanagement/                # 👤 User Management Bounded Context
│   │   ├── domain/
│   │   │   ├── model/                 # Domain entities (User, Role)
│   │   │   └── repository/            # Repository interfaces
│   │   ├── application/
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── service/
│   │   │   ├── service/impl/
│   │   │   └── mapper/
│   │   ├── infrastructure/
│   │   │   └── persistence/
│   │   │       ├── repository/
│   │   │       └── specification/
│   │   └── presentation/
│   │       └── controller/
│   │
│   ├── authentication/                # 🔐 Authentication Bounded Context
│   │   ├── domain/
│   │   │   └── model/                 # Authentication domain models
│   │   ├── application/
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   └── response/
│   │   │   ├── service/
│   │   │   ├── service/impl/
│   │   │   └── mapper/
│   │   ├── infrastructure/
│   │   │   └── config/                # Security configuration, JwtDecoder, etc.
│   │   └── presentation/
│   │       └── controller/
│   │
│   └── dashboard/                     # 📊 Dashboard & Analytics Bounded Context
│       ├── application/
│       │   ├── dto/
│       │   │   └── response/
│       │   ├── service/
│       │   └── service/impl/
│       └── presentation/
│           └── controller/
│
├── ECommerceApplication.java          # Main Spring Boot application
└── (old structure to be migrated)
```

## Bounded Contexts Explained

### 1. **Core Module** 🛠️
**Purpose**: Shared infrastructure and utilities used across all bounded contexts.

**Responsibilities**:
- Exception handling and error codes
- Cross-cutting infrastructure configurations
- Utility functions and helpers
- Common base classes

**Key Components**:
- Global exception handlers
- Shared configuration classes
- Helper utilities

---

### 2. **Product Catalog** 🏷️
**Purpose**: Manages product-related operations and catalog information.

**Entities**:
- `Product` - Product information
- `Category` - Product categories
- `Brand` - Brand information
- `ProductVariant` - Product variants (size, color, etc.)
- `ProductAttribute` - Product attributes
- `VariantImage` - Variant images

**Services**:
- `ProductService` - Product management
- `CategoryService` - Category management
- `BrandService` - Brand management

**Responsibilities**:
- Product creation, update, deletion
- Category and brand management
- Product search and filtering
- Variant management

---

### 3. **Order Management** 📦
**Purpose**: Manages order and shopping cart operations.

**Entities**:
- `Order` - Order information
- `OrderItem` - Items in an order
- `CartItem` - Items in shopping cart

**Services**:
- `OrderService` - Order management
- `CartItemService` - Shopping cart management

**Responsibilities**:
- Order creation and processing
- Shopping cart management
- Order status tracking
- Invoice generation

---

### 4. **User Management** 👤
**Purpose**: Manages user profiles and roles.

**Entities**:
- `User` - User account information
- `Role` - User roles and permissions

**Services**:
- `UserService` - User management
- `RoleService` - Role management

**Responsibilities**:
- User registration and profile management
- User activation/deactivation
- Role assignment and management
- User preferences

---

### 5. **Authentication** 🔐
**Purpose**: Handles authentication and authorization.

**Services**:
- `AuthenticationService` - Authentication logic
- `CustomJwtDecoder` - JWT token decoding
- `SecurityConfig` - Spring Security configuration

**Responsibilities**:
- User login/logout
- JWT token generation and validation
- OAuth2 configuration
- Security policy enforcement

---

### 6. **Dashboard & Analytics** 📊
**Purpose**: Provides dashboards and analytics for business intelligence.

**Services**:
- `StatisticsService` - Statistics aggregation
- `DashboardController` - Dashboard endpoints

**Responsibilities**:
- Order statistics
- Product analytics
- Sales reports
- User statistics

---

## Layered Architecture within Each Context

Each bounded context follows a **4-layer architecture**:

### **1. Domain Layer** (`domain/`)
Contains the core business logic and rules of a bounded context.

**Subdirectories**:
- `model/` - Entity classes (JPA entities)
- `repository/` - Repository interfaces (contracts for data access)
- `enums/` - Domain enums and constants

**Characteristics**:
- Pure business logic
- No Spring annotations (except JPA)
- Independent of infrastructure details
- Should be highly testable

### **2. Application Layer** (`application/`)
Orchestrates business operations using domain entities and services.

**Subdirectories**:
- `dto/request/` - Request DTOs for API input
- `dto/response/` - Response DTOs for API output
- `service/` - Service interfaces defining operations
- `service/impl/` - Service implementations
- `mapper/` - MapStruct mappers for DTO ↔ Entity conversion

**Characteristics**:
- Contains business workflows
- Coordinates domain logic
- Handles DTO transformations
- Spring service beans

### **3. Infrastructure Layer** (`infrastructure/`)
Implements technical details and external integrations.

**Subdirectories**:
- `persistence/repository/` - Spring Data JPA repository implementations
- `persistence/specification/` - JPA Specifications for complex queries
- `config/` - Context-specific configuration (Authentication uses this)

**Characteristics**:
- Database operations
- External service integrations
- Technical configurations
- Spring Data repositories

### **4. Presentation Layer** (`presentation/`)
Handles HTTP requests and responses.

**Subdirectories**:
- `controller/` - REST controllers with `@RestController` and `@RequestMapping`

**Characteristics**:
- REST endpoints
- HTTP request/response handling
- Status code management
- Input validation

---

## Dependencies Between Contexts

### Allowed Dependencies:
```
presentation/ → application/ → domain/ → (no external)
               ↓
            infrastructure/ → (no other contexts)
```

### Context Interaction:
- **Contexts should NOT directly depend on each other's domain models**
- Communication happens through **DTOs** or **Events**
- Each context has its own DTOs and mappers
- If cross-context data is needed, use **Anti-Corruption Layer** pattern

### Example:
```
Order Context needs Product information
    ↓
Order calls ProductService through interface
    ↓ (returns ProductResponse DTO)
Order converts to its own internal representation
```

---

## Migration Guide

### Moving Existing Code

**From old structure to bounded contexts:**

1. **Identify the context** - Which bounded context does this class belong to?
2. **Choose the layer** - Is it domain, application, infrastructure, or presentation?
3. **Create package path** - Follow the structure above
4. **Move the file** - Use IDE refactoring tools
5. **Update imports** - IDE will prompt for import changes
6. **Update Spring component scanning** - If necessary

**Example**:
```
OLD: com.phananh.e_commerce.application.service.ProductService
NEW: com.phananh.e_commerce.modules.productcatalog.application.service.ProductService

OLD: com.phananh.e_commerce.domain.model.entity.Product
NEW: com.phananh.e_commerce.modules.productcatalog.domain.model.Product
```

---

## Best Practices

### 1. **Package Naming**
- Use lowercase, single names for bounded contexts
- Use descriptive names: `productcatalog`, `usermanagement`, `order`

### 2. **Dependency Injection**
- Inject service interfaces, not implementations
- Use `@Autowired` or constructor injection

### 3. **DTO Design**
- Create specific DTOs for each endpoint
- Don't reuse entity classes as DTOs
- Prefix with context name when used across contexts

### 4. **Repository Patterns**
- Define repository interfaces in domain layer
- Use Spring Data JPA in infrastructure layer
- Keep repository logic focused on persistence

### 5. **Service Layer**
- Services should be interfaces with implementations
- Keep services focused on one responsibility
- Use dependency injection, avoid `new` keyword

### 6. **Exception Handling**
- Use custom exceptions from `core.exception` package
- Handle exceptions at controller level
- Return appropriate HTTP status codes

---

## Configuration in Spring Boot

### Component Scanning
Ensure your main application class scans all module packages:

```java
@SpringBootApplication(scanBasePackages = {
    "com.phananh.e_commerce.modules"
})
public class ECommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceApplication.class, args);
    }
}
```

### Configuration Classes
Place shared configurations in `core.infrastructure`:
- DataSource config
- JPA/Hibernate config
- Cache config

Place context-specific configurations in their own `infrastructure` package:
- Authentication security config
- Context-specific beans

---

## Adding a New Bounded Context

1. Create root package: `modules/mynewcontext/`
2. Follow the layer structure (domain, application, infrastructure, presentation)
3. Create package-info.java files
4. Implement domain entities and repository interfaces
5. Implement application services
6. Implement infrastructure repositories
7. Create REST controllers
8. Update Spring component scanning if necessary

---

## Summary of Changes

✅ **Organized code by business domain (bounded contexts)**
✅ **Clear separation of concerns with 4-layer architecture**
✅ **Improved maintainability and testability**
✅ **Easier to understand business logic**
✅ **Supports independent scaling of contexts**
✅ **Follows DDD best practices**

---

## Next Steps

1. Run your IDE's refactoring tools to move classes to new packages
2. Update imports throughout the project
3. Verify Spring component scanning works correctly
4. Test all endpoints to ensure nothing broke
5. Update any configuration files if needed

