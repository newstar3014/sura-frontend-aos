import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogHeartInsertBinding
import com.supercarlounge.supercar.databinding.DialogSecessionBinding
import com.supercarlounge.supercar.viewmodel.SecessionViewModel
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.HeartInsertViewModel


class HeartInsertDialog(
    context: Context,
    var my_seq:String,
    val event: ( Boolean,Boolean,String) -> Unit
) : DialogFragment() {

    var binding: DialogHeartInsertBinding? = null
    var viewmodel: HeartInsertViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun getTheme() = R.style.RoundedCornersDialog
    override fun onResume() {
        super.onResume()
        setDialogWindowSize()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_heart_insert,
            null,
            false
        );
        return binding!!.root
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {
        dialog?.getWindow()?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        val metrisc: DisplayMetrics = resources.displayMetrics
//        val displayPW = metrisc.widthPixels
//        val displayPH = metrisc.heightPixels
//        val window = dialog!!.window
//        window!!.setLayout(displayPW, displayPH)
//        val x = (displayPW * 0.6f).toInt()
//        val y = (displayPH * 0.4f).toInt()
//        var params = FrameLayout.LayoutParams(x, y)
//        params.gravity = Gravity.CENTER
//        binding!!.llDialog.layoutParams = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(HeartInsertViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner

        viewmodel!!.my_seq.value = my_seq
        setclick()

        viewmodel!!.apiCall.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                dismiss()
                when (it) {
                    "등록완료" -> {
                        viewmodel!!.toast.value = "하트 쿠폰 등록 완료!"
                    }

                    "불일치" -> {
                        viewmodel!!.toast.value = "일치하는 하트 쿠폰이 없습니다."
                    }
                    "쿠폰등록실패" -> {
                        viewmodel!!.toast.value = "하트 쿠폰 등록에 실패했습니다."
                    }
                }


            }

        })
                viewmodel!!. toast.observe(viewLifecycleOwner, Observer {
                    if (it.isNotEmpty()){
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

                    }
                })

    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            if (viewmodel?.reason?.value.isNullOrEmpty()){
                Toast.makeText(context, "쿠폰 번호를 입력해 주세요.", Toast.LENGTH_SHORT).show()


            }  else{
                viewmodel?.SetCoupon()

            }

        }
        binding!!.cancel.setOnClickListener {
            event(false,viewmodel?.checkSecession?.value!!,viewmodel?.reason?.value!!)

            dismiss()
        }

    }
}