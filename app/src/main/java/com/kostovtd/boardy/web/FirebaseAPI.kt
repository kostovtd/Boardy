package com.kostovtd.boardy.web

import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.web.responses.BoardgamesByNameResult
import com.kostovtd.boardy.web.responses.CreateGameSessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FirebaseAPI {
//    @GET("getGameSessionById")
//    fun getGameSessionById(@Query("id") id: String)

    @GET("getBoardgamesByName")
    suspend fun getBoardGameByName(@Query("name") name: String): BoardgamesByNameResult

    @POST("createGameSession")
    suspend fun postCreateGameSession(@Body body: GameSessionFirestore): CreateGameSessionResponse
}