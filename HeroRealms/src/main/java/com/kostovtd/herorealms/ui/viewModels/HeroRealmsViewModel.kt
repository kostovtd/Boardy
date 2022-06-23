package com.kostovtd.herorealms.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kostovtd.boardy.ui.viewModels.BaseGameViewModel
import com.kostovtd.herorealms.MINIMUM_POINTS
import com.kostovtd.herorealms.STARTING_POINTS

class HeroRealmsViewModel: BaseGameViewModel() {

    val canStartGameSession: LiveData<Boolean> = Transformations.map(_gameSessionFirestore) {
        it.players.size >= 2
    }

    fun createGameSession() {
        createGameSession(STARTING_POINTS)
    }


    fun addCurrentPlayerToGameSession() {
        addCurrentPlayerToGameSession(STARTING_POINTS)
    }


    fun isGameFinished(): Boolean = _gameSessionDatabase.value?.points?.values?.contains(MINIMUM_POINTS) == true


    fun isCurrentPlayerAWinner(): Boolean {
        return isGameFinished() && gameSessionDatabase.value?.points?.get(currentPlayer?.uid) != MINIMUM_POINTS
    }
}