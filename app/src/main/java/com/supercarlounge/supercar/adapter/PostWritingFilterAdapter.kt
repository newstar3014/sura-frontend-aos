package com.supercarlounge.supercar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BoardCategoryData
import com.supercarlounge.supercar.databinding.ItemPostFilterBinding
import com.supercarlounge.supercar.viewmodel.PostWritingViewModel


class PostWritingFilterAdapter(val con: Context, val data: ArrayList<BoardCategoryData>, val model: PostWritingViewModel, val itemClick: (BoardCategoryData, Int) -> Unit) : RecyclerView.Adapter<PostWritingFilterAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<BoardCategoryData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_post_filter, parent, false)
    ) {
        private val binding: ItemPostFilterBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: BoardCategoryData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            if (binding?.posi == model.filterPosition.value){
                binding.tvText.setTextColor(ContextCompat.getColor(con, R.color.main_color))
            }else{
                binding.tvText.setTextColor(ContextCompat.getColor(con, R.color.white_40))
            }

            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }


}