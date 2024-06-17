package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.OnwerViewModel


class CarDetailsSeriesAdapter(val con: Context, val data: ArrayList<CarSubModelListData>, val model: OnwerViewModel, val itemClick: (CarSubModelListData, Int) -> Unit) : RecyclerView.Adapter<CarDetailsSeriesAdapter.ViewHolder>() {
    var selectindex = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<CarSubModelListData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_car_details_series, parent, false)
    ) {
        private val binding: ItemCarDetailsSeriesBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: CarSubModelListData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data, position)
                if (model.carDetailsSeriesCursorPoint.value == position){
                    model.carDetailsSeriesCursorPoint.value = -1
                    model.stateBottomText.value =  false
                }else{
                    model.carDetailsSeriesCursorPoint.value = position
                    model.stateBottomText.value =  true
                }

            }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }


}