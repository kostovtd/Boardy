package com.kostovtd.boardy.web.bodies

class CreateGameSessionBody(
    val boardGameId: String,
    val adminId: String,
    val players: ArrayList<String>,
    val teams: ArrayList<String>,
    val startingPoints: Int
)