package com.supercarlounge.supercar.dialog


import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.supercarlounge.supercar.BottomDialogEvent
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.RegionAdapter
import com.supercarlounge.supercar.adapter.RegionDetailAdapter
import com.supercarlounge.supercar.customview.CustomGridView
import com.supercarlounge.supercar.ui.activity.LocationSpotDetailActivity
import com.supercarlounge.supercar.viewmodel.dialogviewmodel.BottomSheetRegionDailogViewModel


class BottomSheetRegionDialog(
    context: Context,
    var regiontext:String,
    var regionIndex: Int,
    var regionSelectDetailIndex: Int?,
    val event: (Boolean, Int, String,Int) -> Unit
) : BottomSheetDialogFragment() {
    var mListener: BottomDialogEvent? = null
    var viewmodel: BottomSheetRegionDailogViewModel? = null
    var binding: com.supercarlounge.supercar.databinding.DialogBottomSheetRegion2Binding? = null
    var locationPermissionDialog: LocationPermissionDialog? = null

    var regionAdapter: RegionAdapter? =null
    var regionDetailAdapter: RegionDetailAdapter? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_bottom_sheet_region2,
            null,
            false
        );
        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
//        setDialogWindowSize()
    }

    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
        val window = dialog!!.window
        window!!.setLayout(displayPW, displayPH)
        var x = 0
        var y = 0

        x = (displayPW * 0.6f).toInt()
        y = (displayPH * 0.5f).toInt()

        var params = FrameLayout.LayoutParams(x, y)
        params.gravity = Gravity.BOTTOM
        dialog!!.window!!.setLayout(x, y)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        viewmodel = ViewModelProvider(this).get(BottomSheetRegionDailogViewModel::class.java)
        binding!!.viewModel = viewmodel
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
//        binding!!.rvRegion.setOnTouchListener(OnTouchListener { v, event ->
//            val action = event.action
//            when (action) {
//                MotionEvent.ACTION_DOWN ->                         // Disallow NestedScrollView to intercept touch events.
//                    v.parent.requestDisallowInterceptTouchEvent(true)
//
//                MotionEvent.ACTION_UP ->                         // Allow NestedScrollView to intercept touch events.
//                    v.parent.requestDisallowInterceptTouchEvent(false)
//            }
//
//            // Handle RecyclerView touch events.
//            v.onTouchEvent(event)
//            true
//        })
//        binding!!.rvRegionDetail.setOnTouchListener(OnTouchListener { v, event ->
//            val action = event.action
//            when (action) {
//                MotionEvent.ACTION_DOWN ->                         // Disallow NestedScrollView to intercept touch events.
//                    v.parent.requestDisallowInterceptTouchEvent(true)
//
//                MotionEvent.ACTION_UP ->                         // Allow NestedScrollView to intercept touch events.
//                    v.parent.requestDisallowInterceptTouchEvent(false)
//            }
//
//            // Handle RecyclerView touch events.
//            v.onTouchEvent(event)
//            true
//        })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            viewmodel?.isPermission?.value =
                ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        }else{
            viewmodel?.isPermission?.value =
                ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED||
                        ContextCompat.checkSelfPermission(requireActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
        if (regiontext == "내주변"){
            viewmodel?.eventAroundMe?.value = true
        }
        viewmodel?.regionSelectIndex?.value = regionIndex
        viewmodel?.regionSelectDetailIndex?.value = regionSelectDetailIndex

        setAdapter()
//        viewmodel?.buttonEvent(regionIndex)

        viewmodel?.permissionEvent?.observe(viewLifecycleOwner, Observer {
            if (it) {
                event(true, 0, "위치 팝업",0)


            }
        })

        viewmodel?.okEvent?.observe(viewLifecycleOwner, Observer
        {
            if (it) {
                if (viewmodel?.eventAroundMe?.value == true){
                    event(true, 0, "내주변",0)
                }else{
                    var result = ""
                    if (viewmodel?.regionSelectIndex?.value == 2){
                        Log.d("테스트 시작", viewmodel?.seoulRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!])
                        if (viewmodel?.seoulRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!] == "서울 전체"){
                            result = "서울 전체"

                        }else{
                            result = "서울 "+viewmodel?.seoulRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!]
                        }
                        event(true, viewmodel?.regionSelectIndex?.value!!,result ,viewmodel!!.regionSelectDetailIndex.value!!)
                    }else if (viewmodel?.regionSelectIndex?.value ==3){

                        if (viewmodel?.gyeonggidoRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!]== "경기 전체"){
                            result = "경기 전체"

                        }else{
                            result = "경기 "+viewmodel?.gyeonggidoRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!]
                        }
                        event(true, viewmodel?.regionSelectIndex?.value!!,result,viewmodel!!.regionSelectDetailIndex.value!!)
                    }else{
                        event(true, viewmodel?.regionSelectIndex?.value!!, viewmodel?.regionArray?.value!![viewmodel!!.regionSelectIndex.value!!],0)
                    }
                }


            }
        })


        viewmodel?.locationOk?.observe(viewLifecycleOwner,Observer{
            if (it){
                event(true, 0, "내주변",0)
            }
        })





    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapter() {
        var list : ArrayList<String> = arrayListOf()
        list.add("지역 전체보기")
        regionAdapter = RegionAdapter(requireContext(), viewmodel?.regionArray?.value!!,viewmodel!!) { data, position ->

            viewmodel?.regionSelectIndex?.value = position
            regionAdapter?.notifyDataSetChanged()
            viewmodel?.regionSelectDetailIndex?.value = 0

            when(position){
                1-> list = viewmodel?.seoulRegionArray?.value!!
                2-> list = viewmodel?.gyeonggidoRegionArray?.value!!
                else -> {
                    var list2 : ArrayList<String> = arrayListOf()
                    list2.add("${data} 전체보기")
                    list = list2
                    dismiss()
                }
            }
            regionDetailAdapter = RegionDetailAdapter(requireContext(), list,viewmodel!!) { data, position ->

                viewmodel?.regionSelectDetailIndex?.value = position
                regionAdapter?.notifyDataSetChanged()
                dismiss()

            }
            binding?.rvRegionDetail?.adapter = regionDetailAdapter
            binding!!.rvRegionDetail.layoutManager = GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL, false)

        }


            when(viewmodel?.regionSelectIndex?.value){
                0-> {
                    viewmodel?.regionSelectDetailIndex?.value = 0
                }
                1-> list = viewmodel?.seoulRegionArray?.value!!
                2-> list = viewmodel?.gyeonggidoRegionArray?.value!!
                else -> {
                    var list2 : ArrayList<String> = arrayListOf()
                    list2.add("${viewmodel?.regionArray!!.value!![viewmodel?.regionSelectIndex?.value!!]} 전체보기")
                    list = list2
                    viewmodel?.regionSelectDetailIndex?.value = 0
                }
            }
            regionDetailAdapter = RegionDetailAdapter(requireContext(), list,viewmodel!!) { data, position ->

                viewmodel?.regionSelectDetailIndex?.value = position
                regionAdapter?.notifyDataSetChanged()

            }
            binding?.rvRegionDetail?.adapter = regionDetailAdapter
        binding!!.rvRegionDetail.layoutManager = GridLayoutManager(requireContext(), 2,GridLayoutManager.VERTICAL, false)






        // 4F, 2F , 8F 등으로 속도 조절이 가능하다.


        binding?.rvRegion?.adapter = regionAdapter
        binding!!.rvRegion.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
    }


    override fun onDismiss(dialog: DialogInterface) {
        if (viewmodel?.eventAroundMe?.value == true){
            event(true, 0, "내주변",0)
        }else{
            var result=""
            if (viewmodel?.regionSelectIndex?.value == 1){

                Log.d("테스트 시작", viewmodel?.seoulRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!])
                if (viewmodel?.seoulRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!] == "서울 전체"){
                    result = "서울 전체"

                }else{
                    result = "서울 "+viewmodel?.seoulRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!]
                }
                event(true, viewmodel?.regionSelectIndex?.value!!,result,viewmodel!!.regionSelectDetailIndex.value!!)
            }else if (viewmodel?.regionSelectIndex?.value ==2){
                if (viewmodel?.gyeonggidoRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!]== "경기 전체"){
                    result = "경기 전체"

                }else{
                    result = "경기 "+viewmodel?.gyeonggidoRegionArray?.value!![viewmodel!!.regionSelectDetailIndex.value!!]
                }
                event(true, viewmodel?.regionSelectIndex?.value!!, result,viewmodel!!.regionSelectDetailIndex.value!!)
            }else{
                event(true, viewmodel?.regionSelectIndex?.value!!, viewmodel?.regionArray?.value!![viewmodel!!.regionSelectIndex.value!!],0)
            }
        }
        super.onDismiss(dialog)

    }

    override fun onDestroy() {
        super.onDestroy()


    }
}