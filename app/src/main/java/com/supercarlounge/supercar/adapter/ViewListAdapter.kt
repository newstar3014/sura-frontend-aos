package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.ViewListData
import com.supercarlounge.supercar.databinding.ItemViewListBinding
import com.supercarlounge.supercar.viewmodel.PostViewModel


class ViewListAdapter(val con: Context, var data: ArrayList<ViewListData>, var model: PostViewModel, val itemClick: (ViewListData, Int) -> Unit) : RecyclerView.Adapter<ViewListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<ViewListData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_view_list, parent, false)
    ) {
        private val binding: ItemViewListBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: ViewListData, position: Int) {
            binding?.dataset = data
            binding?.posi = position

            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            if(data.pc_ok.equals("N")) {
                Glide.with(con).load(R.drawable.frame_x4).into(binding!!.itemIv)
            }else{
                Glide.with(con).load(data.u_image).circleCrop().into(binding!!.itemIv2)
            }
//            Glide.with(con).load(data.u_image).circleCrop().into(binding!!.itemIv)
            itemView.setOnClickListener { itemClick(data, position) }

        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:viewlist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<ViewListData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as ViewListAdapter)
                items?.let {
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }
}