package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumNotiBinding
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class NotiSlideAdapter(val con: Context, val data: ArrayList<NotiData>, val itemClick: (NotiData, Int) -> Unit) : RecyclerView.Adapter<NotiSlideAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<NotiData>){
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
        LayoutInflater.from(parent.context).inflate(com.supercarlounge.supercar.R.layout.item_anonymous_frum_noti, parent, false)
    ) {
        private val binding: ItemAnonymousForumNotiBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: NotiData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    itemClick(data, position)
                }
                return@setOnTouchListener true
            }

//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }




    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:AnonymousForumNotiList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView?, items: ArrayList<NotiData>?) {
            if (recyclerView?.adapter != null) {
                var adapter = (recyclerView?.adapter as NotiSlideAdapter?)
                items?.let {
                    adapter?.setdata(it)

                }
            }
        }
    }




}