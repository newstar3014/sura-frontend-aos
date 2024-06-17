package com.supercarlounge.supercar.customview

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MiddleItemFinder(
    context: Context,
    layoutManager: LinearLayoutManager,
    callback: MiddleItemCallback,
    controlState: Int
) :
    RecyclerView.OnScrollListener() {
    private val context: Context
    private val layoutManager: LinearLayoutManager
    private val callback: MiddleItemCallback
    private val controlState: Int

    init {
        this.context = context
        this.layoutManager = layoutManager
        this.callback = callback
        this.controlState = controlState
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        super.onScrolled(recyclerView, dx, dy)
    }
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
      //  if (controlState == ALL_STATES || newState == controlState) {
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()
            val itemsCount = lastVisible - firstVisible + 1
            val screenCenter: Int = context.getResources().getDisplayMetrics().widthPixels / 2
            var minCenterOffset = Int.MAX_VALUE
            var middleItemIndex = 0
            for (index in 0 until itemsCount) {
                val listItem = layoutManager.getChildAt(index) ?: return
                val leftOffset = listItem.left
                val rightOffset = listItem.right
                val centerOffset =
                    Math.abs(leftOffset - screenCenter) + Math.abs(rightOffset - screenCenter)
                if (minCenterOffset > centerOffset) {
                    minCenterOffset = centerOffset
                    middleItemIndex = index + firstVisible
                }
            }
            callback.scrollFinished(middleItemIndex)
      //  }
    }

    interface MiddleItemCallback {
        fun scrollFinished(middleElement: Int)
    }

    companion object {
        const val ALL_STATES = 10
    }
}