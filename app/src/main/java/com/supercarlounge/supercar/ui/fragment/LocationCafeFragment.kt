package com.supercarlounge.supercar.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.GlideImageGetter
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.adapter.LocationListAdapter
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.FragmentDriveAwayNewBinding
import com.supercarlounge.supercar.databinding.FragmentLocationAllBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.DriveAwayListActivity
import com.supercarlounge.supercar.ui.activity.DriveDetailActivity
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.activity.LocationSpotDetailActivity
import com.supercarlounge.supercar.viewmodel.DriveAwayNewViewModel
import com.supercarlounge.supercar.viewmodel.LocationAllViewModel
import com.supercarlounge.supercarnative.ui.fragment.HomeFragment2


class LocationCafeFragment() : Fragment() {
    val TAG = "LocationAllFragment"
    private var _binding: FragmentLocationAllBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: LocationAllViewModel
    private var adapter: LocationListAdapter? = null

    var application: MainApplication? = null

    private var quickUserDialog: QuickUserDialog? = null
    private var fm: FragmentManager? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(LocationAllViewModel::class.java)
        _binding = FragmentLocationAllBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        application = requireContext().applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        fm = childFragmentManager
        val root: View = binding!!.root

        return root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()


        Log.d(TAG, "ONRESUME")
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observes()
        setAdapter()
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

        viewModel.getSpotList(true,"카페")

//        adapter = DriveAwayAdapter(
//            requireActivity(),
//            arrayListOf(),
//            viewModel.rvItemType.value!!
//        ) { data, position ->
//            if (application?.quick_view.equals("퀵유저")) {
//                ShowQuickUserDialog()
//            } else {
//                val i = Intent(requireActivity(), DriveDetailActivity::class.java)
//                i.putExtra("data", data)
//                i.putExtra("type", "신규")
//                i.putExtra("totalCount", viewModel.totalCount.value)
//                i.putExtra("page", viewModel.page.value)
//                i.putParcelableArrayListExtra("allList", adapter?.data)
//                i.putExtra("driveType", "notvip")
//                startActivity(i)
//                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
//            }
//
//        }
//        adapter!!.setHasStableIds(true)





    }

    private fun setAdapter() {
        adapter = LocationListAdapter(
            requireContext(),
            arrayListOf(),viewModel) { data, position ->
//            if (application!!.quick_view.equals("퀵유저")) {
//                ShowQuickUserDialog()
//            } else {

                var i = Intent(requireActivity(), LocationSpotDetailActivity::class.java)
                i.putExtra("spot_seq", data.spot_seq)
                startActivity(i)
                requireActivity().overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
//            }

        }
        binding!!.rvLocation.adapter = adapter
        binding!!.rvLocation.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    private fun observes() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DriveAwayNewFragment", "onDestroyView: ")
    }

    fun newInstance(type: Int): LocationCafeFragment {
        val args = Bundle()
//        viewModel.rvItemType.value = type
        val fragment = LocationCafeFragment()
        fragment.arguments = args
        return fragment
    }


    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(requireActivity(), JoinActivity::class.java)
                    i.putExtra("qickview", "퀵유저")
                    i.putExtra("u_name", application?.userBasicData?.u_name)
                    i.putExtra("u_gender", application?.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application?.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application?.userBasicData?.u_phone)
                    Log.d("아이템3", application?.userBasicData.toString())
                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(fm!!, "")
    }


}