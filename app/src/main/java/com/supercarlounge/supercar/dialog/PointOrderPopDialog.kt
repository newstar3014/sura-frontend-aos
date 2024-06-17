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
import com.supercarlounge.supercar.databinding.DialogPointOrderPopupBinding
import com.supercarlounge.supercar.databinding.DialogPostPopupBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogPostPopUpViewModel


class PointOrderPopDialog(
    displayX:Int,
    displayY:Int,
    context: Context,
//    val listener: View.OnClickListener,
    val mDismisslistener: DialogEvent,
    ) : DialogFragment() , DialogEvent {

    var TAG = "UpdateDialog"
    var binding: DialogPointOrderPopupBinding? = null
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
            R.layout.dialog_point_order_popup,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        setDialogWindowSize()





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


                var w = displayPW * 0.4
                var h = displayPH * 0.08
                wmlp?.x = +w.toInt()
                wmlp?.y = h2 + h.toInt()/2



        val originalPos = IntArray(2)
        binding?.llMain?.getLocationInWindow(originalPos)
        dialog?.window!!.attributes = wmlp
        dialog!!.window!!.setBackgroundDrawableResource(R.color.trn)
        dialog!!.window!!.clearFlags( WindowManager.LayoutParams.FLAG_DIM_BEHIND )





    }

    fun initView(){
        var x = 0
        var y= 0

            var viewx = binding?.llSaveOrder?.width?.times(1)
            var viewh = binding?.llSaveOrder?.height?.times(3.6)
            if (viewx != null) {
                x = viewx.toInt()
            }
            if (viewh != null) {
                y = viewh.toInt()
            }

        dialog!!.window!!.setLayout(x, y)
    }


    override fun dismiss() {
        super.dismiss()
            mDismisslistener.okEvent(0,false,"","","")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogPostPopUpViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        mListener =mDismisslistener



        setText()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    private fun setText(){

    }



    private fun onclickListers(){
        binding!!.llAllOrder.setOnClickListener {
            mDismisslistener.okEvent(Constans.ORDER_MORE_ALL,true,"0","0","0")
            this.dismiss()
        }
        binding!!.llSaveOrder.setOnClickListener {
            mDismisslistener.okEvent(Constans.ORDER_MORE_SAVE,true,"0","0","0")
            this.dismiss()
        }
        binding!!.llDeductOrder.setOnClickListener {
            mDismisslistener.okEvent(Constans.ORDER_MORE_DEDUCTION,true,"0","0","0")
            this.dismiss()
        }
    }

    override fun okEvent(type: Int, okAndCancel: Boolean,comment_value: String,seq:String,u_nickname: String) {
        Log.d("팝업 다이얼로그 이벤트", "$type + $okAndCancel")
    }



}