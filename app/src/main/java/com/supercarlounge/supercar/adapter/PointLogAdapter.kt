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
import com.supercarlounge.supercar.data.PointMallLogData
import com.supercarlounge.supercar.data.PointMallProductData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostNotiBinding
import com.supercarlounge.supercar.databinding.ItemPointlogBinding
import com.supercarlounge.supercar.databinding.ItemPointproductBinding
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import com.supercarlounge.supercar.viewmodel.PointMallLogViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class PointLogAdapter(val con: Context, val data: ArrayList<PointMallLogData>, val model: PointMallLogViewModel, val itemClick: (PointMallLogData, View, Int) -> Unit) : RecyclerView.Adapter<PointLogAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointLogAdapter.ViewHolder {
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_pointlog, parent, false)
    ) {
        private val binding: ItemPointlogBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: PointMallLogData, position: Int) {

            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data,itemView, position) }
        }
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:pointloglist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<PointMallLogData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as PointLogAdapter)
                items?.let {
                    adapter.data.clear()
                    adapter.data.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}