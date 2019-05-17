package com.meeshotask.myapp.networkhelper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.meeshotask.myapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHelper {

    companion object {
        private var retrofit:Retrofit?=null
        fun getInstance():Retrofit{
            return if (retrofit==null){
                retrofit = providesRetrofit()
                retrofit!!
            }else retrofit!!
        }

        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor()
        }

        private fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(4, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(provideHttpLoggingInterceptor()).build()
        }

        private fun providesGson(): Gson {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.setLenient()
            return gsonBuilder.create()
        }

        private fun providesRetrofit(): Retrofit {
            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(providesGson()))
                .client(provideOkHttpClient())
                .build()
        }
    }





}