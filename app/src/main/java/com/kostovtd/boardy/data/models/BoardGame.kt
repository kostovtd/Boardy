package com.kostovtd.boardy.data.models

/**
 * Created by tosheto on 22.01.21.
 */
class BoardGame(
    var id: String = "",
    val name: String = "",
    val nameDynamicFeature: String = "",
    val designers: ArrayList<String> = ArrayList(),
    val artists: ArrayList<String> = ArrayList(),
    val publishers: ArrayList<String> = ArrayList(),
    val minNumberOfPlayers: Int = -1,
    val maxNumberOfPlayers: Int = -1,
    val minPlayingTime: Int = -1, // in minutes
    val maxPlayingTime: Int = -1 // in minutes
)