package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.HeartLogData
import com.supercarlounge.supercar.databinding.ItemHeartBuyListBinding


class HeartLogAdapter(val con: Context, var data: ArrayList<HeartLogData>, val itemClick: (HeartLogData, Int) -> Unit) : RecyclerView.Adapter<HeartLogAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<HeartLogData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_heart_buy_list, parent, false)
    ) {
        private val binding: ItemHeartBuyListBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: HeartLogData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
//            binding!!.executePendingBindings()
            binding?.lifecycleOwner = con as LifecycleOwner
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:heartloglist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<HeartLogData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as HeartLogAdapter)
                items?.let {
                    var count = adapter.data.count()
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }
}