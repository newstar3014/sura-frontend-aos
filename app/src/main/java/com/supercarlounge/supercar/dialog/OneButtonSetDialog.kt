package com.supercarlounge.supercar.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.DialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.*


class OneButtonSetDialog(
    context: Context,
    var type: PopOneTypeStatus,
    var event: (PopOneTypeStatus, Boolean) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogOnesetButtonBinding? = null
    var viewmodel: DialogOneButtonViewModel? = null
    var mListener: DialogEvent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    private fun viewRedefinition() {
        val layoutParamsSub1 = binding!!.ivMain.layoutParams
        layoutParamsSub1.width = 350
        layoutParamsSub1.height = 350
        binding!!.ivMain.layoutParams = layoutParamsSub1

    }

    override fun getTheme() = R.style.RoundedCornersDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_oneset_button,
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
        var y = 0

        if (viewmodel?.type?.value == PopOneTypeStatus.DIALOG_UPDATE) {
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.45).toInt()
        } else if (viewmodel?.type?.value == PopOneTypeStatus.DIALOG_UPDATE_APPROVAL) {
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.38).toInt()
        } else if (viewmodel?.type?.value == PopOneTypeStatus.DIALOG_REQUEST_PROFILE_WAIT) {
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.38).toInt()
        } else if (viewmodel?.type?.value == PopOneTypeStatus.DIALOG_VIP_CHECK) {
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.38).toInt()
        } else {
            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.38).toInt()
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
        viewmodel = ViewModelProvider(this).get(DialogOneButtonViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.type.value = type
        setText(viewmodel!!.type.value!!)
        setImage(viewmodel!!.type.value!!)
        onclickListers()
        binding!!.ivMain.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewRedefinition()

            }


        })
    }

    @SuppressLint("ResourceType")
    private fun setText(type: PopOneTypeStatus) {
        when (type) {
            PopOneTypeStatus.DIALOG_MAIN_CAR -> {
                viewmodel!!.tv_main.value = "대표차량은\n삭제하실 수 없습니다."
                viewmodel!!.tv_sub.value = "대표차량을 다른 차량으로 변경하신 후\n삭제해 주세요."

            }
            PopOneTypeStatus.DIALOG_CAR_REGISTRATION -> {
                viewmodel!!.tv_main.value = "차량 정보 등록을\n취소 하시겠습니까?."
                viewmodel!!.tv_sub.value = "입력하신 내용은 삭제됩니다."
            }
            PopOneTypeStatus.DIALOG_TERMS -> {
                viewmodel!!.tv_main.value = "약관에 동의해 주세요."
                viewmodel!!.tv_sub.value = "동의하지 않을 시 회원가입이 불가합니다.."
            }
            PopOneTypeStatus.DIALOG_POST_CATEGPRY -> {
                viewmodel!!.tv_main.value = "작성하신 게시글의\n카테고리가 이동되었습니다."

            }
            PopOneTypeStatus.DIALOG_POST_CATEGPRY -> {
                viewmodel!!.tv_main.value = "작성하신 게시글의\n카테고리가 이동되었습니다."

            }
            PopOneTypeStatus.DIALOG_INSURANCE -> {
                viewmodel!!.tv_main.value = "보험 정보를\n업데이트해 주세요."
                viewmodel!!.tv_sub.value = "종합보험 만료일,종합보험 가입증서\n업로드를 확인해 주세요."

            }

            PopOneTypeStatus.DIALOG_POST_REPORT -> {
                viewmodel!!.tv_main.value = "신고가 정상적으로\n접수되었습니다"

            }

            PopOneTypeStatus.DIALOG_POST_BLOCK_OK -> {
                viewmodel!!.tv_main.value = "게시글이\n차단 되었습니다."

            }
            PopOneTypeStatus.DIALOG_POST_COMMENT_BLOCK_OK -> {
                viewmodel!!.tv_main.value = "댓글이\n차단 되었습니다."

            }
            PopOneTypeStatus.DIALOG_POST_SUB_COMENT_BLOCK_OK -> {
                viewmodel!!.tv_main.value = "대댓글이\n차단 되었습니다."

            }
            PopOneTypeStatus.DIALOG_REQUEST_PROFILE_WAIT -> {
                viewmodel!!.tv_main.value = "프로필 열람 승인 대기중 입니다."

            }
            PopOneTypeStatus.DIALOG_VIP_OVERLAP -> {
                viewmodel!!.tv_main.value = "VIP 드라이브 등록은\n한 개 이상 등록하실 수 없습니다."

            }
            PopOneTypeStatus.DIALOG_VIP_CHECK -> {
                viewmodel!!.tv_main.value = "서비스 이용 실적 최상위 멤버\n또는 특별 선정 고객만 입장 가능합니다."
                binding?.tvMain?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white_70))
                binding?.tvMain?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX, requireContext().resources.getDimension(
                    com.supercarlounge.supercar.R.dimen._8sdp))
                viewmodel!!.tv_sub.value = null
            }
            PopOneTypeStatus.DIALOG_UPDATE -> {
                viewmodel!!.tv_main.value = "슈라 대규모 업데이트 완료!"

                var content = viewmodel!!.tv_main.value
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )


                binding!!.tvMain.text = spannableString
                viewmodel!!.tv_sub.value =
                    "슈라가 새로운 내용으로 업데이트\n완료되었습니다. 다른 유저에게 보일\n내 프로필을 업데이트해 주세요!"
                viewmodel!!.tv_ok.value = "변경하기"

                binding!!.tvSub.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Blank
                    )
                )
            }
            PopOneTypeStatus.DIALOG_UPDATE_APPROVAL -> {
                viewmodel!!.tv_main.value = "슈라 대규모 업데이트 완료!"

                var content = viewmodel!!.tv_main.value
                val spannableString = SpannableString(content)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
                    0,
                    2,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )


                binding!!.tvMain.text = spannableString
                viewmodel!!.tv_sub.value =
                    "내 프로필 업데이트 수정 신청이 완료되어\n심사중입니다. 심사는 1~3일 소요됩니다.\n완료 즉시 하트 30개가 보상으로 지급됩니다."
                viewmodel!!.tv_ok.value = "다시 수정하기"

                binding!!.tvSub.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.Blank
                    )
                )
            }

        }
    }



    private fun setImage(type: PopOneTypeStatus) {
        when (type) {
            PopOneTypeStatus.DIALOG_MAIN_CAR, PopOneTypeStatus.DIALOG_CAR_REGISTRATION, PopOneTypeStatus.DIALOG_TERMS, PopOneTypeStatus.DIALOG_POST_CATEGPRY, PopOneTypeStatus.DIALOG_REQUEST_PROFILE_WAIT, PopOneTypeStatus.DIALOG_VIP_OVERLAP -> {
                binding!!.ivMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.warning_40x40
                    )
                )
            }
            PopOneTypeStatus.DIALOG_INSURANCE -> {
                binding!!.ivMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.pop_bohoum_56x56
                    )
                )

            }
            PopOneTypeStatus.DIALOG_POST_REPORT -> {
                Glide.with(binding!!.ivMain).load(R.drawable.ic_group_2089).override(200, 200)
                    .centerInside().into(binding!!.ivMain)
            }

            PopOneTypeStatus.DIALOG_POST_BLOCK_OK, PopOneTypeStatus.DIALOG_POST_SUB_COMENT_BLOCK_OK, PopOneTypeStatus.DIALOG_POST_COMMENT_BLOCK_OK -> {
                Glide.with(binding!!.ivMain).load(R.drawable.declaration_40x40).override(200, 200)
                    .centerInside().into(binding!!.ivMain)
            }

            PopOneTypeStatus.DIALOG_UPDATE, PopOneTypeStatus.DIALOG_UPDATE_APPROVAL -> {
                Glide.with(binding!!.ivMain).load(R.drawable.ic_update_40x40).override(200, 200)
                    .centerInside().into(binding!!.ivMain)

            }
            PopOneTypeStatus.DIALOG_VIP_CHECK -> {
                Glide.with(binding!!.ivMain).load(R.drawable.logo_lounge_109x109).override(200, 200)
                    .centerInside().into(binding!!.ivMain)

            }
        }
    }


    private fun onclickListers() {
        binding!!.ok.setOnClickListener {
            event(type, true)
            if (viewmodel!!.type.value != PopOneTypeStatus.DIALOG_UPDATE) {
                dismiss()
            }


        }

    }

}