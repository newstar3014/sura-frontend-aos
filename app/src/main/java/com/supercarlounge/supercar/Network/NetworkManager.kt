package com.supercarlounge.supercar.Network

import android.app.PendingIntent.getActivity
import android.util.Log
import com.google.gson.GsonBuilder
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.data.ResultTokenData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
 class AuthInterceptor( ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        val response = chain.proceed(request);

        when (response.code) {
            400 -> {
                //Show Bad Request Error Message
            }
            401 -> {
                //Show UnauthorizedError Message
            }

            403 -> {
                //Show Forbidden Message
            }

            404 -> {
                //Show NotFound Message
            }

            // ... and so on

        }
        return response
    }


}
object NetworkManager {
    val gson = GsonBuilder().setLenient().create()
    var application :MainApplication? =null



    private val okHttpClientBuilder = OkHttpClient.Builder()
         .addInterceptor(provideLoggingInterceptor())
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(AuthInterceptor())
        .addInterceptor(object:Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request().newBuilder().addHeader("Authorization", MainApplication.token).build()


                return chain.proceed(request)
            }

        })
           .build()
    val serveradapter: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl("https://supercarlounge.com:3002")
                .client(okHttpClientBuilder)
                .addConverterFactory(GsonConverterFactory.create(gson)).build()

    }

    val serveradapterTest: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://supercarlounge.com:3003")
            .client(okHttpClientBuilder)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    }

    val serveraManagerdapter: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://supercarlounge.com:3001")
            .client(okHttpClientBuilder)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    }
    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
      //  interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


}