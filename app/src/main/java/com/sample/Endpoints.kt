package com.sample

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface Endpoints {
    @GET(BuildConfig.DATA_API_SUFFIX)
    fun getCharacterList(): Observable<List<CharacterItem>>
}