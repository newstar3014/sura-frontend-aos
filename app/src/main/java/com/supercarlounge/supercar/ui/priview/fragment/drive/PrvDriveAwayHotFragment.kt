package com.supercarlounge.supercar.ui.priview.fragment.drive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.databinding.FragmentDriveAwayHotBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.priview.fragment.PrvHomeFragment2
import com.supercarlounge.supercar.viewmodel.DriveAwayHotViewModel


class PrvDriveAwayHotFragment() : Fragment() {
    val TAG = "DriveAwayHotFragment"
    private var _binding: FragmentDriveAwayHotBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DriveAwayHotViewModel
    private var adapter: DriveAwayAdapter? = null
    private var page = 1
    private var ppp = 10
    var application: MainApplication? = null
    var my_u_seq = ""
    private var quickUserDialog: QuickUserDialog? = null
    private var fm: FragmentManager? = null

    fun changeviewtype(type: Int) {
        if (binding != null) {
            adapter!!.vtype = type
            if (type == 0) {

                binding!!.rvDriveAwayHot.layoutManager = GridLayoutManager(requireContext(), 3)
                if (binding!!.rvDriveAwayHot.itemDecorationCount != 0) {
                    binding!!.rvDriveAwayHot.removeItemDecorationAt(0)
                }
                application!!.setlistview_padding(8, binding!!.rvDriveAwayHot, 3, false)
                binding!!.rvDriveAwayHot.adapter!!.notifyDataSetChanged()
            } else {

                binding!!.rvDriveAwayHot.layoutManager = LinearLayoutManager(requireContext())
                if (binding!!.rvDriveAwayHot.itemDecorationCount != 0) {
                    binding!!.rvDriveAwayHot.removeItemDecorationAt(0)
                }
                application!!.setlistview_padding(20, binding!!.rvDriveAwayHot, 1, false)
                binding!!.rvDriveAwayHot.adapter!!.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(DriveAwayHotViewModel::class.java)

        _binding = FragmentDriveAwayHotBinding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.setLifecycleOwner(this)
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        application = requireContext().applicationContext as MainApplication
        fm = childFragmentManager
        my_u_seq = application!!.userData!!.u_seq.toString()
        val root: View = binding!!.root




        return root
    }

    override fun onResume() {
        super.onResume()
        var type = (parentFragment as PrvHomeFragment2).viewModel.rvItemType.value!!
        changeviewtype(type)
        adapter!!.cleardata()
        adddrive(true)
        Log.d(TAG, "ONRESUME")
        viewModel.isBoardOne.value = checkVipView(viewModel.count.value!!)
        Log.d(TAG, "ONRESUME")

    }
    private fun checkVipView(viewCount :Int) : Boolean{
        Log.d("홀수짝수", (viewCount % 2 ==0).toString())

        return viewCount % 2 == 0
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = DriveAwayAdapter(
            requireContext(),
            arrayListOf(), viewModel.rvItemType.value!!
        ) { data, position ->
                ShowQuickUserDialog()

        }






        adapter!!.setHasStableIds(true)

        binding!!.rvDriveAwayHot.adapter = adapter
        binding!!.rvDriveAwayHot.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            @SuppressLint("NewApi")
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.viewTreeObserver.removeOnGlobalLayoutListener(
                    this
                ) else view.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })

        binding!!.rvDriveAwayHot.addOnScrollListener(listener)
        binding!!.rvDriveAwayHot.setHasFixedSize(true)


    }

    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition =
                (binding?.rvDriveAwayHot?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            Log.d("getDrive  last", "" + lastposition)
            var itemcount = adapter!!.itemCount

            if (itemcount == (lastposition + 1)) {
//                var totalc = viewModel.totalCount.value!!
//                if (itemcount < totalc && totalc != 0) {
                    var npage = viewModel.page.value!!
                    adddrive(false)
//                }
            }

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(type: Int): PrvDriveAwayHotFragment {
        val args = Bundle()

        val fragment = PrvDriveAwayHotFragment()
        fragment.arguments = args
        return fragment
    }
    fun adddrive(isstart :Boolean){
        var list = ArrayList<DriveAwayData>()
        if(isstart==false){
            list.addAll(viewModel.mDriveList.value!!)

        }
        list.add(
            DriveAwayData(3,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","오늘도 나를위해 오늘 저녁도 달립니다.","오늘도 나를위해 오늘 저녁도 달립니다.",0,0,"",0,"","","","",null
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(4,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","시내도로를 고속도로 처럼 달리고 싶은날","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"VOLKSWAGEN","https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png","","","",0,0,"",0,0,0  ,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(5,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","https://supercarlounge.com:3002/images/temp2/drive_sample3.png","시내도로를 고속도로 처럼 달리고 싶은날","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"","","","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(6,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","https://supercarlounge.com:3002/images/temp2/drive_sample4.png","","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BUGATTI","https://supercarlounge.com:3002/images/car_brand/20211220032636buga.png","MALE","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(1,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","","시내를 고속도로 처럼 달리고 싶은이에요.",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BENZ","https://supercarlounge.com:3002/images/car_brand/20211220031730benz.png","MALE","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(2,"","","",1,1,"","","https://i.ibb.co/7nQkmmT/drive-sample1.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","","아우디 차주분들 같이 달려요! 오늘 밤..",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BMW","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png","","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(3,"","","",1,1,"","","","","","오늘도 나를위해 오늘 저녁도 달립니다.",0,0,"",0,"","","","",""
                ,0,"","","","",0,"AUDI","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895904160.png","MALE","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(1,"","","",1,1,"","","https://i.ibb.co/7nQkmmT/drive-sample1.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","시내를 고속도로 처럼 달리고 싶은이에요.","시내를 고속도로 처럼 달리고 싶은이에요.",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BENZ","https://supercarlounge.com:3002/images/car_brand/20211220031730benz.png","MALE","","",0,0,"",0,0,0,"",
                "","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(2,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","아우디 차주분들 같이 달려요! 오늘 밤..","아우디 차주분들 같이 달려요! 오늘 밤..",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BMW","https://supercarlounge.s3.ap-northeast-2.amazonaws.com/car_brand/1646895890636.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )

        list.add(
            DriveAwayData(5,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","https://supercarlounge.com:3002/images/temp2/drive_sample5.png","시내도로를 고속도로 처럼 달리고 싶은날","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"","","","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(6,"","","",1,1,"","","https://i.ibb.co/7nQkmmT/drive-sample1.png","https://i.ibb.co/7nQkmmT/drive-sample1.png","시내도로를 고속도로 처럼 달리고 싶은날","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"BUGATTI","https://supercarlounge.com:3002/images/car_brand/20211220032636buga.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )
        list.add(
            DriveAwayData(4,"","","",1,1,"","","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","https://supercarlounge.com:3002/images/temp2/drive_sample2.png","시내도로를 고속도로 처럼 달리고 싶은날","시내도로를 고속도로 처럼 달리고 싶은날",0,0,"",0,"","","","",null
                ,0,"","","","",0,"VOLKSWAGEN","https://supercarlounge.com:3002/images/car_brand/20211220031814fox.png","","","",0,0,"",0,0,0,"","","",
                "","",
                "", "",)
        )

        viewModel.mDriveList.value = list
    }
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(requireActivity(), JoinActivity::class.java)
                    i.putExtra("qickview","퀵유저")
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