package com.sparkstudios.cookware.di

import com.sparkstudios.cookware.data.remote.CookApi
import com.sparkstudios.cookware.data.repository.CookRepositoryImpl
import com.sparkstudios.cookware.domain.repository.CookRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides @Singleton
    fun okHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides @Singleton
    fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://jmyqvrvrguhfujgombqe.supabase.co/")
        .client(client).addConverterFactory(GsonConverterFactory.create()).build()

    @Provides @Singleton
    fun api(retrofit: Retrofit): CookApi = retrofit.create(CookApi::class.java)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindRepository(impl: CookRepositoryImpl): CookRepository
}
