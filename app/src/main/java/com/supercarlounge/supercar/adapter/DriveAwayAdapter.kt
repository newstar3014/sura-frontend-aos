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
import com.supercarlounge.supercar.databinding.ItemDriveAwayBinding
import com.supercarlounge.supercar.databinding.ItemDriveAwayLinearBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DriveAwayAdapter(val con: Context, var data: ArrayList<DriveAwayData>,type:Int, val itemClick: (DriveAwayData, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectindex = 0
    var vtype = 0
    init {
        vtype = type
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(vtype) {
            GRID_ADAPTER -> {
               gridViewHolder(parent)
            }

            else -> {
                linearViewHolder(parent)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
       return vtype
//        return super.getItemViewType(position)
    }
    override fun getItemCount(): Int {
        return data!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(datalist:ArrayList<DriveAwayData>){
        val diffUtil = BaseDiffUtil(data, datalist)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        data.clear()
        data.addAll(datalist)
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun adddata(datalist:ArrayList<DriveAwayData>){
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
        when(vtype) {
            GRID_ADAPTER -> {
                (holder as gridViewHolder).bind(data[position], position)

                holder.setIsRecyclable(false)

            }
            LINEAR_ADAPTER -> {
                (holder as linearViewHolder).bind(data[position], position)
                holder.setIsRecyclable(false)
            }

        }
    }


    inner class gridViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_drive_away, parent, false)
    ) {
        private val binding: ItemDriveAwayBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: DriveAwayData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            Log.d("card_name 체크", data.carb_name.toString())
//            initView(binding)


            if(data.b_cover.isNullOrEmpty()){
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
                    .load(data.b_cover)
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

    inner class linearViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_drive_away_linear, parent, false)
    ) {
        private val binding: ItemDriveAwayLinearBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: DriveAwayData, position: Int) {
            var cc = getItemViewType(position)
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()

            binding.lifecycleOwner = con as LifecycleOwner
            Glide.with(con).load(data.b_cover).override(binding!!.itemIv.width,binding!!.itemIv.height).centerCrop().into(binding!!.itemIv)
            itemView.setOnClickListener { itemClick(data, position) }
            if (data.u_gender == "MALE"){
                Glide.with(binding!!.itemIv).load(R.drawable.ic_man_12x12).centerInside().into(binding!!.ivSex)
            }else{
                Glide.with(binding!!.itemIv).load(R.drawable.ic_woman_12x12).centerInside().into(binding!!.ivSex)
            }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:drivelists")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<DriveAwayData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as DriveAwayAdapter)
                items?.let {

                    var count = adapter.data.count()
                    Log.d("getDrive", it.toString())
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }
    private fun initView( binding: ItemDriveAwayBinding){
        CoroutineScope(Dispatchers.IO).launch {

            withContext(Dispatchers.Main){
                var deviceW = binding!!.item.width
                if (deviceW == 0){
                    deviceW = binding!!.itemIv.width
                }

                val h = (deviceW.times(1.5))
                Log.d("아이템 전체넓이", deviceW.toString())
                Log.d("아이템 1:1.5비율", h.toString())
                if (binding.item.layoutParams.height != h.toInt()) {
                    binding.item.layoutParams.height = h.toInt()
                }

            }
        }

    }


}