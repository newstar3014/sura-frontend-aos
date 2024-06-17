package com.supercarlounge.supercar.ui.fragment.PagerInformationUse

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
import com.supercarlounge.supercar.databinding.FragmentIUAboutShuraBinding
import com.supercarlounge.supercar.ui.activity.ConciergeDetailActivity
import com.supercarlounge.supercar.viewmodel.ConciergePageViewModel
import com.supercarlounge.supercar.viewmodel.IUViewModel


class IUAboutShuraFragment : Fragment() {

    private var _binding: FragmentIUAboutShuraBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: IUViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(IUViewModel::class.java)
        _binding = FragmentIUAboutShuraBinding.inflate(inflater, container, false)
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
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : String): IUAboutShuraFragment {
        val args = Bundle()
        args.putString("type",type)
        val fragment = IUAboutShuraFragment()
        fragment.arguments = args
        return fragment
    }


}