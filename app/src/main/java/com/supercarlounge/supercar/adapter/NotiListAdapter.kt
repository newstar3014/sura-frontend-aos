package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumNotiBinding

import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostNoti2Binding
import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostNotiBinding
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class NotiListAdapter(var type :String,val con: Context, val data: ArrayList<NotiData>, val model1: BoardViewModel? ,val model2: AlimViewModel?, val itemClick: (NotiData, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (type=="알림"){
            return ViewHolder2(parent)
        }else{
            return ViewHolder(parent)
        }

    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist: ArrayList<NotiData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (type) {
            "알림"-> {
                (holder as ViewHolder2) .bind(data.get(position), position)
            }
            "게시판" -> {
                (holder as ViewHolder).bind(data.get(position), position)
            }
        }
    }



    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_anonymous_frum_post_noti, parent, false)
    ) {

        private val binding: ItemAnonymousForumPostNotiBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: NotiData, position: Int) {

            binding?.dataset = data

            binding?.viewModel = model1
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            itemView.setOnClickListener { itemClick(data, position) }

            when (data.n_type){
                "0" ->{
                    binding.textView7.setTextColor(ContextCompat.getColor(con, R.color.woman))
                }
                "1","2" ->{
                    binding.textView7.setTextColor(ContextCompat.getColor(con, R.color.main_color))
                }

            }



//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    inner class ViewHolder2(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_anonymous_frum_post_noti2, parent, false)
    ) {

        private val binding: ItemAnonymousForumPostNoti2Binding? = DataBindingUtil.bind(itemView)

        fun bind(data: NotiData, position: Int) {

            binding?.dataset = data
            binding?.viewModel = model2
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            itemView.setOnClickListener { itemClick(data, position) }

            when (data.n_type){
                "0" ->{
                    binding.textView7.setTextColor(ContextCompat.getColor(con, R.color.woman))
                }
                "1","2" ->{
                    binding.textView7.setTextColor(ContextCompat.getColor(con, R.color.main_color))
                }

            }



//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:AnonymousForumPostNotiList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<NotiData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as NotiListAdapter)
                items?.let {
                    adapter.setdata(it)

                }
            }
        }
    }


}