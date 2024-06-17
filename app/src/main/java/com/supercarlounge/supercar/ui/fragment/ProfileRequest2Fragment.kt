package com.supercarlounge.supercar.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.SwipeHelperCallback
import com.supercarlounge.supercar.adapter.ProfileRequestAdapter
import com.supercarlounge.supercar.databinding.FragmentRequest2Binding
import com.supercarlounge.supercar.dialog.OneButtonSetDialog
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.ui.activity.ProfileActivity
import com.supercarlounge.supercar.viewmodel.ProfileRequestViewModel

class ProfileRequest2Fragment:Fragment() {
    val TAG = "ProfileRequest1Fragment"
    var application:MainApplication? = null
    private var binding: FragmentRequest2Binding? = null
    private lateinit var viewModel: ProfileRequestViewModel
    private lateinit var  adapter : ProfileRequestAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ProfileRequestViewModel::class.java)
        binding = FragmentRequest2Binding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        application = requireContext().applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        val root: View = binding!!.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observes()


        adapter = ProfileRequestAdapter(requireContext(),this, arrayListOf(), viewModel!!) { data, position ->
           var ok =data.pc_ok
            if(ok.equals("M")){
               var oneButtonDialog = OneButtonSetDialog(requireContext(), PopOneTypeStatus.DIALOG_REQUEST_PROFILE_WAIT)
                {type,check->

                }
                oneButtonDialog!!.show(this.childFragmentManager,"")
            }else{
                var i = Intent(requireContext(), ProfileActivity::class.java)
                var mseq =viewModel.my_seq.value!!
                var sub_seq = ""
                if(data.target_seq.toString().equals(mseq)){
                    sub_seq = data.sub_seq.toString()
                }else{
                    sub_seq = data.target_seq.toString()
                }
                i.putExtra("sub_seq",sub_seq)
                startActivity(i)
            }
        }

        binding!!.rvProfileView.adapter = adapter
        binding!!.rvProfileView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        binding!!.rvProfileView.setHasFixedSize(true)
// 다른 곳 터치 시 기존 선택했던 뷰 닫기

        // 리사이클러뷰에 스와이프, 드래그 기능 달기
        val swipeHelperCallback = SwipeHelperCallback(adapter) { position,check->
            viewModel.swipe.value = check
            viewModel.cursorPosition.value =position
        }.apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)    // 1080 / 4 = 270
        }

        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding!!.rvProfileView)
        binding!!.rvProfileView.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding!!.rvProfileView)
            false
        }
    }
    private fun observes() {
        viewModel.toast.observe(viewLifecycleOwner, Observer{
            if(!it.isNullOrEmpty()){
                Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onResume() {
        super.onResume()
        viewModel.getrequest("내가 열람한")
    }
    fun newInstance(): ProfileRequest2Fragment {
        val args = Bundle()
        val fragment = ProfileRequest2Fragment()
        fragment.arguments = args
        return fragment
    }
}