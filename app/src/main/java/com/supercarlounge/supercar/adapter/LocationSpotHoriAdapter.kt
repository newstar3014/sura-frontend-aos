package com.supercarlounge.supercar.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.supercarlounge.supercar.BaseDiffUtil
import com.supercarlounge.supercar.Constans.Companion.GRID_ADAPTER
import com.supercarlounge.supercar.Constans.Companion.LINEAR_ADAPTER
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.LocationSpotData
import com.supercarlounge.supercar.databinding.ItemDriveAwayBinding
import com.supercarlounge.supercar.databinding.ItemDriveAwayLinearBinding
import com.supercarlounge.supercar.databinding.ItemDriveLocationBinding
import com.supercarlounge.supercar.databinding.ItemLocationSpotHoriBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LocationSpotHoriAdapter(val con: Context, var data: ArrayList<LocationSpotData>, val itemClick: (LocationSpotData, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return gridViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(datalist:ArrayList<LocationSpotData>){
        val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.clear()
        data.addAll(datalist)
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun adddata(datalist:ArrayList<LocationSpotData>){
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }
    fun cleardata(){
        data.clear()
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

                (holder as gridViewHolder).bind(data[position], position)

                holder.setIsRecyclable(false)


    }


    inner class gridViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_location_spot_hori, parent, false)
    ) {
        private val binding: ItemLocationSpotHoriBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationSpotData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner



            if(data.spot_main_image.isNullOrEmpty()){
                Glide.with(con)
                    .load(R.drawable.app_icon)
                    .override(binding.itemIv.width, binding.itemIv.height)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                            initView(binding)

//                            initView(binding)
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
//                            initView(binding)
                           return false
                        }
                    })

                    .into(binding!!.itemIv)
            }else {
                Glide.with(con)
                    .load(data.spot_main_image)
                    .override(binding.itemIv.width, binding.itemIv.height).centerCrop()
//                    .placeholder()
                    .listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                            return false
                        }


                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            return false
                        }
                    })

                    .into(binding.itemIv)
            }

            itemView.setOnClickListener { itemClick(data, position) }

//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)

        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:locationspothori")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<LocationSpotData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as LocationSpotHoriAdapter)
                items?.let {
                    var count = adapter.data.count()
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }


}