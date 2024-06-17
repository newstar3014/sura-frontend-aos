package com.supercarlounge.supercar.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogPrvminiProfileBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogPrvMiniProfileViewModel


class PrvMiniProfileDialog(
    context: Context,
    val event: ( Boolean) -> Unit
) : DialogFragment() {
    var TAG = "UpdateDialog"
    var binding: DialogPrvminiProfileBinding? = null
    var viewmodel: DialogPrvMiniProfileViewModel? = null

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
            R.layout.dialog_prvmini_profile,
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

            x = (displayPW * 0.6f).toInt()
            y = (displayPH * 0.50f).toInt()

        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.CENTER
        dialog!!.window!!.setLayout(x, y)
    }


    override fun dismiss() {
        super.dismiss()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(DialogPrvMiniProfileViewModel::class.java)
        binding!!.viewModel = viewmodel
        dialog!!.setCancelable(true)
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        onclickListers()

    }

    private fun setText() {

    }


    private fun onclickListers() {
        binding!!.rlMain.setOnClickListener {
            this.dismiss()
        }


    }

}