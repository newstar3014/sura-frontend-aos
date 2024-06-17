package com.supercarlounge.supercar.ui.priview.fragment.drive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.FragmentDriveAwayNewBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.DriveAwayListActivity
import com.supercarlounge.supercar.ui.activity.DriveDetailActivity
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.priview.fragment.PrvHomeFragment2
import com.supercarlounge.supercar.viewmodel.DriveAwayNewViewModel


class PrvDriveAwayNewFragment() : Fragment() {
    val TAG = "DriveAwayNewFragment"
    private var _binding: FragmentDriveAwayNewBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DriveAwayNewViewModel
    private var  adapter : DriveAwayAdapter? = null
    var ischeck = false
    var application:MainApplication  ? = null
    var my_u_seq = ""
    private var quickUserDialog :QuickUserDialog? = null
    private var fm : FragmentManager? =null
    var overallXScroll = 0
   init {

   }
//    fun changeviewtype(type:Int){
//        binding!!.rvDriveAwayNew.visibility = View.VISIBLE
//        if(binding!=null) {
//            adapter!!.vtype = type
//            if (type == 0) {
//
//                binding!!.rvDriveAwayNew.layoutManager = GridLayoutManager(requireContext(), 3)
//                if (binding!!.rvDriveAwayNew.itemDecorationCount != 0) {
//                    binding!!.rvDriveAwayNew.removeItemDecorationAt(0)
//                }
//                application!!.setlistview_padding(8,binding!!.rvDriveAwayNew,3,false)
//                binding!!.rvDriveAwayNew.adapter!!.notifyDataSetChanged()
//            } else {
//
//                binding!!.rvDriveAwayNew.layoutManager = LinearLayoutManager(requireContext())
//                if (binding!!.rvDriveAwayNew.itemDecorationCount != 0) {
//                    binding!!.rvDriveAwayNew.removeItemDecorationAt(0)
//                }
//                application!!.setlistview_padding(20,binding!!.rvDriveAwayNew,1,false)
//                binding!!.rvDriveAwayNew.adapter!!.notifyDataSetChanged()
//            }
//        }
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


        return root
    }

    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
       var type = (parentFragment as PrvHomeFragment2).viewModel.rvItemType.value!!
//        changeviewtype(type)
        viewModel.page.value = 1
//        adapter!!.cleardata()
        adddrive(true)
        Log.d(TAG,"ONRESUME")
        viewModel.isBoardOne.value = checkVipView(viewModel.count.value!!)
        Log.d(TAG, "ONRESUME")

    }
    private fun checkVipView(viewCount :Int) : Boolean{
        Log.d("홀수짝수", (viewCount % 2 ==0).toString())

        return viewCount % 2 == 0
    }


    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition=(binding?.rvDriveAwayNew?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=adapter!!.itemCount
            Log.d("getDrive : last" , ""+itemcount)
            Log.d("getDrive : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
//                var totalc = viewModel.totalCount.value!!
//                if (itemcount < totalc&& totalc !=0) {
//                    if (itemcount < viewModel.totalCount.value!!) {
                        var npage =  viewModel.page.value!!
                        adddrive(false)
//                    }
//                }
            }



        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)





        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observes()
        adapter = DriveAwayAdapter(requireActivity(), arrayListOf(),viewModel.rvItemType.value!!) { data, position ->
            ShowQuickUserDialog()

        }
        adapter!!.setHasStableIds(true)
        binding!!.rvDriveAwayNew.adapter = adapter
        binding!!.rvDriveAwayNew.addOnScrollListener(listener)
        binding!!.llMain.setOnClickListener {

            Log.d("옵저버1414", viewModel.rvItemType.value .toString())
        }


        Log.d("DriveAwayNewFragment", "adapter: ")

    }
    private fun observes() {
        viewModel.intentActivityDriveWayListEvent.observe(requireActivity(), Observer {
            if (it) {
               ShowQuickUserDialog()
            }
        })
        viewModel.awayItemIndex.observe(requireActivity(),Observer{

            ShowQuickUserDialog()

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DriveAwayNewFragment", "onDestroyView: ")
    }
    fun newInstance(type:Int): PrvDriveAwayNewFragment {
        val args = Bundle()
//        viewModel.rvItemType.value = type
        val fragment = PrvDriveAwayNewFragment()
        fragment.arguments = args
        return fragment
    }


    fun adddrive(isstart :Boolean){
        var list = ArrayList<DriveAwayData>()
        if(isstart==false){
            list.addAll(viewModel.mDriveList.value!!)

        }
        list.add(
            DriveAwayData(1,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-7.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","써킷왔오요","시내를 고속도로 처럼 달리고 싶은이에요.",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BENZ","https://supercarlounge.com:3002/images/car_brand/20211220031730benz.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(2,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-1.png","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","자가격리 중인데...","아우디 차주분들 같이 달려요! 오늘 밤..",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BMW","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-2.png","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","카페 가서 감성놀이\n하고 싶어요","오늘도 나를위해 오늘 저녁도 달립니다.",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(4,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-3.png","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","이번에 불꽃축제 가신분 계신가요?","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"VOLKSWAGEN","https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(5,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-2.png","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","슈라의 협찬","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"","","","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(6,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-10.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","슈라에서 만난분 덕분에","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BUGATTI","https://supercarlounge.com:3002/images/car_brand/20211220032636buga.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(4,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-6.png","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","같이 드라이브 고고요","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"VOLKSWAGEN","https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(5,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-7.png","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","위스키 한잔 하고 싶은 주말밤~","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"","","","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(6,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-8.png","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","기분 좋은 밤 드라이브","기분 좋은 밤 드라이브",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BUGATTI","https://supercarlounge.com:3002/images/car_brand/20211220032636buga.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(1,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-9.png","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","방금 드라이브 하는데","방금 드라이브 하는데",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BENZ","https://supercarlounge.com:3002/images/car_brand/20211220031730benz.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(2,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-10.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","굿모닝~!~! \n 좋은 아침 이에요!!","굿모닝~!~! \n좋은 아침 이에요!!",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BMW","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-11.png","","기분 좋은 날 드라이브","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "" ,"",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-12.png","","저녁에 차한잔 하면서 드라이브 어때요?","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-13.png","","즐점!","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-14.png","","슈라 프라이빗 파티 다녀왔어요! 너무 즐거...","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-15.png","","슈라 드라이브 매칭은","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-16.png","","아우디 차주분들 같이 달려요! 오늘 밤..","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/preview/Card-17.png","","비오는날 운치있게 드라이브 어때요??","",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )

        viewModel.mDriveList.value = list
    }
    private fun ShowQuickUserDialog(){
        quickUserDialog = QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
            if (okcancel) {
                quickUserDialog!!.dismiss()
                val i = Intent(requireActivity(), JoinActivity::class.java)
                i.putExtra("qickview","퀵유저")
                i.putExtra("u_name", application?.userBasicData?.u_name)
                i.putExtra("u_gender", application?.userBasicData?.u_gender)
                i.putExtra("u_birthday",application?.userBasicData?.u_birthday)
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