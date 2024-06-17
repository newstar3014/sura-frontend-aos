package com.supercarlounge.supercar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Log
import android.widget.TextView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

class GlideImageGetter internal constructor(
    private val context: Context,
    private val textView: TextView
) :
    Html.ImageGetter {
    override fun getDrawable(url: String): Drawable {

        var drawable: BitmapDrawablePlaceholder = BitmapDrawablePlaceholder()
//        var d = Target.SIZE_ORIGINAL

         drawable= Glide.with(context)
            .asBitmap()
            .override(9/Target.SIZE_ORIGINAL)
            .dontTransform()
            .load(url)
            .into(drawable)
        return drawable

    }
    var drawableWidth = 0
    private inner class BitmapDrawablePlaceholder internal constructor() :
        BitmapDrawable(context.resources, Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)),
        Target<Bitmap> {
        private var drawable: Drawable? = null
        override fun draw(canvas: Canvas) {
            if (drawable != null) {
                Log.d("drawable", "캔버스")
                drawable!!.draw(canvas)
            }
        }

        private fun setDrawable(drawable: Drawable) {
            this.drawable = drawable
             drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight
            val maxWidth = textView.measuredWidth
            Log.d("dr drawableWidth", drawableWidth.toString())
            Log.d("dr drawableHeight", drawableHeight.toString())
            Log.d("dr maxWidth", maxWidth.toString())
            if (drawableWidth > maxWidth) {
                val calculatedHeight = maxWidth * drawableHeight / drawableWidth
                drawable.setBounds(0, 0, drawableWidth, calculatedHeight)
                setBounds(0, 0, drawableWidth, calculatedHeight)
            } else {
                drawable.setBounds(0, 0, drawableWidth, drawableHeight)
                setBounds(0, 0, drawableWidth, drawableHeight)
            }
            textView.text = textView.text
            val param = textView.layoutParams

            param.width = drawableWidth
            textView.layoutParams = param

        }

        override fun onLoadStarted(@Nullable placeholderDrawable: Drawable?) {
            Log.d("비트맵", bitmap.toString())
            placeholderDrawable?.let { setDrawable(it) }
        }

        override fun onLoadFailed(@Nullable errorDrawable: Drawable?) {
            errorDrawable?.let { setDrawable(it) }
        }

        override fun onResourceReady(bitmap: Bitmap, @Nullable transition: Transition<in Bitmap>?) {
            Log.d("비트맵", bitmap.toString())
            setDrawable(BitmapDrawable(context.resources, bitmap))

        }

        override fun onLoadCleared(@Nullable placeholderDrawable: Drawable?) {
            placeholderDrawable?.let { setDrawable(it) }
        }

        override fun getSize(cb: SizeReadyCallback) {
            textView.post {
                cb.onSizeReady(
                    textView.width,
                    textView.height
                )
            }


        }


        override fun removeCallback(cb: SizeReadyCallback) {}
        override fun setRequest(@Nullable request: Request?) {}

        @Nullable
        override fun getRequest(): Request? {
            return null
        }

        override fun onStart() {}
        override fun onStop() {}
        override fun onDestroy() {}
    }
}