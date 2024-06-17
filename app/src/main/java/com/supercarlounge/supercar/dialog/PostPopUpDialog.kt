package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.databinding.DialogPostPopupBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogPostPopUpViewModel


class PostPopUpDialog(
    displayX:Int,
    displayY:Int,
    context: Context,
//    val listener: View.OnClickListener,
    val mDismisslistener: DialogEvent,
    var type: Int,
    var commentType:Int,
    var seq:String,
    var u_nickname : String,
    var comment_value:String

    ) : DialogFragment() , DialogEvent {

    var TAG = "UpdateDialog"
    var binding: DialogPostPopupBinding? = null
    var viewmodel: DialogPostPopUpViewModel? = null
    var displayX = displayX
    var displayY = displayY
    var calenderList: ArrayList<String>? = arrayListOf()
    var mListener: DialogEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_post_popup,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        setDialogWindowSize()
        binding?.llNoti?.viewTreeObserver?.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {

                    initView()
                    // 1회성을 위해 Listener 제거
                    binding?.llNoti!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })





    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val displayRealPH = displayPH /0.9
        Log.d("전체해상도", displayPH.toString())
        val window = dialog!!.window
//        window!!.setLayout(displayPW, displayPH)






//        }else{
//            y = (displayPH * 0.08f).toInt()
//        }

        val wmlp = dialog?.window?.attributes
        Log.d("기존 x축", displayX.toString())
        Log.d("기존 y축", displayY.toString())

        Log.d("변경된 x축", wmlp?.x.toString())
        Log.d("변경된 y축", wmlp?.y.toString())

        //디바이스높이 반지름
        var deviceh2 = displayPH/2
        //가져온높이 반지름

        var h2 = 0
        if (displayY > deviceh2){
             h2 = displayY - deviceh2

        }else{
            var hh = deviceh2 - displayY
             h2 = -hh
        }

        Log.d("확인용 ", h2.toString())
//        if (h2 > deviceh2 ){
//            var xxx = deviceh2 - h2
//
//            h2 =  xxx
//            Log.d("확인용 플러스", h2.toString())
//        }else{
//            var xxx = deviceh2 - h2
//            h2 = (-xxx).toInt()
//            Log.d("확인용 마이너스", h2.toString())
//        }


        when(commentType){
            Constans.COMMENT_MORE->{
                var d = displayPW * 0.31
                wmlp?.x = -d.toInt()
                wmlp?.y = h2
            }
            Constans.COMMENT_SUB_MORE->{
                var d = displayPW * 0.25
                wmlp?.x = -d.toInt()
                wmlp?.y = h2
            }
            Constans.POST_MORE->{
                var d = displayPW * 1
                wmlp?.x = +d.toInt()
                wmlp?.y = h2
            }
            Constans.ORDER_MORE->{
                var w = displayPW * 0.31
                var h = displayPH * 0.15
                wmlp?.x = +w.toInt()
                wmlp?.y = h2 + h.toInt()/2
            }
        }

//
//        if (commentType == Constans.ORDER_MORE){
//            var viewx = binding?.llNewOrder?.width?.times(1.2)
//            var viewh = binding?.llNewOrder?.height?.times(3.6)
//            if (viewx != null) {
//                x = viewx.toInt()
//            }
//            if (viewh != null) {
//                y = viewh.toInt()
//            }
//        }else{
//            var viewx = binding?.llNoti?.width?.times(1.2)
//            var viewh = binding?.llNoti?.height?.times(2.4)
//            if (viewx?.toInt() == 0){
//                viewx = binding?.llDelete?.width?.times(1.2)
//                viewh = binding?.llDelete?.height?.times(2.4)
//            }
//
//            if (viewx != null) {
//                x = viewx.toInt()
//            }
////        if (viewmodel?.viewtype?.value == 2){
//
//            if (viewh != null) {
//                y = viewh.toInt()
//            }
//        }

        val originalPos = IntArray(2)
        binding?.llMain?.getLocationInWindow(originalPos)
        dialog?.window!!.attributes = wmlp
        dialog!!.window!!.setBackgroundDrawableResource(R.color.trn)
        dialog!!.window!!.clearFlags( WindowManager.LayoutParams.FLAG_DIM_BEHIND )





    }

    fun initView(){
        var x = 0
        var y= 0

        if (commentType == Constans.ORDER_MORE){
            var viewx = binding?.llViewsOrder?.width?.times(1)
            var viewh = binding?.llViewsOrder?.height?.times(3.6)
            if (viewx != null) {
                x = viewx.toInt()
            }
            if (viewh != null) {
                y = viewh.toInt()
            }
        }else{
            var viewx = binding?.llNoti?.width?.times(1.1)
            var viewh = binding?.llNoti?.height?.times(2.4)
            if (viewx?.toInt() == 0){
                viewx = binding?.llDelete?.width?.times(1.1)
                viewh = binding?.llDelete?.height?.times(2.4)
            }

            if (viewx != null) {
                x = viewx.toInt()
            }
//        if (viewmodel?.viewtype?.value == 2){

            if (viewh != null) {
                y = viewh.toInt()
            }


        }
        dialog!!.window!!.setLayout(x, y)
    }


    override fun dismiss() {
        super.dismiss()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogPostPopUpViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtype.value = type
        viewmodel!!.b_seq.value = seq.toInt()
        mListener =mDismisslistener
        Log.d("체크", type.toString())


//        mListener = mDismisslistener

//
//        viewmodel!!.ok.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let {
//                if (it) {
//
//                }
//            }
//        })
//
//
//
//        viewmodel!!.cancel.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let {
//                if (it) {
//
//                }
//            }
//
//        })
        setText()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    private fun setText(){

    }



    private fun onclickListers(){
        binding!!.llBlock.setOnClickListener {
            when(commentType){
                Constans.POST_MORE ->   mDismisslistener.okEvent(Constans.POST_MORE_BLOCK,true,"",seq,"")
                Constans.COMMENT_MORE ->   mDismisslistener.okEvent(Constans.COMMENT_MORE_BLOCK,true,"",seq,"")
                Constans.COMMENT_SUB_MORE ->   mDismisslistener.okEvent(Constans.COMMENT_SUB_MORE_BLOCK,true,"",seq,"")

            }
            this.dismiss()

        }
        binding!!.llDelete.setOnClickListener {
            when(commentType){
                Constans.POST_MORE ->   mDismisslistener.okEvent(Constans.POST_MORE_DELETE,true,"",seq,"")
                Constans.COMMENT_MORE ->   mDismisslistener.okEvent(Constans.COMMENT_MORE_DELETE,true,"",seq,"")
                Constans.COMMENT_SUB_MORE ->   mDismisslistener.okEvent(Constans.COMMENT_SUB_MORE_DELETE,true,"",seq,"")

            }

            this.dismiss()

        }
        binding!!.llEdit.setOnClickListener {

            when(commentType){
                Constans.POST_MORE ->   mDismisslistener.okEvent(Constans.POST_MORE_EDIT,true,"",seq,"")
                Constans.COMMENT_MORE ->   mDismisslistener.okEvent(Constans.COMMENT_MORE_EDIT,true,comment_value,seq,"")
                Constans.COMMENT_SUB_MORE ->   mDismisslistener.okEvent(Constans.COMMENT_SUB_MORE_EDIT,true,comment_value,seq,u_nickname)

            }

            this.dismiss()
        }
        binding!!.llNoti.setOnClickListener {
            when(commentType) {
                Constans.POST_MORE -> mDismisslistener.okEvent(Constans.POST_MORE_NOTI, true,"",seq,"")
                Constans.COMMENT_MORE -> mDismisslistener.okEvent(Constans.COMMENT_MORE_NOTI, true,"",seq,"")
                Constans.COMMENT_SUB_MORE -> mDismisslistener.okEvent(Constans.COMMENT_SUB_MORE_NOTI, true,"",seq,"")
            }
            this.dismiss()
        }

        binding!!.llCommentOrder.setOnClickListener {
            mDismisslistener.okEvent(Constans.ORDER_MORE_COMMENT,true,"0","0","0")
            this.dismiss()
        }
        binding!!.llNewOrder.setOnClickListener {
            mDismisslistener.okEvent(Constans.ORDER_MORE_NEW,true,"0","0","0")
            this.dismiss()
        }
        binding!!.llViewsOrder.setOnClickListener {
            mDismisslistener.okEvent(Constans.ORDER_MORE_VIEWS,true,"0","0","0")
            this.dismiss()
        }
    }

    override fun okEvent(type: Int, okAndCancel: Boolean,comment_value: String,seq:String,u_nickname: String) {
        Log.d("팝업 다이얼로그 이벤트", "$type + $okAndCancel")
    }



}