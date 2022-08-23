package com.example.canvaspaint_lytnevaag


class CanvasViewModel : BaseViewModel <ViewState>() {
    override fun initialViewState(): ViewState = ViewState(

        //функция enumValues может выдернуть все значения, которые есть в emun классе
        colorList= enumValues<COLOR>().map { ToolItem.ColorModel (it.value) },
        toolsList = enumValues<TOOLS>().map {ToolItem.ToolModel (it)},
        canvasViewState = CanvasViewState(color = COLOR.GREEN, size = SIZE.MEDIUM, tools = TOOLS.PALETTE),
        isPaletteVisible = false,
        isToolsVisible = false
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
                        return previousState.copy(isPaletteVisible = !previousState.isPaletteVisible)
                    }

                    else -> {

                        val toolsList = previousState.toolsList.mapIndexed() { index, model ->
                            if (index == event.index) {
                                model.copy(isSelected = true)
                            } else {
                                model.copy(isSelected = false)
                            }
                        }

                        return previousState.copy(toolsList = toolsList)
                    }
                }
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