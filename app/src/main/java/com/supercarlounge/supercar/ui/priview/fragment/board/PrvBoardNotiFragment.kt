package com.supercarlounge.supercar.ui.priview.fragment.board

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.NotiListAdapter
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.databinding.FragmentAnonymousforumNotiBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.activity.PostNoticeActivity
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class PrvBoardNotiFragment : Fragment() {

    private var _binding: FragmentAnonymousforumNotiBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: BoardViewModel
    private var  adapter : NotiListAdapter? = null
    private lateinit var application: MainApplication
    private var quickUserDialog: QuickUserDialog?= null
    private var fm : FragmentManager? = null



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
        fm = childFragmentManager

        return root
    }




    fun changeSearch(b_contents:String,bookmark:String,order:String){
        Log.d("외부이벤트 get changeSearch",b_contents)
            viewModel.b_contents.postValue(b_contents)


    }
    override fun onResume() {
        super.onResume()
        Log.d("CALLFragment", adapter!!.itemCount.toString())
        addnoti()
        //viewModel.getAnonymousForumPostNoti(viewModel.notipage.value!!,viewModel.ppp.value!!)
    }
    fun addnoti(){
        var list : ArrayList<NotiData> = arrayListOf()
        list.add(NotiData("","","공지","슈라! 문자 기능 업데이트!","","","","2022-10-24 11:00:00",""))
        list.add(NotiData("","","공지","2022 대한민국 프리미엄 브랜드 대상 수상","","","","2022-10-15 11:00:00",""))
        list.add(NotiData("","","공지","2022 대한민국 굿컴퍼니 대상 수상","","","","2022-10-15 11:00:00",""))
        list.add(NotiData("","","공지","기쁨과 사랑이 가득한,추석 보내세요!","","","","2022-09-09 11:00:00",""))
        list.add(NotiData("","","공지","슈라 드라이브 이용 가이드","","","","2022-08-06 11:00:00",""))
        list.add(NotiData("","","공지","슈라 게시판 이용 가이드","","","","2022-08-06 11:00:00",""))
        viewModel.anonymousForumNoitList.value = list
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NotiListAdapter("게시판",requireContext(),
            arrayListOf(),viewModel!!,null) { data, position ->
            ShowQuickUserDialog()
        }
//        viewModel.getAnonymousForumPostNoti(page,ppp)
        binding!!.rvAnonymousForumPostNoti.adapter = adapter
        binding!!.rvAnonymousForumPostNoti.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

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



    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("CALLFragment", "onDestroyView: ")
    }
    fun newInstance(type : Int): PrvBoardNotiFragment {
        val args = Bundle()

        val fragment = PrvBoardNotiFragment()
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
}