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
import android.view.*
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLoadingViewModel
import java.util.*


class ComprehensiveInsuranceDialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
    var type: Int
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogComprehensiveInsuranceBinding? = null
    var viewmodel: DialogLoadingViewModel? = null
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
            R.layout.dialog_comprehensive_insurance,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()


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
       var xx = binding?.llDialog2?.width!!
        var yy = binding?.llDialog2?.height!!
        x = xx.times(1.15).toInt()
        y = yy.times(1.15).toInt()
//                x = (displayPW * 0.6f).toInt()
//                y = (displayPH * 0.5).toInt()
        var params = LinearLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        binding!!.llDialog1.layoutParams = params
    }


    override fun dismiss() {
        super.dismiss()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogLoadingViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        viewmodel!!.viewtype.value = type

        setText()
        onclickListers()
        binding!!.llDialog2.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                setDialogWindowSize()
//                    model.startTopViewPosition.value =con.getViewCoordinate(com.example.supercarx.R.id.pager_banner_2)
                binding!!.llDialog1.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

    }

    @SuppressLint("ResourceType")
    private fun setText() {
        var content = binding!!.tvColor.text
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor(getString(R.color.main_color))),
            18,
            38,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvColor.text = spannableString
    }


    private fun onclickListers() {
        binding!!.ok.setOnClickListener {
            dismiss()
        }

    }

}