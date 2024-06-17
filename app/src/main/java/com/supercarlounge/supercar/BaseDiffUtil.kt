package com.supercarlounge.supercar

import androidx.recyclerview.widget.DiffUtil

class BaseDiffUtil<T>(private val newList: List<T>, private val oldList: List<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newList[newItemPosition] == oldList[oldItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newList[newItemPosition] == oldList[oldItemPosition]
}