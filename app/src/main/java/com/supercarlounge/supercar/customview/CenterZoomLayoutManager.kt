package com.supercarlounge.supercar.customview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.supercarlounge.supercar.adapter.ProfileSugesstAdapter


class CenterZoomLayoutManager : LinearLayoutManager {
    private val mShrinkAmount = 0.2f
    private val mShrinkDistance = 0.1f
    var setadapter:ProfileSugesstAdapter?=null
    var mcontext:Context?=null
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean,adapter:ProfileSugesstAdapter) : super(
        context,
        orientation,
        reverseLayout,
    ) {
        mcontext = context
        setadapter = adapter

    }

    override fun scrollVerticallyBy(dy: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val orientation = orientation
        return if (orientation == VERTICAL) {
            val scrolled = super.scrollVerticallyBy(dy, recycler, state)
        changeview()
            scrolled
        } else {
            0
        }
    }
    override fun scrollHorizontallyBy(dx: Int, recycler: Recycler, state: RecyclerView.State): Int {
        val orientation = orientation
        return if (orientation == HORIZONTAL) {
            val scrolled = super.scrollHorizontallyBy(dx, recycler, state)
          changeview()
            scrolled
        } else {
            0
        }
    }
    fun changeview(){
        val midpoint = width / 2f
        val d0 = 0f
        val d1 = mShrinkDistance * midpoint
        val s0 = 1f
        val s1 = 1f - mShrinkAmount

        for (i in 0 until childCount) {
            val child: View = getChildAt(i)!!
            val childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f
            val d = Math.min(d1, Math.abs(midpoint - childMidpoint))
            val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)

            if(scale>0.8){

            }

            child.setScaleX(scale)
            child.setScaleY(scale)
        }
    }
}