package com.kostovtd.boardy.web

import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.web.bodies.*
import com.kostovtd.boardy.web.responses.BaseFirebaseResponse
import com.kostovtd.boardy.web.responses.BoardgamesByNameResult
import com.kostovtd.boardy.web.responses.CreateGameSessionResponse
import com.kostovtd.boardy.web.responses.GameSessionByIdResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FirebaseAPI {
//    @GET("getGameSessionById")
//    fun getGameSessionById(@Query("id") id: String)

    @GET("getBoardgamesByName")
    suspend fun getBoardGameByName(@Query("name") name: String): BoardgamesByNameResult

    @GET("getGameSessionById")
    suspend fun getGameSessionById(@Query("id") id: String): GameSessionByIdResult

    @POST("createGameSession")
    suspend fun postCreateGameSession(@Body body: GameSessionFirestore): CreateGameSessionResponse

    @POST("updateGameSession")
    suspend fun postUpdateGameSession(@Body body: UpdateGameSessionBody): BaseFirebaseResponse

    @POST("changeRealtimeDatabasePoints")
    suspend fun changePoints(@Body body: IncrementPointsBody): BaseFirebaseResponse

    @POST("changeGameSessionStatus")
    suspend fun changeGameSessionStatus(@Body body: ChangeGameSessionStatusBody): BaseFirebaseResponse

    @POST("addPlayerToGameSession")
    suspend fun addPlayerToGameSession(@Body body: AddPlayerToGameSessionBody): BaseFirebaseResponse

    @POST("removePlayerFromGameSession")
    suspend fun removePlayerFromGameSession(@Body body: RemovePlayerFromGameSessionBody): BaseFirebaseResponse

}