package com.kostovtd.boardy.util

import com.kostovtd.boardy.data.models.BoardgamesByNameResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FirebaseAPI {
//    @GET("getGameSessionById")
//    fun getGameSessionById(@Query("id") id: String)

    @GET("getBoardgamesByName")
    fun getBoardgameByName(@Query("name") name: String): Call<BoardgamesByNameResult>
}