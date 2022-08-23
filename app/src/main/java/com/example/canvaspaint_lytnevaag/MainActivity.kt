package com.example.canvaspaint_lytnevaag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    // вводим константы для определения palleteLayout и toolsLayout,
    // для того, чтобы определить на какой индекс toolslistа был клик
    companion object {
        private const val PALLETE_VIEW =0
        private const val TOOLS_VIEW =1

    }

    private val viewModel:CanvasViewModel by viewModel()

    //создаем toolslist и заполняем его в onCreate. нужно для того чтобы если элементов много,
    // то можно было через этот лист подать сигнал всем элементам, чтоб они скрылись
    private var toolsList : List<ToolsLayout> = listOf()

    private val palleteLayout:ToolsLayout by lazy { findViewById(R.id.paletteLayout) }
    private val toolsLayout:ToolsLayout by lazy { findViewById(R.id.toolsLayout) }
    private val ivTools:ImageView by lazy { findViewById(R.id.ivTools) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolsList = listOf(palleteLayout, toolsLayout)
        viewModel.viewState.observe(this,::render)
        palleteLayout.setOnClickListener {
            viewModel.processUiEvent(UiEvent.OnPaletteClicked(it))
        }

        toolsLayout.setOnClickListener {
            viewModel.processUiEvent(UiEvent.OnToolsClick(it))
        }

        ivTools.setOnClickListener{
            viewModel.processUiEvent(UiEvent.OnToolbarClicked)
        }


    }

    // в рендере отлавливается работа с...
    private fun render (viewState: ViewState){
        with  (toolsList[PALLETE_VIEW]) {
            render(viewState.colorList)
            isVisible = viewState.isPaletteVisible
        }

        with (toolsList[TOOLS_VIEW]) {
            render(viewState.toolsList)
            isVisible = viewState.isToolsVisible
        }

    }
}