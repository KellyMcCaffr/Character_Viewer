package com.sample


import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class CharactersViewModel {

    fun loadCharacterData(
    ): Observable<List<CharacterItem>> {
        val mInterface = EndpointsImpl()
        return mInterface.getCharacterList()
            .subscribeOn(Schedulers.io())
    }
}