package com.supercarlounge.supercar.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogMiniProfileBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.MiniProfileViewModel


class MiniProfileDialog(
    context: Context,
    var my_seq:String,
    var sub_seq:String,
    var anonymousNickname : String,
    val event: ( Boolean) -> Unit
) : DialogFragment() {
    var TAG = "UpdateDialog"
    var binding: DialogMiniProfileBinding? = null
    var viewmodel: MiniProfileViewModel? = null

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
            R.layout.dialog_mini_profile,
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
        viewmodel = ViewModelProvider(this).get(MiniProfileViewModel::class.java)
        binding!!.viewModel = viewmodel
        dialog!!.setCancelable(true)
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.my_seq.value =my_seq
        viewmodel!!.sub_seq.value =sub_seq
        viewmodel!!.anonymousNickname.value = anonymousNickname
            Log.d("미니프로필1", viewmodel!!.anonymousNickname.value.toString()).toString()
        Log.d("미니프로필2", anonymousNickname.toString())

        viewmodel!!.getUserInformation()



        setText()
        onclickListers()

    }

    private fun setText() {

    }


    private fun onclickListers() {
        binding!!.cancel.setOnClickListener {
            this.dismiss()
        }
        binding!!.tvLike.setOnClickListener {
            event(true)
            dismiss()
        }

    }

}