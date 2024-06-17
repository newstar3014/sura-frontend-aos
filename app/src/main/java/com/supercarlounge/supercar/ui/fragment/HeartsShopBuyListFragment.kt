package com.supercarlounge.supercar.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.HeartLogAdapter
import com.supercarlounge.supercar.databinding.FragmentHeartsShopBuyInviteBinding
import com.supercarlounge.supercar.databinding.FragmentHeartsShopBuyListBinding
import com.supercarlounge.supercar.dialog.InviteDialog
import com.supercarlounge.supercar.viewmodel.HeartsShopViewModel

class HeartsShopBuyListFragment : Fragment() {

    private var _binding: FragmentHeartsShopBuyInviteBinding? = null
    var application:MainApplication?=null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private val viewModel: HeartsShopViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )
    override fun onResume() {
        super.onResume()

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       //viewModel = ViewModelProvider(this).get(HeartsShopViewModel::class.java)
        _binding = FragmentHeartsShopBuyInviteBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value= application!!.userData!!.u_seq.toString()
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //어댑터 연결하기

        binding?.llKakao?.setOnClickListener {

              viewModel.GetInviteCode("카카오")

        }

        viewModel!!.inviteset.observe(viewLifecycleOwner, Observer{
            if(!it.isNullOrEmpty()){
                if (it.equals("복사")){
                        val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipData = ClipData.newPlainText("CODE",viewModel.invite_code.value.toString())
                        clipboardManager!!.setPrimaryClip(clipData)
                    Toast.makeText(context,"초대코드가 복사되었습니다.",Toast.LENGTH_SHORT).show()
                }else{
                    application!!.onDynamicLinkClick(requireContext(),viewModel.invite_code.value.toString())
                }

            }
        })
        binding?.myCode?.setOnClickListener {
            viewModel.GetInviteCode("복사")
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(): HeartsShopBuyListFragment {
        val args = Bundle()

        val fragment = HeartsShopBuyListFragment()
        fragment.arguments = args
        return fragment
    }
}