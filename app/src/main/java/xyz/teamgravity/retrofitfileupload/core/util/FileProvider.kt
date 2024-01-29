package xyz.teamgravity.retrofitfileupload.core.util

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileProvider(
    private val application: Application,
) {

    suspend fun provide(): File {
        return withContext(Dispatchers.IO) {
            File(application.cacheDir, "books_weapon.jpg").apply {
                createNewFile()
                outputStream().use { stream ->
                    application.assets.open("image/books_weapon.jpg").copyTo(stream)
                }
            }
        }
    }
}