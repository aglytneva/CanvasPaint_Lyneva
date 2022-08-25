package com.example.canvaspaint_lytnevaag.data

import androidx.annotation.ColorRes
import com.example.canvaspaint_lytnevaag.R

enum class COLOR(
    @ColorRes
    val value: Int
) {
    YELLOW (R.color.yellow),
    FUCHSIA (R.color.fuchsia),
    RED (R.color.red),
    SILVER (R.color.silver),
    GREY (R.color.gray),
    OLIVE (R.color.olive),
    PURPLE (R.color.purple),
    MAROON (R.color.maroon),
    AQUA (R.color.aqua),
    LIME (R.color.lime),
    TEAL (R.color.teal),
    GREEN (R.color.green),
    LIGHTBLUE (R.color.blue),
    BLUE(R.color.purple_700),
    NAVY (R.color.navy),
    BLACK(R.color.black),
    WHITE (R.color.white);

    companion object {
        private val map = values().associateBy(COLOR::value)
        fun from(color: Int) = map[color] ?: BLACK
    }
}
//
