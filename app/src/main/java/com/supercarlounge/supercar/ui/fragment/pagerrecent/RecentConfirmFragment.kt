package com.supercarlounge.supercar.ui.fragment.pagermatching

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.RecentDriveAdapter
import com.supercarlounge.supercar.data.RecentDriveData
import com.supercarlounge.supercar.data.RecentDriveListData
import com.supercarlounge.supercar.databinding.FragementRecentConfirmBinding
import com.supercarlounge.supercar.dialog.DrivePassDialog
import com.supercarlounge.supercar.dialog.MiniProfileDialog
import com.supercarlounge.supercar.ui.activity.HeartsShopActivity
import com.supercarlounge.supercar.ui.activity.LikeSendActivity
import com.supercarlounge.supercar.ui.activity.ProfileActivity
import com.supercarlounge.supercar.viewmodel.RecentDriveViewModel

class RecentConfirmFragment : Fragment() {
    var application: MainApplication?=null
    private var _binding: FragementRecentConfirmBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    private var  adapter : RecentDriveAdapter? = null
    private var viewModel : RecentDriveViewModel? =null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       viewModel = ViewModelProvider(this).get(RecentDriveViewModel::class.java)
        _binding = FragementRecentConfirmBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        viewModel!!.type.value ="호감"
        var b_seq = requireArguments().getString("b_seq")
        var passCheck = requireArguments().getBoolean("passCheck")
        viewModel!!.passCheck.value = passCheck
        viewModel!!.b_seq.value = b_seq
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //어댑터 연결하기

        adapter = RecentDriveAdapter(requireContext(), viewLifecycleOwner,
            arrayListOf(),viewModel!!) { data, position ->
            viewModel!!.selectrecentlist.value = data
            viewModel!!.GetMatchingData(viewModel!!.my_seq.value!!,data.u_seq.toString())
            viewModel?.myPageViewChk("2",data.pc_seq.toString())

        }

        binding!!.rvMatchingCrush.adapter = adapter
        binding!!.rvMatchingCrush.layoutManager =  LinearLayoutManager(requireContext())
        binding!!.rvMatchingCrush.setHasFixedSize(true)
        viewModel?.recentDrivelist?.observe(viewLifecycleOwner, Observer {
            onvisible()
        })
        binding?.llBlur?.setOnClickListener {
            var drivepassdialog = DrivePassDialog(requireContext()) { isok->
                if(isok){
                    val i= Intent(requireActivity(), HeartsShopActivity::class.java)
                    startActivity(i)
                    requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                }

            }
            drivepassdialog!!.show(childFragmentManager,"")
        }
        viewModel!!.miniprofilestatus.observe(viewLifecycleOwner, Observer {
            if (viewModel!!.selectrecentlist.value != null) {
                var data = viewModel!!.selectrecentlist.value!!
                if (it == false) {
                    showminiprofile(data)
                } else {
                    var i = Intent(context, ProfileActivity::class.java)
                    i.putExtra("sub_seq", data.u_seq)
                    startActivity(i)
                    requireActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout)
                }
            }
        })
    }
    private fun onvisible(){


        if (viewModel?.passCheck?.value == true){
            if (viewModel?.recentDrivelist?.value?.size != 0){
                binding?.rvMatchingCrush?.visibility= View.VISIBLE
                binding?.llNolist?.visibility= View.GONE
            }else{
                binding?.rvMatchingCrush?.visibility= View.GONE
                binding?.llNolist?.visibility= View.VISIBLE
            }
            binding?.llBlur?.visibility = View.GONE

        }else{
            binding?.llBlur?.visibility = View.VISIBLE
            binding?.llNolist?.visibility= View.GONE
            binding?.rvMatchingCrush?.visibility= View.GONE
        }

    }
    override fun onResume() {
        super.onResume()
        viewModel!!.GetRecentList()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(rdata: RecentDriveData?,passCheck:Boolean): RecentConfirmFragment {
        val args = Bundle()
        args.putString("b_seq",rdata?.b_seq.toString())
        args.putBoolean("passCheck",passCheck)
        val fragment = RecentConfirmFragment()
        fragment.arguments = args
        return fragment
    }
    fun showminiprofile(data: RecentDriveListData){
        var miniprodialog = MiniProfileDialog(requireContext(),viewModel!!.my_seq.value!!,data.u_seq.toString(),data.u_nickname){ isprofile->
            var i  = Intent(requireContext(), LikeSendActivity::class.java)
            i.putExtra("sub_seq",data.u_seq.toString())
            i.putExtra("nick",data.u_nickname)
            startActivity(i)
        }
        Log.d("받은 호감 기록", data.bl_seq.toString())
        miniprodialog.show(childFragmentManager,"")
    }
}