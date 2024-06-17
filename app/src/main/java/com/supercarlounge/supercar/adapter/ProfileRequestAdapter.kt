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
import com.supercarlounge.supercar.data.ProfileRequestData
import com.supercarlounge.supercar.databinding.ItemProfileRequestBinding
import com.supercarlounge.supercar.viewmodel.ProfileRequestViewModel
import java.util.*
import kotlin.collections.ArrayList


class ProfileRequestAdapter(val con: Context, owner:LifecycleOwner, val data: ArrayList<ProfileRequestData>, val model: ProfileRequestViewModel, val itemClick: (ProfileRequestData, Int) -> Unit) : RecyclerView.Adapter<ProfileRequestAdapter.ViewHolder>() {
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

    fun setdata(datalist:ArrayList<ProfileRequestData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_profile_request, parent, false)
    ) {
        private val binding: ItemProfileRequestBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: ProfileRequestData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            model.cursorPosition.value = position
            binding.swipeView!!.setOnClickListener { itemClick(data, position) }
            // 삭제 텍스트뷰 클릭시 토스트 표시
            binding.delete.setOnClickListener {
                model.delete_request(data.pc_seq.toString(),data.target_seq.toString(),data.sub_seq.toString(),position)
                //removeData(this.layoutPosition)

            }
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:requestlist")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<ProfileRequestData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as ProfileRequestAdapter)
                items?.let {
                    Log.d("데이터체크용", items.toString())
                    adapter.data.clear()
                    adapter.data.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }
}