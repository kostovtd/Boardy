package com.kostovtd.boardy.web.bodies

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore

class UpdateGameSessionBody(
    val gameSessionId: String,
    val gameSessionFirestore: GameSessionFirestore? = null,
    val gameSessionDatabase: GameSessionDatabase? = null
)