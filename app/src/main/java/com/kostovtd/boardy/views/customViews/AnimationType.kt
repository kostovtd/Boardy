package com.kostovtd.boardy.views.customViews

import com.kostovtd.boardy.R

// animation key = animation raw id
// animation value = if repeat animation
enum class AnimationType(val animations: HashMap<Int, Boolean>) {
    REMOVE_PLAYER(
        hashMapOf(
            Pair(R.raw.animation_taking_out_the_trash, true),
            Pair(R.raw.animation_trash_bin, true),
            Pair(R.raw.animation_trash_bin_circle, false),
            Pair(R.raw.animation_attention_1, false),
            Pair(R.raw.animation_attention_4, false)
        )
    ),

    BAD_NEWS(
        hashMapOf(
            Pair(R.raw.animation_sad_emoji, true),
            Pair(R.raw.animaton_sad_star, true)
        )
    ),

    SUCCESS(
        hashMapOf(
            Pair(R.raw.animation_clapping_hands, false)
        )
    ),

    ATTENTION(
        hashMapOf(
            Pair(R.raw.animation_attention_1, false),
            Pair(R.raw.animation_attention_4, false)
        )
    )
}