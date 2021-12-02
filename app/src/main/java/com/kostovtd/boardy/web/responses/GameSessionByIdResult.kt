package com.kostovtd.boardy.web.responses

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.data.models.GameSessionFirestore

class GameSessionByIdResult(val data: GameSessionByIdData): BaseFirebaseResponse()

class GameSessionByIdData(
    val gameSession: GameSessionFirestore,
    val realTimeGameSession: GameSessionDatabase
)