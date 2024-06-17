package com.supercarlounge.supercar.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.AlimAdapter
import com.supercarlounge.supercar.adapter.NotiListAdapter
import com.supercarlounge.supercar.databinding.FragmentAlim1Binding
import com.supercarlounge.supercar.databinding.FragmentAlim2Binding
import com.supercarlounge.supercar.ui.activity.*
import com.supercarlounge.supercar.viewmodel.AlimViewModel
import com.supercarlounge.supercar.viewmodel.BoardViewModel


class Alim1Fragment() : Fragment() {
    val TAG = "Alim2Fragment"
    private var _binding: FragmentAlim1Binding? = null
    private val binding get() = _binding

    private lateinit var viewModel2: AlimViewModel
    private lateinit var  adapter : NotiListAdapter
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
        viewModel2 = ViewModelProvider(this).get(AlimViewModel::class.java)

        _binding = FragmentAlim1Binding.inflate(inflater, container, false)
        binding!!.viewModel = viewModel2
        binding!!.lifecycleOwner = this.viewLifecycleOwner
        application = requireContext().applicationContext as MainApplication
        fm = childFragmentManager
        my_u_seq = application!!.userData!!.u_seq.toString()
        val root: View = binding!!.root




        return root
    }

    override fun onResume() {
        super.onResume()
        viewModel2.getAnonymousForumPostNoti(viewModel2.notipage.value!!,viewModel2.ppp.value!!)
    }
    private fun observes() {
       
    }

    private fun clickListeners() {

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        application = requireContext().applicationContext as MainApplication
        viewModel2.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel2
        clickListeners()
        //log
        observes()



        var intent = Intent()
        var push = intent.getBooleanExtra("push",true)

        adapter = NotiListAdapter("알림",requireContext(),
            arrayListOf(),null,viewModel2) { data, position ->
            val i= Intent(requireActivity(), PostNoticeActivity::class.java)

            i.putExtra("n_seq", data.n_seq)
            i.putExtra("u_seq", data.u_seq)
            i.putExtra("n_sday", data.n_sday)
            i.putExtra("n_title", data.n_title)
            i.putExtra("n_eday", data.n_eday)
            i.putExtra("n_type", data.n_type)
            i.putExtra("n_date",data.n_date)
            i.putExtra("n_click",data.n_click)
            application?.htmlText = data.n_text!!
            startActivity(i)
            requireActivity().overridePendingTransition(R.anim. fadein, R.anim.fadeout)
        }
//        viewModel2.getAnonymousForumPostNoti(page,ppp)
        binding!!.rvAnonymousForumPostNoti.adapter = adapter
        binding!!.rvAnonymousForumPostNoti.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

//        getConcierge()
//        binding?.swipeContainer?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
//            viewModel2.page.value = 1
//            viewModel2.ppp.value = 15
//            viewModel2.getAnonymousForumPostNoti(viewModel2.notipage.value!!,viewModel2.ppp.value!!)
//
//            binding?.swipeContainer?.isRefreshing = false
//
//        })

        
//        binding!!.rvDriveAwayHot.addOnScrollListener(listener)
//        binding!!.rvDriveAwayHot.setHasFixedSize(true)

    }
    fun String.htmlToString() : String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            return Html.fromHtml(this).toString()
        }}
    var listener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            var lastposition=(binding?.rvAnonymousForumPostNoti?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            var itemcount=adapter!!.itemCount
            Log.d("getDrive : last" , ""+itemcount)
            Log.d("getDrive : itemcount" , ""+lastposition)
            if(itemcount== (lastposition +1)) {
                var totalc = viewModel2.totalCount.value!!
                if (itemcount < totalc&& totalc !=0) {
                    if (itemcount < viewModel2.totalCount.value!!) {

                        viewModel2.notipage.value = viewModel2.notipage.value?.plus(1)

                        viewModel2.getAnonymousForumPostNoti(viewModel2.notipage.value!!,viewModel2.ppp.value!!)
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
        _binding = null
    }

    fun newInstance(): Alim1Fragment {
        val args = Bundle()

        val fragment = Alim1Fragment()
        fragment.arguments = args
        return fragment
    }





}