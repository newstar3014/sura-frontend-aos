package com.supercarlounge.supercar.customview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SuggestItemDeco (var padding:Int):RecyclerView.ItemDecoration(){
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.left = padding
            outRect.right = padding

    }

}