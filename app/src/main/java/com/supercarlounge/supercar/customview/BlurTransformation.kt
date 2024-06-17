package com.supercarlounge.supercar.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.renderscript.*
import android.util.Log
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest


class BlurTransformation @JvmOverloads constructor(
    ctx: Context,
    radius: Int = MAX_RADIUS,
    sampling: Int = DEFAULT_DOWN_SAMPLING
) :
    BitmapTransformation() {
    private val ctx: Context
    private val mRadius: Int
    private val mSampling: Int

    init {
        this.ctx = ctx.applicationContext
        mRadius = Math.max(1, Math.min(radius, MAX_RADIUS))
        mSampling = sampling

    }

    private fun getBlurBitmap(ctx: Context, resource: Bitmap, radius: Int): Bitmap {
        var renderScript: RenderScript? = null
        try {
            renderScript = RenderScript.create(ctx)
            val input = Allocation.createFromBitmap(
                renderScript,
                resource,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT
            )
            val output = Allocation.createTyped(renderScript, input.type)
            val blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
            blur.setInput(input)
            blur.setRadius(radius.toFloat())
            blur.forEach(output)
            output.copyTo(resource)
        } finally {
            renderScript?.destroy()
        }
        return resource
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val scaledWidth = toTransform.width / mSampling
        val scaleHeight = toTransform.height / mSampling
        Log.d("scaledWidth", scaleHeight.toString())
        Log.d("scaleHeight", scaleHeight.toString())
        var bitmap = pool[scaledWidth, scaleHeight, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(bitmap)
        canvas.scale(1f / mSampling.toFloat(), 1f / mSampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                bitmap = getBlurBitmap(ctx, toTransform, mRadius)
            } catch (e: RSRuntimeException) {
                e.printStackTrace()
            }
        }
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + mRadius + mSampling).toByteArray())
    }

    override fun toString(): String {
        return "BlurTransformation(radius=$mRadius, sampling=$mSampling)"
    }

    override fun equals(obj: Any?): Boolean {
        return (obj is BlurTransformation
                && obj.mRadius == mRadius && obj.mSampling == mSampling)
    }

    companion object {
        private const val VERSION = 1
        private const val ID = "blurTransformation." + VERSION
        private const val MAX_RADIUS = 25
        private const val DEFAULT_DOWN_SAMPLING = 1
    }
}