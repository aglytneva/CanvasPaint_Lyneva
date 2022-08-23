package com.example.canvaspaint_lytnevaag
import androidx.annotation.ColorRes

// item, который находится в инструметов
sealed class ToolItem : Item {
    //цвет, опиание цвета - это ресурсы
    data class ColorModel(@ColorRes val color: Int) : ToolItem()
    data class ToolModel(
        val type: TOOLS,
        val selectedTool: TOOLS = TOOLS.NORMAL,
        val isSelected: Boolean = false,
////        val selectedSize: SIZE = SIZE.SMALL,
        val selectedColor: COLOR = COLOR.BLACK
    ) : ToolItem()
}