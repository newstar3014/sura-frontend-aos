package com.supercarlounge.supercar.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BlurTransformation
import com.supercarlounge.supercar.data.ProfileSuggestionData
import com.supercarlounge.supercar.databinding.ItemProfileSuggestion2Binding
import com.supercarlounge.supercar.ui.activity.OwnerAndPassengerActivity
import com.supercarlounge.supercar.viewmodel.OwnerAndPassengerViewModel
import eightbitlab.com.blurview.RenderScriptBlur
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class OwnerAndPassengerProfileSugesstAdapter(
//    var deviceW :Int,var deviceH:Int ,
    val con: Context,
    var type:Int,
    var datalist: ArrayList<ProfileSuggestionData>,
    val model: OwnerAndPassengerViewModel,
    val itemClick: (ProfileSuggestionData, Int, Int) -> Unit
) : RecyclerView.Adapter<OwnerAndPassengerProfileSugesstAdapter.ViewHolder>() {
    var selectindex = -1

    //    var mDeviceW = deviceW
//    var mDeviceH = deviceH
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        var result = 0
        if (datalist!!.size != 0) {
            result = Int.MAX_VALUE
        }
        return result
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(datalist: ArrayList<ProfileSuggestionData>) {
        datalist.clear()
        datalist.addAll(datalist)
        notifyDataSetChanged()
    }

    fun setselect(index: Int) {
        var next = index % datalist.size
        if (next != selectindex) {
            selectindex = next
            notifyDataSetChanged()
            Log.d("SCALE", "" + selectindex)
        }


    }

    fun getdata(index: Int): ProfileSuggestionData {
        var next = index % datalist.size
        return datalist.get(next)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist.get(position % datalist.size), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_profile_suggestion2, parent, false)
    ) {
        private val binding: ItemProfileSuggestion2Binding? = DataBindingUtil.bind(itemView)

        fun bind(data: ProfileSuggestionData, position: Int) {
            var ap = con.applicationContext as MainApplication

            var check = false
            var slist = ap.select_index
            if (slist!!.contains(data.u_seq.toString())) {
                check = true
            }
            Log.d("사이즈", (position% model.profilesuggest.value!!.size).toString())
            Log.d("2024체크", "4 ======== ${position}")
            binding?.checkset = check
            binding?.dataset = data
            binding?.posi = position
            binding!!.viewModel = model
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            itemView.setOnClickListener {
                    itemClick(data, position, binding.root.width)
            }


                if (!data.u_image.isNullOrEmpty() && data.u_seq > 0) {
                    var bitmapBlur: Bitmap? = null
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
//                        var image_task: URLtoBitmapTask = URLtoBitmapTask()
//                        image_task = URLtoBitmapTask().apply {
//                            url = URL(data.u_image)
//                        }
//                        var bitmap: Bitmap = image_task.execute().get()

                            val multi = MultiTransformation<Bitmap>(BlurTransformation(con,20,20))
                            val multiBase = MultiTransformation<Bitmap>(BlurTransformation(con,0,0),RoundedCornersTransformation(  99999, 0, RoundedCornersTransformation.CornerType.ALL))
                            if (type == 0){
                                if (binding?.checkset == true){

                                    binding.ivBlurview?.isVisible = false
                                    (con as OwnerAndPassengerActivity).runOnUiThread {
                                        binding.ivProfile?.let {
                                            Glide.with(con).load(data.u_image).centerCrop()

                                    //                                            .apply(
                                    //                                                RequestOptions.bitmapTransform(multiBase)
                                    //                                            )

                                                .into(it)
                                        }
//                                Picasso.get()
//                                    .load(data.u_image)
//                                    .transform(BlurTransformation(con, 25, 1))
//                                    .into(binding.ivProfile)
                                    }

                                }else {
                                    (con as OwnerAndPassengerActivity).runOnUiThread {

                                        binding.ivProfile?.let {

                                                Glide.with(con).load(data.u_image)
                                                    .into(binding.ivProfile!!)


                                            binding.ivBlurview?.isVisible = true
                                            var radius = 10f;
                                            val decorView = con.window.getDecorView()
                                            val windowBackground: Drawable = decorView.background
                                            val rootView =
                                                decorView.findViewById<View>(android.R.id.content) as ViewGroup

                                            binding.ivBlurview?.setupWith(
                                                rootView,
                                                RenderScriptBlur(con)
                                            ) // or RenderEffectBlur
                                                ?.setFrameClearDrawable(windowBackground) // Optional
                                                ?.setBlurRadius(radius)
                                        }
                                    }

                                }
                            }else{

                                binding.ivBlurview?.isVisible = false
                                (con as OwnerAndPassengerActivity).runOnUiThread {
                                    Glide.with(con).load(data.u_image)

                                        .apply(
                                            RequestOptions.bitmapTransform(multi)
                                        )
                                        .into(binding.ivProfile!!)
                                }

                            }




//                        Glide.with(context).load(url)
//                            .apply(RequestOption().circleCrop().centerCrop())
//                            .into<Target<Drawable>>(imageview)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.d("비트맵2", e.toString())
                        }

                    }


//
//                val bitmap = Blurry.with(this)
//                    .radius(10)
//                    .sampling(8)
//                    .capture(data.u_image).get()

                    binding!!.ivProfile!!.setPadding(0,0,0,0)
                } else {
                    binding!!.ivProfile!!.setPadding(30,30,30,30)
                    Glide.with(binding.ivProfile!!).load(R.drawable.frame_x4)
                        .override(binding.ivProfile.width, binding.ivProfile.height).centerInside()
                        .into(binding.ivProfile!!)
                }




        }
    }

    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun blur(image: Bitmap): Bitmap {
        var BITMAP_SCALE = 0.6f;
        var BLUR_RADIUS = 15f;
        val width = Math.round(image.getWidth() * BITMAP_SCALE).toInt()
        val height = Math.round(image.getHeight() * BITMAP_SCALE).toInt()
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)
        val rs = RenderScript.create(con)
        val intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        intrinsicBlur.setRadius(BLUR_RADIUS)
        intrinsicBlur.setInput(tmpIn)
        intrinsicBlur.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }

    fun StringToBitmap(encodedString: String?): Bitmap? {
        val bitmap: Bitmap? = null
        try {
            if (!encodedString.isNullOrEmpty()) {
                val url = URL(encodedString)
                val strem = url.openStream()
                return BitmapFactory.decodeStream(strem)
            } else {
                return null
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return null;
        } catch (e: IOException) {
            e.printStackTrace()
            return null;
        }
        return bitmap;
    }

    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.getInputStream()
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun blur(context: Context?, image: Bitmap?, blurRadius: Float): Bitmap? {
        if (null == image) return null
        val outputBitmap = Bitmap.createBitmap(image)
        val renderScript = RenderScript.create(context)
        val tmpIn = Allocation.createFromBitmap(renderScript, image)
        val tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap)
        //Intrinsic Gausian blur filter
        val theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        theIntrinsic.setRadius(blurRadius) // radius should be >0.0F && <= 25.0F
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)
        return outputBitmap
    }


    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:profilesugesst2" ,"app:posi2")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<ProfileSuggestionData>,posi:Int) {
            var adapter = (recyclerView.adapter as OwnerAndPassengerProfileSugesstAdapter?)
            items?.let {
                adapter?.datalist = it
                if (it.size != 0) {
                    recyclerView.scrollToPosition(500)
                }
                adapter?.notifyDataSetChanged()
               var pos = 510
                if (posi!= 0){
                    pos = posi
                }

                recyclerView.smoothScrollToPosition(pos)
            }

        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("app:setsize2")
        fun setLayoutSize(view: RelativeLayout, isTranslateSize: Boolean?) {
            isTranslateSize ?: return
            view.layoutParams = view.layoutParams.apply {
                val metrisc: DisplayMetrics = view.context.resources.displayMetrics
                val displayPW = metrisc.widthPixels

                var w = displayPW.div(5)
                var h = displayPW.div(5)

                this.height = if (isTranslateSize) displayPW.div(1).toInt() else displayPW.div(1).toInt()
                this.width = if (isTranslateSize) displayPW.div(1.75).toInt() else displayPW.div(1.75).toInt()
//                view.scaleX =if (isTranslateSize) 1.1F else 0.9F
//
//                view.scaleY =if (isTranslateSize) 1.1F else 0.9F
            }
        }
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("app:setsizemain2")
        fun setLayoutMainSize(view: LinearLayoutCompat, isTranslateSize: Boolean?) {
            isTranslateSize ?: return
            view.layoutParams = view.layoutParams.apply {
                val metrisc: DisplayMetrics = view.context.resources.displayMetrics
                val displayPW = metrisc.widthPixels

                var w = displayPW.div(5)
                var h = displayPW.div(5)
//
//                this.height = if (isTranslateSize) displayPW.div(6).toInt() else displayPW.div(6).toInt()
//                this.width = if (isTranslateSize) displayPW.div(6).toInt() else displayPW.div(6).toInt()
                view.scaleX =if (isTranslateSize) 1F else 1F

                view.scaleY =if (isTranslateSize) 1F else 1F
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("app:psetsize")
        fun setpLayoutSize(view: RelativeLayout, isTranslateSize: Boolean?) {
            isTranslateSize ?: return
            view.layoutParams = view.layoutParams.apply {
                val metrisc: DisplayMetrics = view.context.resources.displayMetrics
                val displayPW = metrisc.widthPixels

                var w = displayPW.div(5)
                var h = displayPW.div(5)


                this.width = displayPW.div(5)

            }
        }
    }


}