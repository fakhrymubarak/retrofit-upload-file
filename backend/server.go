package main

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"strconv"
	"time"
)

const (
	defaultPort        = "8080"
	defaultUploadDir   = "./uploads"
	defaultMaxFileSize = 100 << 20 // 100MB
)

var (
	uploadDir   string
	maxFileSize int64
)

func main() {
	// Get configuration from environment variables
	port := getEnv("PORT", defaultPort)
	uploadDir = getEnv("UPLOAD_DIR", defaultUploadDir)

	// Parse max file size from environment
	if maxSizeStr := os.Getenv("MAX_FILE_SIZE"); maxSizeStr != "" {
		if size, err := strconv.ParseInt(maxSizeStr, 10, 64); err == nil {
			maxFileSize = size
		} else {
			maxFileSize = defaultMaxFileSize
		}
	} else {
		maxFileSize = defaultMaxFileSize
	}

	// Create upload directory if it doesn't exist
	if err := os.MkdirAll(uploadDir, 0755); err != nil {
		log.Fatalf("Failed to create upload directory: %v", err)
	}

	// Setup routes
	http.HandleFunc("/file", handleFileUpload)
	http.HandleFunc("/health", handleHealth)

	// Start server
	log.Printf("Server starting on port %s", port)
	log.Printf("Upload directory: %s", uploadDir)
	log.Printf("Max file size: %d bytes", maxFileSize)

	if err := http.ListenAndServe(":"+port, nil); err != nil {
		log.Fatalf("Server failed to start: %v", err)
	}
}

func handleFileUpload(w http.ResponseWriter, r *http.Request) {
	// Set CORS headers
	setCORSHeaders(w)

	// Only allow POST requests
	if r.Method != http.MethodPost {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	// Parse multipart form
	if err := r.ParseMultipartForm(maxFileSize); err != nil {
		log.Printf("Error parsing multipart form: %v", err)
		http.Error(w, "Failed to parse multipart form", http.StatusBadRequest)
		return
	}

	// Get the file from the form
	file, handler, err := r.FormFile("image")
	if err != nil {
		log.Printf("Error getting file from form: %v", err)
		http.Error(w, "No file provided or invalid file field", http.StatusBadRequest)
		return
	}
	defer file.Close()

	// Validate file size
	if handler.Size > maxFileSize {
		log.Printf("File too large: %d bytes (max: %d)", handler.Size, maxFileSize)
		http.Error(w, "File too large", http.StatusRequestEntityTooLarge)
		return
	}

	// Generate unique filename
	timestamp := time.Now().Unix()
	filename := fmt.Sprintf("%d_%s", timestamp, handler.Filename)
	filepath := filepath.Join(uploadDir, filename)

	// Create the file on disk
	dst, err := os.Create(filepath)
	if err != nil {
		log.Printf("Error creating file: %v", err)
		http.Error(w, "Failed to create file", http.StatusInternalServerError)
		return
	}
	defer dst.Close()

	// Copy the uploaded file to the destination
	if _, err := io.Copy(dst, file); err != nil {
		log.Printf("Error copying file: %v", err)
		http.Error(w, "Failed to save file", http.StatusInternalServerError)
		return
	}

	// Log successful upload
	log.Printf("File uploaded successfully: %s (size: %d bytes)", filename, handler.Size)

	// Return success response
	w.WriteHeader(http.StatusOK)
	fmt.Fprintf(w, "File uploaded successfully: %s", filename)
}

func handleHealth(w http.ResponseWriter, r *http.Request) {
	setCORSHeaders(w)
	w.WriteHeader(http.StatusOK)
	fmt.Fprint(w, "Server is healthy")
}

func setCORSHeaders(w http.ResponseWriter) {
	w.Header().Set("Access-Control-Allow-Origin", "*")
	w.Header().Set("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
	w.Header().Set("Access-Control-Allow-Headers", "Content-Type")
	w.Header().Set("Content-Type", "text/plain; charset=utf-8")
}

func getEnv(key, defaultValue string) string {
	if value := os.Getenv(key); value != "" {
		return value
	}
	return defaultValue
}
