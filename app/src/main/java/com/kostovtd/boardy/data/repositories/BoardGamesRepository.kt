package com.kostovtd.boardy.data.repositories

import com.kostovtd.boardy.data.models.BoardGame
import com.kostovtd.boardy.util.APIClient
import com.kostovtd.boardy.util.ErrorType
import retrofit2.await

/**
 * Created by tosheto on 23.01.21.
 */
class BoardGamesRepository {

    suspend fun findGamesByName(name: String): Resource<ArrayList<BoardGame>> {
        val response = APIClient.firebaseAPI.getBoardgameByName(name).await()

        return if(response.success) {
            Resource(ResourceStatus.SUCCESS, response.data)
        } else {
            Resource(ResourceStatus.ERROR, null, ErrorType.DATABASE_FIND_GAME_SESSION)
        }
    }
}