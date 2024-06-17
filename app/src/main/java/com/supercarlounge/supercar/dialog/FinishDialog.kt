import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.DialogFinishBinding
import com.supercarlounge.supercar.ui.activity.WebViewActivity
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogFinishViewModel


class FinishDialog(
    context: Context,
    val event: ( Int) -> Unit
) : DialogFragment() {

    var binding: DialogFinishBinding? = null
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
            R.layout.dialog_finish,
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
        Glide.with(this)
            .load(R.drawable.img_back2)
            .transform(GranularRoundedCorners(50F, 50F, 0f, 0f))
            .into(binding!!.ivImage)
        binding!!.ivImage.setOnClickListener {
//            event(Constans.FinishDialog_BANNER)
//            dismiss()

        }
    }
    fun setclick(){
        binding!!.ok.setOnClickListener {
            event(Constans.FinishDialog_OK)
            dismiss()
        }
        binding!!.cancel.setOnClickListener {
            event(Constans.FinishDialog_CANCEL)
            dismiss()
        }
    }
}