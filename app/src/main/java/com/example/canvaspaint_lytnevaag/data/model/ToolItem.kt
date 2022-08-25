package com.example.canvaspaint_lytnevaag.data.model
import androidx.annotation.ColorRes
import com.example.canvaspaint_lytnevaag.data.COLOR
import com.example.canvaspaint_lytnevaag.data.SIZE
import com.example.canvaspaint_lytnevaag.data.TOOLS

// item, который находится в инструметов
sealed class ToolItem : Item {
    //цвет, опиcание цвета - это ресурсы
    data class ColorModel(@ColorRes val color: Int) : ToolItem()
    data class SizeModel(val size: Int) : ToolItem()
    data class ToolModel(
        val type: TOOLS,
        val selectedTool: TOOLS = TOOLS.NORMAL,
        val isSelected: Boolean = false,
        val selectedSize: SIZE = SIZE.SMALL,
        val selectedColor: COLOR = COLOR.BLACK
    ) : ToolItem()
}