package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.AlimData
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.data.PointMallLogData
import com.supercarlounge.supercar.data.PointMallProductData
import com.supercarlounge.supercar.databinding.ItemAnonymousForumPostNotiBinding
import com.supercarlounge.supercar.databinding.ItemPointlogBinding
import com.supercarlounge.supercar.databinding.ItemPointproductBinding
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import com.supercarlounge.supercar.viewmodel.PointMallLogViewModel
import com.supercarlounge.supercar.viewmodel.PointMallViewModel

class UserRestOptionAdapter(
    private val options: List<String>,
    private val context: Context
) : RecyclerView.Adapter<UserRestOptionAdapter.ViewHolder>() {

    private var selectedPosition: Int? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val text: TextView = itemView.findViewById(R.id.text)

        init {
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition ?: -1)
                notifyItemChanged(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_rest_option, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.text.text = option
        if (selectedPosition == position) {
            holder.icon.setImageResource(R.drawable.ic_frame_2409)
            holder.text.setTextColor(Color.WHITE)  // 원하는 색상으로 변경
            holder.text.setTypeface(null, Typeface.BOLD)
        } else {
            holder.icon.setImageResource(R.drawable.ic_frame_2408)
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.white_70))
            holder.text.setTypeface(null, Typeface.NORMAL)
        }
    }

    override fun getItemCount() = options.size

    fun getSelectedOption(): String? {
        return selectedPosition?.let { options[it] }
    }
}