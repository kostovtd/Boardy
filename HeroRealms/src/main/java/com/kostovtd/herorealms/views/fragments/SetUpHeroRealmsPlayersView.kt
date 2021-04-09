package com.kostovtd.herorealms.views.fragments

import android.graphics.Bitmap
import com.kostovtd.boardy.views.activities.BaseView

interface SetUpHeroRealmsPlayersView: BaseView {
    fun showQRcode(qrBitmap: Bitmap)
    fun showPlayers(names: ArrayList<String>, adminId: String)
}