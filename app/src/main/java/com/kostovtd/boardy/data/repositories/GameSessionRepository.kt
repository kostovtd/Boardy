package com.kostovtd.boardy.data.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore
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
    private lateinit var databaseEventListener: ValueEventListener


    suspend fun createGameSessionFirestore(gameSessionFirestore: GameSessionFirestore): Resource<GameSessionFirestore> {
        val gameSessionsCollection = firestore.collection(GAME_SESSIONS_COLLECTION_PATH)
        lateinit var result: Resource<GameSessionFirestore>

        gameSessionsCollection.add(gameSessionFirestore)
            .addOnSuccessListener {
                gameSessionFirestore.id = it.id
                result = Resource(ResourceStatus.SUCCESS, gameSessionFirestore)
            }
            .addOnFailureListener {
                //TODO add logging logic
                result = Resource(ResourceStatus.ERROR, gameSessionFirestore, ErrorType.UNKNOWN)
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


    suspend fun updateGameSessionFirestore() {

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


    suspend fun createGameSessionDatabase(gameSessionFirestore: GameSessionFirestore): Resource<GameSessionDatabase> {
        lateinit var result: Resource<GameSessionDatabase>
        val gameSessionDatabase = GameSessionDatabase(points = HashMap())

        gameSessionFirestore.teams.forEach { team ->
            gameSessionDatabase.points[team.key] =
                gameSessionFirestore.startingPoints
        }

        database.child(GAME_SESSION_CHILD + "_" + gameSessionFirestore.id)
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


    suspend fun updateGameSessionDatabase() {

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


    fun findAndListenGameSessionDatabase(
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

        database.removeEventListener(databaseEventListener)
        database.child(GAME_SESSION_CHILD + "_" + gameSessionId)
            .addValueEventListener(databaseEventListener)
    }


    fun stopListenGameSessionDatabase() = database.removeEventListener(databaseEventListener)
}