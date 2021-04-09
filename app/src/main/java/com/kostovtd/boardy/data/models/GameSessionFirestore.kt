package com.kostovtd.boardy.data.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by tosheto on 16.02.21.
 */
class GameSessionFirestore(
    @get:Exclude var id: String = "",
    var adminId: String = "",
    var boardGameId: String = "",
    @ServerTimestamp var startTime: Date? = Date(),
    @ServerTimestamp var endTime: Date? = Date(),
    var startingPoints: Int = 0,
    var players: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var teams: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var winners: ArrayList<String> = ArrayList(), // data format {userId}|{userName}
    var losers: ArrayList<String> = ArrayList() // data format {userId}|{userName}
): Serializable