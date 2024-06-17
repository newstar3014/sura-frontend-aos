package com.supercarlounge.supercar.ui.fragment.pagerconicergef

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.ConciergeAdapter
import com.supercarlounge.supercar.databinding.FragmentConcieAllBinding
import com.supercarlounge.supercar.ui.activity.ConciergeDetailActivity
import com.supercarlounge.supercar.viewmodel.ConciergePageViewModel


class ConciergeAllFragment : Fragment() {

    private var _binding: FragmentConcieAllBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ConciergePageViewModel
    private var  adapter : ConciergeAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(ConciergePageViewModel::class.java)
        _binding = FragmentConcieAllBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        var type =requireArguments().getString("type")
        viewModel.type.value = type
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this


        return root
    }


    override fun onResume() {
        super.onResume()
        Log.d("CALLFragment", viewModel.type.value!!)
        viewModel.getConcierge()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ConciergeAdapter(requireContext(), arrayListOf()) { data, position ->
            val i= Intent(requireActivity(), ConciergeDetailActivity::class.java)
            i.putExtra("data",data)
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }

        binding!!.rvConcierge.adapter = adapter
        binding!!.rvConcierge.layoutManager = LinearLayoutManager(requireActivity())



    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : String): ConciergeAllFragment {
        val args = Bundle()
        args.putString("type",type)
        val fragment = ConciergeAllFragment()
        fragment.arguments = args
        return fragment
    }


}