package com.kostovtd.boardy.web.bodies

class RemovePlayerFromGameSessionBody(
    val gameSessionId: String,
    val playerId: String,
    val playerEmail: String
)