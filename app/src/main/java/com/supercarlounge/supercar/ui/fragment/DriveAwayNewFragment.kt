package com.supercarlounge.supercar.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.FragmentDriveAwayNewBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.DriveAwayListActivity
import com.supercarlounge.supercar.ui.activity.DriveDetailActivity
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.viewmodel.DriveAwayNewViewModel
import com.supercarlounge.supercarnative.ui.fragment.HomeFragment2


class DriveAwayNewFragment() : Fragment() {
    val TAG = "DriveAwayNewFragment"
    private var _binding: FragmentDriveAwayNewBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DriveAwayNewViewModel
    private var adapter: DriveAwayAdapter? = null
    var ischeck = false
    var application: MainApplication? = null
    var my_u_seq = ""
    private var quickUserDialog: QuickUserDialog? = null
    private var fm: FragmentManager? = null
    private var c1 = false
    private var driveSize = 999

    private var checkCallback = false
    private var overallXScroll = 0
    var vipInitView = 0

    init {

    }

//    fun changeviewtype(type: Int) {
//        if (binding != null) {
//            adapter!!.vtype = type
//            if (type == 0) {
//
//                binding!!.rvDriveAwayNew.layoutManager = GridLayoutManager(requireContext(), 3)
//                //    binding!!.rvDriveAwayNew.edgeEffectFactory = BounceEdgeEffectFactory()
//                if (binding!!.rvDriveAwayNew.itemDecorationCount != 0) {
//                    binding!!.rvDriveAwayNew.removeItemDecorationAt(0)
//                }
//                application!!.setlistview_padding(8, binding!!.rvDriveAwayNew, 3, false)
//                binding!!.rvDriveAwayNew.adapter!!.notifyDataSetChanged()
//            } else {
//
//                binding!!.rvDriveAwayNew.layoutManager = LinearLayoutManager(requireContext())
//                //  binding!!.rvDriveAwayNew.edgeEffectFactory = BounceEdgeEffectFactory()
//                if (binding!!.rvDriveAwayNew.itemDecorationCount != 0) {
//                    binding!!.rvDriveAwayNew.removeItemDecorationAt(0)
//                }
//                application!!.setlistview_padding(20, binding!!.rvDriveAwayNew, 1, false)
//                binding!!.rvDriveAwayNew.adapter!!.notifyDataSetChanged()
//            }
//        }
//    }

    fun changePost() {
//        refreshFragment(this, fm!!)
        if (binding != null) {
            Log.d("스크롤확인용", "changeScroll: 11")
            viewModel.page.value = 1
            viewModel.getDrive(my_u_seq, true, binding!!)
        }
    }
//    fun refreshFragment(fragment: Fragment, fragmentManager: FragmentManager) {
//        var ft: FragmentTransaction = fragmentManager.beginTransaction()
//        ft.detach(fragment).attach(fragment).commit()
//    }
//
//    }
//    fun changeScrollFocus(type:Boolean) {
//        if (binding != null) {
//            if (type){
//                binding?.rvDriveAwayNew?.isNestedScrollingEnabled = true
//            }else{
//                binding?.rvDriveAwayNew?.isNestedScrollingEnabled = false
//            }
//
//        }
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(DriveAwayNewViewModel::class.java)
        _binding = FragmentDriveAwayNewBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        application = requireContext().applicationContext as MainApplication
        my_u_seq = application!!.userData!!.u_seq.toString()
        fm = childFragmentManager
        val root: View = binding!!.root
        ischeck = true

        viewModel.getDrive(my_u_seq, true, binding!!)
        return root
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        var type = (parentFragment as HomeFragment2).viewModel.rvItemType.value!!
//        changeviewtype(type)

//        adapter!!.cleardata()
//        setVipItem(arrayListOf(), viewModel.isBoardOne.value!!)
        if (checkCallback){
            viewModel.page.value = 1
            viewModel.getDrive(my_u_seq, true, binding!!)
            checkCallback =false
        }

        viewModel.getVipDrive(my_u_seq)

//        visibilityVip(viewModel.isBoardOne.value!!)
        Log.d(TAG, "ONRESUME")
    }

//    private fun visibilityVip(isBoardOne :Boolean) {
//        binding!!.itemVip1.visibility = View.GONE
//        binding!!.itemVip2.visibility = View.GONE
//        binding!!.itemVip3.visibility = View.GONE
//        binding!!.itemVip4.visibility = View.GONE
//        binding!!.itemVip5.visibility = View.GONE
//        binding!!.itemVip6.visibility = View.GONE
//
//        if (isBoardOne){
//
//            when(viewModel.count.value){
//                2 -> {
//                    binding!!.itemVip1.visibility = View.VISIBLE
//                }
//            }
//
//        }else{
//
//            when(viewModel.count.value){
//                1 -> {
//
//                    binding!!.itemVip1.visibility = View.VISIBLE
//
//                }
//
//                3 -> {
//
//                }
//            }
//
//        }
//
//
//
//
//    }

    private fun checkVipView(viewCount: Int): Boolean {
        Log.d("홀수짝수", (viewCount % 2 == 0).toString())

        return viewCount % 2 == 0
    }

    @SuppressLint("CheckResult", "ResourceType")
    private fun setVipItem(list: ArrayList<DriveAwayData>, isBoardOne: Boolean) {
        binding?.itemVip1?.visibility = View.GONE
        binding?.itemVip2?.visibility = View.GONE
        binding?.itemVip3?.visibility = View.GONE
        binding?.itemVip4?.visibility = View.GONE
        binding?.itemVip5?.visibility = View.GONE
        binding?.itemVip6?.visibility = View.GONE
        if (isBoardOne) {

            if (list.size >= 1) {
                binding?.itemVip1?.visibility = View.VISIBLE
                binding?.itemVipMainTv1?.text = list[0].b_title
                binding?.itemVipSubTv1?.text =
                    if (list[0].carb_name?.isNotEmpty() == true) list[0].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv1!!).load(list[0].b_cover).override(500, 500)
                    .into(binding?.itemVipIv1!!)
                if (list[0].u_gender == "MALE") {
                    Glide.with(binding?.ivSex1!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex1!!)
                } else {
                    Glide.with(binding?.ivSex1!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex1!!)

                }
//                    .into(view)
//
//            }else{
//                if (index == 0){
//                    Glide.with(view).load(R.drawable.frame_1440).override(500, 500)
//                        .into(view)
//                }else if (index == 1){
//                    Glide.with(view).load(R.drawable.frame_1438).override(500, 500)
//                        .into(view)
            }
            if (list.size!! >= 2) {
                binding?.itemVip2?.visibility = View.VISIBLE
                binding?.itemVipMainTv2?.text = list[1].b_title
                binding?.itemVipSubTv2?.text =
                    if (list[1].carb_name?.isNotEmpty() == true) list[1].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv2!!).load(list[1].b_cover).override(500, 500)
                    .into(binding?.itemVipIv2!!)
                if (list[1].u_gender == "MALE") {
                    Glide.with(binding?.ivSex2!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex2!!)
                } else {
                    Glide.with(binding?.ivSex2!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex2!!)

                }
            }
            if (list.size!! >= 3) {
                binding?.itemVip3?.visibility = View.VISIBLE
                binding?.itemVipMainTv3?.text = list[2].b_title
                binding?.itemVipSubTv3?.text =
                    if (list[2].carb_name?.isNotEmpty() == true) list[2].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv3!!).load(list[2].b_cover).override(500, 500)
                    .into(binding?.itemVipIv3!!)
                if (list[2].u_gender == "MALE") {
                    Glide.with(binding?.ivSex3!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex3!!)
                } else {
                    Glide.with(binding?.ivSex3!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex3!!)

                }
            }
            if (list.size!! >= 4) {
                binding?.itemVip4?.visibility = View.VISIBLE
                binding?.itemVipMainTv4?.text = list[3].b_title
                binding?.itemVipSubTv4?.text =
                    if (list[3].carb_name?.isNotEmpty() == true) list[3].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv4!!).load(list[3].b_cover).override(500, 500)
                    .into(binding?.itemVipIv4!!)
                if (list[3].u_gender == "MALE") {
                    Glide.with(binding?.ivSex4!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex4!!)
                } else {
                    Glide.with(binding?.ivSex4!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex4!!)

                }
            }


        } else {

            if (list.size >= 1) {
                binding?.itemVip2?.visibility = View.VISIBLE
                binding?.itemVipMainTv2?.text = list[0].b_title
                binding?.itemVipSubTv2?.text =
                    if (list[0].carb_name?.isNotEmpty() == true) list[0].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv2!!).load(list[0].b_cover).override(500, 500)
                    .into(binding?.itemVipIv2!!)
                if (list[0].u_gender == "MALE") {
                    Glide.with(binding?.ivSex2!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex2!!)
                } else {
                    Glide.with(binding?.ivSex2!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex2!!)

                }
            }
            if (list.size >= 2) {
                binding?.itemVip3?.visibility = View.VISIBLE
                binding?.itemVipMainTv3?.text = list[1].b_title
                binding?.itemVipSubTv3?.text =
                    if (list[1].carb_name?.isNotEmpty() == true) list[1].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv3!!).load(list[1].b_cover).override(500, 500)
                    .into(binding?.itemVipIv3!!)
                if (list[1].u_gender == "MALE") {
                    Glide.with(binding?.ivSex3!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex3!!)
                } else {
                    Glide.with(binding?.ivSex3!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex3!!)

                }

            }
            if (list.size >= 3) {
                binding?.itemVip4?.visibility = View.VISIBLE
                binding?.itemVipMainTv4?.text = list[2].b_title
                binding?.itemVipSubTv4?.text =
                    if (list[2].carb_name?.isNotEmpty() == true) list[2].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv4!!).load(list[2].b_cover).override(500, 500)
                    .into(binding?.itemVipIv4!!)
                if (list[2].u_gender == "MALE") {
                    Glide.with(binding?.ivSex4!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex4!!)
                } else {
                    Glide.with(binding?.ivSex4!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex4!!)

                }
            }
            if (list.size >= 4) {
                binding?.itemVip5?.visibility = View.VISIBLE
                binding?.itemVipMainTv5?.text = list[3].b_title
                binding?.itemVipSubTv5?.text =
                    if (list[3].carb_name?.isNotEmpty() == true) list[3].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv5!!).load(list[3].b_cover).override(500, 500)
                    .into(binding?.itemVipIv5!!)
                if (list[3].u_gender == "MALE") {
                    Glide.with(binding?.ivSex5!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex5!!)
                } else {
                    Glide.with(binding?.ivSex5!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex5!!)

                }

            }
            if (list.size >= 5) {
                binding?.itemVip6?.visibility = View.VISIBLE
                binding?.itemVipMainTv6?.text = list[4].b_title
                binding?.itemVipSubTv6?.text =
                    if (list[4].carb_name?.isNotEmpty() == true) list[4].carb_name.toString() else "PASSENGER"
                Glide.with(binding?.itemVipIv6!!).load(list[4].b_cover).override(500, 500)
                    .into(binding?.itemVipIv6!!)

                if (list[4].u_gender == "MALE") {
                    Glide.with(binding?.ivSex6!!).load(R.drawable.ic_man_12x12).override(500, 500)
                        .into(binding?.ivSex6!!)
                } else {
                    Glide.with(binding?.ivSex6!!).load(R.drawable.ic_woman_12x12).override(500, 500)
                        .into(binding?.ivSex6!!)
                }


            }

                if (binding!!.llWidthView.width != 0){
                    var x = 0
                    val sizeInPixels = resources.getDimension(R.dimen._220sdp)

                    x = binding!!.llWidthView.width / 2
                    var params = LinearLayoutCompat.LayoutParams(x, sizeInPixels.toInt())
//                params.gravity = Gravity.CENTER
                    binding!!.itemVip2.layoutParams = params

                }



        }

    }

//    var listener = object : RecyclerView.OnScrollListener() {
//
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            var lastposition =
//                (binding?.rvDriveAwayNew?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//            var itemcount = adapter!!.itemCount
//            Log.d("스크롤홈확인용3 : last","" )
//
//
//
//
//            if (itemcount == (lastposition + 1)) {
//                var totalc = viewModel.totalCount.value!!
//                if (itemcount < totalc && totalc != 0) {
//                    if (itemcount < viewModel.totalCount.value!!) {
//                        var npage = viewModel.page.value!!
//                        if (viewModel.issend.value == false) {
//                            viewModel.page.value = npage + 1
//                            viewModel.getDrive(my_u_seq, false,binding!!)
//
//                        }
//
//
//                    }
//                }
//            }
//        }

//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//            Log.d("홈프래그먼트 y", recyclerView.computeVerticalScrollOffset().toString())
//
//
//
//            if (application?.scrollcheck == true) {
//                if (recyclerView.computeVerticalScrollOffset() == 0) {
////                binding?.rvDriveAwayNew?.isNestedScrollingEnabled = true
//                } else {
////                binding?.rvDriveAwayNew?.isNestedScrollingEnabled = false
//                }
//            } else {
//
////                    binding?.rvDriveAwayNew?.isNestedScrollingEnabled = true
//
//
//            }
////            Log.d("리사이클러뷰", dy.toString())
////                  overallXScroll += dy;
////            Log.i("리사이클러뷰", "overall X  = " + overallXScroll);
//
//            //binding!!.rvDriveAwayNew.isNestedScrollingEnabled = false
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observes()
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


        binding!!.itemVip2.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {


//
//                if (viewModel.isBoardOne.value == false){
//                    var x  = 0
//                    var y  = binding!!.itemVip2.height
//                    x  =binding!!.itemVip2.width /2
//                    var params = LinearLayoutCompat.LayoutParams(x, y)
////                params.gravity = Gravity.CENTER
//                    binding!!.itemVip2.layoutParams = params
//                    Log.d("홀수", "onGlobalLayout: ")
//                }else{
//                    Log.d("짝수", "onGlobalLayout: ")
//
//                }


                binding!!.itemVip2.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }


        })


//        binding!!.rvDriveAwayNew.adapter = adapter
        //binding!!.rvDriveAwayNew.isNestedScrollingEnabled = false
//        binding!!.rvDriveAwayNew.addOnScrollListener(listener)
//        binding!!.rvDriveAwayNew.setHasFixedSize(true)
//        binding!!.rvDriveAwayNew.layoutManager = object : LinearLayoutManager(requireContext()) {
//            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
//                // force height of viewHolder here, this will override layout_height from xml
//                lp.height = (width.times(1.5).toInt())
//                Log.d("확인", lp.height.toString())
//                return true
//            }
//        }
//        binding?.rvDriveAwayNew?.viewTreeObserver?.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//
//                deviceSetView()
////                    model.startTopViewPosition.value =con.getViewCoordinate(com.example.supercarx.R.id.pager_banner_2)
//                binding?.rvDriveAwayNew?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
//            }
//        })

//        binding!!.rvAlim.addOnScrollListener(listener)
        binding!!.llMain.setOnClickListener {

            Log.d("옵저버1414", viewModel.rvItemType.value.toString())
        }

//        viewModel.issend.observe(viewLifecycleOwner, Observer {
//            if (it) {
//                binding!!.rvDriveAwayNew.animate().translationY(-200f).setDuration(500).start()
//            } else {
//                binding!!.rvDriveAwayNew.animate().translationY(0f).setDuration(500).start()
//
//            }
//
//        })
        Log.d("DriveAwayNewFragment", "adapter: ")

    }

    private fun observes() {
        viewModel.intentActivityDriveWayListEvent.observe(requireActivity(), Observer {
            if (it) {
                checkCallback = true
                val i = Intent(requireActivity(), DriveAwayListActivity::class.java)
                i.putExtra("type", "신규")
                startActivity(i)
                requireActivity().overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )

            }
        })
        viewModel.awayItemIndex.observe(requireActivity(), Observer {

            if (viewModel.mDriveList.value?.size!! > it) {
                checkCallback = true
                val i = Intent(requireActivity(), DriveDetailActivity::class.java)
                i.putExtra("data", viewModel.mDriveList.value!![it])
                i.putExtra("type", "신규")
                i.putExtra("totalCount", viewModel.totalCount.value)
                i.putExtra("page", viewModel.page.value)
                i.putParcelableArrayListExtra("allList", viewModel.mDriveList.value)
                i.putExtra("driveType", "notvip")
                startActivity(i)
                requireActivity().overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }

        })

        viewModel.vipItemIndex.observe(requireActivity(), Observer {

            if (viewModel.mVipDriveList.value?.size!! >= it) {
                var index = it

                if (viewModel.isBoardOne.value == false) {
                    if (index != 0) index--
                }

                if (application?.quick_view.equals("퀵유저")) {
                    ShowQuickUserDialog()
                } else {
                    checkCallback = true
                    val i = Intent(requireContext(), DriveDetailActivity::class.java)
                    i.putExtra("data", viewModel.mVipDriveList.value!![index])
                    i.putExtra("isvip", true)
                    i.putExtra("type", "신규")
                    i.putExtra("page", viewModel.page.value)
                    i.putExtra("allList", viewModel.mVipDriveList.value)
                    i.putExtra("driveType", "vip")
                    requireContext().startActivity(i)
                    requireActivity().overridePendingTransition(
                        android.R.anim.fade_in,
                        android.R.anim.fade_out
                    )
                }
            }
        })
        viewModel.count.observe(requireActivity(), Observer {
            if (it != 0) {


            }

        })
        viewModel.mVipDriveList.observe(requireActivity(), Observer {
            if (it.size != 0) {
                if (driveSize != it.size){
                    viewModel.mVipDriveList.value?.let { it1 ->
                        viewModel.isBoardOne.value = checkVipView(it.size)
                        driveSize = it.size
                        setVipItem(it1, viewModel.isBoardOne.value!!)
                    }

                }

            }
        })
    }

    fun deviceSetView() {
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels
//        var deviceW =binding!!.rvDriveAwayNew.width
//        var params = RelativeLayout.LayoutParams(deviceW,    displayPH)

//        var dim10 = view?.resources?.getDimensionPixelSize(com.supercarlounge.supercarnative.R.dimen._12sdp)
//        if (dim10 != null) {
//            params.marginStart = dim10
//            params.marginEnd = dim10
//        }
//        binding!!.rvDriveAwayNew.layoutParams = params
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DriveAwayNewFragment", "onDestroyView: ")
    }

    fun newInstance(type: Int): DriveAwayNewFragment {
        val args = Bundle()
//        viewModel.rvItemType.value = type
        val fragment = DriveAwayNewFragment()
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