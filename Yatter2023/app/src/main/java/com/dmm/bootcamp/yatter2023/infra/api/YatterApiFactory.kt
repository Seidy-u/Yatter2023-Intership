package com.dmm.bootcamp.yatter2023.infra.api

import com.dmm.bootcamp.yatter2023.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class YatterApiFactory {
  fun create(): YatterApi {
    val moshi = Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL + "/v1/")
      .client(
        OkHttpClient.Builder()
          .addInterceptor(
            HttpLoggingInterceptor().apply {
              level = HttpLoggingInterceptor.Level.BODY
            }
          )
          .build()
      )
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addCallAdapterFactory(CoroutineCallAdapterFactory())
      .build()
      .create(YatterApi::class.java)
  }
}
