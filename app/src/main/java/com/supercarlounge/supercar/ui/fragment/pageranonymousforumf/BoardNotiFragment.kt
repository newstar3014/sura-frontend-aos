package com.supercarlounge.supercar.ui.fragment.pageranonymousforumf

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide

import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.NotiListAdapter
import com.supercarlounge.supercar.databinding.FragmentAnonymousforumNotiBinding
import com.supercarlounge.supercar.ui.activity.PostNoticeActivity
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class BoardNotiFragment : Fragment() {

    private var _binding: FragmentAnonymousforumNotiBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: BoardViewModel
    private var  adapter : NotiListAdapter? = null
    private lateinit var application: MainApplication




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      viewModel = ViewModelProvider(this).get(BoardViewModel::class.java)

        _binding = FragmentAnonymousforumNotiBinding.inflate(inflater, container, false)

        val root: View = binding!!.root
        binding!!.viewModel = viewModel
        binding!!.lifecycleOwner =  this.viewLifecycleOwner
        application = requireContext()!!.applicationContext as MainApplication


        return root
    }




    fun changeSearch(b_contents:String,bookmark:String,order:String){
        Log.d("외부이벤트 get changeSearch",b_contents)
            viewModel.b_contents.postValue(b_contents)


    }
    override fun onResume() {
        super.onResume()
        Log.d("CALLFragment", adapter!!.itemCount.toString())
        viewModel.getAnonymousForumPostNoti(viewModel.notipage.value!!,viewModel.ppp.value!!)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        adapter = ConciergeAdapter(requireContext(), getConcierge(), viewModel) { data, position ->
//            val i= Intent(requireActivity(), AffiliateActivity::class.java)
//            i.putExtra("titleText",data.co_company)
//            i.putExtra("boonText",data.co_vip)
//            startActivity(i)
//            requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
//        }
        Glide.with(binding!!.ivLoading).load(R.raw.loading_anim).into(binding!!.ivLoading)

        adapter = NotiListAdapter("게시판",requireContext(),
            arrayListOf(),viewModel,null) { data, position ->
            val i= Intent(requireActivity(), PostNoticeActivity::class.java)

                i.putExtra("n_seq", data.n_seq)
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("n_sday", data.n_sday)
                i.putExtra("n_title", data.n_title)
                i.putExtra("n_eday", data.n_eday)
                i.putExtra("n_type", data.n_type)
                i.putExtra("n_date",data.n_date)
            i.putExtra("n_click",data.n_click)
            application.htmlText = data.n_text!!
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }
//        viewModel.getAnonymousForumPostNoti(page,ppp)
        binding!!.rvAnonymousForumPostNoti.adapter = adapter
        binding!!.rvAnonymousForumPostNoti.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

//        getConcierge()
        binding?.swipeContainer?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            viewModel.page.value = 1
            viewModel.ppp.value = 15
            viewModel.getAnonymousForumPostNoti(viewModel.notipage.value!!,viewModel.ppp.value!!)

            binding?.swipeContainer?.isRefreshing = false

        })

    }



    fun String.htmlToString() : String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            return Html.fromHtml(this).toString()
        }}
//    private fun testPostNotiData(): ArrayList<AnonymousForumPostNotiData> {
//        var list : ArrayList<AnonymousForumPostNotiData> = arrayListOf()
//        var x = 0
//        for(i: Int in 0 .. 10){
//            x = x.plus(1)
//            var data = AnonymousForumPostNotiData("$x +공지입444","공지사항제목","[공지]","설정 즉시 적용되며, 지인으로 등록된 회원과 매칭이\n" +
//                    "불가능한 것은 아닙니다.\n" +
//                    "\n" +
//                    "지인 매칭, 리매칭 시 상대방에게 매칭 동의를 얻는\n" +
//                    "절차가 추가됩니다.\n" +
//                    "\n" +
//                    "원치 않으실 경우 지인으로 등록된 회원이 누구인지\n" +
//                    "서로 알 수 없으며 지인 매칭, 리매칭의 경우 소모되는하트가 최초 매칭과 달라 신중하게 선택하셔야 합니다.\n" +
//                    "\n" +
//                    "\n" +
//                    "감사합니다.\n", "2022.02.01")
//            list.add(data)
//        }
//        return  list
//    }
var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        var lastposition=(binding?.rvAnonymousForumPostNoti?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        var itemcount=adapter!!.itemCount
        Log.d("getDrive : last" , ""+itemcount)
        Log.d("getDrive : itemcount" , ""+lastposition)
        if(itemcount== (lastposition +1)) {
            var totalc = viewModel.totalCount.value!!
            if (itemcount < totalc&& totalc !=0) {
                if (itemcount < viewModel.totalCount.value!!) {

                    viewModel.notipage.value = viewModel.notipage.value?.plus(1)

                    viewModel.getAnonymousForumPostNoti(viewModel.notipage.value!!,viewModel.ppp.value!!)
                }
            }
        }
    }
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
    }
}


    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : Int): BoardNotiFragment {
        val args = Bundle()

        val fragment = BoardNotiFragment()
        fragment.arguments = args
        return fragment
    }
//    private class ImageGetter(var context:Context) : Html.ImageGetter {
//        override fun getDrawable(source: String): Drawable {
//            var source = source
//            source = source.replace(".png".toRegex(), "") //이때 .png .jpg 등이 붙어 있으면 drawable에서 이미지를 못불러 온다.
//            val id: Int = context.resources.getIdentifier(source, "drawable", context.packageName)
//            val d: Drawable = context.resources.getDrawable(id)
//            val w = d.intrinsicWidth
//            val h = d.intrinsicHeight
//            d.setBounds(0, 0, w * 25 / h, 25) //이미지 크기 설정
//            return d
//        }
//    }

}