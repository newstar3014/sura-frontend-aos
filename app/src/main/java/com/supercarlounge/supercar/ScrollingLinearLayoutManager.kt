package com.supercarlounge.supercar

import android.content.Context
import android.graphics.PointF
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class ScrollingLinearLayoutManager(context: Context?, orientation: Int, reverseLayout: Boolean,
    private val duration: Int
) :
    LinearLayoutManager(context, orientation, reverseLayout) {
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView, state: RecyclerView.State,
        position: Int
    ) {
        val firstVisibleChild = recyclerView.getChildAt(0)
        if (firstVisibleChild != null){
            var itemHeight =0
            if (firstVisibleChild.height != null) {
                itemHeight  =  firstVisibleChild.height
                val currentPosition = recyclerView.getChildLayoutPosition(firstVisibleChild)
                var distanceInPixels = Math.abs((currentPosition - position) * itemHeight)
                if (distanceInPixels == 0) {
                    distanceInPixels = Math.abs(firstVisibleChild.y).toInt()
                }
                val smoothScroller = SmoothScroller(
                    recyclerView.context, distanceInPixels,
                    duration
                )
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }


    }

    private inner class SmoothScroller(context: Context?, distanceInPixels: Int, duration: Int) :
        LinearSmoothScroller(context) {
        private val distanceInPixels: Float
        private val duration: Float
        private var state: Boolean = false
        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@ScrollingLinearLayoutManager
                .computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateTimeForScrolling(dx: Int): Int {
            val proportion = dx.toFloat() / distanceInPixels
            var d = (duration * proportion).toInt()
            var startD = 222222
            var lastD = 3088
            var y = 0
            if (!state){
                y = startD
                state = true
            }else{
                y = lastD
            }
            return y
        }

        init {
            this.distanceInPixels = distanceInPixels.toFloat()
            this.duration = duration.toFloat()
        }
    }
}