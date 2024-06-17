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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.ItemVipTopDriveBinding
import com.supercarlounge.supercar.viewmodel.HomeViewModel


class VipTopDriveAdapter(val con: Context, var data: ArrayList<DriveAwayData>, val model: HomeViewModel, val itemClick: (DriveAwayData, Int) -> Unit) : RecyclerView.Adapter<VipTopDriveAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<DriveAwayData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_vip_top_drive, parent, false)
    ) {
        private val binding: ItemVipTopDriveBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: DriveAwayData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            if (binding.itemIv.width != 0){
                initView(binding)
            }
            Glide.with(con)
                .load(data.b_cover)
                .override(binding.itemIv.width,binding.itemIv.height).centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        initView(binding)
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

                .into(binding!!.itemIv)

            itemView.setOnClickListener { itemClick(data, position) }

        }
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:viptoplists")
        @JvmStatic
        fun setItems(pager: RecyclerView, items: ArrayList<DriveAwayData>) {
            var adapter =  (pager.adapter as? VipTopDriveAdapter)
            items?.let {
                adapter?.data = it
                adapter?.notifyDataSetChanged()
            }
        }
    }
    private fun initView(binding: ItemVipTopDriveBinding){
        var deviceW = binding!!.item.width
        val h = (deviceW.times(1.5))
        Log.d("아이템 전체넓이", deviceW.toString())
        Log.d("아이템 1:1.5비율", h.toString())
        binding.item.layoutParams.height = h.toInt()

    }

}