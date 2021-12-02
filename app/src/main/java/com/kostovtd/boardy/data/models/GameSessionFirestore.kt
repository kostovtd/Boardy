package com.kostovtd.boardy.data.models

import com.google.firebase.firestore.Exclude
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
    var losers: ArrayList<String> = ArrayList() // data format {userId}|{userName}
): Serializable