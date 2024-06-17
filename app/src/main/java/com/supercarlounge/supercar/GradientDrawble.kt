package com.supercarlounge.supercar
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path.FillType
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import java.lang.RuntimeException

//그라디엔트 디자인 클래스 예)가운데 투명 외곽선 그라디엔트 적용하거나 할때 사용
class GradientDrawable(
    private var strokeStartColor: Int,
    private var strokeEndColor: Int,
    private var fillStartColor: Int,
    private var fillEndColor: Int,
    private var strokeWidth: Float,
    private var radius: Float,
    private var strokeGradientDirection: Direction,
    private var fillGradientDirection: Direction
) : Drawable() {

    enum class Direction {
        LEFT_RIGHT,
        TOP_BOTTOM,
        RIGHT_LEFT,
        BOTTOM_TOP,
        TL_BR,
        TR_BL,
        BR_TL,
        BL_TR
    }

    private val strokePaint: Paint = Paint(ANTI_ALIAS_FLAG)
    private val fillPaint: Paint = Paint(ANTI_ALIAS_FLAG)
    private val strokeOuterRect = RectF()
    private val fillRect = RectF()
    private val path = Path()

    init {
        strokePaint.style = Paint.Style.FILL
        path.fillType = FillType.EVEN_ODD
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)

        path.reset()

        strokeOuterRect.set(bounds)
        fillRect.set(
            bounds.left + strokeWidth,
            bounds.top + strokeWidth,
            bounds.right - strokeWidth,
            bounds.bottom - strokeWidth
        )

        path.addRoundRect(
            strokeOuterRect,
            radius,
            radius,
            Path.Direction.CW
        )

        path.addRoundRect(
            fillRect,
            radius,
            radius,
            Path.Direction.CW
        )
    }

    override fun draw(@NonNull canvas: Canvas) {
        var x0: Float
        var y0: Float
        var x1: Float
        var y1: Float

        // drawing the stroke

        when (strokeGradientDirection) {
            Direction.LEFT_RIGHT -> {
                x0 = strokeOuterRect.left
                y0 = strokeOuterRect.centerY()
                x1 = strokeOuterRect.right
                y1 = strokeOuterRect.centerY()
            }
            Direction.TOP_BOTTOM -> {
                x0 = strokeOuterRect.centerX()
                y0 = strokeOuterRect.top
                x1 = strokeOuterRect.centerX()
                y1 = strokeOuterRect.bottom
            }
            Direction.RIGHT_LEFT -> {
                x0 = strokeOuterRect.right
                y0 = strokeOuterRect.centerY()
                x1 = strokeOuterRect.left
                y1 = strokeOuterRect.centerY()
            }
            Direction.BOTTOM_TOP -> {
                x0 = strokeOuterRect.centerX()
                y0 = strokeOuterRect.bottom
                x1 = strokeOuterRect.centerX()
                y1 = strokeOuterRect.top
            }
            Direction.TL_BR -> {
                x0 = strokeOuterRect.left
                y0 = strokeOuterRect.top
                x1 = strokeOuterRect.right
                y1 = strokeOuterRect.bottom
            }
            Direction.TR_BL -> {
                x0 = strokeOuterRect.right
                y0 = strokeOuterRect.top
                x1 = strokeOuterRect.left
                y1 = strokeOuterRect.bottom
            }
            Direction.BR_TL -> {
                x0 = strokeOuterRect.right
                y0 = strokeOuterRect.bottom
                x1 = strokeOuterRect.left
                y1 = strokeOuterRect.top
            }
            Direction.BL_TR -> {
                x0 = strokeOuterRect.left
                y0 = strokeOuterRect.bottom
                x1 = strokeOuterRect.right
                y1 = strokeOuterRect.top
            }

        }

        strokePaint.shader = LinearGradient(
            x0,
            y0,
            x1,
            y1,
            strokeStartColor,
            strokeEndColor,
            Shader.TileMode.MIRROR
        )

        canvas.drawPath(path, strokePaint)

        // filling the shape

        when (fillGradientDirection) {
            Direction.LEFT_RIGHT -> {
                x0 = fillRect.left
                y0 = fillRect.centerY()
                x1 = fillRect.right
                y1 = fillRect.centerY()
            }
            Direction.TOP_BOTTOM -> {
                x0 = fillRect.centerX()
                y0 = fillRect.top
                x1 = fillRect.centerX()
                y1 = fillRect.bottom
            }
            Direction.RIGHT_LEFT -> {
                x0 = fillRect.right
                y0 = fillRect.centerY()
                x1 = fillRect.left
                y1 = fillRect.centerY()
            }
            Direction.BOTTOM_TOP -> {
                x0 = fillRect.centerX()
                y0 = fillRect.bottom
                x1 = fillRect.centerX()
                y1 = fillRect.top
            }
            Direction.TL_BR -> {
                x0 = fillRect.left
                y0 = fillRect.top
                x1 = fillRect.right
                y1 = fillRect.bottom
            }
            Direction.TR_BL -> {
                x0 = fillRect.right
                y0 = fillRect.top
                x1 = fillRect.left
                y1 = fillRect.bottom
            }
            Direction.BR_TL -> {
                x0 = fillRect.right
                y0 = fillRect.bottom
                x1 = fillRect.left
                y1 = fillRect.top
            }
            Direction.BL_TR -> {
                x0 = fillRect.left
                y0 = fillRect.bottom
                x1 = fillRect.right
                y1 = fillRect.top
            }
            else -> {}
        }

        fillPaint.shader = LinearGradient(
            x0,
            y0,
            x1,
            y1,
            fillStartColor,
            fillEndColor,
            Shader.TileMode.MIRROR
        )

        canvas.drawRoundRect(
            fillRect,
            radius,
            radius,
            fillPaint
        )
    }

    override fun setAlpha(alpha: Int) {
        strokePaint.alpha = alpha
        fillPaint.alpha = alpha

        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        throw RuntimeException("Color filter cannot be set to this Drawable")
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    fun setStrokeColors(startColor: Int, endColor: Int) {
        this.strokeStartColor = startColor
        this.strokeEndColor = endColor

        invalidateSelf()
    }

    fun setFillColors(startColor: Int, endColor: Int) {
        this.fillStartColor = startColor
        this.fillEndColor = endColor

        invalidateSelf()
    }

    fun setRadius(radius: Float) {
        this.radius = radius

        invalidateSelf()
    }

    fun setStrokeWidth(width: Float) {
        this.strokeWidth = width

        invalidateSelf()
    }
}