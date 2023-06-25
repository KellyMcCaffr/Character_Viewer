package com.sample

import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class EndpointsImpl: Endpoints{

    @Override
    override fun getCharacterList(): Observable<List<CharacterItem>> {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.DATA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
        val apiService: Endpoints = retrofit.create(Endpoints::class.java)
        return apiService.getCharacterList()
    }
}