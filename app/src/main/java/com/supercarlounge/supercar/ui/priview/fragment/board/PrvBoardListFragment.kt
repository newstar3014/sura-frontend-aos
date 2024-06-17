package com.supercarlounge.supercar.ui.priview.fragment.board

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.supercarlounge.supercar.LinearLayoutManagerWrapper
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.adapter.BoardListAdapter
import com.supercarlounge.supercar.data.BoardData
import com.supercarlounge.supercar.databinding.FragmentAnonymousforumAllBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.viewmodel.BoardViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class PrvBoardListFragment() : Fragment() {

    private var _binding: FragmentAnonymousforumAllBinding? = null
    private var viewType : Int? = null
    private val binding get() = _binding
    private  var viewModel: BoardViewModel? = null
    private var  adapter : BoardListAdapter? = null
    private lateinit var application : MainApplication
    private var quickUserDialog: QuickUserDialog?= null
    private var fm :FragmentManager ? = null
    var day = 25
    var month = 9
    var monthString ="09"
    var dayString ="01"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      viewModel = ViewModelProvider(this).get(BoardViewModel::class.java)

        _binding = FragmentAnonymousforumAllBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        binding!!.viewModel = viewModel
        var vtype = requireArguments().getInt("type")
        viewType = vtype
        binding!!.lifecycleOwner =  this.viewLifecycleOwner
        viewModel!!.viewType.value = viewType
        fm = childFragmentManager


        return root
    }



    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition=(binding?.rvAnonymousForumPostAll?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=adapter!!.itemCount
            Log.d("getDrive : last" , ""+itemcount)
            Log.d("getDrive : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
//                var totalc = viewModel!!.totalCount.value!!
//                if (itemcount < totalc&& totalc !=0) {
//                    if (itemcount < viewModel!!.totalCount.value!!) {
                        if(viewModel!!.viewType.value!!  < 3) {
                            viewModel!!.page.value = viewModel!!.page.value?.plus(1)
//                            addboard(viewModel!!.viewType.value!!, false)
                        }
                       // viewModel!!.getAnonymousForumPostScroll(viewModel!!.filterArray[viewModel!!.viewType.value!!])
                        Log.d("getAnonymousForumPost","getAnonymousForumPostScroll")
                    }
//                }
//            }
        }
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }

    fun changeSearch(b_contents:String,bookmark:String,order:String){
        Log.d("외부이벤트 get changeSearch",b_contents)
            if(viewModel!=null) {
                viewModel!!.b_contents.postValue(b_contents)
                viewModel!!.bookmark.postValue(bookmark)
                viewModel!!.order.postValue(order)
                addboard(viewModel!!.viewType.value!!,true)
//                viewModel!!.getAnonymousForumPost(
//                    viewModel!!.filterArray[viewType!!],
//                    b_contents,
//                    bookmark,
//                    order
//                )
                Log.d("getAnonymousForumPost", "changeSearch")
            }


//        if ()


    }
    override fun onResume() {
        super.onResume()
        viewModel!!.page.value = 1
        addboard(viewModel!!.viewType.value!!,true)
        Log.d("외부이벤트 뷰모델",""+ viewModel)
        Log.d("리스트 onResume","type :$viewType "+ viewModel!!.anonymousForumList.value.toString())


    }

    override fun onPause() {
        super.onPause()
//        activity?.viewModelStore?.clear();

    }

    override fun onDestroy() {
        super.onDestroy()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        application=requireContext()!!.applicationContext as MainApplication
        observes()
        adapter = BoardListAdapter(requireContext(),  arrayListOf(), viewModel!!) { data, time, position ->
                ShowQuickUserDialog()

        }



        val linearLayoutManagerWrapepr = LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding!!.rvAnonymousForumPostAll.adapter = adapter
        binding!!.rvAnonymousForumPostAll.layoutManager = linearLayoutManagerWrapepr
        binding!!.rvAnonymousForumPostAll.addOnScrollListener(listener)

        binding?.swipeContainer?.setOnRefreshListener(OnRefreshListener {
            viewModel!!.page.value =1
            viewModel!!.ppp.value =15
            addboard(viewModel!!.viewType.value!!,true)
            //viewModel!!.getAnonymousForumPost(viewModel!!.filterArray[viewType!!],viewModel!!.b_contents.value!!,viewModel!!.bookmark.value!!,viewModel!!.order.value!!)
            Log.d("getAnonymousForumPost","swipeContainer")
            Log.d("확인1", viewModel!!.filterArray[viewType!!])
            Log.d("확인2", viewModel!!.b_contents.value!!)
            Log.d("확인3", viewModel!!.order.value!!)
            binding?.swipeContainer?.isRefreshing = false

        })
//        getConcierge()

    }
    fun addboard(viewTypeSet: Int,isstart:Boolean){
        var list :ArrayList<BoardData>? = arrayListOf()

        if(isstart == false){
            list!!.addAll(viewModel!!.anonymousForumList.value!!)


        }
        if (day >1){
            day = day.minus(1)

        }else{
            day = 30
        }

        if (day <10){
            dayString = "0"+day.toString()+"T"
        }else{
            dayString = day.toString()+"T"
        }

        if (month >1){
            month = month.minus(1)
        }else{
            month = 12

        }
        if (month <10){
            monthString = "0"+month.toString()

        }else{
            monthString = month.toString()
        }
        Log.d("시간 월", month.toString())
        Log.d("시간 데이", day.toString())
        when(viewTypeSet){
            1->{
                //전체
                list!!.add(BoardData("",9,"헬린이 입니다. 헬스장 추천좀 부탁해요.","https://supercarlounge.com:3002/images/preview/Rectangle-1631.png","",GetPrvTime(0,0,1),0,0,0,"","","",0,"","","","골프/운동","","",0,"","FEMALE","",0,"",0,0,100))
                list!!.add(BoardData("",9,"서킷다녀왔습니다. 역시 재밌네요.","https://supercarlounge.com:3002/images/preview/Rectangle-1632.png","",GetPrvTime(0,0,3),0,0,0,"","","",0,"","","","연예","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"이번 주말은 뭐할까?.","","",GetPrvTime(0,0,3),0,0,0,"","","",0,"","","","연예","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",0,"차량 선택 고민중입니다 조언좀 부탁드려요ㅠㅠ","","",GetPrvTime(0,0,5),0,0,0,"","","",0,"","","","캠핑","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"커피 좋아하세요?","https://supercarlounge.com:3002/images/preview/Rectangle-1633.png","",GetPrvTime(0,0,10),0,0,0,"","","",0,"","","","연예","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"다들 재테크 어떻게 하고 계신가요?","https://supercarlounge.com:3002/images/preview/Rectangle-1634.png","",GetPrvTime(0,0,15),0,0,0,"","","",0,"","","","연예","","",0,"","FEMALE","",0,"",0,0,7))
                list!!.add(BoardData("",9,"오늘 오전 드라이브 놓쳤는데, 오후 드라이브","https://supercarlounge.com:3002/images/preview/Rectangle-1635.png","",GetPrvTime(0,0,30),0,0,0,"","","",0,"","","","캠핑","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"이번에 파티하면 나도 가고싶다구요~~","","",GetPrvTime(0,0,40),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"차량선택 고민중입니다. 조언좀부탁드려요","","",GetPrvTime(0,0,50),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"학디야에서 모여서 회원님들이랑","https://supercarlounge.com:3002/images/preview/Rectangle-1636.png","",GetPrvTime(0,1,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,7))
                list.add(BoardData("",20,"불금인데 드라이브 가고 싶네요","","",GetPrvTime(0,2,0),0,0,0,"","","",0,"","","","재테크","","",0,"","MALE","",0,"",0,0,25))
                list.add(BoardData("",9,"이번주 주말은 뭐할까?","https://supercarlounge.com:3002/images/preview/Rectangle-1637.png","",GetPrvTime(0,2,0),0,0,0,"","","",0,"","","","골프/운동","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"어디 놀러가 볼까용??","https://supercarlounge.com:3002/images/preview/Rectangle-1638.png","",GetPrvTime(0,3,0),0,0,0,"","","",0,"","","","CAR","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"출출하다 밥 뭐 먹을까?","","",GetPrvTime(0,3,0),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"즐거운 한주 보내십쇼","https://supercarlounge.com:3002/images/preview/Rectangle-1636.png","",GetPrvTime(0,4,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,7))
                list.add(BoardData("",20,"오늘 오전 드라이브 놓쳤는데","","",GetPrvTime(0,7,0),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,25))
                list!!.add(BoardData("",9,"뚜따 드라이브 시켜주세요?","","",GetPrvTime(1,1,0),0,0,0,"","","",0,"","","","골프/운동","","",0,"","FEMALE","",0,"",0,0,100))

            }
            2->{
                //베스트
                list!!.add(BoardData("",9,"여의도에서 다같이 즐겨요 ㅎㅎ","","",GetPrvTime(0,5,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"골프하러 왔어요 ㅋㅋㅋ","","",GetPrvTime(0,7,0),0,0,0,"","","",0,"","","","골프/운동","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"벤츠 신차로 뽑았어요 ㅋㅋㅋㅋ","","",GetPrvTime(0,10,0),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"강원도 계곡 차박","","",GetPrvTime(0,11,0),0,0,0,"","","",0,"","","","캠핑","","",0,"","FEMALE","",0,"",0,0,7))
                list.add(BoardData("",20,"서킷다녀왔습니다. 역시 재밌네요.","","",GetPrvTime(1,1,0),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,25))
                list.add(BoardData("",9,"노량진 다녀왔어요 ㅋㅋㅋ","","",GetPrvTime(2,1,0),0,0,0,"","","",0,"","","","골프/운동","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"최신 벤츠 뭐 살까요?","","",GetPrvTime(3,1,0),0,0,0,"","","",0,"","","","CAR","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"아우디 샀어요 ㅋㅋㅋ","","",GetPrvTime(4,1,0),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"여의도 불꽃축제","","",GetPrvTime(5,1,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,7))
                list.add(BoardData("",20,"차가운 맥주 개꿀","","",GetPrvTime(6,1,0),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,25))
            }
            3->{
                //자유
                list!!.add(BoardData("",1,"치킨 같이 먹으러가실분?","","",GetPrvTime(0,1,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,7))
                list.add(BoardData("",20,"코로나 걸렸네요 ㅠㅠ","","",GetPrvTime(0,2,0),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,25))
                list.add(BoardData("",0,"이번주 주말은 뭐할까?","","",GetPrvTime(0,3,0),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",1,"커피 좋아하세요?","","",GetPrvTime(0,4,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,7))
                list!!.add(BoardData("",9,"여의도에서 다같이 즐겨요 ㅎㅎ","","",GetPrvTime(0,5,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",1,"여의도 불꽃축제","","",GetPrvTime(0,6,0),0,0,0,"","","",0,"","","","자유","","",0,"","FEMALE","",0,"",0,0,7))
                list.add(BoardData("",20,"차가운 맥주 개꿀","","",GetPrvTime(0,7,0),0,0,0,"","","",0,"","","","자유","","",0,"","MALE","",0,"",0,0,25))
            }
            4->{
                //연애
            }
            5->{
                //세차
                list!!.add(BoardData("",9,"세차하고 싶은데 비와서 ㅠㅠ..","","",GetPrvTime(0,8,0),0,0,0,"","","",0,"","","","연예","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"셀프 세차 추워서 빡시군요 ㅠㅠ..","","",GetPrvTime(0,9,0),0,0,0,"","","",0,"","","","연예","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"세차 시작하려니까 비오네요 ㅠㅠ...","","",GetPrvTime(0,10,0),0,0,0,"","","",0,"","","","연예","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"오늘 날씨 굿잡 ㅋㅋㅋ","","",GetPrvTime(0,11,0),0,0,0,"","","",0,"","","","연예","","",0,"","FEMALE","",0,"",0,0,7))

            }
            6->{
                //CAR
                list!!.add(BoardData("",9,"슈라에서 만난분들과 놀러다녀왔습니다. ㅋㅋ","","",GetPrvTime(0,6,0),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"드벙가즈아~!!","","",GetPrvTime(0,7,0),0,0,0,"","","",0,"","","","CAR","","",0,"","MALE","",0,"",0,0,8))
                list.add(BoardData("",5,"놀쟈 ㅋㅋㅋㅋㅋ","","",GetPrvTime(0,8,0),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,20))
                list.add(BoardData("",1,"드라이브 갑시다~~","","",GetPrvTime(0,10,0),0,0,0,"","","",0,"","","","CAR","","",0,"","FEMALE","",0,"",0,0,7))

            }
            7->{
                //캠핑
                list!!.add(BoardData("",9,"겨울 휴가다 ㅋㅋㅋ","","",GetPrvTime(0,1,0),0,0,0,"","","",0,"","","","캠핑","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"강원도 글램핑 왓어요 ㅋㅋ","","",GetPrvTime(0,2,0),0,0,0,"","","",0,"","","","캠핑","","",0,"","MALE","",0,"",0,0,8))

            }
            8->{
                //골프/운동
                list!!.add(BoardData("",9,"골치러 가시죠","","",GetPrvTime(0,1,0),0,0,0,"","","",0,"","","","골프/운동","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"겨울철 운동 가는게 세상에서 제일 힘들어용","","",GetPrvTime(0,2,0),0,0,0,"","","",0,"","","","골프/운동","","",0,"","MALE","",0,"",0,0,8))
            }
            9->{
                //핫플
                list!!.add(BoardData("",9,"할맥 돈까스 존맛탱입니다.","","",GetPrvTime(0,1,0),0,0,0,"","","",0,"","","","핫플","","",0,"","FEMALE","",0,"",0,0,100))
                list.add(BoardData("",0,"팔당오징어 아시는분?","","",GetPrvTime(0,2,0),0,0,0,"","","",0,"","","","핫플","","",0,"","MALE","",0,"",0,0,8))
            }
            10->{
                //재테크
            }
            11->{
                //직업
            }
            12->{
                //서킷
            }
            13->{
                //+19
            }

        }
        viewModel!!.anonymousForumList.value = list
    }

    private fun observes() {
        viewModel!!.anonymousForumList.observe(viewLifecycleOwner, Observer {


                if (it != null) {

                    if (viewModel!!.page.value == 1){
                        adapter?.setdata(it)
                        Log.d("체크데이터1", viewType.toString())
                    }else{
                        adapter?.adddata(it)
                        Log.d("체크데이터2", viewType.toString())
                    }

                }

        })



    }
    override fun onDestroyView() {
        super.onDestroyView()
    viewModel!!.state.value = false

        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : Int): PrvBoardListFragment {
        Log.d("CALLFragment", "newInstance: ")
        val args = Bundle()
        args.putInt("type",type)
        val fragment = PrvBoardListFragment()
        fragment.arguments = args
        return fragment
    }

    private fun ShowQuickUserDialog(){
        quickUserDialog = QuickUserDialog(requireContext(), QuickTypeStatus.QUICK_WARNING) { type, okcancel ->
            if (okcancel) {
                quickUserDialog!!.dismiss()
                val i = Intent(requireActivity(), JoinActivity::class.java)
                i.putExtra("qickview","퀵유저")
                i.putExtra("u_name", application.userBasicData?.u_name)
                i.putExtra("u_gender", application.userBasicData?.u_gender)
                i.putExtra("u_birthday",application.userBasicData?.u_birthday)
                i.putExtra("u_phone", application.userBasicData?.u_phone)
                Log.d("아이템3", application.userBasicData.toString())
                startActivity(i)
            } else {
                quickUserDialog!!.dismiss()
            }
        }
        quickUserDialog!!.show(fm!!, "")
    }
    fun GetPrvTime(date:Int,hour:Int,min:Int):String{
        // 현재 시간 가져오기
        val currentTime = Date()

        // Calendar 객체를 사용하여 현재 시간에서 1분 전의 시간 계산
        val calendar = Calendar.getInstance()
        calendar.time = currentTime
        if(date > 0) {
            calendar.add(Calendar.DAY_OF_MONTH, (-1*date))
        }
        if (hour > 0){
            calendar.add(Calendar.HOUR_OF_DAY, (-1*hour))
        }
        if (min > 0){
            calendar.add(Calendar.MINUTE, (-1*min))
        }

        val oneMinuteAgo = calendar.time

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var result = dateFormat.format(oneMinuteAgo)
        return  result
    }
}