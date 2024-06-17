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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogConfirmBinding
import com.supercarlounge.supercar.enumset.ConfirmStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogConfirmViewModel

class ConfirmDilaog(
    context: Context,
    var type: ConfirmStatus,
    var viewtype: Int,
    var nick: String? = null
    , val event: (ConfirmStatus, Boolean) -> Unit
) : DialogFragment() {
    var binding: DialogConfirmBinding? = null
    var viewmodel: DialogConfirmViewModel? = null

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
            R.layout.dialog_confirm,
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
//        when (viewmodel!!.viewtype.value) {
//            1 -> {
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.25).toInt()
//            }
//            2 -> {
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.3).toInt()
//            }
//            3 -> {
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.4).toInt()
//            }
//            4 -> {
                x = (displayPW * 0.6f).toInt()
                y = (displayPH * 0.3).toInt()
//            }
//        }
        var params = FrameLayout.LayoutParams(x, y)

        params.gravity = Gravity.CENTER
        dialog!!.window!!.setLayout(x, y)
//        binding!!.llDialog.layoutParams = params
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogConfirmViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.viewtypeset.value = type
        viewmodel!!.viewtype.value = viewtype
        setview()
//        setViewType()
        onclickListers()

    }
    @SuppressLint("ResourceType")
    fun setview(){

        when(type) {
            ConfirmStatus.PROFILE_CONFIRM -> {
                binding!!.ivMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.profile_40x40
                    )
                )
                viewmodel!!.tv_main.value = "프로필 열람 승인을\n하시겠습니까?"
                val content: String = viewmodel!!.tv_main.value!!
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
                binding!!.tvMain.text = spannableString
            }
            ConfirmStatus.CRUSH_CONFIRM -> {
                Glide.with(binding!!.ivMain).load(R.raw.heart_gif).override(200, 200).centerInside()
                    .into(binding!!.ivMain)
                viewmodel!!.tv_main.value = "${nick}님의\n호감을 수락하시겠습니까??"
                var length = nick?.length
                val content: String = viewmodel!!.tv_main.value!!
                val spannableString = SpannableString(content)
                if (length != null) {
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                        0,
                        length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                if (length != null) {
                    spannableString.setSpan(
                        RelativeSizeSpan(1.08f),
                        0,
                        length,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                if (length != null) {
                    spannableString.setSpan(
                        ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                        length + 3,
                        length + 5,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
                if (length != null) {
                    spannableString.setSpan(
                        RelativeSizeSpan(1.08f),
                        length + 3,
                        length + 5,
                        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                binding!!.tvMain.text = spannableString
            }
            ConfirmStatus.PROFILE_EDIT -> {
                Glide.with(binding!!.ivMain).load(R.drawable.ic_group_2242).override(200, 200)
                    .centerInside().into(binding!!.ivMain)
                viewmodel!!.tv_main.value = "프로필 사진 변경시\n관리자의 승인이 필요합니다."
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



                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    16,
                    18,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )


                spannableString.setSpan(
                    RelativeSizeSpan(1.08f),
                    16,
                    18,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding!!.tvMain.text = spannableString
                viewmodel!!.tv_sub.value = "프로필 사진을 변경하시겠습니까?"
            }

        }

    }
    private fun onclickListers() {
        binding!!.ok.setOnClickListener{
            event(type,true)
            dismiss()
        }
        binding!!.cancel.setOnClickListener{
            event(type,false)
            dismiss()
        }
    }
}