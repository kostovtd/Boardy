package com.kostovtd.boardy.web.bodies

class AddPlayerToGameSessionBody(
    val gameSessionId: String,
    val playerEmail: String,
    val playerId: String,
    val points: Int
)