package com.example.canvaspaint_lytnevaag

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.canvaspaint_lytnevaag.data.model.ToolItem
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

//Кастомный вью. Cоздаем LinearLouyaut название loyaut_tools под эту кастомную вьюшку
// переиспользуем логику CardView
// Мы берем кардвью за базис нашей новой кастомной вью, кардвью является фреймлоутом и идеальном
//Мы хотим иметь атомарный блок, который содержит в себе ресайклервью
//Унифицированная абстракция, которая делает одиннаковые вещи
//@JvmOverloads переопределение кастомной вьюхи
// Наследоваться можно и от других вью, не только кард вью
//Блок, в котором прописаа логика
//Можно создавать через xml кастомную вьюху, но мы делаем с помощью ренде
class ToolsLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var onClick: (Int) -> Unit = {}

    private val toolsList: RecyclerView by lazy { findViewById(R.id.rvTools) }

    private val adapterDelegate = ListDelegationAdapter(
        colorAdapterDelegate {
            onClick(it)
        },
        toolsAdapterDelegate {
            onClick(it) },

        sizeAdapterDelegate {
            onClick(it) }

    )

//    private val adapterDelegate = ListDelegationAdapter(
//        colorAdapterDelegate {
//            onClick(it)
//        },
//        toolsAdapterDelegate {
//            onClick(it)
//        }
//    )

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
// ресуклервью, у которго есть LinearLayoutManager менеджер, reverseLayout- это нужно ли повернуть Layout
        toolsList.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        toolsList.setAdapterAndCleanupOnDetachFromWindow(adapterDelegate)
    }

// В этот адаптер делегат будут помещаться тулайтемы, которые будут отрисовываться внутри нашего ресайклера
    fun render(list: List<ToolItem>) {
        adapterDelegate.setData(list)
    }

    fun setOnClickListener(onClick: (Int) -> Unit) {
        this.onClick = onClick
    }
}