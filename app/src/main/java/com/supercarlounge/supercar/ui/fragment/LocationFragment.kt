package com.supercarlounge.supercar.ui.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.BannerPagerAdapter
import com.supercarlounge.supercar.adapter.ConciergePagerAdapter
import com.supercarlounge.supercar.adapter.LocationNearListAdapter
import com.supercarlounge.supercar.adapter.LocationSpotAdapter
import com.supercarlounge.supercar.adapter.LocationTotalViewListAdapter
import com.supercarlounge.supercar.adapter.LocationViewListAdapter
import com.supercarlounge.supercar.data.ConciergeCategoryData
import com.supercarlounge.supercar.databinding.FragmentLocationAllBinding
import com.supercarlounge.supercar.databinding.FragmentLocationBinding
import com.supercarlounge.supercar.databinding.TestFragmentVipLoungeBinding
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.dialog.OneButtonSetDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.PopOneTypeStatus
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.viewmodel.LocationFragmentViewModel
import com.supercarlounge.supercar.viewmodel.TestVipLoungeViewModel
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class LocationFragment : Fragment() {
    var application:MainApplication?=null
    private var _binding: FragmentLocationBinding? = null
    private val binding get() = _binding
    var viewModel : LocationFragmentViewModel? = null
    var images: List<Bitmap>? =null
    private var thread : Thread? =null
    private var job : Job? =null

    var fm: FragmentManager? =  null
    var pageradapter : ConciergePagerAdapter? =null
    var pagerbanneradapter1 : BannerPagerAdapter? =null
    var pagerbanneradapter2 : BannerPagerAdapter? =null
    private var viewPager: ViewPager2? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(LocationFragmentViewModel::class.java)
        _binding = FragmentLocationBinding.inflate(inflater, container, false)
        application = requireContext().applicationContext as MainApplication
        viewModel!!.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        val root: View = binding!!.root

        return root
    }

    fun observe(){
        viewModel!!.inviteset.observe(viewLifecycleOwner, Observer {
            if(it){

            }
        })

        viewModel!!.toast.observe(viewLifecycleOwner, Observer {toast->
            if(!toast.isNullOrEmpty()){

                if (toast.equals("아직 VIP 회원이 아닙니다")){
                   var  oneButtonDialog = OneButtonSetDialog(requireContext(), PopOneTypeStatus.DIALOG_VIP_CHECK){ type, check->

                    }
                    oneButtonDialog!!.show(fm!!,"")
                }else{
                    Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel!!.eventSearch.observe(viewLifecycleOwner, Observer {
            if (it){
                val i= Intent(requireActivity(), LocationMainActivity::class.java)
                i.putExtra("search",viewModel?.searchValue?.value)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                hideKeyboard(requireActivity())
            }
        })
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fm = childFragmentManager
        setViewAdapter()
        setNearAdapter()
        setTotalViewAdapter()
        observe()

        binding?.edtSearch?.setOnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel?.eventSearch(true)

                handled = true
            }
            handled
        }

    }
    fun hideKeyboard(activity: Activity){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.window.decorView.applicationWindowToken, 0)
    }
    override fun onResume() {
        super.onResume()
        if(application!!.Check30days(application!!.checkguide7)) {

        }else{
            var d = GuideDialog(requireContext(), GuidePopType.LOCATION) {
                application!!.save_guide(GuidePopType.LOCATION)
            }
            d.show(fm!!, "")
        }
        val metrisc: DisplayMetrics = resources.displayMetrics
        viewModel?.widthPixels?.value = metrisc.widthPixels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewModel?.isPermission?.value =
                ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        }else{
            viewModel?.isPermission?.value =
                ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }

        viewModel?.getViewList(application!!)
        viewModel?.getNearList(true,application!!)
        viewModel?.getTotalViewList(application!!)
    }

    override fun onPause() {
        super.onPause()

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
    fun newInstance(): LocationFragment {
        val args = Bundle()

        val fragment = LocationFragment()
        fragment.arguments = args
        return fragment
    }
    fun setTotalViewAdapter(){

        var  spotadapter = LocationTotalViewListAdapter(requireContext(), arrayListOf(),viewModel!!) { data, index, ->
            val i= Intent(requireActivity(), LocationSpotDetailActivity::class.java)
            i.putExtra("spot_seq", data.spot_seq)
            startActivity(i)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        binding!!.rvTotalView.adapter = spotadapter
        binding!!.rvTotalView.layoutManager  =GridLayoutManager(requireContext(), 5, GridLayoutManager.HORIZONTAL, false)
    }
    fun setViewAdapter(){

        var  spotadapter = LocationViewListAdapter(requireContext(), arrayListOf(),viewModel!!) { data, index, ->

                val i= Intent(requireActivity(), LocationSpotDetailActivity::class.java)
                i.putExtra("spot_seq", data.spot_seq)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
        binding!!.rvViewList.adapter = spotadapter
        binding!!.rvViewList.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

    fun setNearAdapter(){

        var  spotadapter = LocationNearListAdapter(requireContext(), arrayListOf(),viewModel!!) { data, index, ->
            if (data.spot_seq.isNullOrEmpty()){
                val i= Intent(requireActivity(), LocationMainActivity::class.java)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }else{
                val i= Intent(requireActivity(), LocationSpotDetailActivity::class.java)
                i.putExtra("spot_seq", data.spot_seq)
                startActivity(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }

        }
        binding!!.rvNear.adapter = spotadapter
        binding!!.rvNear.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

}