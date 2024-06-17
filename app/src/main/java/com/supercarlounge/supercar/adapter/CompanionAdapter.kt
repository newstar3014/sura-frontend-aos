package com.supercarlounge.supercar.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.supercarlounge.supercar.BaseDiffUtil
import com.supercarlounge.supercar.Constans.Companion.GRID_ADAPTER
import com.supercarlounge.supercar.Constans.Companion.LINEAR_ADAPTER
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BoardCommentData
import com.supercarlounge.supercar.data.CarSubModelListData
import com.supercarlounge.supercar.data.CompanionData
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.ItemCarDetailsSeriesBinding
import com.supercarlounge.supercar.databinding.ItemCompanionsBinding
import com.supercarlounge.supercar.databinding.ItemDriveAwayBinding
import com.supercarlounge.supercar.databinding.ItemDriveAwayLinearBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CompanionAdapter(val con: Context, var data: ArrayList<CompanionData>, val itemClick: (CompanionData, Int) -> Unit): RecyclerView.Adapter<CompanionAdapter.ViewHolder>() {
    var selectindex = 0



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<CompanionData>){
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_companions, parent, false)
    ) {
        private val binding: ItemCompanionsBinding? = DataBindingUtil.bind(itemView)

        fun bind(data: CompanionData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:companionlists")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<CompanionData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as CompanionAdapter)
                items?.let {
                    var count = adapter.data.count()
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }

}