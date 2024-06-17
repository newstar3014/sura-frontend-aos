package com.supercarlounge.supercar.ui.fragment.pageranonymousforumf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.LinearLayoutManagerWrapper
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.BoardListAdapter
import com.supercarlounge.supercar.databinding.FragmentAnonymousforumAllBinding
import com.supercarlounge.supercar.dialog.QuickUserDialog
import com.supercarlounge.supercar.enumset.QuickTypeStatus
import com.supercarlounge.supercar.ui.activity.JoinActivity
import com.supercarlounge.supercar.ui.activity.PostActivity
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class BoardListFragment() : Fragment() {

    private var _binding: FragmentAnonymousforumAllBinding? = null
    private var viewType : Int? = null
    private val binding get() = _binding
    private  var viewModel: BoardViewModel? = null
    private var  adapter : BoardListAdapter? = null
    private lateinit var application : MainApplication
    private var quickUserDialog: QuickUserDialog?= null
    private var fm :FragmentManager ? = null



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


//        var b = Bundle()
//    var    d=b.getInt("1")
//        Log.d("dddd", d.toString())

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
                var totalc = viewModel!!.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                    if (itemcount < viewModel!!.totalCount.value!!) {
                        if(viewModel!!.issend.value == false) {


                            viewModel!!.getAnonymousForumPostScroll(viewModel!!.filterArray[viewModel!!.viewType.value!!])
                            Log.d("getAnonymousForumPost", "getAnonymousForumPostScroll")
                        }
                    }
                }
            }
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
                viewModel!!.getAnonymousForumPost(
                    viewModel!!.filterArray[viewType!!],
                    b_contents,
                    bookmark,
                    order
                )
                Log.d("getAnonymousForumPost", "changeSearch")
            }


//        if ()


    }
    override fun onResume() {
        super.onResume()
//        viewModel!!.page.value = 1
//        var b_contents = (parentFragment as AnonymousForumFragment).viewModel?.b_contents?.value!!
//        changeSearch(b_contents,viewModel.bookmark.value!!,viewModel.order.value!!)


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
        viewModel?.userdata?.value = application?.userData
        observes()
        Glide.with(binding!!.ivLoading).load(R.raw.loading_anim).into(binding!!.ivLoading)

        adapter = BoardListAdapter(requireContext(),  arrayListOf(), viewModel!!) { data, time, position ->
            if (application.quick_view.equals("퀵유저")){
                ShowQuickUserDialog()
            }else {
                val i = Intent(requireActivity(), PostActivity::class.java)

                i.putExtra("c_seq", data.c_seq)
                i.putExtra("b_date", time)
                i.putExtra("b_contents", data.b_contents)
                i.putExtra("rn_nickname", data.rn_nickname)
                i.putExtra("image", data.b_cover)
                i.putExtra("views", data.views)
                i.putExtra("like", "+${data.b_like}")
                i.putExtra("u_gender", data.u_gender)
                i.putExtra("b_seq", data.b_seq.toString())
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("like_type", data.like_check)
                //Log.d("개인포스트", data.toString())
                resultLauncher.launch(i)

                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }



        val linearLayoutManagerWrapepr = LinearLayoutManagerWrapper(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding!!.rvAnonymousForumPostAll.adapter = adapter
        binding!!.rvAnonymousForumPostAll.layoutManager = linearLayoutManagerWrapepr
        binding!!.rvAnonymousForumPostAll.addOnScrollListener(listener)

        binding?.swipeContainer?.setOnRefreshListener(OnRefreshListener {
            viewModel!!.page.value =1
            viewModel!!.ppp.value =15
            viewModel!!.getAnonymousForumPost(viewModel!!.filterArray[viewType!!],viewModel!!.b_contents.value!!,viewModel!!.bookmark.value!!,viewModel!!.order.value!!)
            Log.d("getAnonymousForumPost","swipeContainer")
            Log.d("확인1", viewModel!!.filterArray[viewType!!])
            Log.d("확인2", viewModel!!.b_contents.value!!)
            Log.d("확인3", viewModel!!.order.value!!)
            binding?.swipeContainer?.isRefreshing = false

        })
//        getConcierge()
        viewModel!!.issend.observe(viewLifecycleOwner, Observer {
            if(it){
                binding!!.rvAnonymousForumPostAll.animate().translationY(-200f).setDuration(500).start()
            }else{
                binding!!.rvAnonymousForumPostAll.animate().translationY(0f).setDuration(500).start()
            }
        })
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data != null) {
                    var result =data.getStringExtra("type")

                    if (result!!.isNotEmpty()){
                        viewModel!!.getAnonymousForumPost(viewModel!!.filterArray[viewType!!],viewModel!!.b_contents.value!!,viewModel!!.bookmark.value!!,viewModel!!.order.value!!)
                        Log.d("getAnonymousForumPost","resultLauncher")
                    }


                }

            }
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
    fun newInstance(type : Int): BoardListFragment {
        Log.d("CALLFragment", "newInstance: ")
        val args = Bundle()
        args.putInt("type",type)
        val fragment = BoardListFragment()
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