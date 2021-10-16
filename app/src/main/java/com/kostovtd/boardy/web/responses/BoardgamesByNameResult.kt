package com.kostovtd.boardy.web.responses

import com.kostovtd.boardy.data.models.BoardGame

class BoardgamesByNameResult(val data: ArrayList<BoardGame>? = null): BaseFirebaseResponse()