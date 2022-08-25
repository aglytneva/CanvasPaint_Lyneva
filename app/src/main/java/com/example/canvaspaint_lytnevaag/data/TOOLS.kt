package com.example.canvaspaint_lytnevaag.data

import androidx.annotation.DrawableRes
import com.example.canvaspaint_lytnevaag.R

enum class TOOLS(
    @DrawableRes
    val value: Int
) {
    NORMAL(R.drawable.ic_horizontal_line),
    DASH(R.drawable.ic_dashed_line),
    SIZE(R.drawable.ic_baseline_format_size_24),
    PALETTE(R.drawable.ic_baseline_brightness_1_24),
    ERASER (R.drawable.ic_baseline_cleaning_services_24)
}