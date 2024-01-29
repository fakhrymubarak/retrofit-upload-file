package xyz.teamgravity.retrofitfileupload.data.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import xyz.teamgravity.retrofitfileupload.data.remote.api.FileApi
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class FileRepository(
    private val api: FileApi,
) {
    suspend fun uploadImage(fileRequestBody: RequestBody, fileName: String) =
        try {
            val multipartFile =MultipartBody.Part.createFormData("image", fileName, fileRequestBody)
            api.uploadImage(multipartFile)
            NetworkResult.Success(true)
        } catch (e: HttpException) {
            e.printStackTrace()
            NetworkResult.Error("HttpException ${e.message()}")
        } catch (e: IOException) {
            e.printStackTrace()
            NetworkResult.Error("IOException ${e.message}")
        } catch (e: CancellationException) {
            e.printStackTrace()
            NetworkResult.Error("CancellationException ${e.message}")
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error("$e ${e.message}")
        }
}

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T?) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
}