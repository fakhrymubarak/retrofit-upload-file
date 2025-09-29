# Go File Upload Server

A simple Go backend server for handling multipart file uploads from Android applications.

## Quick Start

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Run the server:**
   ```bash
   go run server.go
   ```

3. **Server will start on:** `http://localhost:8080`

## Requirements

- **Go 1.21 or later**
- No external dependencies (uses only standard library)

### Installing Go

If you don't have Go installed, follow these steps:

1. **Download Go** from [https://golang.org/dl/](https://golang.org/dl/)
2. **Install Go** following the instructions for your operating system
3. **Verify installation**:
   ```bash
   go version
   ```
   Should output: `go version go1.21.x` or later

## Features

- ✅ Multipart file upload handling
- ✅ CORS support for Android apps
- ✅ File size validation (100MB default)
- ✅ Automatic directory creation
- ✅ Unique filename generation
- ✅ Health check endpoint
- ✅ Environment variable configuration

## API Endpoints

- `POST /file` - Upload a file
- `GET /health` - Health check

## Configuration

Set environment variables to customize the server:

```bash
# Custom port
PORT=8081 go run server.go

# Custom upload directory
UPLOAD_DIR=/path/to/uploads go run server.go

# Custom max file size (in bytes)
MAX_FILE_SIZE=52428800 go run server.go  # 50MB
```

## Testing

Test the server with curl:

```bash
curl -X POST -F "image=@/path/to/your/image.jpg" http://localhost:8080/file
```

## File Storage

Uploaded files are saved to the `uploads/` directory with timestamped filenames:
- Format: `{timestamp}_{original_filename}`
- Example: `1703123456_myimage.jpg`
