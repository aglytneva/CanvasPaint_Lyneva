package com.example.canvaspaint_lytnevaag

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class CanvasViewState(val color: COLOR, val size: SIZE, val tools: TOOLS)

enum class COLOR(
    @ColorRes
    val value: Int
) {

    BLACK(R.color.black),
    RED(R.color.purple_500),
    BLUE(R.color.purple_700),
    GREEN(R.color.purple_200),
    PINK(R.color.teal_700);

    companion object {
        private val map = values().associateBy(COLOR::value)
        fun from(color: Int) = map[color] ?: BLACK
    }
}

enum class SIZE(
    val value: Int
) {
    SMALL(4),
    MEDIUM(16),
    LARGE(32);

    companion object {
        private val map = values().associateBy(SIZE::value)
        fun from(size: Int) = map[size] ?: SMALL
    }
}

enum class TOOLS(
    @DrawableRes
    val value: Int
) {
//    NORMAL(R.drawable.ic_horizontal_line),
//    DASH(R.drawable.ic_dashed_line),
//    SIZE(R.drawable.ic_baseline_brightness_1_24),
//    PALETTE(R.drawable.ic_baseline_brightness_1_24)
}