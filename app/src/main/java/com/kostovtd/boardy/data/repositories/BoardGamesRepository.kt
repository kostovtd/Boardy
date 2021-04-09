package com.kostovtd.boardy.data.repositories

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kostovtd.boardy.data.models.BoardGame
import com.kostovtd.boardy.util.Constants
import com.kostovtd.boardy.util.ErrorType
import kotlinx.coroutines.tasks.await

/**
 * Created by tosheto on 23.01.21.
 */
class BoardGamesRepository {

    private val firestore = Firebase.firestore

    //TODO use success / failure listeners instead of try-catch
    suspend fun findGamesByName(name: String): Resource<ArrayList<BoardGame>> {
        val boardGamesCollection = firestore.collection(Constants.BOARDGAMES_COLLECTION_PATH)

        val query = boardGamesCollection.whereGreaterThanOrEqualTo(Constants.NAME_FIELD, name)
            .whereLessThanOrEqualTo(Constants.NAME_FIELD, name + '\uf8ff')

        val results = ArrayList<BoardGame>()

        try {
            val documents = query.get().await().documents

            documents.forEach { document ->
                val boardGame = document.toObject(BoardGame::class.java)
                boardGame?.let {
                    it.id = document.id
                    results.add(it)
                }
            }
        }  catch (e: Exception) {
            Log.d("BoardGamesRepository", e.toString())
        }

        return Resource(ResourceStatus.SUCCESS, results)
    }


    suspend fun findBoardGameById(id: String): Resource<BoardGame> {
        lateinit var result: Resource<BoardGame>

        firestore.collection(Constants.BOARDGAMES_COLLECTION_PATH)
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val boardGame = document.toObject(BoardGame::class.java)
                    boardGame?.id = id
                    result = Resource(ResourceStatus.SUCCESS, boardGame)
                }
            }
            .addOnFailureListener {
                result = Resource(ResourceStatus.ERROR, null, ErrorType.FIRESTORE_FIND_GAME_SESSION)
            }
            .await()

        return result
    }
}