package com.kostovtd.boardy.util

import android.content.Context

/**
 * Created by tosheto on 23.11.20.
 */
fun spToPixels(sp: Float, context: Context): Float {
    val scaledDensity: Float = context.resources.displayMetrics.scaledDensity
    return sp * scaledDensity
}