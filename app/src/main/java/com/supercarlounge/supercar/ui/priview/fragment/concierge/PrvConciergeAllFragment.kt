package com.supercarlounge.supercar.ui.priview.fragment.concierge

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.ConciergeAdapter
import com.supercarlounge.supercar.data.ConciergeData
import com.supercarlounge.supercar.databinding.FragmentConcieAllBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.viewmodel.ConciergePageViewModel


class PrvConciergeAllFragment : Fragment() {
    var application:MainApplication?=null
    private var _binding: FragmentConcieAllBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: ConciergePageViewModel
    private var  adapter : ConciergeAdapter? = null

    private var quickUserDialog: QuickUserDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        application = requireContext().applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(ConciergePageViewModel::class.java)
        _binding = FragmentConcieAllBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        var type =requireArguments().getString("type")
        viewModel.type.value = type
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner = this


        return root
    }


    override fun onResume() {
        super.onResume()
        Log.d("CALLFragment", adapter!!.itemCount.toString())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ConciergeAdapter(requireContext(), arrayListOf()) { data, position ->
            ShowQuickUserDialog()
        }

        binding!!.rvConcierge.adapter = adapter
        binding!!.rvConcierge.layoutManager = LinearLayoutManager(requireActivity())
        addconciert(viewModel.type.value!!)
//        viewModel.getConcierge()


    }
    private fun ShowQuickUserDialog() {
        quickUserDialog =
            QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
                if (okcancel) {
                    quickUserDialog!!.dismiss()
                    val i = Intent(requireActivity(), JoinActivity::class.java)
                    i.putExtra("qickview","퀵유저")
                    i.putExtra("u_name", application!!.userBasicData?.u_name)
                    i.putExtra("u_gender", application!!.userBasicData?.u_gender)
                    i.putExtra("u_birthday", application!!.userBasicData?.u_birthday)
                    i.putExtra("u_phone", application!!.userBasicData?.u_phone)
                    Log.d("아이템3", application!!.userBasicData.toString())
                    startActivity(i)
                } else {
                    quickUserDialog!!.dismiss()
                }
            }
        quickUserDialog!!.show(childFragmentManager, "")
    }
    @SuppressLint("SuspiciousIndentation")
    fun addconciert(type:String){
        var list = arrayListOf<ConciergeData>()
            if(type.equals("전체")){
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2023-05-31concierge/20230531142617게러지드블랑썸네일.png","게러지드블랑",0,"차마카세","차마카세", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2023-06-02concierge/20230602110651KakaoTalk2023053017123007302.png","게이즈샵",0,"별도문의","별도문의", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2023-05-16concierge/20230516122917logo스타일카케어.png","스타일카케어",1,"","", "[\"내차소 게시글 작성이벤트\",\"슈라 회원이라면 슈라 세차권 증정!\"]","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164321lo01.png","파리스토테일러",0,"10% 즉시사용가능 적립금","25% 즉시사용 가능 적립금",
                    "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209154328logoJJ팩토리.png","JJ팩토리",0,"회원 5% 할인 제공","VIP회원 10% 할인 제공",
                    "[\"차량 기본 전체점검 서비스 제공\"]","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164548Frame.png","휴먼성형외과",0,"별도문의","별도문의", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153802logo닥터스.png","닥터스피부과",0,"","",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164443lo04.png","솔라가드",0,"솔라가드프리미엄 10%할인","솔라가드프리미엄 20%할인", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164351lo02.png","띠에노",0,"추천인 코드 등록 시 적립금","5%할인 + 프리미엄 패키징서비스",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-11-11concierge/20221111164516lo05.png","비비드골프",0,"추가 5%적립","추가 20%적립",  "","","","","","",""))

                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164414lo03.png","바로가",0,"즉시사용가능 적립금 2만원","즉시사용가능 적립금 3만원", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153831logo에너캠프1.png","에너캠프",0,"별도문의","별도문의",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153842logo패브.png","패브",0,"별도문의","별도문의",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153818logo해리슨테일러.png","헤리슨테일러",0,"별도문의","별도문의",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153908logo코드디.png","코드디",0,"별도문의","별도문의",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153936logo스파에이르.png","스파에이르",0,"별도문의","별도문의",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153855logo에어차터코리아.png","에어차터코리아",0,"별도문의","별도문의", "","","","","","",""))

                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153922logovip리무진코리아.png","VIP 리무진 코리아",0,"별도문의","별도문의",  "","","","","","",""))

            }
        else if(type.equals("1")){
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153802logo닥터스.png","닥터스피부과",0,"별도문의","별도문의",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164548Frame.png","휴먼성형외과",0,"","",  "","","","","","",""))
            }
            else if(type.equals("2")){
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164321lo01.png","파리스토테일러",0,"10% 즉시사용가능 적립금","25% 즉시사용 가능 적립금",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164351lo02.png","띠에노",0,"추천인 코드 등록 시 적립금","5%할인 + 프리미엄 패키징서비스",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153818logo해리슨테일러.png","헤리슨테일러",0,"","",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153842logo패브.png","패브",0,"","",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153908logo코드디.png","코드디",0,"","",  "","","","","","",""))
            }
            else if(type.equals("3")){
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164443lo04.png","솔라가드",0,"솔라가드프리미엄 10%할인","솔라가드프리미엄 20%할인", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164414lo03.png","바로가",0,"즉시사용가능 적립금 2만원","즉시사용가능 적립금 3만원",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153831logo에너캠프1.png","에너캠프",0,"","", "","","","","","",""))
            }
            else if(type.equals("4")){
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3002/images/2022-11-11concierge/20221111164351lo02.png","비비드골프",0,"추가 5%적립","추가 20%적립",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153855logo에어차터코리아.png","에어차터코리아",0,"","", "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153922logovip리무진코리아.png","VIP 리무진 코리아",0,"","",  "","","","","","",""))
                list.add(ConciergeData(0,0,"","https://supercarlounge.com:3000/images/2022-12-09concierge/20221209153936logo스파에이르.png","스파에이르",0,"","",  "","","","","","",""))
            }
            else if(type.equals("5")){

            }
            else if(type.equals("6")){

            }
        viewModel.conciergelist.value =list
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : String): PrvConciergeAllFragment {
        val args = Bundle()
        args.putString("type",type)
        val fragment = PrvConciergeAllFragment()
        fragment.arguments = args
        return fragment
    }


}