package com.supercarlounge.supercar.dialog


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.BottomDialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.viewmodel.LoginViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog(
//    val itemClick: (Int) -> Unit,
                        context: Context,
//    val listener: View.OnClickListener,
    val mDismisslistener: BottomDialogEvent,
    var type: Int,
      var allchecked: Boolean,
                        var checked1: Boolean,
                        var checked2: Boolean,
                        var checked3: Boolean,
                        var checked4: Boolean,
                        var checked5: Boolean,
) : BottomSheetDialogFragment() {
    var mListener: BottomDialogEvent? = null
    var viewmodel :LoginViewModel?= null
    var binding: com.supercarlounge.supercar.databinding.DialogBottomSheetBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_bottom_sheet,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
//        setDialogWindowSize()
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
        y = (displayPH * 0.5f).toInt()

        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.BOTTOM
        dialog!!.window!!.setLayout(x, y)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        mListener =mDismisslistener
        viewmodel!!.allchecked.value = allchecked
        viewmodel!!.checked1.value = checked1
        viewmodel!!.checked2.value = checked2
        viewmodel!!.checked3.value = checked3
        viewmodel!!.checked4.value = checked4
        viewmodel!!.checked5.value = checked5
        val termsUrl = "https://supercarlounge.com/page1.php"
        val privacyUrl = "https://supercarlounge.com/page3.php"
        val locationUrl = "https://supercarlounge.com/page2.php"
        val marketingUrl = "https://supercarlounge.com/page3.php"
        val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
        val privacyIntent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
        val locationIntent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl))
        val marketingUrlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(marketingUrl))
        binding!!.ivTerms.setOnClickListener {
            startActivity(termsIntent)
        }
        binding!!.tvTerms.setOnClickListener {
//            startActivity(termsIntent)
            viewmodel!!.checked1.value = !viewmodel!!.checked1.value!!
//            viewmodel!!.setCheck1(viewmodel!!.checked2.value!!)
        }

        binding!!.ivPrivacy.setOnClickListener {
            startActivity(privacyIntent)
        }
        binding!!.ivMarketing.setOnClickListener {
            startActivity(marketingUrlIntent)
        }


        binding!!.tvPrivacy.setOnClickListener {
//            startActivity(privacyIntent)
            viewmodel!!.checked2.value = !viewmodel!!.checked2.value!!

        }
        binding!!.ivLocationConsent.setOnClickListener {
            startActivity(locationIntent)
        }
        binding!!.tvLocationConsent.setOnClickListener {
//            startActivity(locationIntent)
            viewmodel!!.checked3.value = !viewmodel!!.checked3.value!!


        }
        binding!!.tvSecession.setOnClickListener {
//            startActivity(marketingUrlIntent)
            viewmodel!!.checked4.value = !viewmodel!!.checked4.value!!


        }
        binding!!.tvSecessionSub.setOnClickListener {
//            startActivity(marketingUrlIntent)
            viewmodel!!.checked4.value = !viewmodel!!.checked4.value!!


        }

        binding!!.tvMarketing.setOnClickListener {
//            startActivity(marketingUrlIntent)
            viewmodel!!.checked5.value = !viewmodel!!.checked5.value!!


        }
        binding!!.tvMarketingSub.setOnClickListener {
//            startActivity(marketingUrlIntent)
            viewmodel!!.checked5.value = !viewmodel!!.checked5.value!!


        }

        binding!!.tvBottomOk.setOnClickListener {
           this.dismiss()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        viewmodel?.allchecked?.value?.let {

        }
        mDismisslistener.okEvent( viewmodel?.allchecked?.value!!,viewmodel?.checked1?.value!!,viewmodel?.checked2?.value!!,viewmodel?.checked3?.value!!,viewmodel?.checked4?.value!!,viewmodel?.checked5?.value!!)
    }
}