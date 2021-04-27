package com.kostovtd.herorealms.views.fragments

import com.kostovtd.boardy.data.models.GameSessionFirestore
import com.kostovtd.boardy.views.activities.BaseView

interface HeroRealmsGameView: BaseView {
    fun updatePoints(pointsHashMap: HashMap<String, Int>)
    fun updateGameSessionInfo(gameSessionFirestore: GameSessionFirestore)
}