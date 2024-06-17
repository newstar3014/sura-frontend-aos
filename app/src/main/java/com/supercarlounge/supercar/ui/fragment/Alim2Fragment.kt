package com.supercarlounge.supercar.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.Network.MatchingService
import com.supercarlounge.supercar.Network.NetworkManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.DriveAwayAdapter
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.ResultData
import com.supercarlounge.supercar.databinding.FragmentAlim2Binding
import com.supercarlounge.supercar.databinding.FragmentDriveAwayHotBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.DriveAwayHotViewModel
import com.supercarlounge.supercarnative.ui.fragment.HomeFragment2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class Alim2Fragment() : Fragment() {
    val TAG = "Alim2Fragment"
    private var _binding: FragmentAlim2Binding? = null
    private val binding get() = _binding
    private lateinit var viewModel: AlimViewModel
    private lateinit var adapter: AlimAdapter
    private var page = 1
    private var ppp = 10
    var application: MainApplication? = null
    var my_u_seq = ""
    private var fm: FragmentManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AlimViewModel::class.java)

        _binding = FragmentAlim2Binding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        application = requireContext().applicationContext as MainApplication
        fm = childFragmentManager
        my_u_seq = application!!.userData!!.u_seq.toString()
        val root: View = binding!!.root




        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getalim()
    }

    private fun observes() {
        viewModel.select_profile_seq.observe(viewLifecycleOwner, Observer { seq ->
            if (!seq.isNullOrEmpty()) {
                Log.d("알림 받는 seq", seq)
                var i = Intent(requireContext(), ProfileActivity::class.java)
                i.putExtra("sub_seq", seq)
                startActivity(i)
            }
        })
        viewModel.userCheck.observe(viewLifecycleOwner, Observer { it ->
            if (it) {
                if (viewModel.pa_check.value.equals("0")) {

                    viewModel.AlimRead(
                        viewModel.pa_seq.value.toString(),
                        viewModel.screen.value!!,
                        viewModel.send_seq.value!!
                    )
                } else {

                    viewModel.select_profile_seq.value = viewModel.send_seq.value!!

                }
            } else {
                Toast.makeText(requireContext(), "탈퇴한 회원입니다.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.select_board_data.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                var i = Intent(requireContext(), PostActivity::class.java)
                i.putExtra("c_seq", data.c_seq)
                i.putExtra("b_date", data.b_date)
                i.putExtra("b_contents", data.b_contents)
                i.putExtra("rn_nickname", data.rn_nickname)
                i.putExtra("image", data.b_cover)
                i.putExtra("views", data.views)
                i.putExtra("like", "+${data.b_like}")
                i.putExtra("u_gender", data.u_gender)
                i.putExtra("b_seq", data.b_seq.toString())
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("like_type", data.like_check)
                i.putExtra("c_seq", data.c_seq)
                startActivity(i)
            }
        })

        viewModel.alimlist.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()){
                adapter.notifyDataSetChanged()
            }
        })

        viewModel.isHartShop.observe(viewLifecycleOwner, Observer {
            if (it){
                var i = Intent(requireContext(), HeartsShopActivity::class.java)
                startActivity(i)
            }
        })
        viewModel.select_mycar_data.observe(viewLifecycleOwner, Observer { data ->
            if (data != null) {
                var i = Intent(requireContext(), MyCarBoardActivity::class.java)
                i.putExtra("mmi_seq", data.mmi_seq)
                startActivity(i)
            }
        })
        viewModel.toast.observe(viewLifecycleOwner, Observer { toast ->
            if (!toast.isNullOrEmpty()) {
                Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.ispasscheck.observe(viewLifecycleOwner, Observer { type ->
            if (type.isNullOrEmpty() == false) {
                var intent = Intent()
                if (type.equals("success")) {

                    intent = Intent(requireContext(), RecentDriveListActivity::class.java);
                    intent?.putExtra("passCheck", true)
                } else {
                    intent = Intent(requireContext(), RecentDriveListActivity::class.java);
                    intent?.putExtra("passCheck", false)
                }
                intent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent?.putExtra("b_seq", viewModel.select_drive.value!!)
                intent?.putExtra("push", false)
                if (intent != null) {
                    startActivity(intent)
                }
            }
        })
        viewModel.ismycarrank.observe(viewLifecycleOwner, Observer {
            if (it) {
                val i = Intent(requireContext(), MyCarBestBoardActivity::class.java)
                startActivity(i)
            }
        })
//
    }

    private fun clickListeners() {

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        application = requireContext().applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        clickListeners()
        //log
        observes()
        viewModel.email.value = application!!.userData!!.u_email.toString()


        var intent = Intent()
        var push = intent.getBooleanExtra("push", true)
        if (!push) {
            viewModel.toast.value = "드라이브 패스권을 구매 후 확인하실 수 있습니다"
        }
        //어댑터 연결하기
        adapter = AlimAdapter(requireContext(), arrayListOf()) { data, position ->

            Log.d("ㅇㅇ", data.toString())

            if (data.pa_body.equals("이메일을 입력해주세요.")) {
                var i = Intent(requireContext(), ProfileCorrectionActivity::class.java)
                i.putExtra("u_seq", viewModel.my_seq.value)
                i.putExtra("view", "수정")
                startActivity(i)
                //overridePendingTransition(R.anim. fadein, R.anim.fadeout)
            } else {
                var dd = data.pa_data
                try {
                    val obj: JsonObject = JsonParser.parseString(dd).getAsJsonObject()
                    var screen = obj.get("screen").toString().replace("\"", "")
                    var send_seq = obj.get("send_seq").toString().replace("\"", "")

                    Log.d("받는 pa_seq", data.pa_seq.toString())
                    Log.d("받는 screen", screen.toString())
                    Log.d("받는 send_seq", send_seq)
                    when (screen) {
                        "ApplyProfile", "SendProfile", "ReadProfile", "SendLike", "GivenLike", "AcceptLike", "SendOpenPhone", "OpenPhone", "GivenDrive" -> {
                            viewModel.send_seq.value = send_seq
                            viewModel.pa_seq.value = data.pa_seq.toString()
                            viewModel.pa_check.value = data.pa_check.toString()
                            viewModel.screen.value = screen
                            if (data.pa_check.toString().equals("0")) {

                                viewModel.AlimRead(data.pa_seq.toString(), screen, "0")
                            } else {
                                viewModel.getMatchingDataYN()
                            }

//
//                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
//                            var intent = Intent(this, ProfileActivity::class.java);
//                            intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                            intent.putExtra("screen", screen)
//                            intent.putExtra("sub_seq", send_seq)
//                            startActivity(intent)

                        }

                        "SendProfile", "SendDrive" -> {
                            viewModel.send_seq.value = send_seq
                            viewModel.pa_seq.value = data.pa_seq.toString()
                            viewModel.pa_check.value = data.pa_check.toString()
                            viewModel.screen.value = screen
                            if (data.pa_check.toString().equals("0")) {

                                viewModel.AlimRead(data.pa_seq.toString(), screen, "0")
                            } else {
                                viewModel.getMatchingDataYN()
                            }
                        }

                        "BoardComment", "BoardSubComment", "BoardLike" -> {
                            if (data.pa_check.toString().equals("0")) {

                                viewModel.AlimRead(data.pa_seq.toString(), screen, "0")
                            } else {
                                var b_seq = obj.get("b_seq").toString().replace("\"", "")
                                var send_seq = obj.get("send_seq").toString().replace("\"", "")
                                var uid = obj.get("uid").toString().replace("\"", "")
                                var intent = Intent(requireContext(), PostActivity::class.java);
                                intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                intent.putExtra("screen", screen)
                                intent.putExtra("sub_seq", send_seq)
                                intent.putExtra("u_seq", send_seq.toInt())
                                intent.putExtra("b_seq", b_seq)
                                intent.putExtra("uid", uid)
                                startActivity(intent)
                            }
                        }

                        "LocationDrive" -> {
                            if (data.pa_check.toString().equals("0")) {

                                viewModel.AlimRead(data.pa_seq.toString(), screen, "0")
                            } else {
                                var b_seq = obj.get("b_seq").toString().replace("\"", "")
                                var intent =
                                    Intent(
                                        requireContext(),
                                        LocationSpotDetailActivity::class.java
                                    );
                                intent!!.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                intent.putExtra("screen", screen)
                                intent.putExtra("spot_seq", b_seq)
                                startActivity(intent)
                                Log.d("이승주푸시 seq", b_seq)
                            }
                        }

                        "MycarComment", "MycarSubComment", "MycarLike" -> {
                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
                            if (data.pa_check.toString().equals("0")) {

                                viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
                            } else {
                                viewModel.getOneMycarData(b_seq)
                            }
                        }

                        "DriveLike" -> {
                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
                            var send_seq = obj.get("send_seq").toString().replace("\"", "")
                            var uid = obj.get("uid").toString().replace("\"", "")
                            if (data.pa_check.toString().equals("0")) {
                                viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
                            } else {
                                viewModel.DrivePassCheck(b_seq)
                            }
                        }

                        "MycarRankingHeart" -> {
                            if (data.pa_check.toString().equals("0")) {
                                var b_seq = obj.get("b_seq").toString().replace("\"", "")
                                viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
                            } else {
                                var intent =
                                    Intent(requireContext(), MyCarBestBoardActivity::class.java);
                                startActivity(intent)
                            }

                        }

                        "DrivePass30Buy", "DrivePass1Buy", "HeartCharge" ,"DrivePass1Date" ,"DrivePass3Date"-> {
                            if (data.pa_check.toString().equals("0")) {
                                var b_seq = obj.get("b_seq").toString().replace("\"", "")
                                viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
                            } else {

                                var intent = Intent(requireContext(), HeartsShopActivity::class.java);
                                startActivity(intent)
                            }

                        }

                    }
//                    if (screen.equals("ApplyProfile")|| screen.equals("MatchingManage")){
//
//
//
//
//
//                    }else if (screen.equals("BoardDetail")||screen.equals("BoardLike")){
//                        var uid = obj.get("uid").toString().replace("\"", "")
//                        if (screen.equals("BoardDetail")||screen.equals("BoardLike")) {
//                            var b_seq = obj.get("b_seq").toString().replace("\"", "")
//                            if (data.pa_check.toString().equals("0")) {
//
//                                viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
//                            } else {
//                                viewModel.getBoard(b_seq)
//                            }
//                        }
//
//                    }else if (screen.equals("MycarDetail")||screen.equals("MycarLike") ){
//                        var b_seq = obj.get("b_seq").toString().replace("\"", "")
//                        if (data.pa_check.toString().equals("0")) {
//
//                            viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
//                        } else {
//                            viewModel.getOneMycarData(b_seq)
//                        }
//                    }else if(screen.equals("DriveLike")){
//                        var b_seq = obj.get("b_seq").toString().replace("\"", "")
//                        var send_seq = obj.get("send_seq").toString().replace("\"", "")
//                        var uid = obj.get("uid").toString().replace("\"", "")
//                        if (data.pa_check.toString().equals("0")) {
//                            viewModel.AlimRead(data.pa_seq.toString(), screen, b_seq)
//                        }else {
//                            viewModel.DrivePassCheck(b_seq)
//                        }
//
//                    }else if (screen.equals("MycarRankingHeart")) {
//                        if (data.pa_check.toString().equals("0")) {
//                            viewModel.AlimRead(data.pa_seq.toString(), screen, "0")
//                        } else {
//                            viewModel.ismycarrank.value = true
//                        }
//                    }
//


                } catch (e: java.lang.Exception) {
                    Log.d("Exception", e.toString())
                }

            }


        }

        binding!!.rvNotice.adapter = adapter
        binding!!.rvNotice.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvNotice.setHasFixedSize(true)



        binding!!.rvNotice.adapter = adapter
        binding!!.rvNotice.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            @SuppressLint("NewApi")
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) view.viewTreeObserver.removeOnGlobalLayoutListener(
                    this
                ) else view.viewTreeObserver.removeGlobalOnLayoutListener(this)
            }
        })

//        binding!!.rvDriveAwayHot.addOnScrollListener(listener)
//        binding!!.rvDriveAwayHot.setHasFixedSize(true)

    }

    fun DrivePassCheck(
        u_seq: String,
        screen: String,
        send_seq: String,
        b_seq: String,
        uid: String
    ) {
        var intent: Intent? = null
        CoroutineScope(Dispatchers.IO).launch {
            val d = NetworkManager.serveradapter.create(MatchingService::class.java)
                .DrivePassCheck(u_seq)
            d!!.enqueue(object : retrofit2.Callback<ResultData> {
                override fun onResponse(
                    call: retrofit2.Call<ResultData>,
                    response: Response<ResultData>
                ) {
                    var data = response.body()
                    if (data != null) {
                        var type = data.type
                        if (type.equals("success")) {


                            intent = Intent(requireContext(), RecentDriveListActivity::class.java);
                            intent?.putExtra("passCheck", true)

                        } else {
                            intent = Intent(requireContext(), RecentDriveListActivity::class.java);
                            intent?.putExtra("passCheck", false)
                            //intent = Intent(this@MainActivity, AlimActivity::class.java);

                        }

                        intent?.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        intent?.putExtra("screen", screen)
                        intent?.putExtra("sub_seq", send_seq)
                        intent?.putExtra("b_seq", b_seq)
                        intent?.putExtra("uid", uid)
                        intent?.putExtra("push", false)
                        if (intent != null) {
                            startActivity(intent)
                        }


                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<ResultData>,
                    t: Throwable
                ) {
                    Log.d("check", t.toString())
                }

            })

        }
    }
//    var listener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
//        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//            super.onScrollStateChanged(recyclerView, newState)
//            var lastposition =
//                (binding?.rvDriveAwayHot?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
//            Log.d("getDrive  last", "" + lastposition)
//            var itemcount = adapter!!.itemCount
//
//            if (itemcount == (lastposition + 1)) {
//                var totalc = viewModel.totalCount.value!!
//                if (itemcount < totalc && totalc != 0) {
//                    var npage = viewModel.page.value!!
//                    if(viewModel.issend.value==false) {
//                        viewModel.page.value = npage + 1
//                        viewModel.getDrive(my_u_seq, false)
//                    }
//                }
//            }
//
//        }
//
//        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//            super.onScrolled(recyclerView, dx, dy)
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun newInstance(): Alim2Fragment {
        val args = Bundle()

        val fragment = Alim2Fragment()
        fragment.arguments = args
        return fragment
    }

    fun allDelet() {
        if (binding != null) {
            viewModel.alldelete()
        }

    }


}