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
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogProfileBrowseBaseBinding
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogProfileBrowseViewModel


class ProfileDriveLikeBaseDialog(
    context: Context,
    var nick:String,
    var type: PopTypeStatus,
    val event: (PopTypeStatus, Boolean) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogProfileBrowseBaseBinding? = null
    var viewmodel: DialogProfileBrowseViewModel? = null

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
            R.layout.dialog_profile_browse_base,
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
        var x=0
        var y=0
        when(type){
            PopTypeStatus.USER_EDIT_CHECK->{
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.26f).toInt()
            }
            else ->{
                 x = (displayPW * 0.6f).toInt()
                 y = (displayPH * 0.31f).toInt()
            }
        }

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
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.type.value = type


        setview()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    private fun setview(){
        when(viewmodel!!.type.value){
            PopTypeStatus.DRIVE_NOT_FRIEND->{
                Glide.with(binding!!.ivMain).load(R.drawable.profile_40x40).into(binding!!.ivMain)
                var content =  getString(R.string.not_friend_drive_main_drive)
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    7,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    7,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = getString(R.string.not_friend_drive_sub_drive)
                viewmodel!!.tv_ok.value = "확인"
                viewmodel!!.tv_cancel.value = "취소"
            }
            PopTypeStatus.POST_NOT_FRIEND->{
                Glide.with(binding!!.ivMain).load(R.drawable.ic_group_2057).into(binding!!.ivMain)
                var nicklength =  nick.length
                var content =  nick+" "+getString(R.string.not_friend_post_main)
                val spannableString = SpannableString(content)
                if(nicklength!= 0) {
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
                }
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value =  getString(R.string.not_friend_post_sub)
                viewmodel!!.tv_ok.value = "확인"
                viewmodel!!.tv_cancel.value = "취소"
            }
            PopTypeStatus.USER_EDIT_CHECK->{
                Glide.with(binding!!.ivMain).load(R.drawable.pop_icon_40x40).into(binding!!.ivMain)
                var content = "변경된 정보가 있습니다\n수정하시겠습니까?"

                binding!!.tvMain.text =content
                viewmodel!!.tv_ok.value = "수정하기"
                viewmodel!!.tv_cancel.value = "아니요"
            }
            else -> {}
        }
    }



    private fun onclickListers(){
        binding!!.ok.setOnClickListener {
           event(type,true)
        }
        binding!!.cancel.setOnClickListener {
            event(type,false)
        }

    }

}