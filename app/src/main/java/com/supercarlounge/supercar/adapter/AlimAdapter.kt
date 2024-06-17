package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.AlimData
import com.supercarlounge.supercar.databinding.ItemNotice2Binding
import com.supercarlounge.supercar.databinding.ItemNoticeBinding


class AlimAdapter(
    val con: Context,
    val data: ArrayList<AlimData>,
    val itemClick: (AlimData, Int) -> Unit
) : RecyclerView.Adapter<AlimAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist: ArrayList<AlimData>) {
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }

    fun setselect(index: Int) {
        selectindex = index
        notifyDataSetChanged()
    }

    @SuppressLint("ResourceType")
    fun textRetouch(binding: ItemNotice2Binding, data: AlimData) {

        var title = data.getTitle()
        var body = data.getBody()

        var myNick = data.u_nickname
        var givenNick = data.pa_nick


        var type1 = "BoardComment"
        var type2 = "BoardSubComment"

        var type3 = "MycarComment"
        var type4 = "MycarSubComment"

        var type5 = "SendProfile"
        var type6 = "ReadProfile"
        var type7 = "ApplyProfile"

        var type8 = "SendLike"
        var type9 = "GivenLike"
        var type10 = "AcceptLike"

        var type11 = "SendOpenPhone"
        var type12 = "OpenPhone"

        var type13 = "BoardLike"
        var type14 = "DriveLike"
        var type15 = "MycarLike"

        var type16 = "SuraMaster"
        var type17 = "Login"
        var type18 = "RejectScreen"


        var type19 = "SendDrive"
        var type20 = "GivenDrive"
        var type21 = "LocationDrive"

        var type22 = "DrivePass1Date"
        var type23 = "DrivePass3Date"
        var type24 = "DrivePass1Buy"
        var type25 = "DrivePass30Buy"

        var type26 = "HeartCharge"
        val titleString = SpannableString(title)
        val bodyString = SpannableString(body)
        when (data.pa_screen) {
            type1 -> {
                var tv1Start = 0
                var tv1End = 7
                var tv2Start = 9
                var tv2End = 11
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv1Start,
                    tv1End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv2Start,
                    tv2End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type2 -> {
                var tv1Start = 0
                var tv1End = 6
                var tv2Start = 8
                var tv2End = 11
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv1Start,
                    tv1End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv2Start,
                    tv2End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            type3 -> {
                var tv1Start = 0
                var tv1End = 7
                var tv2Start = 9
                var tv2End = 11
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv1Start,
                    tv1End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv2Start,
                    tv2End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type4 -> {
                var tv1Start = 0
                var tv1End = 6
                var tv2Start = 8
                var tv2End = 11
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv1Start,
                    tv1End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    tv2Start,
                    tv2End,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            type5 -> {

                var tv1Start = 0
                var tv1End = 6
                var tv2Start = 8
                var tv2End = 11
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 3,
                    givenNick.length + 13,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 5,
                    givenNick.length + 15,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type6 -> {
                var tv1Start = 0
                var tv1End = 6
                var tv2Start = 8
                var tv2End = 11
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 3,
                    givenNick.length + 16,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 6,
                    givenNick.length + 19,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type7 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 2,
                    givenNick.length + 13,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 5,
                    givenNick.length + 15,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

            }

            type8 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 3,
                    givenNick.length + 5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

            }
            type9 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 6,
                    givenNick.length + 8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type10 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 3,
                    givenNick.length + 5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            type11 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 3,
                    givenNick.length + 6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type12 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    givenNick.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    givenNick.length + 3,
                    givenNick.length + 6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }


            type13 -> {


                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    9,
                    13,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type14 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    9,
                    13,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type15 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    9,
                    13,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            type16 -> {
                title = ""
            }
            type17 -> {
                title = ""
            }
            type18 -> {
                title = "슈라 프로필 변경 신청이 반려되었습니다."
            }

            type19 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    7,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    7,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type20 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    7,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                bodyString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    7,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type21 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    3,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    10,
                    18,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            type22 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type23 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type24 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    11,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type25 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    12,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            type26 -> {
                titleString.setSpan(
                    ForegroundColorSpan(Color.parseColor(con.getString(R.color.main_color))),
                    0,
                    5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        binding.tvTitle.text = titleString
        binding.tvBody.text = bodyString


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_alim2, parent, false)
    ) {
        private val binding: ItemNotice2Binding? = DataBindingUtil.bind(itemView)

        fun bind(data: AlimData, position: Int) {
            binding?.dataset = data
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            if (data.pa_body.equals("이메일을 입력해주세요.")) {
                binding.ivLog.visibility = View.GONE
                binding.tvTime.visibility = View.GONE
                binding.tvTitle.visibility = View.GONE
                binding.Blank.visibility= View.VISIBLE


            } else {
                binding.Blank.visibility= View.GONE
                binding.tvTime.visibility = View.VISIBLE
                if (data.pa_check == 0) {
                    binding.ivLog.visibility = View.VISIBLE

                } else {
                    binding.ivLog.visibility = View.GONE
                }

            }
            Log.d("알림", data.getBody())
            textRetouch(binding, data)
            itemView.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }

    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:alimlist", "app:email")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<AlimData>, email: String) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as AlimAdapter)
                items?.let {
                    adapter.data.clear()
                    Log.d("getAlim 2", it.toString())
                    adapter.data.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}