package com.kostovtd.boardy.data.models

import java.io.Serializable

class BoardGameGameSession(
    var gameSessionId: String? = null,
    val boardGameId: String? = null,
    val packageName: String,
    val moduleName: String,
    val activityName: String
): Serializable