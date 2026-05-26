# API Errors & Client Handling Patterns (Frontend)

Tài liệu này mô tả cấu trúc lỗi phổ biến, mẫu `ApiError` và pattern xử lý ở client (với Axios example).

## 1) Các status code thường gặp & cách xử lý

- 400 Bad Request
  - Hiện thông thường kèm message hoặc validation errors. Hiển thị form validation nếu có.

- 401 Unauthorized
  - Hành động: cố gắng gọi `/auth/refresh` (1 lần), nếu thành công retry request; nếu thất bại -> redirect `/login`.

- 403 Forbidden
  - Hiển thị trang/notification `Không có quyền`.

- 404 Not Found
  - Hiển thị empty state / 404 page.

- 409 Conflict
  - Hiển thị toast `Xung đột dữ liệu` và gợi ý làm mới.

- 500 Server Error
  - Hiển thị thông báo hệ thống; khuyến cáo retry hoặc liên hệ admin.

## 2) Mẫu `ApiError` (response body)

Một số API trả body dạng JSON error, ví dụ:

```json
{
  "code": 1401,
  "message": "Validation failed",
  "errors": [ { "field": "email", "message": "Invalid email" } ]
}
```

Frontend nên chuẩn hoá thành interface:

```ts
interface ApiErrorPayload {
  code: number;
  message: string;
  errors?: Array<{ field?: string; message: string }>;
}
```

## 3) Axios handling pattern (summary)

- Request interceptor: attach `Authorization` header
- Response interceptor:
  - If status 200 but `ApiResponse.code` indicates business error -> throw custom error with message and payload
  - If status 401 -> attempt refresh token (only once), retry original request
  - If refresh fails -> clear tokens and redirect to login

### Example: throw business error when ApiResponse.code !== 1000

In services above we unwrap `resp.data.result`. For business error check `resp.data.code` and throw custom error:

```ts
if (!resp.data) throw new Error('Empty response');
if (resp.data.code !== 1000) {
  const payload = resp.data as ApiErrorPayload;
  const e = new Error(payload.message || 'Business error');
  (e as any).payload = payload;
  throw e;
}
```

## 4) UI integration tips

- Show global toast for `message` from ApiResponse when appropriate
- For form errors, map `errors` array -> show under input fields
- For long-running requests show skeleton / spinner; disable submit button while pending
- Centralize error handling so components can call `throw` and a top-level error boundary or hook shows consistent UI

## 5) Security notes

- Store `accessToken` in memory or `httpOnly` cookie if backend supports it; if using `localStorage`, protect against XSS
- `refreshToken` should be stored securely; if possible use httpOnly cookie to avoid JS access

---

Nếu muốn, mình có thể:
- (1) tạo `postman_collection.json` dựa trên file `FRONTEND_API_ENDPOINTS_DETAILED.md` để import vào Postman;
- (2) chuyển toàn bộ TypeScript stubs vào một repo template React+TS (Vite) kèm script mẫu `npm install` / `npm run dev`.

