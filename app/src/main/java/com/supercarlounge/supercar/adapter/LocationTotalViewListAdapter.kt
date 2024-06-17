package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.LocationSpotData
import com.supercarlounge.supercar.databinding.ItemLocationTotalViewBinding
import com.supercarlounge.supercar.viewmodel.LocationFragmentViewModel


class LocationTotalViewListAdapter(val con: Context, val data: ArrayList<LocationSpotData>, val model: LocationFragmentViewModel, val itemClick: (LocationSpotData, Int) -> Unit) : RecyclerView.Adapter<LocationTotalViewListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return 10
    }

    fun setdata(datalist:ArrayList<LocationSpotData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data.size > position){
            Log.d("로케이션 데이터", data[position].toString())
            holder.bind(data[position], position)
        }

    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_location_total_view, parent, false)
    ) {
        private val binding: ItemLocationTotalViewBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: LocationSpotData, position: Int) {
            Log.d("데이터 리스트", data.toString())
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding?.num = position+1
            binding!!.executePendingBindings()
            val layoutParams: ViewGroup.LayoutParams? = binding.item.layoutParams
            layoutParams?.width = model?.widthPixels?.value!!/2 - model?.widthPixels?.value!!/10
            binding!!.item.layoutParams = layoutParams

            binding.lifecycleOwner = con as LifecycleOwner
            binding.item.setOnClickListener { itemClick(data, position) }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:locationTotalViewList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<LocationSpotData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as LocationTotalViewListAdapter)
                items?.let {
                    adapter.setdata(it)

                }
            }
        }
    }
}