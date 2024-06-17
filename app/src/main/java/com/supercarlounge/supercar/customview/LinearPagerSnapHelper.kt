package com.supercarlounge.supercar.customview


import android.util.DisplayMetrics
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.SmoothScroller.ScrollVectorProvider
import kotlin.math.*

/**
 * 1.Linear는 fling이 되고 Pager는 fling이 되지 않는다. dragging 속도에 따라 두 sanpHelper의 기능을 적절히 선별하여 쓰도록 만들었다.
 * 2.Snap Gravity를 선택할 수 있다. (현재 horizontal만 지원)
 *
 * validateFling(), getFlingVelocity()를 수정하여 fling 인지 민감도와 scroll양을 조절할 수 있다.
 */

class LinearPagerSnapHelper(val gravity: Gravity = Gravity.CENTER) : LinearSnapHelper() {

    enum class Gravity {
        LEFT, CENTER
    }

    companion object {
        private const val MAX_SCROLL_ON_FLING_DURATION = 1 // ms
        private const val MILLISECONDS_PER_INCH = 100f
    }

    private var flingSlopMin = 0
    private var flingSlopMax = 0

    // Orientation helpers are lazily created per LayoutManager.
    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    private lateinit var recyclerView: RecyclerView

    private var fling = true

    fun snapToFirst() {
        val snapDistance = calculateDistanceToFinalSnap(recyclerView.layoutManager!!, recyclerView.getChildAt(0))
        recyclerView.smoothScrollBy(snapDistance[0], snapDistance[1])
    }

    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
        var targetIndex = RecyclerView.NO_POSITION
        if (fling) {
            //linear
            targetIndex = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
        }

        if (fling && targetIndex == RecyclerView.NO_POSITION) {
            fling = false
        }

        if (!fling) {
            //pager
            val itemCount = layoutManager.itemCount
            if (itemCount == 0) {
                return RecyclerView.NO_POSITION
            }

            var baseView: View? = null

            if (layoutManager.canScrollVertically()) {
                // TODO: 2017. 5. 15. vertical
                baseView = findCenterView(layoutManager, getVerticalHelper(layoutManager))
            } else if (layoutManager.canScrollHorizontally()) {
                baseView = if (gravity == Gravity.CENTER)
                    findCenterView(layoutManager, getHorizontalHelper(layoutManager)) else findLeftView()
            }

            if (baseView == null) {
                return RecyclerView.NO_POSITION
            }

            val basePosition = layoutManager.getPosition(baseView)
            if (basePosition == RecyclerView.NO_POSITION) {
                return RecyclerView.NO_POSITION
            }

            val forwardDirection = if (layoutManager.canScrollHorizontally()) {
                velocityX > 0
            } else {
                velocityY > 0
            }

            var reverseLayout = false

            if (layoutManager is ScrollVectorProvider) {
                val vectorProvider = layoutManager as ScrollVectorProvider
                val vectorForEnd = vectorProvider.computeScrollVectorForPosition(itemCount - 1)
                if (vectorForEnd != null) {
                    reverseLayout = vectorForEnd.x < 0 || vectorForEnd.y < 0
                }
            }

            var retValue =
                if (gravity == Gravity.CENTER) {
                    if (reverseLayout) {
                        if (forwardDirection) basePosition - 1 else basePosition + 1
                    } else {
                        if (forwardDirection) basePosition + 1 else basePosition - 1
                    }
                } else {
                    if (reverseLayout) {
                        if (forwardDirection) basePosition else basePosition + 1
                    } else {
                        if (forwardDirection) basePosition + 1 else basePosition
                    }
                }

            if (retValue < 0) retValue = 0
            targetIndex = retValue
        }

        return targetIndex
    }

    override fun createSnapScroller(layoutManager: RecyclerView.LayoutManager): LinearSmoothScroller? {
        if (layoutManager !is ScrollVectorProvider) {
            return null
        }

        return if (fling) {
            super.createSnapScroller(layoutManager)
        } else {
            object : LinearSmoothScroller(recyclerView.context) {
                override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
                    val snapDistances = calculateDistanceToFinalSnap(recyclerView.layoutManager!!, targetView)
                    val dx = snapDistances[0]
                    val dy = snapDistances[1]

                    val time = calculateTimeForDeceleration(max(abs(dx), abs(dy)))
                    if (time > 0) {
                        action.update(dx, dy, time, mDecelerateInterpolator)
                    }
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                    return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
                }

                override fun calculateTimeForScrolling(dx: Int): Int {
                    return min(MAX_SCROLL_ON_FLING_DURATION, super.calculateTimeForScrolling(dx))
                }
            }
        }
    }

    /**
     * control fling recognition sensitiveness
     * If you want less sensitiveness put some positive value to 'controlFactor'
     */
    private fun validateFling(velocity: Int) {
        val controlFactor = 1f
        fling = abs(velocity) > flingSlopMin * controlFactor
    }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        val layoutManager = recyclerView.layoutManager ?: return false
        recyclerView.adapter ?: return false

        validateFling(if (layoutManager.canScrollVertically()) abs(velocityY) else abs(velocityX))

        return snapFromFling(layoutManager, velocityX, velocityY)
    }

    private fun snapFromFling(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Boolean {
        if (layoutManager !is ScrollVectorProvider) {
            return false
        }

        val targetPosition = findTargetSnapPosition(layoutManager, getFlingVelocity(velocityX), getFlingVelocity(velocityY))
        if (targetPosition == RecyclerView.NO_POSITION) {
            return false
        }

        val smoothScroller = createSnapScroller(layoutManager) ?: return false

        smoothScroller.targetPosition = targetPosition
        layoutManager.startSmoothScroll(smoothScroller)
        return true;
    }

    /**
     * control how much you want to make it scroll when fling.
     * The value 'reduceFactor' 0.5 means that it will scroll half compared to normal LinearSnapHelper
     */
    private fun getFlingVelocity(velocity : Int) : Int{
        val reduceFactor = 0.5f
        return (velocity.coerceAtMost(flingSlopMax) * reduceFactor).toInt()
    }

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView!!
        super.attachToRecyclerView(recyclerView)

        flingSlopMin = ViewConfiguration.get(recyclerView.context.applicationContext).scaledMinimumFlingVelocity
        flingSlopMax = ViewConfiguration.get(recyclerView.context.applicationContext).scaledMaximumFlingVelocity
    }

    /**
     * Return the child view that is currently closest to the center of this parent.
     *
     * @param layoutManager The [RecyclerView.LayoutManager] associated with the attached
     * [RecyclerView].
     * @param helper        The relevant [OrientationHelper] for the attached [RecyclerView].
     * @return the child view that is currently closest to the center of this parent.
     */
    private fun findCenterView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        var closestChild: View? = null
        val center = if (layoutManager.clipToPadding) {
            helper.startAfterPadding + helper.totalSpace / 2
        } else {
            helper.end / 2
        }

        var absClosest = Int.MAX_VALUE
        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)

            // If first view completely visible, the first view will be returned.
            if (layoutManager.getPosition(child!!) == 0 && child.x >= 0) {
                closestChild = child
                break
            }
            val childCenter = (helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2)
            val absDistance = abs(childCenter - center)

            /** if child center is closer than previous closest, set it as closest   */
            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

    private fun findLeftView(): View? {
        return recyclerView.getChildAt(0)
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mVerticalHelper == null) {
            mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return mVerticalHelper!!
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (mHorizontalHelper == null) {
            mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return mHorizontalHelper!!
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
        val out = IntArray(2)

        if (layoutManager.canScrollHorizontally()) {
            out[0] =
                if (gravity == Gravity.CENTER)
                    distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager))
                else
                    distanceToLeft(layoutManager, targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            out[1] =
                if (gravity == Gravity.CENTER)
                    distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager))
                else
                    distanceToLeft(layoutManager, targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }

        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager.canScrollVertically()) {
            return if (gravity == Gravity.CENTER)
                findCenterView(layoutManager, getVerticalHelper(layoutManager))
            else
                recyclerView.getChildAt(0)
        } else if (layoutManager.canScrollHorizontally()) {
            return if (gravity == Gravity.CENTER)
                findCenterView(layoutManager, getHorizontalHelper(layoutManager))
            else findFirstView(layoutManager)
        }
        return null
    }

    // TODO: 2017. 7. 11. trim..
    private fun findFirstView(layoutManager: RecyclerView.LayoutManager): View? {
        val lastItemVisible = ((layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                == recyclerView.adapter!!.itemCount - 1)
        val isFooter =/*lastItemVisible && ((FeedAdapter)recyclerView.getAdapter()).isNeedFooter()*/false

        return if (isFooter) {
            recyclerView.getChildAt(recyclerView.childCount - 1)
        } else {
            val firstView = recyclerView.getChildAt(0)
            if (firstView != null) {
                if (firstView.x < 0 && abs(firstView.x) > firstView.width / 2) {
                    return recyclerView.getChildAt(1)
                }
            }
            firstView
        }
    }

    private fun distanceToCenter(layoutManager: RecyclerView.LayoutManager, targetView: View, helper: OrientationHelper): Int {
        if (layoutManager.getPosition(targetView) == 0) {
            val childStart = helper.getDecoratedStart(targetView)
            val containerStart = if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                if (layoutManager.clipToPadding) helper.startAfterPadding else 0
            } else {
                0
            }
            return childStart - containerStart
        } else {
            val childCenter = helper.getDecoratedStart(targetView) + helper.getDecoratedMeasurement(targetView) / 2
            val containerCenter = if (layoutManager.clipToPadding) {
                helper.startAfterPadding + helper.totalSpace / 2
            } else {
                helper.end / 2
            }
            return childCenter - containerCenter
        }
    }

    private fun distanceToLeft(layoutManager: RecyclerView.LayoutManager, targetView: View, helper: OrientationHelper): Int {
        if (layoutManager.getPosition(targetView) == 0) {
            val childStart = helper.getDecoratedStart(targetView)
            val containerStart = if (recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                if (layoutManager.clipToPadding) helper.startAfterPadding else 0
            } else {
                0
            }
            return childStart - containerStart
        } else {
            val childLeft = helper.getDecoratedStart(targetView)
            val containerLeft = if (layoutManager.clipToPadding) {
                helper.startAfterPadding
            } else {
                0
            }
            return childLeft - containerLeft
        }
    }
}