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
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogSecessionBinding
import com.supercarlounge.supercar.viewmodel.SecessionViewModel


class SecessionDialog(
    context: Context,
    val event: ( Boolean,Boolean,String) -> Unit
) : DialogFragment() {

    var binding: DialogSecessionBinding? = null
    var viewmodel: SecessionViewModel? = null


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
            R.layout.dialog_secession,
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
        viewmodel = ViewModelProvider(this).get(SecessionViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        setclick()
        binding!!.edtSecession.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, count: Int) {
                viewmodel!!.reason.value = p0!!.toString()

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            if (viewmodel?.reason?.value.isNullOrEmpty()){
                Toast.makeText(context, "탈퇴신청 사유를 적어주세요", Toast.LENGTH_SHORT).show()
            }else{
                event(true,viewmodel?.checkSecession?.value!!,viewmodel?.reason?.value!!)
                dismiss()
            }

        }
        binding!!.cancel.setOnClickListener {
            event(false,viewmodel?.checkSecession?.value!!,viewmodel?.reason?.value!!)

            dismiss()
        }

        binding!!.llCheked.setOnClickListener {
            if (viewmodel!!.checkSecession.value!!){
                viewmodel!!.checkSecession.value = false
            }else{
                viewmodel!!.checkSecession.value = true
            }
        }
    }
}