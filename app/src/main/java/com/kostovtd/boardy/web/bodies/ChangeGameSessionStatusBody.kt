package com.kostovtd.boardy.web.bodies

import com.kostovtd.boardy.data.models.GameSessionStatus

class ChangeGameSessionStatusBody(
    val gameSessionId: String,
    val status: GameSessionStatus
)