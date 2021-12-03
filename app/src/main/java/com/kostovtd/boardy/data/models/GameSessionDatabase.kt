package com.kostovtd.boardy.data.models


/**
 * Created by tosheto on 22.02.21.
 */
class GameSessionDatabase(
    var id: String = "",
    var active: Boolean = false,
    var points: HashMap<String, Int> = HashMap()
)