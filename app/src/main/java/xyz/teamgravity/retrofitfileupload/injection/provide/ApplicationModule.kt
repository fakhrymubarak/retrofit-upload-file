package xyz.teamgravity.retrofitfileupload.injection.provide

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import xyz.teamgravity.retrofitfileupload.BuildConfig
import xyz.teamgravity.retrofitfileupload.core.util.RonaldoProvider
import xyz.teamgravity.retrofitfileupload.data.remote.api.FileApi
import xyz.teamgravity.retrofitfileupload.data.remote.constant.FileApiConst
import xyz.teamgravity.retrofitfileupload.data.repository.FileRepository
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        return OkHttpClient.Builder()
            .writeTimeout(0, TimeUnit.MINUTES)
            .readTimeout(0, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideFileApi(client: OkHttpClient): FileApi = Retrofit.Builder()
        .baseUrl(FileApiConst.BASE_URL)
        .client(client)
        .build()
        .create(FileApi::class.java)

    @Provides
    @Singleton
    fun provideFileRepository(fileApi: FileApi): FileRepository = FileRepository(fileApi)

    @Provides
    @Singleton
    fun provideRonaldoProvider(application: Application): RonaldoProvider = RonaldoProvider(application)
}