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
import com.supercarlounge.supercar.data.ConciergeData
import com.supercarlounge.supercar.databinding.ItemConciergeBinding


class ConciergeAdapter(val con: Context, var data: ArrayList<ConciergeData>, val itemClick: (ConciergeData, Int) -> Unit) : RecyclerView.Adapter<ConciergeAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<ConciergeData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_concierge, parent, false)
    ) {
        private val binding: ItemConciergeBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: ConciergeData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data, position) }

        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:conciergelist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<ConciergeData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as ConciergeAdapter)
                items?.let {
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }
}