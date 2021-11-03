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
import com.kostovtd.boardy.util.Constants.GAME_SESSIONS_COLLECTION_PATH
import com.kostovtd.boardy.util.Constants.GAME_SESSION_CHILD
import com.kostovtd.boardy.util.ErrorType
import com.kostovtd.boardy.web.APIClient
import com.kostovtd.boardy.web.bodies.UpdateGameSessionBody
import com.kostovtd.boardy.web.responses.BaseFirebaseResponse
import com.kostovtd.boardy.web.responses.CreateGameSessionResponse
import kotlinx.coroutines.tasks.await

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
    ): Resource<CreateGameSessionResponse> {
        val response = APIClient.firebaseAPI.postCreateGameSession(gameSessionFirestore)

        return if (response.success) {
            Resource(ResourceStatus.SUCCESS, response)
        } else { //TODO Add other network error handling
            Resource(ResourceStatus.ERROR, null, ErrorType.FIREBASE_CREATE_GAME_SESSION)
        }
    }


    suspend fun updateGameSession(
        gameSessionId: String,
        gameSessionFirestore: GameSessionFirestore?,
        gameSessionDatabase: GameSessionDatabase?
    ): Resource<BaseFirebaseResponse> {
        val updateGameSessionBody =
            UpdateGameSessionBody(gameSessionId, gameSessionFirestore, gameSessionDatabase)

        val response = APIClient.firebaseAPI.postUpdateGameSession(updateGameSessionBody)

        return if (response.success) {
            Resource(ResourceStatus.SUCCESS, response)
        } else { //TODO Add other network error handling
            Resource(ResourceStatus.ERROR, null, ErrorType.FIRESTORE_UPDATE_GAME_SESSION)
        }
    }


    suspend fun findGameSessionFirestore(gameSessionId: String): Resource<GameSessionFirestore> {
        lateinit var result: Resource<GameSessionFirestore>

        val gameSessionCollection = firestore.collection(GAME_SESSIONS_COLLECTION_PATH)

        gameSessionCollection.document(gameSessionId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val gameSession = document.toObject(GameSessionFirestore::class.java)
                    gameSession?.id = gameSessionId
                    result = Resource(ResourceStatus.SUCCESS, gameSession)
                }
            }
            .addOnFailureListener {
                result = Resource(ResourceStatus.ERROR, null, ErrorType.FIRESTORE_FIND_GAME_SESSION)
            }
            .await()

        return result
    }


    fun subscribeGameSessionFirestore(gameSessionId: String, listener: IGameSessionRepository) {
        firestoreEventListener = firestore.collection(GAME_SESSIONS_COLLECTION_PATH)
            .document(gameSessionId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    //TODO add logging logic
                    listener.onGameSessionFirestoreUpdateError(ErrorType.UNKNOWN)
                }

                if (snapshot != null && snapshot.exists()) {
                    snapshot.toObject(GameSessionFirestore::class.java)?.let {
                        listener.onGameSessionFirestoreUpdated(it)
                    }
                } else {
                    //TODO add logging logic
                    listener.onGameSessionFirestoreUpdateError(ErrorType.UNKNOWN)
                }
            }
    }


    fun unsubscribeGameSessionFirestore() = firestoreEventListener?.remove()


    suspend fun createGameSessionDatabase(gameSessionDatabase: GameSessionDatabase): Resource<GameSessionDatabase> {
        lateinit var result: Resource<GameSessionDatabase>

        database.child(GAME_SESSION_CHILD + "_" + gameSessionDatabase.id)
            .setValue(gameSessionDatabase)
            .addOnSuccessListener {
                result = Resource(ResourceStatus.SUCCESS, gameSessionDatabase)
            }
            .addOnFailureListener {
                //TODO add logging logic
                result = Resource(ResourceStatus.ERROR, gameSessionDatabase, ErrorType.UNKNOWN)
            }
            .await()


        return result
    }


    suspend fun updateGameSessionDatabase(
        gameSessionId: String,
        gameSessionDatabase: GameSessionDatabase
    ): Resource<GameSessionDatabase> {
        lateinit var result: Resource<GameSessionDatabase>

        database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
            .setValue(gameSessionDatabase)
            .addOnSuccessListener {
                result = Resource(ResourceStatus.SUCCESS, gameSessionDatabase)
            }
            .addOnFailureListener {
                //TODO add logging logic
                result = Resource(ResourceStatus.ERROR, gameSessionDatabase, ErrorType.UNKNOWN)
            }
            .await()

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


    fun subscribeGameSessionDatabase(
        gameSessionId: String,
        listener: IGameSessionRepository
    ) {
        databaseEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val gameSessionDatabase = dataSnapshot.getValue<GameSessionDatabase>()
                gameSessionDatabase?.let { gameSession ->
                    listener.onGameSessionDatabaseUpdated(gameSession)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //TODO add logging logic
                listener.onGameSessionDatabaseUpdateError(ErrorType.DATABASE_FIND_GAME_SESSION)
            }
        }

        databaseEventListener?.let {
            database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
                .addValueEventListener(it)
        }
    }


    fun unsubscribeGameSessionDatabase(gameSessionId: String) {
        databaseEventListener?.let { listener ->
            database.removeEventListener(listener)
            database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
                .removeEventListener(listener)
        }
    }
}