package com.kostovtd.herorealms.views.activities

import com.kostovtd.boardy.data.models.GameSessionDatabase
import com.kostovtd.boardy.views.activities.BaseView


/**
 * Created by tosheto on 25.12.20.
 */
interface HeroRealmsView : BaseView  {
    fun handleGameSessionChanges(gameSessionData: GameSessionDatabase)
}