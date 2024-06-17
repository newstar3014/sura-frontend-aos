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
import com.supercarlounge.supercar.databinding.ItemVipDriveBinding
import com.supercarlounge.supercar.databinding.ItemVipDriveNotBinding
import com.supercarlounge.supercar.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VipDriveAdapter(val con: Context, var data: ArrayList<DriveAwayData>, val model: HomeViewModel, val itemClick: (DriveAwayData, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0){
            return ViewHolder(parent)
        }else{
            return ViewHolderNot(parent)
        }

    }

    override fun getItemViewType(position: Int): Int {
        var dd = data.get(position)
        if(dd.b_seq>0){
            return 0
        }else{
            return 1
        }
        return 0
    }

    override fun getItemCount(): Int {
        return data!!.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setdata(datalist:ArrayList<DriveAwayData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var data = data.get(position)
        if(data.b_seq<0){
            (holder as ViewHolderNot).bind(data,position)
        }else{
            (holder as ViewHolder).bind(data,position)
        }
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_vip_drive, parent, false)
    ) {
        private val binding: ItemVipDriveBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: DriveAwayData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner



//                initView(binding)

            if (data.u_gender.equals("MALE")){
                Glide.with(con)
                    .load(R.drawable.ic_man_12x12)
                    .override(binding.itemIv.width, binding.itemIv.height)
                    .into(binding!!.ivSex)

            }else{

                Glide.with(con)
                    .load(R.drawable.ic_woman_12x12)
                    .override(binding.itemIv.width, binding.itemIv.height)
                    .into(binding!!.ivSex)
            }


            Glide.with(con)
                .load(data.b_cover)
                .override(binding.itemIv.width, binding.itemIv.height).centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
//                        initView(binding)
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
    inner class ViewHolderNot(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_vip_drive_not, parent, false)
    ) {
        private val binding: ItemVipDriveNotBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: DriveAwayData, position: Int) {
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            initView2(binding)
//            if(data.b_seq == -1){
//                Glide.with(con).load(R.drawable.not_vip_drive_img1).centerCrop().into(binding!!.itemIv)
//            }else{
//                Glide.with(con).load(R.drawable.not_vip_drive2).centerCrop().into(binding!!.itemIv)
//            }

           // itemView.setOnClickListener { itemClick(data, position) }

        }
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:viplists")
        @JvmStatic
        fun setItems(pager: RecyclerView, items: ArrayList<DriveAwayData>) {
            var adapter =  (pager.adapter as VipDriveAdapter)
            items?.let {
                if(it.size == 0) {
                    var i = DriveAwayData(
                        -1,
                        "",
                        "",
                        "",
                        0,
                        0,
                        "",
                        "",
                        "https://supercarlounge.com:3002/images/preview/Frame%201440.png",
                        "",
                        "",
                        "",
                        0,
                        0,
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        0,
                        "",
                        0,
                        0,
                    0,  "",
                        "","",
                        "","",
                        "",
                        "",
                    )
                    var i2 = DriveAwayData(
                        -2,
                        "",
                        "",
                        "",
                        0,
                        0,
                        "",
                        "",
                        "https://supercarlounge.com:3002/images/preview/Frame%201438.png",
                        "",
                        "",
                        "",
                        0,
                        0,
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        0,
                        "",
                        "",
                        "",
                        "",
                        "",
                        0,
                        0,
                        "",
                        0,
                        0,
                        0,  "",
                        "","",
                        "","",
                        "",
                        "",
                    )
                    it.add(i)
                    it.add(i2)
                }
                adapter.data = it
                adapter.notifyDataSetChanged()
            }
        }
    }
//    private fun initView( binding: ItemVipDriveBinding){
//        CoroutineScope(Dispatchers.IO).launch {
//
//            withContext(Dispatchers.Main){
//                var deviceW = binding!!.item.width
//                if (deviceW == 0){
//                    deviceW = binding!!.itemIv.width
//                }
//
//                val h = (deviceW.times(1.5))
//                Log.d("아이템 전체넓이", deviceW.toString())
//                Log.d("아이템 1:1.5비율", h.toString())
//                binding.item.layoutParams.height = h.toInt()
//
//            }
//        }
//
//    }
//
//    private fun initView2( binding: ItemVipDriveNotBinding){
//        CoroutineScope(Dispatchers.IO).launch {
//
//            withContext(Dispatchers.Main){
//                var deviceW = binding!!.item.width
//                if (deviceW == 0){
//                    deviceW = binding!!.itemIv.width
//                }
//
//                val h = (deviceW.times(1.5))
//                Log.d("아이템 전체넓이", deviceW.toString())
//                Log.d("아이템 1:1.5비율", h.toString())
//                binding.itemIv.layoutParams.height = h.toInt()
//
//            }
//        }
//
//    }

}