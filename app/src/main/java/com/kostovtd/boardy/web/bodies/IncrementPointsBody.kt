package com.kostovtd.boardy.web.bodies

class IncrementPointsBody(
    val gameSessionId: String,
    val playerId: String,
    val points: Int
)