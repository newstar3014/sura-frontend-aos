package com.supercarlounge.supercar.ui.fragment.PagerInformationUse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.databinding.FragmentIUAboutShuraBinding
import com.supercarlounge.supercar.databinding.FragmentIUDriveBoardBinding
import com.supercarlounge.supercar.databinding.FragmentIUDrivePaasBinding
import com.supercarlounge.supercar.viewmodel.IUViewModel


class IUDrivePassFragment : Fragment() {

    private var _binding: FragmentIUDrivePaasBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: IUViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(IUViewModel::class.java)
        _binding = FragmentIUDrivePaasBinding.inflate(inflater, container, false)
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
    fun newInstance(type : String): IUDrivePassFragment {
        val args = Bundle()
        args.putString("type",type)
        val fragment = IUDrivePassFragment()
        fragment.arguments = args
        return fragment
    }


}