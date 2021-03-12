package com.kostovtd.boardy.data.models

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
 * Created by tosheto on 16.02.21.
 */
class GameSessionFirestore(
    @get:Exclude var id: String = "",
    var adminId: String = "",
    var boardGameId: String = "",
    @ServerTimestamp var endTime: Date? = Date(),
    var lossers: ArrayList<String> = ArrayList(),
    var players: HashMap<String, String> = HashMap(),
    var teams: HashMap<String, String> = HashMap(),
    @ServerTimestamp var startTime: Date? = Date(),
    var winners: ArrayList<String> = ArrayList(),
    var startingPoints: Int = 0
)