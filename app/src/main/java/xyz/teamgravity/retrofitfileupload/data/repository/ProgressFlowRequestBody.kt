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
    private val mediaType: String, private val file: File
) : RequestBody() {

    private val _progressFlow = MutableStateFlow(0)
    val progressFlow = _progressFlow.asStateFlow()

    override fun contentType() = mediaType.toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(BUFFER_SIZE)
            var uploaded: Long = 0
            val fileSize = file.length()

            try {
                while (true) {
                    val read = inputStream.read(buffer)
                    if (read == -1) break

                    uploaded += read
                    sink.write(buffer, 0, read)

                    val progress = (((uploaded / fileSize.toDouble())) * 100).toInt()
                    _progressFlow.update { progress }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream.close()
            }
    }

    companion object {
        private const val BUFFER_SIZE = 1024
    }
}