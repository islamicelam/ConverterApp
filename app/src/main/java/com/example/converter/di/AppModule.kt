package com.example.converter.di

import com.example.converter.BuildConfig
import com.example.converter.data.CurrencyAPI
import com.example.converter.main.DefaultMainRepository
import com.example.converter.main.MainRepository
import com.example.converter.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "http://api.exchangeratesapi.io/v1/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    val okHttp = OkHttpClient.Builder().addInterceptor(logger)
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttp.build())
            .build()
    }


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CurrencyAPI =
        retrofit.create(CurrencyAPI::class.java)

    @Singleton
    @Provides
    fun provideMainRepository(api: CurrencyAPI): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}