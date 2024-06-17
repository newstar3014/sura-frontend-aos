package com.supercarlounge.supercar

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView


class Spacing : RecyclerView.ItemDecoration() {
    private var mSpacing = 0
     var mTopSpacing = 0
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // Column Index
        val index = (view.getLayoutParams() as GridLayoutManager.LayoutParams).spanIndex
        // Item 포지션
        val position = parent.getChildLayoutPosition(view)
        if (index == 0) {
            //좌측 Spacing 절반
            outRect.right = mSpacing / 2
        } else {
            //우측 Spacing 절반
            outRect.left = mSpacing / 2
        }
        // 상단 탑 Spacing 맨 위에 포지션 0, 1은 Spacing을 안 줍니다.
        if (position < 2) {
            outRect.top = 0
        } else {
            outRect.top = mTopSpacing
        }
    }

}
