import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogViewProfileBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogFinishViewModel


class ViewProfileDialog(
    context: Context,
    val event: ( Boolean) -> Unit
) : DialogFragment() {

    var binding: DialogViewProfileBinding? = null
    var viewmodel: DialogFinishViewModel? = null


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
            R.layout.dialog_view_profile,
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
        viewmodel = ViewModelProvider(this).get(DialogFinishViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        setclick()

    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            event(true)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(false)
            dismiss()
        }
    }
}