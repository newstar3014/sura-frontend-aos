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
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.Constans.Companion.DIALOG_CONTACT_OPEN
import com.supercarlounge.supercar.Constans.Companion.DIALOG_POST_COMMENT_DELETE_API_CALL
import com.supercarlounge.supercar.Constans.Companion.DIALOG_POST_DELETE
import com.supercarlounge.supercar.Constans.Companion.DIALOG_POST_DELETE_API_CALL
import com.supercarlounge.supercar.Constans.Companion.DIALOG_POST_SUB_COMMENT_DELETE_API_CALL
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.databinding.DialogTwoButtonBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogTwoButtonViewModel


class TwoButtonDialog(
    context: Context,
    var type: Int,
    val mDismisslistener: DialogEvent,
    val deleteType :Int,
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogTwoButtonBinding? = null
    var viewmodel: DialogTwoButtonViewModel? = null
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
            R.layout.dialog_two_button,
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
        var x = 0
        var y= 0
        when (viewmodel!!.viewtype.value) {
            1 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.25).toInt()
            }
            2 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.3).toInt()
            }
            3 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.4).toInt()
            }
            4 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.4).toInt()
            }
        }
        var params = FrameLayout.LayoutParams(x, y)

        params.gravity = Gravity.CENTER
        dialog!!.window!!.setLayout(x, y)
//        binding!!.llDialog.layoutParams = params
    }


    override fun dismiss() {
        super.dismiss()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogTwoButtonViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.type.value = type
//        viewmodel!!.b_seq.value = b_seq
        mListener = mDismisslistener
//
        viewmodel!!.tv_ok.observe(viewLifecycleOwner, Observer {
            it?.let {

//                    mListener?.okEvent(DIALOG_OK);

            }
        })



        viewmodel!!.tv_cancel.observe(viewLifecycleOwner, Observer {
            it?.let {
//             dismiss()
            }

        })
        setText()
        setViewType()
        onclickListers()
        setImage(viewmodel!!.type.value!!)


    }

    private fun setImage(type:Int) {
        when (type) {
            Constans.DIALOG_PASS_BUY -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pop_hartshop_40x40))
            }
            Constans.DIALOG_EVALUATION -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pop_rpm_40x40))
            }
            Constans.DIALOG_DRIVE_RESET -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pop_rpm_40x40))
            }

            Constans.DIALOG_PROFILE_CHANGE -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_group_2242))
            }

            Constans.DIALOG_CRUSH_ARRIVAL_PAYMENT -> {
                Glide.with(binding!!.ivMain).load(R.raw.cash_gif).override(200,200).centerInside().into(binding!!.ivMain)
            }

            Constans.DIALOG_CRUSH_ARRIVAL -> {
                Glide.with(binding!!.ivMain).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivMain)
            }


            Constans.DIALOG_CONTACT_OPEN -> {
                Glide.with(binding!!.ivMain).load(R.raw.call).override(200,200).centerInside().into(binding!!.ivMain)
            }
            Constans.DIALOG_CONTACT_OPEN_COMPLETION -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_group_1606))
            }
            Constans.DIALOG_CRUSH_ACCEPT -> {
                Glide.with(binding!!.ivMain).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivMain)

            }
            Constans.DIALOG_CRUSH_SEND1 -> {
                Glide.with(binding!!.ivMain).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivMain)
            }

            Constans.DIALOG_CRUSH_SEND2 -> {
                Glide.with(binding!!.ivMain).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivMain)
            }

            Constans.DIALOG_CRUSH_TRY_ACCEPT -> {
                Glide.with(binding!!.ivMain).load(R.raw.heart_gif).override(200,200).centerInside().into(binding!!.ivMain)
            }
            Constans.DIALOG_LACK_HEART -> {
                Glide.with(binding!!.ivMain).load(R.drawable.ic_frame_1862).override(200,200).centerInside().into(binding!!.ivMain)
            }
            Constans.DIALOG_POST_DELETE -> {
                Glide.with(binding!!.ivMain).load(R.drawable.ic_delete_40x40).override(200,200).centerInside().into(binding!!.ivMain)
            }

            Constans.DIALOG_IMAGE_DELETE -> {
                binding!!.ivMain.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_group_2242))
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun setText() {

        var content: String =""
        when (viewmodel!!.type.value) {

            Constans.DIALOG_PASS_BUY -> {
                viewmodel!!.tv_main.value = "드라이브 패스권을 구매해야\n열람하실 수 있습니다."
                viewmodel!!.tv_sub.value = "상점으로 이동하시겠습니까?"
                content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    8,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            Constans.DIALOG_EVALUATION -> {
                viewmodel!!.tv_main.value = "3점으로\n평가하시겠습니까?"
                viewmodel!!.tv_sub.value = "상점으로 이동하시겠습니까?"
                content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

            Constans.DIALOG_DRIVE_RESET -> {
                viewmodel!!.tv_main.value = "RPM 점수를\n리셋하시겠습니까?"
                content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    9,
                    10,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    9,
                    10,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
//                spannableString.setSpan(
//                    StyleSpan(Typeface.BOLD),
//                    start,
//                    end,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//
//                )


                viewmodel!!.tv_sub.value = "3500 하트"
            }

            Constans.DIALOG_PROFILE_CHANGE -> {
                viewmodel!!.tv_main.value = "프로필 사진 변경시\n관리자의 승인이 필요합니다"
                viewmodel!!.tv_sub.value = "프로필 사진을 변경하시겠습니까?"
                content = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    16,
                    18,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    16,
                    18,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

            Constans.DIALOG_CRUSH_ARRIVAL_PAYMENT -> {
                viewmodel!!.tv_main.value = "내 게시글에 설레이는\n호감이 도착했습니다."
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                val start: Int = content.indexOf(content)
                val end: Int = start + content.length


                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    12,
                    14,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
//                spannableString.setSpan(
//                    StyleSpan(Typeface.BOLD),
//                    start,
//                    end,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//
//                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    12,
                    14,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // 4

                // 4
                binding!!.tvMain.text =spannableString
                viewmodel!!.tv_sub.value = "호감 하트 대납 옵션이 적용되어,\n회원님의 보유 하트가 10 하트 차감되었습니다."
            }

            Constans.DIALOG_CRUSH_ARRIVAL -> {
                viewmodel!!.tv_main.value = "내 게시글에 설레이는\n호감이 도착했습니다."
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    12,
                    14,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    12,
                    14,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

            Constans.DIALOG_CONTACT_OPEN -> {
                viewmodel!!.tv_main.value = "연락처 공개하기\n20 하트가 소모됩니다."
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString

            }
            Constans.DIALOG_CONTACT_OPEN_COMPLETION -> {
                viewmodel!!.tv_main.value = "연락처가 공개되었습니다!"
                viewmodel!!.tv_sub.value = "안심번호는 공개 후 24시간 동안 유효합니다."
                viewmodel!!.tv_third.value = "인증번호는 양뱡향 안심번호 형태이며\n어느 누가 전화를 걸더라도 원래 전화번호가\n노출되지 않습니다. 안심하고 연락하세요!"
                val content: String = viewmodel!!.tv_third.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    6,
                    14,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    6,
                    14,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvThird.text =spannableString
            }
            Constans.DIALOG_CRUSH_ACCEPT -> {
                viewmodel!!.tv_main.value = "상대방이 호감을 수락했습니다"
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            Constans.DIALOG_CRUSH_SEND1 -> {
                viewmodel!!.tv_main.value = "두근두근,\n호감이 발송되었습니다."
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
           }
            Constans.DIALOG_CRUSH_SEND2 -> {
                viewmodel!!.tv_main.value = "진심을 담은\n호감이 발송되었습니다."
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    6,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    0,
                    6,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            Constans.DIALOG_CRUSH_TRY_ACCEPT -> {
                var name = "호빵맨"
                var sub = "님의\n호감을 수락하시겠습니까?"
                viewmodel!!.tv_main.value = name+sub
                val content: String = name
                val start: Int = content.indexOf(content)
                val end: Int = start + content.length
                val spannableString = SpannableString(viewmodel!!.tv_main.value)

                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    start,
                    end,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    end+3,
                    end+5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    end+3,
                    end+5,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }

            Constans.DIALOG_LACK_HEART -> {
                viewmodel!!.tv_main.value = "보유 하트가 부족합니다."
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    3,
                    5,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    3,
                    5,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            Constans.DIALOG_POST_DELETE -> {
                viewmodel!!.tv_main.value = "해당 글을\n삭제하시겠습니까?"
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    6,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    6,
                    8,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
            Constans.DIALOG_IMAGE_DELETE -> {
                viewmodel!!.tv_main.value = "사진을 삭제하시겠습니까?"
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    5,
                    8,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    5,
                    5,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text =spannableString
            }
        }

    }

    private fun setViewType() {
        when (viewmodel!!.type.value) {
            Constans.DIALOG_PASS_BUY -> {
                viewmodel!!.viewtype.value =2
            }
            Constans.DIALOG_EVALUATION -> {
                viewmodel!!.viewtype.value =1
            }

            Constans.DIALOG_DRIVE_RESET -> {
                viewmodel!!.viewtype.value =2
            }

            Constans.DIALOG_PROFILE_CHANGE -> {
                viewmodel!!.viewtype.value =2
            }

            Constans.DIALOG_CRUSH_ARRIVAL_PAYMENT -> {
                viewmodel!!.viewtype.value =4
            }

            Constans.DIALOG_CRUSH_ARRIVAL -> {
                viewmodel!!.viewtype.value =1
            }

            Constans.DIALOG_CONTACT_OPEN -> {
                viewmodel!!.viewtype.value =1

            }
            Constans.DIALOG_CONTACT_OPEN_COMPLETION -> {
                viewmodel!!.viewtype.value =3
            }
            Constans.DIALOG_CRUSH_ACCEPT -> {
                viewmodel!!.viewtype.value =1

            }
            Constans.DIALOG_CRUSH_SEND1 -> {
                viewmodel!!.viewtype.value =1
            }
            Constans.DIALOG_EVALUATION -> {
                viewmodel!!.viewtype.value =1
            }
            Constans.DIALOG_CRUSH_TRY_ACCEPT -> {
                viewmodel!!.viewtype.value =1
            }
            Constans.DIALOG_LACK_HEART -> {
                viewmodel!!.viewtype.value =2
            }
            Constans.DIALOG_POST_DELETE -> {
                viewmodel!!.viewtype.value =1
            }
            Constans.DIALOG_IMAGE_DELETE -> {
                viewmodel!!.viewtype.value =0
            }
        }
    }



    private fun onclickListers() {
        binding!!.ok.setOnClickListener{
            when(viewmodel?.type?.value){
                DIALOG_POST_DELETE  ->{
                    when(deleteType){
                        Constans.POST_MORE_DELETE->{
                            mDismisslistener.okEvent(DIALOG_POST_DELETE_API_CALL,true,"","0","")
                        }
                        Constans.COMMENT_MORE_DELETE->{
                            mDismisslistener.okEvent(DIALOG_POST_COMMENT_DELETE_API_CALL,true,"","0","")
                        }
                        Constans.COMMENT_SUB_MORE_DELETE->{
                            mDismisslistener.okEvent(DIALOG_POST_SUB_COMMENT_DELETE_API_CALL,true,"","0","")
                        }
                    }

                }
                DIALOG_CONTACT_OPEN ->{
                    mDismisslistener.okEvent(0,true,"","","")
                }
            }

            dismiss()
        }
        binding!!.cancel.setOnClickListener{
//            mDismisslistener.okEvent(1,false,"","0","")
            dismiss()
        }
    }



}