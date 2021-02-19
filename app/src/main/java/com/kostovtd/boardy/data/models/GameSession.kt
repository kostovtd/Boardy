package com.kostovtd.boardy.data.models


/**
 * Created by tosheto on 16.02.21.
 */
class GameSession(
    var id: String = "",
    var adminId: String = "",
    var boardGameId: String = "",
    var endTime: String = "",
    var lossers: ArrayList<String> = ArrayList(),
    var players: HashMap<String, String> = HashMap(),
    var teams: HashMap<String, String> = HashMap(),
    var startTime: String = "",
    var winners: ArrayList<String> = ArrayList(),
    var startingPoints: Int
)