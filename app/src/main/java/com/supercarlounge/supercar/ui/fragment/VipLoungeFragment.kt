package com.supercarlounge.supercar.ui.fragment


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.databinding.FragmentVipLoungeBinding
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.viewmodel.VipLoungeViewModel
import kotlinx.coroutines.*


class VipLoungeFragment : Fragment() {
    var application:MainApplication?=null
    private var _binding: FragmentVipLoungeBinding? = null
    private val binding get() = _binding
    var viewModel :VipLoungeViewModel? = null
    var images: List<Bitmap>? =null
    private var thread : Thread? =null
    private var job : Job? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(VipLoungeViewModel::class.java)
        _binding = FragmentVipLoungeBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        viewModel!!.name.value = application!!.userData!!.u_name
        viewModel!!.image.value =  application!!.userData!!.u_image
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root
        viewModel!!.text.value = "vip라운지"

        return root
    }

    fun observe(){
        viewModel!!.ondial.observe(viewLifecycleOwner, Observer {
            if(it){

                    val number: Uri = Uri.parse("tel:"+"01020675724")
                    val callIntent = Intent(Intent.ACTION_DIAL, number)
                    startActivity(callIntent)


            }
        })
        viewModel!!.viplounge_gauge.observe(viewLifecycleOwner, Observer { percent->
            Log.d("게이지1", percent.toString())
            setVIPAnimation(percent)
        })
        viewModel!!.toast.observe(viewLifecycleOwner, Observer {toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel!!.viplounge_Check.observe(viewLifecycleOwner, Observer { percent->
            Log.d("게이지2", percent.toString())
            setVIPAnimation(percent)
        } )
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setVIPAnimation(percent :Int){
        job = CoroutineScope(Dispatchers.Main).launch {
            var animation = AnimationDrawable()
            var percents: ArrayList<Int> = arrayListOf()
            var duration = 30
            animation.stop()

            for (i in 0..100){
                var du = 30
                when(i) {
                    20 -> duration = 20
                    40 -> duration = 10
                }
                percents?.add(i)
                val name = "vip_$i"
                val drawableResourceId = this@VipLoungeFragment.resources.getIdentifier(name, "drawable", requireActivity().packageName)
                animation.addFrame(resources.getDrawable(drawableResourceId), duration)

            }
            for (i in 99 downTo percent){
                percents?.add(i)
                val name = "vip_$i"
                val drawableResourceId = this@VipLoungeFragment.resources.getIdentifier(name, "drawable", requireActivity().packageName)
                animation.addFrame(resources.getDrawable(drawableResourceId), duration)
            }


            binding?.rpm?.setBackgroundDrawable(animation);
            animation.isOneShot = true
            animation.start()

            Log.d("ㅎㅇ", "setVIPanimation: ")
        }
//        delay(2000L) // delay a bit
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnclickListeners()
        observe()
    }

    private fun setOnclickListeners() {

        binding!!.rpm.setOnClickListener {

        }


    }

    override fun onResume() {
        super.onResume()
        Log.d("생명주기", "onResume: ")
        viewModel!!.viewStack.value =1

        viewModel!!.CheckVipType()
        if(application!!.Check30days(application!!.checkguide3)) {

        }else{
            var d = GuideDialog(requireContext(), GuidePopType.GUIDE_VIP_LOUNGE) {
                application!!.save_guide(GuidePopType.GUIDE_VIP_LOUNGE)
            }
            d.show(childFragmentManager, "")
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("생명주기", "onPause: ")
        var animation = AnimationDrawable()
        val name = "vip_0"
        val drawableResourceId = this.resources.getIdentifier(name, "drawable", requireActivity().packageName)
        animation.addFrame(resources.getDrawable(drawableResourceId), 10)
        binding?.rpm?.setBackgroundDrawable(animation);
        job?.cancel() // cancels the job

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("생명주기", "onAttach: ")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden) {
            return
        }
        Log.d("생명주기", hidden.toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun newInstance(): VipLoungeFragment {
        val args = Bundle()

        val fragment = VipLoungeFragment()
        fragment.arguments = args
        return fragment
    }
    fun reset(){
        if(viewModel!!.viewStack.value ==2) {
            viewModel!!.viewStack.value = 1
        }
    }

}