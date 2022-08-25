package com.example.canvaspaint_lytnevaag

import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.canvaspaint_lytnevaag.data.TOOLS
import com.example.canvaspaint_lytnevaag.data.model.Item
import com.example.canvaspaint_lytnevaag.data.model.ToolItem
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateLayoutContainer

//AdapterDelegate Заменяет адаптер, который мы писали в новостном
fun colorAdapterDelegate(
    onClick: (Int) -> Unit
): AdapterDelegate <List<Item>> =
    adapterDelegateLayoutContainer<ToolItem.ColorModel, Item>(
        R.layout.item_palette
    ) {
        val color:ImageView = findViewById(R.id.color)
        itemView.setOnClickListener { onClick(adapterPosition) }
        // bind - это является функция из обычного адаптера onBind, логика которая происодит внутри
        bind { list ->
            color.setColorFilter(
                context.resources.getColor(item.color, null),
                PorterDuff.Mode.SRC_IN
            )

        }
    }

fun sizeAdapterDelegate(
    onClick: (Int) -> Unit
): AdapterDelegate <List<Item>> =
    adapterDelegateLayoutContainer<ToolItem.SizeModel, Item>(
        R.layout.item_size
    ) {
        val size:TextView = findViewById(R.id.size)
//        val sizeImage:ImageView = findViewById(R.id.sizeImage)
        itemView.setOnClickListener { onClick(adapterPosition) }
        // bind - это является функция из обычного адаптера onBind, логика которая происодит внутри
        bind { list ->

            size.text = item.size.toString()
//            size.textSize=item.size.toFloat()




        }
    }

//AdapterDelegate  for tools
fun toolsAdapterDelegate(
    onToolsClick: (Int) -> Unit
): AdapterDelegate<List<Item>> = adapterDelegateLayoutContainer<ToolItem.ToolModel, Item>(
    R.layout.item_tools
) {
    val ivTool: ImageView = findViewById(R.id.ivTool)
    val lineView: View by lazy { findViewById(R.id.viewLine) }

    bind { list ->

       ivTool.setImageResource(item.type.value)


        when (item.type) {

            TOOLS.SIZE -> {

                val textSelectedSize:TextView by lazy {findViewById(R.id.tvSelectedSize)}
                textSelectedSize.text = item.selectedSize.value.toString()
                ivTool.isVisible = false
                textSelectedSize.isVisible = true

            }
            TOOLS.PALETTE -> {
                ivTool.setColorFilter(
                    context.resources.getColor(item.selectedColor.value, null),
                    // PorterDuff позволяет окрасить векторную картинку в цвет, который нам нужен
                    PorterDuff.Mode.SRC_IN
                )

            }

            else -> {
                if (item.isSelected) {
                    ivTool.setBackgroundResource(R.drawable.bg_selected)
                } else {
                    ivTool.setBackgroundResource(android.R.color.transparent)
                }
            }
        }

        itemView.setOnClickListener {
            onToolsClick(adapterPosition)
        }
    }
}