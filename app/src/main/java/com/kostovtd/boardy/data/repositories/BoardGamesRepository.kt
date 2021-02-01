package com.kostovtd.boardy.data.repositories

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kostovtd.boardy.data.models.BoardGame
import kotlinx.coroutines.tasks.await
import java.lang.Exception

/**
 * Created by tosheto on 23.01.21.
 */
class BoardGamesRepository {

    private val firestore = Firebase.firestore


    suspend fun findGamesByName(name: String): Resource<ArrayList<BoardGame>> {
        val boardGamesCollection = firestore.collection("boardGames")

        val query = boardGamesCollection.whereGreaterThanOrEqualTo("name", name)
            .whereLessThanOrEqualTo("name", name + '\uf8ff')

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

}