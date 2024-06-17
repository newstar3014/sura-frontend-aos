package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.AlimData
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.data.PointMallProductData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostNotiBinding
import com.supercarlounge.supercar.databinding.ItemPointproductBinding
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class PointProductAdapter(val con: Context, val data: ArrayList<PointMallProductData>, val model: PointMallViewModel, val itemClick: (PointMallProductData, View, Int) -> Unit) : RecyclerView.Adapter<PointProductAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointProductAdapter.ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var pdata = data.get(position)
        holder.bind(pdata,position)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pointproduct, parent, false)
    ) {
        private val binding: ItemPointproductBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: PointMallProductData, position: Int) {

            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data,itemView, position) }
            binding!!.llTrade.setOnClickListener {
                itemClick(data,binding!!.llTrade, position)
            }
        }
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:pointproductlist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<PointMallProductData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as PointProductAdapter)
                items?.let {
                    adapter.data.clear()
                    adapter.data.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("setproductphoto")
        fun setproductphoto(view: ImageView, url: String?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (!url.isNullOrEmpty()) {
                Glide.with(view).load(url).override(view.width, view.height)
                    .centerCrop().into(view)
            }
        }
    }
}