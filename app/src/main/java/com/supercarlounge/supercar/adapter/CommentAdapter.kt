package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.BoardCommentData
import com.supercarlounge.supercar.databinding.ItemCommentBinding
import com.supercarlounge.supercar.dialog.PostPopUpDialog
import com.supercarlounge.supercar.enumset.TimeValue
import com.supercarlounge.supercar.viewmodel.PostViewModel
import java.text.ParseException
import java.text.SimpleDateFormat


class CommentAdapter(val mDismisslistener: DialogEvent, val con: Context, var data: ArrayList<BoardCommentData>, val model: PostViewModel, val fm :FragmentManager, val itemClick: (BoardCommentData, Int, View, Int) -> Unit ) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    var selectindex = 0
    private var postPopUpDialog : PostPopUpDialog? =null
    private var  fragm = fm
    lateinit var adapter :CommentSubAdapter
    var mListener: DialogEvent? = null
    init {
        mListener =mDismisslistener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist:ArrayList<BoardCommentData>){
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

        LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
    ) {
        private val binding: ItemCommentBinding? = DataBindingUtil.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(data: BoardCommentData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            itemView.setOnClickListener { itemClick(data, position,binding.tvNickname , 0) }
            binding!!.tvNickname.setOnClickListener {

                model!!.scrollY.value = binding.llScroll.scrollY
                itemClick(data, position,binding.tvNickname ,Constans.NICKNAME)  }
            binding!!.llHeart.setOnClickListener { itemClick(data, position,binding.tvNickname ,Constans.HEART) }
            binding!!.tvDate.text = setTime(dateTimeToMillSec(data.cm_date))
//            binding!!.tvLike.text = data.m_like.toString()
            binding!!.tvCmValue.text =data.decodeset(data.cm_values)
            val animator = binding.rvCommentSub?.itemAnimator     //리사이클러뷰 애니메이터 get
            if (animator is SimpleItemAnimator){          //아이템 애니메이커 기본 하위클래스
                animator.supportsChangeAnimations = false  //애니메이션 값 false (리사이클러뷰가 화면을 다시 갱신 했을때 뷰들의 깜빡임 방지)
            }
            Log.d("ww", data.like_yn.toString())
            Log.d("gd", model.comment_like_type.value.toString())
//            if ( data.like_yn.equals("Y")){
                Glide.with(binding!!.ivHeart).load(R.drawable.ic_cell_4).centerInside().into(binding!!.ivHeart)
//            }else{
//                Glide.with(binding!!.ivHeart).load(R.drawable.sendheart_01_24x24).centerInside().into(binding!!.ivHeart)
//            }
            if (data.u_gender.equals("MALE")){
                Glide.with(binding!!.ivSex).load(R.drawable.ic_man_12x12).centerInside().into(binding!!.ivSex)
            }else{
                Glide.with(binding!!.ivSex).load(R.drawable.ic_woman_12x12).centerInside().into(binding!!.ivSex)
            }

            binding!!.rvCommentSub.apply {

                adapter = CommentSubAdapter(con,data.sub_comment,model){ subdata, position ,view,type->


                    model!!.subCommentPosition.value =position

                    model.cs_seq.value  = subdata.cs_seq.toString()
                    Log.d("코멘트 서브데이타", subdata.toString())
                    if (type == Constans.NICKNAME) {
                        model!!.us_seq.value = subdata.u_seq.toString()
                        val originalPos = IntArray(2)
                        view.getLocationInWindow(originalPos)
                        val x = originalPos[0]
                        val y = originalPos[1]
                        Log.d("좌표", "$y + $x")
                        var viewtype =0
                        if (model.my_u_seq.value == data.sub_comment.get(position).u_seq || model.ismymanager.value!!) viewtype = Constans.MY_SEQ else viewtype = Constans.YOU_SEQ
                        postPopUpDialog = PostPopUpDialog(x, y, con,mListener!!, viewtype,Constans.COMMENT_SUB_MORE,subdata.cs_seq.toString(),subdata.u_nickname ,subdata.cs_values)
                        model.cs_seq.value  = subdata.cs_seq.toString()
                        val manager = (context as AppCompatActivity).supportFragmentManager
                        postPopUpDialog!!.show(manager, "")

                    }else if(type ==Constans.HEART) {
//                        if (model.subcomment_like_type.value== 1) {
////                            model.subcomment_like_type.value = 1
//                        } else {
////                            model.subcomment_like_type.value =1
//
//                        }
                        var application = con.applicationContext as MainApplication
                        if(application.CheckCompany() == false) {
                            model.cs_seq.value = subdata.cs_seq.toString()
                            model.SubCommentList.value = data.sub_comment
                            model.select_seq.value = subdata.u_seq.toString()
//                        model.insertSubComMainLike()
                            if (model.my_u_seq.value.toString() != model.select_seq.value) {
                                model.boardViewCheckClick(model.select_seq.value.toString())
                            }
                        }else{
                            model.toast.value = con.getString(R.string.company_profile_open_text)
                        }
                    }else{
                        model.tagNickname.value = "@"+subdata.u_nickname
                        model.cs_seq.value  = subdata.cs_seq.toString()
                        model.cm_seq.value = subdata.cm_seq.toString()
                    }


                }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }


//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun dateTimeToMillSec(dateTime: String): Long{
        Log.d("변환전 시간", dateTime)

        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        val target = "T"
        var result = dateTime.replace(target," ") // '-'를 공백으로 바꿔준다.
        var timeInMilliseconds: Long = 0
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val mDate = sdf.parse(result)

            timeInMilliseconds = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        Log.d("변환된 시간", timeInMilliseconds.toString())
        return timeInMilliseconds
    }
    fun setTime(time : Long) :String{
        val curTime = System.currentTimeMillis()
        Log.d("현재 시간", curTime.toString())
        var diffTime = (curTime- time) / 1000
        var msg: String? = null
        if(diffTime < TimeValue.SEC.value ) {
            msg = "방금 전"
            return msg.toString()
        }else {
            for (i in TimeValue.values()) {
                diffTime = diffTime.div(i.value)


                if (diffTime > 14 && i == TimeValue.HOUR ||i > TimeValue.HOUR){
                    msg = "지난글"
                    return msg.toString()
                }
                if (diffTime < i.maximum) {
                    msg=i.msg
                    break
                }
            }
        }
        return diffTime.toString() + msg.toString()
    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:commentList")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<BoardCommentData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as CommentAdapter)
                items?.let {
                    var count = adapter.data.count()
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }
    }



}