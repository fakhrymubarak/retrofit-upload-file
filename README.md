# Android File Upload with Retrofit

A modern Android application demonstrating file upload functionality using Retrofit, Jetpack
Compose, and Hilt dependency injection. This project showcases real-time upload progress tracking
and clean architecture principles.

## 🚀 Features

- **File Upload**: Upload images to a backend server using Retrofit
- **Real-time Progress**: Track upload progress with a progress bar
- **Modern UI**: Built with Jetpack Compose and Material 3
- **Clean Architecture**: MVVM pattern with Repository pattern
- **Dependency Injection**: Hilt for dependency management
- **Progress Tracking**: Custom `ProgressFlowRequestBody` for upload progress
- **Error Handling**: Comprehensive error handling with user feedback
- **Cancellation Support**: Ability to cancel ongoing uploads

## 🏗️ Architecture

This project follows clean architecture principles:

```
📁 presentation/
├── 🎨 screen/ - Jetpack Compose UI screens
├── 🧠 viewmodel/ - ViewModels for state management
├── 📊 model/ - UI state models
└── 🎨 theme/ - Material 3 theming

📁 data/
├── 🌐 remote/ - API interfaces and constants
└── 📦 repository/ - Data repositories and network logic

📁 core/
└── 🛠️ util/ - Utility classes (FileProvider)

📁 injection/
├── 🏗️ app/ - Application class
└── 🔧 provide/ - Hilt modules

📁 backend/
├── 🚀 server.go - Go backend server
├── 📚 SERVICES.md - Backend documentation
├── 📖 README.md - Backend setup guide
└── 📦 go.mod - Go module file
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Repository Pattern
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Async**: Coroutines + Flow
- **Material Design**: Material 3

## 📋 Prerequisites

- Android Studio Ladybug or later
- Android SDK 21+ (Android 5.0)
- Kotlin 1.8+
- Backend server running (see Backend Setup section)

## 🔧 Backend Setup

**Important**: This Android app requires a backend server to handle file uploads.

### Use the Included Go Backend

This project includes a simple Go backend server in the `backend/` directory:

1. **Navigate to the backend directory**:
   ```bash
   cd backend
   ```

2. **Run the Go server**:
   ```bash
   go run server.go
   ```

3. **Server will start on**: `http://localhost:8080`


### Verify the Backend is Running

- The backend should be accessible at `http://localhost:8080`
- The Android app is configured to connect to `http://0.0.0.0:8080/` (which maps to localhost)
- Test with: `curl http://localhost:8080/health`

## 🚀 Running the Backend

### Quick Start (Go Backend)

1. **Navigate to the backend directory**:
   ```bash
   cd backend
   ```

2. **Run the Go server**:
   ```bash
   go run server.go
   ```

3. **Server will start on**: `http://localhost:8080`

### Backend Configuration

The Go backend supports environment variables for customization:

```bash
# Custom port
PORT=8081 go run server.go

# Custom upload directory
UPLOAD_DIR=/path/to/uploads go run server.go

# Custom max file size (in bytes)
MAX_FILE_SIZE=209715200 go run server.go  # 200MB
```

### Testing the Backend

Test the server with curl:

```bash
# Health check
curl http://localhost:8080/health

# File upload test
curl -X POST -F "image=@/path/to/your/image.jpg" http://localhost:8080/file
```

## 🚀 Getting Started

1. **Clone this repository**:
   ```bash
   git clone https://github.com/fakhrymubarak/retrofit-upload-file.git
   cd retrofit-upload-file
   ```

2. **Open in Android Studio**:
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Sync the project**:
   - Wait for Gradle sync to complete
   - Ensure all dependencies are downloaded

4. **Start the backend server** (see Backend Setup section above)

5. **Run the app**:
   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio
   - The app will install and launch

## 📱 How to Use

1. **Launch the app** on your Android device/emulator
2. **Tap "UPLOAD IMAGE"** to start uploading the sample image
3. **Watch the progress** as the upload progresses with a real-time progress bar
4. **Cancel if needed** by tapping "CANCEL UPLOAD" during upload
5. **View results** - Success or error messages will be displayed

## 🔍 Key Components

### FileApi

```kotlin
@Multipart
@POST("/file")
suspend fun uploadImage(@Part image: MultipartBody.Part)
```

### ProgressFlowRequestBody

Custom `RequestBody` implementation that tracks upload progress:

- Reads file in chunks
- Emits progress updates via Flow
- Provides real-time progress feedback

### ImageUploadViewModel

- Manages upload state (Initial, Loading, Success, Error)
- Handles progress tracking
- Supports upload cancellation
- Uses Hilt for dependency injection

### FileProvider

- Provides sample image from assets
- Creates temporary file for upload
- Handles file operations on background thread

## 🎨 UI States

The app handles four main UI states:

1. **INITIAL**: Shows "UPLOAD IMAGE" button
2. **LOADING**: Shows progress bar, percentage, and "CANCEL UPLOAD" button
3. **SUCCESS**: Shows "UPLOAD IMAGE" button (ready for next upload)
4. **ERROR**: Shows "ERROR, REUPLOAD" button for retry

## 🔧 Configuration

### Base URL

The app is configured to connect to `http://0.0.0.0:8080/` in `FileApiConst.kt`. To change the
server URL:

```kotlin
object FileApiConst {
   const val BASE_URL = "http://your-server-url:port/"
}
```

### Network Security Configuration

The app includes a network security configuration (`res/xml/network_security_config.xml`) that
allows HTTP traffic for development servers:

- **localhost** and **127.0.0.1** - for local development
- **0.0.0.0** - for the configured backend server
- **10.0.2.2** - for Android emulator host access

For production, you should update the network security config to only allow HTTPS traffic to your
production domains.

### Sample Image

The app uses a sample image from `assets/image/books_weapon.jpg`. You can replace this with your own
image.

## 🐛 Troubleshooting

### Common Issues

1. **Connection Refused**: Ensure the backend server is running
2. **Upload Fails**: Check network connectivity and server status
3. **Build Errors**: Clean and rebuild the project
4. **Dependency Issues**: Sync project with Gradle files

### BASE_URL Configuration Issues

The app's BASE_URL configuration depends on whether you're using an emulator or physical device:

#### For Android Emulator:
- **Current Configuration**: `http://0.0.0.0:8080/` ✅ (Works correctly)
- **Alternative**: `http://10.0.2.2:8080/` (Android emulator host mapping)
- **Why**: Emulator maps `10.0.2.2` to the host machine's `localhost`

#### For Physical Device:
- **Required Configuration**: `http://YOUR_COMPUTER_IP:8080/`
- **Find your IP**: 
  ```bash
  # Windows
  ipconfig
  
  # macOS/Linux
  ifconfig | grep "inet "
  # or
  ipconfig getifaddr en0
  ```
- **Example**: `http://192.168.1.100:8080/`
- **Why**: Physical devices need the actual IP address of your computer

#### How to Change BASE_URL:

1. **Open**: `app/src/main/java/xyz/teamgravity/retrofitfileupload/data/remote/constant/FileApiConst.kt`
2. **Update the BASE_URL**:
   ```kotlin
   object FileApiConst {
       // For emulator (current)
       const val BASE_URL = "http://0.0.0.0:8080/"
       
       // For physical device (replace with your computer's IP)
       const val BASE_URL = "http://192.168.1.100:8080/"
   }
   ```

#### Network Security Configuration:

The app's network security config (`res/xml/network_security_config.xml`) already includes:
- `localhost` and `127.0.0.1` - for emulator
- `0.0.0.0` - for current configuration
- `10.0.2.2` - for emulator host access

For physical devices, you may need to add your computer's IP to the network security config.

### Debug Tips

- Check Android Studio's Logcat for error messages
- Verify the backend server is accessible from your device/emulator
- Ensure both devices are on the same network (for physical devices)
- Test server connectivity: `curl http://YOUR_IP:8080/health`
- Check firewall settings on your computer

## 📚 Learning Resources

This project demonstrates several Android development concepts:

- **Retrofit**: HTTP client for API communication
- **Jetpack Compose**: Modern UI toolkit
- **Hilt**: Dependency injection framework
- **Coroutines**: Asynchronous programming
- **Flow**: Reactive streams
- **MVVM**: Architecture pattern
- **Material 3**: Design system

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENCE](LICENCE) file for details.

## 🙏 Acknowledgments

- Backend server: [philipplackner/FileUpload](https://github.com/philipplackner/FileUpload.git)
- Material 3 design system
- Android development community

---

**Note**: Make sure to run the backend server
from [philipplackner/FileUpload](https://github.com/philipplackner/FileUpload.git) before testing
the Android app, as the app requires a server to handle file uploads.