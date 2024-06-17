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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.LikeListAdapter
import com.supercarlounge.supercar.data.LikeListData
import com.supercarlounge.supercar.databinding.DialogLikeListBinding
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.DialogLikeListViewModel
import java.util.*


class LikeListDialog(
    context: Context,
//    val listener: View.OnClickListener,
//    val mDismisslistener: DialogListener,
    var type: Int,
    var my_u_seq: Int,
    var u_seq: Int,
    var b_seq: Int,
    val event: (LikeListData,Boolean) -> Unit
) : DialogFragment() {

    var TAG = "UpdateDialog"
    var binding: DialogLikeListBinding? = null
    var viewmodel: DialogLikeListViewModel? = null
    var calenderList: ArrayList<String>? = arrayListOf()
//    var mListener: DialogListener? = null
    private var  adapter : LikeListAdapter? = null

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
            R.layout.dialog_like_list,
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
            y = (displayPH * 0.4f).toInt()

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
        viewmodel = ViewModelProvider(this).get(DialogLikeListViewModel::class.java)
        binding!!.viewModel = viewmodel
        dialog!!.setCancelable(true)
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        viewmodel!!.viewtype.value =type
//        mListener = mDismisslistener

        var list : ArrayList<LikeListData> = arrayListOf()
        adapter = LikeListAdapter(requireContext(), list, viewmodel!!) { data, position ->

                    event(data,true)
                    dismiss()

        }
        binding!!.rvLikeList.adapter = adapter
        binding!!.rvLikeList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewmodel!!.checked.observe(viewLifecycleOwner, Observer {
            Log.d("test1", it.toString())
        })

        viewmodel!!.my_u_seq.value = my_u_seq.toString()
        viewmodel!!.u_seq.value = u_seq.toString()
        viewmodel!!.b_seq.value = b_seq.toString()


        viewmodel!!.getLikeList()


        setText()
        onclickListers()
        observes()

    }

    private fun setText() {

    }

    private fun observes() {
        viewmodel!!.mLikeList.observe(this, Observer {
            if (it != null) {
                adapter?.setdata(it)
            }

        })
    }
    private fun onclickListers() {
        binding!!.cancel.setOnClickListener {
            this.dismiss()
        }

    }

}