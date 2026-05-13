# Cloudinary Implementation

## Overview

Cloudinary is integrated into the e-commerce application to handle image and file uploads. It provides cloud-based storage and optimization for media files with features like automatic resizing, format conversion, and CDN delivery.

## Architecture

### Components

1. **CloudinaryConfig** (`infrastructure.config`)
   - Spring Configuration class that creates a Cloudinary bean
   - Initializes Cloudinary with credentials from application properties

2. **CloudinaryService** (`application.service`)
   - Service interface defining file upload operations
   - Public API for file upload functionality

3. **CloudinaryServiceImpl** (`application.service.impl`)
   - Implementation of CloudinaryService
   - Handles the actual upload logic to Cloudinary

## Setup Instructions

### 1. Add Cloudinary Dependency

Add the following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>com.cloudinary</groupId>
    <artifactId>cloudinary-http44</artifactId>
    <version>1.33.0</version>
</dependency>
```

### 2. Configure Cloudinary Credentials

Add the following properties to `application.properties`:

```properties
# Cloudinary Configuration
cloudinary.cloud_name=your_cloud_name
cloudinary.api_key=your_api_key
cloudinary.api_secret=your_api_secret
```

**How to get Cloudinary credentials:**
- Create an account at [Cloudinary](https://cloudinary.com)
- Go to Dashboard → Settings → API Keys
- Copy your Cloud Name, API Key, and API Secret

### 3. Security Considerations

⚠️ **IMPORTANT:** Never commit API credentials to version control. Use environment variables in production:

```properties
# For production, use environment variables
cloudinary.cloud_name=${CLOUDINARY_CLOUD_NAME}
cloudinary.api_key=${CLOUDINARY_API_KEY}
cloudinary.api_secret=${CLOUDINARY_API_SECRET}
```

## Usage

### Uploading Files

Inject `CloudinaryService` into your controller or service:

```java
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "e-commerce") String folder,
            @RequestParam(value = "publicId", required = false) String publicId) {
        try {
            String fileUrl = cloudinaryService.uploadFile(file, folder, publicId);
            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }
}
```

### Method Signature

```java
String uploadFile(MultipartFile file, String folder, String publicId) throws IOException;
```

**Parameters:**
- `file` (MultipartFile): The file to upload (required)
- `folder` (String): Cloudinary folder path for organization (e.g., "products", "users")
- `publicId` (String): Custom identifier for the file in Cloudinary (optional)

**Returns:**
- `String`: The secure URL of the uploaded file

**Throws:**
- `IOException`: If an error occurs during the upload process

## Upload Configuration

The implementation uses the following upload parameters:

```java
Map<String, Object> uploadParams = new HashMap<>();
uploadParams.put("folder", folder);                    // Organize files in folders
uploadParams.put("resource_type", "auto");             // Auto-detect file type
uploadParams.put("unique_filename", false);            // Use custom public_id
uploadParams.put("public_id", publicId);               // Custom identifier
uploadParams.put("overwrite", true);                   // Overwrite if exists
uploadParams.put("invalidate", true);                  // Invalidate CDN cache
```

## Features

### Automatic Processing
- **Format Conversion**: Automatically convert images to optimal formats
- **Responsive Images**: Generate multiple sizes for responsive design
- **CDN Delivery**: Global CDN for fast content delivery
- **Secure URLs**: Uses HTTPS for all served content

### File Organization
- Files are organized by `folder` parameter
- Example folder structure:
  - `products/` - Product images
  - `users/` - User profile pictures
  - `categories/` - Category images

## Error Handling

The service includes comprehensive error handling:

| Error | Cause | Solution |
|-------|-------|----------|
| `Empty file` | File is empty | Ensure file has content before upload |
| `Blank publicId` | Invalid public ID | Provide valid non-blank public ID |
| `IOException` | Network or upload error | Check Cloudinary credentials and network connectivity |

## Logging

The implementation uses SLF4J for logging:

```
[INFO] File uploaded successfully to Cloudinary: https://res.cloudinary.com/...
[ERROR] Error uploading file to Cloudinary: [error details]
```

## Best Practices

1. **Folder Organization**: Use meaningful folder names to organize files
   ```
   products/category-id/
   users/user-id/
   categories/
   ```

2. **Public ID Convention**: Use descriptive public IDs based on content
   ```
   product-123-thumbnail
   user-456-avatar
   ```

3. **Validation**: Always validate file type and size on the client side before uploading

4. **Error Handling**: Implement retry logic for failed uploads

5. **Performance**: Consider async uploads for better UX
   ```java
   @Async
   public CompletableFuture<String> uploadFileAsync(MultipartFile file, String folder, String publicId)
   ```

## Examples

### Product Image Upload

```java
// Upload product main image
String imageUrl = cloudinaryService.uploadFile(
    productImage,
    "products/product-123",
    "main-image"
);
```

### User Avatar Upload

```java
// Upload user profile picture
String avatarUrl = cloudinaryService.uploadFile(
    avatarFile,
    "users/avatars",
    "user-456"
);
```

### Category Banner

```java
// Upload category banner image
String bannerUrl = cloudinaryService.uploadFile(
    bannerFile,
    "categories",
    "electronics-banner"
);
```

## URL Structure

Uploaded files are accessible via secure URLs:

```
https://res.cloudinary.com/{CLOUD_NAME}/image/upload/f_auto,q_auto/v{VERSION}/{FOLDER}/{PUBLIC_ID}.{FORMAT}
```

Example:
```
https://res.cloudinary.com/dc3gsivt6/image/upload/f_auto,q_auto/v1234567890/products/product-123/main-image.jpg
```

## Resources

- [Cloudinary Documentation](https://cloudinary.com/documentation)
- [Cloudinary Java SDK](https://github.com/cloudinary/cloudinary_java)
- [API Reference](https://cloudinary.com/documentation/image_upload_api_reference)

## Troubleshooting

### Common Issues

1. **Authentication Failed**
   - Verify credentials in application.properties
   - Check if API key and secret are correct

2. **Upload Timeout**
   - Check network connectivity
   - Verify file size is reasonable
   - Consider increasing timeout in configurations

3. **File Not Found After Upload**
   - Ensure folder path is correct
   - Check if file was actually uploaded (check logs)
   - Verify Cloudinary account has sufficient storage

### Debug Mode

Enable debug logging in `application.properties`:

```properties
logging.level.com.cloudinary=DEBUG
logging.level.com.phananh.e_commerce.application.service=DEBUG
```

## Performance Optimization

### Image Transformations

Use Cloudinary URL parameters for on-the-fly transformations:

```
https://res.cloudinary.com/{CLOUD_NAME}/image/upload/w_300,h_300,c_fill/v{VERSION}/{FOLDER}/{PUBLIC_ID}
```

Parameters:
- `w_300` - Width 300px
- `h_300` - Height 300px
- `c_fill` - Crop to fill
- `f_auto` - Auto format selection
- `q_auto` - Auto quality optimization

## Security

### Credentials Management

- **Never** hardcode credentials in source files
- **Always** use environment variables in production
- **Rotate** API keys regularly
- **Monitor** API usage in Cloudinary dashboard

### Access Control

- Use restricted API keys for production
- Implement proper authentication on upload endpoints
- Validate file types on server side

---

**Last Updated**: May 2026
