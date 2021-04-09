package com.kostovtd.herorealms.views.fragments

import com.kostovtd.boardy.data.models.BoardGameGameSession
import com.kostovtd.boardy.views.activities.BaseView

interface SetUpHeroRealmsModeView: BaseView {
    fun startSetUpHeroRealmsPlayersFragmentAsAdmin(boardGameGameSession: BoardGameGameSession)
}