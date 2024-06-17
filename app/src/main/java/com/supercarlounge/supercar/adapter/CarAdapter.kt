package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.ItemCarBinding
import com.supercarlounge.supercar.viewmodel.OnwerViewModel


class CarAdapter(val con: Context, val data: ArrayList<CarBrandListData>, val model: OnwerViewModel, val itemClick: (CarBrandListData, Int) -> Unit) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {
    var selectindex = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<CarBrandListData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
    ) {
        private val binding: ItemCarBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: CarBrandListData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            Glide.with(binding!!.ivCar).load(data!!.carb_image).into(binding!!.ivCar)
            itemView.setOnClickListener { itemClick(data, position)
                model.carSeriesCursorPoint.value = -1
                if (model.carCursorPoint.value == position){
                    model.carCursorPoint.value = -1

                }else{
                    model.carCursorPoint.value = position


                }

            }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }


}