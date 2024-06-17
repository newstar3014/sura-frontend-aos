package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogProfileBrowseBinding
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogProfileBrowseViewModel
import java.util.*


class ProfileLikeFriendDialog(
    context: Context,
    var nick:String,
    var type: PopTypeStatus,
     val event: (PopTypeStatus, Boolean,String) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogProfileBrowseBinding? = null
    var viewmodel: DialogProfileBrowseViewModel? = null
    var calenderList: ArrayList<String>? = arrayListOf()
//    var mListener: DialogListener? = null

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
            R.layout.dialog_profile_browse,
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
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        val x = (displayPW * 0.6f).toInt()
        val y = (displayPH * 0.45f).toInt()
        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog.layoutParams = params
    }


    override fun dismiss() {
        super.dismiss()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogProfileBrowseViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.type.value = type

        setText()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    private fun setText(){
        when(viewmodel!!.type.value){
            PopTypeStatus.DRIVE_FRIEND->{


                var content =  getString(R.string.friend_drive_main)
                val spannableString = SpannableString(content)

                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                        0,
                        9,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString.setSpan(
                        RelativeSizeSpan(1.08f),
                        0,
                        9,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = getString(R.string.warning_profile)
            }
            PopTypeStatus.POST_FRIEND-> {
                var nicklength = nick.length
                var content = nick+" "+getString(R.string.friend_post_main)
                val spannableString = SpannableString(content)
                if (nicklength != 0) {
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                        0,
                        nicklength,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableString.setSpan(
                        RelativeSizeSpan(1.08f),
                        0,
                        nicklength,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    binding!!.tvMain.text = spannableString
                    viewmodel!!.tv_sub.value = getString(R.string.warning_profile)
                }
            }
            else -> {}
        }

//        when(viewmodel!!.type.value!!){
//            Constans.DIALOG_BROWSE_REQUEST_PAYMENT-> {
//                viewmodel!!.tv_main.value = "프로필 열람 승인을\n요청하시겠습니까?\n120하트가 소모됩니다."
//                var content = viewmodel!!.tv_main.value!!
//                val spannableString = SpannableString(content)
//                spannableString.setSpan(
//                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
//                    0,
//                    9,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                spannableString.setSpan(
//                    RelativeSizeSpan(1.08f),
//                    0,
//                    9,
//                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                binding!!.tvMain.text =spannableString
//                viewmodel!!.tv_third.value = "이미 열람한 프로필\n또는 지인의 프로필일 수 있습니다."
//            }
//            Constans.DIALOG_BROWSE_REQUEST -> {
//                viewmodel!!.tv_main.value = "프로필 열람 승인을\n요청하시겠습니까?\n하트가 소모되지 않습니다."
//                var content = viewmodel!!.tv_main.value!!
//                val spannableString = SpannableString(content)
//                spannableString.setSpan(
//                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
//                    0,
//                    9,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                spannableString.setSpan(
//                    RelativeSizeSpan(1.08f),
//                    0,
//                    9,
//                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                binding!!.tvMain.text =spannableString
//                viewmodel!!.tv_third.value = "이미 열람한 프로필\n또는 지인의 프로필일 수 있습니다."
//            }
//            Constans.DIALOG_BROWSE_CHECK ->{
//                viewmodel!!.tv_main.value = "프로필 열람 승인을\n하시겠습니까?"
//                var content = viewmodel!!.tv_main.value!!
//                val spannableString = SpannableString(content)
//                spannableString.setSpan(
//                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
//                    0,
//                    9,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                spannableString.setSpan(
//                    RelativeSizeSpan(1.08f),
//                    0,
//                    9,
//                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
//                )
//                binding!!.tvMain.text =spannableString
//
//                viewmodel!!.tv_third.value = "이미 열람한 프로필\n또는 지인의 프로필일 수 있습니다."
//            }
//        }





    }



    private fun onclickListers(){
        binding!!.ok.setOnClickListener {
           event(type,true,viewmodel!!.tv_edit.value!!)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(type,false,viewmodel!!.tv_edit.value!!)
            dismiss()
        }

    }

}