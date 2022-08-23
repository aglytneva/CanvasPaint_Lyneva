package com.example.canvaspaint_lytnevaag

import androidx.annotation.ColorRes

enum class COLOR(
    @ColorRes
    val value: Int
) {

    BLACK(R.color.black),
    RED(R.color.purple_500),
    BLUE(R.color.purple_700),
    PINK(R.color.teal_700),
    GREEN (R.color.teal_200);


    companion object {
        private val map = values().associateBy(COLOR::value)
        fun from(color: Int) = map[color] ?: GREEN
    }
}
