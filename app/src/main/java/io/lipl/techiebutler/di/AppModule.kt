package io.lipl.techiebutler.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.lipl.techiebutler.common.Conts
import io.lipl.techiebutler.domain.rest.RetrofitService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    // region retrofit
    @Provides
    internal fun provideGson(): Gson =
        GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        }.create()

    @Provides
    @Named("GetApis")
    fun provideGetOkHttpClient(): OkHttpClient {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(60L, TimeUnit.SECONDS)
        okClient.writeTimeout(60L, TimeUnit.SECONDS)
        okClient.readTimeout(60L, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okClient.interceptors().add(interceptor)

        okClient.interceptors().add(Interceptor { chain ->

            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()

            val response = chain.proceed(request)
            response
        })
        return okClient.build()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okClient = OkHttpClient.Builder()
        okClient.connectTimeout(60L, TimeUnit.SECONDS)
        okClient.writeTimeout(60L, TimeUnit.SECONDS)
        okClient.readTimeout(60L, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        okClient.interceptors().add(interceptor)


        return okClient.build()
    }

    @Provides
    @Singleton
    @Named("Normal")
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(Conts.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiInterface(@Named("Normal") retrofit: Retrofit): RetrofitService =
        retrofit.create(RetrofitService::class.java)
    // endregion


    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("App.SharedPreferences", Context.MODE_PRIVATE)

    // endregion

}