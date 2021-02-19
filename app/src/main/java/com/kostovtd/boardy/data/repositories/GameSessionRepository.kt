package com.kostovtd.boardy.data.repositories

import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kostovtd.boardy.data.models.GameSession
import com.kostovtd.boardy.util.Constants.GAME_SESSIONS_COLLECTION_PATH
import com.kostovtd.boardy.util.Constants.GAME_SESSION_CHILD
import com.kostovtd.boardy.util.Constants.POINTS_CHILD
import com.kostovtd.boardy.util.ErrorType
import kotlinx.coroutines.tasks.await

/**
 * Created by tosheto on 16.02.21.
 */
class GameSessionRepository {

    private val firestore = Firebase.firestore
    private val database = Firebase.database.reference


    suspend fun createGameSessionFirestore(gameSession: GameSession): Resource<GameSession> {
        val gameSessionsCollection = firestore.collection(GAME_SESSIONS_COLLECTION_PATH)
        lateinit var result: Resource<GameSession>

        gameSessionsCollection.add(gameSession)
            .addOnSuccessListener {
                gameSession.id = it.id
                result = Resource(ResourceStatus.SUCCESS, gameSession)
            }
            .addOnFailureListener {
                //TODO add logging logic
                result = Resource(ResourceStatus.ERROR, gameSession, ErrorType.UNKNOWN)
            }
            .await()

        return result
    }


    suspend fun deleteGameSessionFirestore(gameSessionId: String): Resource<String> {
        lateinit var result: Resource<String>

        firestore.collection(GAME_SESSIONS_COLLECTION_PATH)
            .document(gameSessionId)
            .delete()
            .addOnSuccessListener {
                result = Resource(ResourceStatus.SUCCESS, gameSessionId)
            }
            .addOnFailureListener {
                //TODO add logging logic
                result = Resource(ResourceStatus.ERROR, gameSessionId, ErrorType.UNKNOWN)
            }
            .await()

        return result
    }


    suspend fun createGameSessionDatabase(gameSession: GameSession): Resource<GameSession> {
        lateinit var result: Resource<GameSession>

        gameSession.teams.forEach { team ->
            database.child(GAME_SESSION_CHILD + "_" + gameSession.id)
                .child(POINTS_CHILD + "_" + team.key)
                .setValue(gameSession.startingPoints)
                .addOnSuccessListener {
                    result = Resource(ResourceStatus.SUCCESS, gameSession)
                }
                .addOnFailureListener {
                    //TODO add logging logic
                    result = Resource(ResourceStatus.ERROR, gameSession, ErrorType.UNKNOWN)
                }
                .await()
        }

        return result
    }


    suspend fun deleteGameSessionDatabase(gameSessionId: String): Resource<String> {
        lateinit var result: Resource<String>

        database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
            .removeValue()
            .addOnSuccessListener {
                result = Resource(ResourceStatus.SUCCESS, gameSessionId)
            }
            .addOnFailureListener {
                //TODO add logging logic
                result = Resource(ResourceStatus.ERROR, gameSessionId)
            }
            .await()

        return result
    }
}