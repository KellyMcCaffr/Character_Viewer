package com.sample.mvm

import com.google.gson.JsonObject
import com.sample.BuildConfig
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RequestApiImpl: RequestApi {

    @Override
    override fun getCharacterList(): Observable<JsonObject> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.DATA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        val apiService: RequestApi = retrofit.create(RequestApi::class.java)
        return apiService.getCharacterList()
    }
}