package xyz.teamgravity.retrofitfileupload.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class ProgressFlowRequestBody(
    private val mediaType: String,
    private val file: File,
) : RequestBody() {

    private val _progressFlow = MutableStateFlow(0)
    val progressFlow = _progressFlow.asStateFlow()

    override fun contentType() = mediaType.toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {

        val buffer = ByteArray(BUFFER_SIZE)
        var uploaded: Long = 0
        val fileSize = file.length()

        FileInputStream(file).use { inputStream ->
            try {
                while (true) {
                    val read = inputStream.read(buffer)
                    if (read == -1) break

                    uploaded += read
                    sink.write(buffer, 0, read)

                    val progress = if (fileSize > 0) {
                        (((uploaded / fileSize.toDouble())) * 100).toInt()
                    } else {
                        100
                    }
                    _progressFlow.update { progress }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e // Re-throw to let the caller handle the error appropriately
            }
        }
    }

    companion object {
        /**
         * Buffer size for reading file chunks in bytes.
         *
         * 8KB provides a good balance between memory usage and I/O performance.
         * Larger buffers reduce the number of read operations but use more memory.
         * Smaller buffers provide more granular progress updates but may impact performance.
         */
        private const val BUFFER_SIZE = 8192
    }
}