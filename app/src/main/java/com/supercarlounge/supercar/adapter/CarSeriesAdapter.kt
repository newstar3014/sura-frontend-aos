package com.supercarlounge.supercar.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.OnwerViewModel


class CarSeriesAdapter(val con: Context, val data: ArrayList<CarModelListData>,val detailsData:ArrayList<CarSubModelListData>, val model: OnwerViewModel, val itemClick: (CarModelListData, Int) -> Unit) : RecyclerView.Adapter<CarSeriesAdapter.ViewHolder>() {
    var selectindex = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<CarModelListData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_car_series, parent, false)
    ) {
        private val binding: ItemCarSeriesBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: CarModelListData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            model.carDetailsSeriesList.observe(con, Observer {
                it?.let {

                    binding.detailsSeriesRecyclerview.adapter = CarDetailsSeriesAdapter(con, model.carDetailsSeriesList.value!! ,model) { data, position ->
                        model.selectCarSeries.value = data.csm_seq.toString()
                        model.selectCarSeriesName.value = data.csm_name

                    }
                    if (model.view.value.equals("재등록")){
                        for ((count, i) in it.withIndex()){
                            if (i.csm_name.toString() == model.selectCarSeriesName.value){
                                model.carDetailsSeriesCursorPoint.value = count
                                Log.d("check", "bind: ")
                            }
                        }
                    }else if (model.view.value.equals("수정")) {

                        var count = 0
                        for (i in it) {
                            if (i.csm_name.equals(model.my_user_car_data.value?.csm_name)) {
                                model.carDetailsSeriesCursorPoint.value = count
                                model.selectCarSeries.value = i.csm_seq.toString()
                                model.selectCarSeriesName.value = i.csm_name
                                break
                            }
                            if (i.csm_seq.toString() == model.selectCarModel.value){
                                model.carDetailsSeriesCursorPoint.value = count
                            }
                            count++
                        }
                    }
                }

            })


            itemView.setOnClickListener { itemClick(data, position)




                if (model.carSeriesCursorPoint.value == position){
                    model.getCarSubModelList(model.selectCarModel.value.toString())
                    model.stateBottomText.value =  false
                    model.carSeriesCursorPoint.value = -1
                    binding.detailsSeriesRecyclerview.layoutManager = LinearLayoutManager(con,
                        LinearLayoutManager.VERTICAL,false)
                }else{

                    model.carDetailsSeriesCursorPoint.value = -1
                    model.getCarSubModelList(model.selectCarModel.value.toString())
                    model.carSeriesCursorPoint.value = position

                    binding.detailsSeriesRecyclerview.layoutManager = LinearLayoutManager(con,
                        LinearLayoutManager.VERTICAL,false)
                }

            }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }




}