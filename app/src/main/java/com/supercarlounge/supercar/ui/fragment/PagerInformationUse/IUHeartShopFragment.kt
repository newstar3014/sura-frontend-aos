package com.supercarlounge.supercar.ui.fragment.PagerInformationUse

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import com.supercarlounge.supercar.databinding.FragmentIUAboutShuraBinding
import com.supercarlounge.supercar.databinding.FragmentIUDriveBoardBinding
import com.supercarlounge.supercar.databinding.FragmentIUDrivePaasBinding
import com.supercarlounge.supercar.databinding.FragmentIUHeartShopBinding
import com.supercarlounge.supercar.dialog.DrivePassPayDialog
import com.supercarlounge.supercar.enumset.DrivePassDialogStatus
import com.supercarlounge.supercar.viewmodel.IUViewModel


class IUHeartShopFragment : Fragment() {

    private var _binding: FragmentIUHeartShopBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: IUViewModel
    private var application: MainApplication? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(IUViewModel::class.java)
        _binding = FragmentIUHeartShopBinding.inflate(inflater, container, false)
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
        application= context?.applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData?.u_seq.toString()
        binding?.llKakao?.setOnClickListener {

            viewModel.GetInviteCode("카카오")

        }

        viewModel!!.inviteset.observe(viewLifecycleOwner, Observer{
            if(!it.isNullOrEmpty()){
                if (it.equals("복사")){
                    val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("CODE",viewModel.invite_code.value.toString())
                    clipboardManager!!.setPrimaryClip(clipData)
                    Toast.makeText(context,"초대코드가 복사되었습니다.", Toast.LENGTH_SHORT).show()
                }else{
                    application!!.onDynamicLinkClick(requireContext(),viewModel.invite_code.value.toString())
                }

            }
        })
        binding?.myCode?.setOnClickListener {
            viewModel.GetInviteCode("복사")
        }
//        binding!!.llDrivepass30.setOnClickListener {
//            var drivePassPayDialog = DrivePassPayDialog(requireContext(), DrivePassDialogStatus.DAY30,"",0) { type, isok->
//                if(isok){
//                    viewModel.DrivePassPayment1(DrivePassDialogStatus.DAY30,childFragmentManager,requireContext())
//                }
//
//            }
//            drivePassPayDialog!!.show(childFragmentManager,"")

//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : String): IUHeartShopFragment {
        val args = Bundle()
        args.putString("type",type)
        val fragment = IUHeartShopFragment()
        fragment.arguments = args
        return fragment
    }


}