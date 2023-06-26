package com.sample.mvm

import com.google.gson.JsonObject
import com.sample.BuildConfig
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface RequestApi {
    @GET(BuildConfig.DATA_API_SUFFIX)
    fun getCharacterList(): Observable<JsonObject>
}