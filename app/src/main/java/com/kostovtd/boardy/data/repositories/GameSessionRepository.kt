package com.kostovtd.boardy.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.data.models.GameSessionStatus
import com.kostovtd.boardy.util.Constants.GAME_SESSIONS_COLLECTION_PATH
import com.kostovtd.boardy.util.Constants.GAME_SESSION_CHILD
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.web.APIClient
import com.kostovtd.boardy.web.bodies.AddPlayerToGameSessionBody
import com.kostovtd.boardy.web.bodies.ChangeGameSessionStatusBody
import com.kostovtd.boardy.web.bodies.IncrementPointsBody
import com.kostovtd.boardy.web.bodies.RemovePlayerFromGameSessionBody
import com.kostovtd.boardy.web.responses.BaseFirebaseResponse
import com.kostovtd.boardy.web.responses.CreateGameSessionResponse
import com.kostovtd.boardy.web.responses.GameSessionByIdData
import com.kostovtd.boardy.web.responses.GameSessionByIdResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by tosheto on 16.02.21.
 */
class GameSessionRepository {

    private val firestore = Firebase.firestore
    private val database = Firebase.database.reference
    private var databaseEventListener: ValueEventListener? = null
    private var firestoreEventListener: ListenerRegistration? = null


    suspend fun createGameSession(
        gameSessionFirestore: GameSessionFirestore
    ): ResourceWithMessage<CreateGameSessionResponse> {
        val response = APIClient.firebaseAPI.postCreateGameSession(gameSessionFirestore)

        return if (response.success) {
            ResourceWithMessage(ResourceStatus.SUCCESS, response)
        } else {
            ResourceWithMessage(ResourceStatus.ERROR, null, MessageType.CREATE_GAME_SESSION_ERROR)
        }
    }


    @Deprecated("Use getGameSessionByIdV2")
    suspend fun getGameSessionById(gameSessionId: String): Resource<GameSessionByIdResult> {
        val response = APIClient.firebaseAPI.getGameSessionById(gameSessionId)

        return if (response.success) {
            Resource(ResourceStatus.SUCCESS, response)
        } else {
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_GET_GAME_SESSION_BY_ID)
        }
    }


    suspend fun getGameSessionByIdV2(gameSessionId: String): ResourceWithMessage<GameSessionByIdData> {
        val response = APIClient.firebaseAPI.getGameSessionById(gameSessionId)

        return if (response.success) {
            ResourceWithMessage(ResourceStatus.SUCCESS, response.data)
        } else {
            ResourceWithMessage(
                ResourceStatus.ERROR,
                null,
                MessageType.FIREBASE_GET_GAME_SESSION_BY_ID_ERROR
            )
        }
    }


    suspend fun changeGameSessionStatus(
        gameSessionId: String,
        status: GameSessionStatus
    ): ResourceWithMessage<BaseFirebaseResponse> {
        val response = APIClient.firebaseAPI.changeGameSessionStatus(
            ChangeGameSessionStatusBody(
                gameSessionId,
                status
            )
        )

        return if (response.success) {
            ResourceWithMessage(ResourceStatus.SUCCESS, response)
        } else {
            ResourceWithMessage(
                ResourceStatus.ERROR,
                null,
                MessageType.FIRESTORE_UPDATE_GAME_SESSION_ERROR
            )
        }
    }


    suspend fun changePoints(
        gameSessionId: String,
        playerId: String,
        points: Int
    ): ResourceWithMessage<BaseFirebaseResponse> {
        val incrementPointsBody = IncrementPointsBody(gameSessionId, playerId, points)

        val response = APIClient.firebaseAPI.changePoints(incrementPointsBody)

        return if (response.success) {
            ResourceWithMessage(ResourceStatus.SUCCESS, response)
        } else {
            ResourceWithMessage(
                ResourceStatus.ERROR,
                null,
                MessageType.FIRESTORE_UPDATE_GAME_SESSION_ERROR
            )
        }
    }


    fun subscribeGameSessionFirestore(gameSessionId: String): Flow<GameSessionFirestore> =
        callbackFlow {
            firestoreEventListener = firestore.collection(GAME_SESSIONS_COLLECTION_PATH)
                .document(gameSessionId)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null && snapshot.exists()) {
                        if (!snapshot.metadata.isFromCache) {
                            snapshot.toObject(GameSessionFirestore::class.java)?.let {
                                it.id = gameSessionId
                                trySend(it)
                            }
                        }
                    }
                }

            awaitClose {
                unsubscribeGameSessionFirestore()
            }
        }


    fun unsubscribeGameSessionFirestore() = firestoreEventListener?.remove()


    fun subscribeGameSessionDatabase(
        gameSessionId: String
    ): Flow<GameSessionDatabase> = callbackFlow {
        databaseEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameSessionDatabase = dataSnapshot.getValue<GameSessionDatabase>()
                gameSessionDatabase?.let { gameSession ->
                    gameSession.id = gameSessionId
                    trySend(gameSession)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }

        databaseEventListener?.let {
            database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
                .addValueEventListener(it)
        }

        awaitClose {
            unsubscribeGameSessionDatabase(gameSessionId)
        }
    }


    fun unsubscribeGameSessionDatabase(gameSessionId: String) {
        databaseEventListener?.let { listener ->
            database.removeEventListener(listener)
            database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
                .removeEventListener(listener)
        }
    }


    suspend fun addPlayerToGameSession(
        gameSessionId: String,
        playerEmail: String,
        playerId: String,
        points: Int
    ): ResourceWithMessage<BaseFirebaseResponse> {
        val addPlayerBody = AddPlayerToGameSessionBody(gameSessionId, playerEmail, playerId, points)

        val response = APIClient.firebaseAPI.addPlayerToGameSession(addPlayerBody)

        return if (response.success) {
            ResourceWithMessage(ResourceStatus.SUCCESS, response)
        } else {
            ResourceWithMessage(
                ResourceStatus.ERROR,
                null,
                MessageType.FIRESTORE_UPDATE_GAME_SESSION_ERROR
            )
        }
    }


    suspend fun removePlayerFromGameSession(
        gameSessionId: String,
        playerId: String,
        playerEmail: String
    ): ResourceWithMessage<BaseFirebaseResponse> {
        val removePlayerBody = RemovePlayerFromGameSessionBody(gameSessionId, playerId, playerEmail)

        val response = APIClient.firebaseAPI.removePlayerFromGameSession(removePlayerBody)

        return if (response.success) {
            ResourceWithMessage(ResourceStatus.SUCCESS, response)
        } else {
            ResourceWithMessage(
                ResourceStatus.ERROR,
                null,
                MessageType.FIRESTORE_UPDATE_GAME_SESSION_ERROR
            )
        }
    }
}