package com.kostovtd.boardy.data.models

import com.google.firebase.database.Exclude


/**
 * Created by tosheto on 22.02.21.
 */
class GameSessionDatabase(
    @get:Exclude var id: String = "",
    var active: Boolean = false,
    var points: HashMap<String, Int> = HashMap()
)