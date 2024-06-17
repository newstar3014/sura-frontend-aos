package com.supercarlounge.supercar.dialog

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.CompanyPhoneAdapter
import com.supercarlounge.supercar.data.CompanyPhoneData
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLoadingViewModel
import java.util.*
import kotlin.collections.ArrayList


class CompanyPhoneDialog(
    context: Context,
    val phone_list:ArrayList<CompanyPhoneData>
) : DialogFragment() {

    var TAG = "CompanyPhoneDialog"
    var binding: DialogCompanyPhoneBinding? = null
    var viewmodel: DialogLoadingViewModel? = null
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
            R.layout.dialog_company_phone,
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

                x = (displayPW * 0.4f).toInt()
                y = (displayPH * 0.4).toInt()
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
        viewmodel = ViewModelProvider(this).get(DialogLoadingViewModel::class.java)
        binding!!.setLifecycleOwner(this)


        setadpater()


    }



    private fun setadpater() {
//        var list = arrayListOf<CompanyPhoneData>()
//        list.add(CompanyPhoneData("dd","0554443333"))
//        list.add(CompanyPhoneData("cc","0544443333"))
//        list.add(CompanyPhoneData("ff","0534443333"))
//        list.add(CompanyPhoneData("aa","0524443333"))
//        list.add(CompanyPhoneData("ze","0514443333"))

        binding!!.rvPhone.adapter = CompanyPhoneAdapter(requireContext(),phone_list){ data,index ->
            val number: Uri = Uri.parse("tel:"+data.phone_num)
            val callIntent = Intent(Intent.ACTION_DIAL, number)
            startActivity(callIntent)

        }
        binding!!.rvPhone.layoutManager = LinearLayoutManager(requireContext())
        binding!!.btnClose.setOnClickListener {
            dismiss()
        }
    }

}