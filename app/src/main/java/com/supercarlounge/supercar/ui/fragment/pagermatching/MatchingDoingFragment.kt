package com.supercarlounge.supercar.ui.fragment.pagermatching

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.SwipeHelperCallback2
import com.supercarlounge.supercar.adapter.ProfileMatchingAdapter
import com.supercarlounge.supercar.data.MatchingMiniData
import com.supercarlounge.supercar.databinding.FragementMatchingDoingBinding
import com.supercarlounge.supercar.ui.activity.ProfileActivity
import com.supercarlounge.supercar.viewmodel.MatchingViewModel

class MatchingDoingFragment : Fragment() {
    var application: MainApplication?=null
    private var _binding: FragementMatchingDoingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    private var  adapter : ProfileMatchingAdapter? = null
    private var viewModel : MatchingViewModel? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       viewModel = ViewModelProvider(this).get(MatchingViewModel::class.java)
        _binding = FragementMatchingDoingBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        viewModel!!.type.value ="보낸 호감"
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //어댑터 연결하기

        adapter = ProfileMatchingAdapter(requireContext(), viewLifecycleOwner,arrayListOf<MatchingMiniData>(),viewModel!!) { data, position ->
            var u_seq = 0
            if (application?.userData?.u_seq ==  data.sub_seq){
                u_seq = data.target_seq
            }else{
                u_seq = data.sub_seq
            }
            val i= Intent(requireActivity(), ProfileActivity::class.java)
            i.putExtra("sub_seq",u_seq.toString())
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)

        }

        binding!!.rvMatchingCrush.adapter = adapter
        binding!!.rvMatchingCrush.layoutManager =  LinearLayoutManager(requireContext())
        binding!!.rvMatchingCrush.setHasFixedSize(true)
        val swipeHelperCallback = SwipeHelperCallback2(adapter!!) { position,check->
            viewModel!!.swipe.value = check
            viewModel!!.cursorPosition.value =position
        }.apply {
            // 스와이프한 뒤 고정시킬 위치 지정
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)    // 1080 / 4 = 270
        }

        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(binding!!.rvMatchingCrush)
        binding!!.rvMatchingCrush.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(binding!!.rvMatchingCrush)
            false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel!!.GetMatchigmini()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(): MatchingDoingFragment {
        val args = Bundle()

        val fragment = MatchingDoingFragment()
        fragment.arguments = args
        return fragment
    }
}