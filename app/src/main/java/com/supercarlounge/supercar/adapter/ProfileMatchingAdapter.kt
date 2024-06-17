package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.MatchingMiniData
import com.supercarlounge.supercar.databinding.ItemProfileMatching
import com.supercarlounge.supercar.viewmodel.MatchingViewModel
import java.util.*
import kotlin.collections.ArrayList


class ProfileMatchingAdapter(val con: Context, owner:LifecycleOwner, var data: ArrayList<MatchingMiniData>, val model: MatchingViewModel, val itemClick: (MatchingMiniData, Int) -> Unit) : RecyclerView.Adapter<ProfileMatchingAdapter.ViewHolder>() {
    var selectindex = 0
    init {
        model.delindex.observe(owner, androidx.lifecycle.Observer {
            removeData(it)
        })
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<MatchingMiniData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }
    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    // 현재 선택된 데이터와 드래그한 위치에 있는 데이터를 교환
    fun swapData(fromPos: Int, toPos: Int) {
        Collections.swap(data, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_profile_matching, parent, false)
    ) {
        private val binding: ItemProfileMatching? = DataBindingUtil.bind(itemView)

        fun bind(data: MatchingMiniData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            model.cursorPosition.value = position
            Log.d("확인", data.mypage_view)
            binding.swipeView!!.setOnClickListener { itemClick(data, position) }
            // 삭제 텍스트뷰 클릭시 토스트 표시
            binding.delete.setOnClickListener {
                model.delete_request(data.target_seq.toString(),data.u_seq.toString(),data.sub_seq.toString(),position)
                //removeData(this.layoutPosition)

            }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:matchinglist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<MatchingMiniData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as ProfileMatchingAdapter)
                items?.let {
                    adapter.data.clear()
                    adapter.data.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}