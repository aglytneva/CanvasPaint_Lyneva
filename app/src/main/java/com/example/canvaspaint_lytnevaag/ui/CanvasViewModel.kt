package com.example.canvaspaint_lytnevaag.ui

import com.example.canvaspaint_lytnevaag.base.BaseViewModel
import com.example.canvaspaint_lytnevaag.base.Event
import com.example.canvaspaint_lytnevaag.data.COLOR
import com.example.canvaspaint_lytnevaag.data.SIZE
import com.example.canvaspaint_lytnevaag.data.TOOLS
import com.example.canvaspaint_lytnevaag.data.model.ToolItem


class CanvasViewModel : BaseViewModel<ViewState>() {
    override fun initialViewState(): ViewState = ViewState(

        //функция enumValues может выдернуть все значения, которые есть в emun классе
        colorList = enumValues<COLOR>().map { ToolItem.ColorModel(it.value) },
        toolsList = enumValues<TOOLS>().map { ToolItem.ToolModel(it) },
        sizeList = enumValues<SIZE>().map { ToolItem.SizeModel(it.value) },
        canvasViewState = CanvasViewState(
            color = COLOR.BLACK,
            size = SIZE.MEDIUM,
            tools = TOOLS.PALETTE
        ),
        isBrushSizeChangerVisible = false,
        isPaletteVisible = false,
        isToolsVisible = false,

    )

    init {
        processDataEvent(DataEvent.OnSetDefaultTools(tool = TOOLS.NORMAL, color = COLOR.BLACK))
    }

    override fun reduce(event: Event, previousState: ViewState): ViewState? {
        when (event) {
            is UiEvent.OnToolbarClicked -> {
                return previousState.copy(
                    isToolsVisible = !previousState.isToolsVisible,
                    isPaletteVisible = false
                )
                //инвертируем предыдущее значение, можно было написать =true, но тогда можно запутаться
            }

            // ordinal - это одинаковый порядок вещей
            is UiEvent.OnToolsClick -> {
                when (event.index) {

                    TOOLS.PALETTE.ordinal -> {
                        if (previousState.isBrushSizeChangerVisible == true)
                            return previousState.copy(
                                isPaletteVisible = !previousState.isPaletteVisible,
                                isBrushSizeChangerVisible = !previousState.isBrushSizeChangerVisible
                            )
                        return previousState.copy(
                            isPaletteVisible = !previousState.isPaletteVisible
                        )
                    }

                    TOOLS.SIZE.ordinal -> {
                        if (previousState.isPaletteVisible == true)
                            return previousState.copy(
                                isBrushSizeChangerVisible = !previousState.isBrushSizeChangerVisible,
                                isPaletteVisible = !previousState.isPaletteVisible
                            )
                        return previousState.copy(
                            isBrushSizeChangerVisible = !previousState.isBrushSizeChangerVisible
                        )
                    }
                    TOOLS.ERASER.ordinal -> {
                        return previousState.copy(
                            canvasViewState = previousState.canvasViewState.copy (
                                color = COLOR.WHITE,
                                tools = TOOLS.NORMAL)
                        )
                    }

                    else -> {

                        val toolsList = previousState.toolsList.mapIndexed() { index, model ->
                            if (index == event.index) {
                                model.copy(isSelected = true)
                            } else {
                                model.copy(isSelected = false)
                            }
                        }

                        return previousState.copy(
                            toolsList = toolsList,
                            canvasViewState = previousState.canvasViewState.copy(tools = TOOLS.values()[event.index])
                        )
                    }
                }
            }

            is UiEvent.OnPaletteClicked -> {
                val selectedColor = COLOR.values()[event.index]
                val toolsList = previousState.toolsList.map {
                    if (it.type == TOOLS.PALETTE) {
                        it.copy(selectedColor = selectedColor)
                    } else{
                        it
                    }
                }
                return previousState.copy(
                    toolsList = toolsList,
                    canvasViewState = previousState.canvasViewState.copy (color = selectedColor))
            }

            is UiEvent.OnSizeClick -> {
                val selectedSize = SIZE.values()[event.index]
                val toolsList = previousState.toolsList.map {
                    if (it.type == TOOLS.SIZE) {
                        it.copy(selectedSize = selectedSize)
                    } else{
                        it
                    }
                }
                return previousState.copy(
                    toolsList = toolsList,
                    canvasViewState = previousState.canvasViewState.copy (size = selectedSize))
            }

            is DataEvent.OnSetDefaultTools -> {
                val toolsList = previousState.toolsList.map { model ->
                    if (model.type == event.tool) {
                        model.copy(isSelected = true, selectedColor = event.color)
                    } else {
                        model.copy(isSelected = false)
                    }
                }
                return previousState.copy(toolsList = toolsList)
            }
            else -> return null
        }
    }
}