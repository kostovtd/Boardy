package com.kostovtd.boardy.data.models

import com.google.firebase.firestore.Exclude
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * Created by tosheto on 16.02.21.
 */
class GameSessionFirestore(
    @get:Exclude var id: String = "",
    var adminId: String = "",
    var boardGameId: String = "",
    var startTime: Long? = null,
    var endTime: Long? = null,
    var startingPoints: Int = 0,
    var players: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var teams: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var winners: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var losers: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var status: GameSessionStatus = GameSessionStatus.NONE
): Serializable


enum class GameSessionStatus {
    @SerializedName("NONE")
    NONE,
    @SerializedName("NOT_STARTED_YET")
    NOT_STARTED_YET,
    @SerializedName("ACTIVE")
    ACTIVE,
    @SerializedName("SUSPENDED")
    SUSPENDED,
    @SerializedName("FINISHED")
    FINISHED
}