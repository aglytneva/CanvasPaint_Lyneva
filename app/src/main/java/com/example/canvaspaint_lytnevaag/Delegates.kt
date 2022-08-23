package com.example.canvaspaint_lytnevaag

import android.graphics.PorterDuff
import android.system.Os.bind
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView
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


//AdapterDelegate  for tools
fun toolsAdapterDelegate(
    onToolsClick: (Int) -> Unit
): AdapterDelegate<List<Item>> = adapterDelegateLayoutContainer<ToolItem.ToolModel, Item>(
    R.layout.item_tools
) {

    val ivTool: ImageView = findViewById(R.id.ivTool)

    bind { list ->

       ivTool.setImageResource(item.type.value)

//        if (itemView.tvToolsText.visibility == View.VISIBLE) {
//            itemView.tvToolsText.visibility = View.GONE
//        }



        when (item.type) {

//            TOOLS.SIZE -> {
//                itemView.tvToolsText.visibility = View.VISIBLE
//                itemView.tvToolsText.text = item.selectedSize.value.toString()
//            }

            TOOLS.PALETTE -> {
                ivTool.setColorFilter(
                    context.resources.getColor(item.selectedColor.value, null),
                    // PorterDuff gjpdjkztn окрасить векторную картинку в цвет, который нам нужен
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