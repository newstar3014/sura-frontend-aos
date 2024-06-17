package com.supercarlounge.supercar.ui.activity


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.ConciergeData
import com.supercarlounge.supercar.databinding.ActivityConciergeDetailBinding
import com.supercarlounge.supercar.viewmodel.ConciergeDetailViewModel


class ConciergeDetailActivity: BaseActivitiy() {
    var application:MainApplication?=null
    private lateinit var binding: ActivityConciergeDetailBinding
    private lateinit var viewModel: ConciergeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        application = applicationContext as MainApplication
        viewModel = ViewModelProvider(this).get(ConciergeDetailViewModel::class.java)
        binding = ActivityConciergeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel
        var data =intent.getParcelableExtra<ConciergeData>("data")
        if(data!=null) {
            viewModel.data.value = data
        }
        clickListeners()
        observes()



    }





    private fun observes() {
        viewModel.islink.observe(this, Observer {
            if(it){
                var link= viewModel.data.value!!.co_link
                var link_type= viewModel.data.value!!.co_link_type
                if(!link.isNullOrEmpty()){
                    if(link.startsWith("http")){
                        val packageManager: PackageManager = packageManager
                        if (isAppInstalled(getString(R.string.chrome_package), packageManager)) {

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            startActivity(intent)
                        } else {
                            if (isAppInstalled(getString(R.string.internet_package), packageManager)) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                startActivity(intent)
                            } else {
                                Toast.makeText(this,"크롬이나 삼성 인터넷이 설치되어 있지 않습니다", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else {
                        if (link_type.isNullOrEmpty() == false) {
                            if (link_type.equals("공지")) {
                                viewModel.getOneNoti(link)
                            }
                        }
                    }
                }else{
                    viewModel.toast.value = "해당 주소가 없습니다. 슈퍼카 라운지에 문의해주세요."
                }
            }
        })
        viewModel.isopen.observe(this, Observer {
            if(it){
                if (viewModel.data.value != null) {
                    var link = viewModel.data.value!!.co_link
                    var link_type = viewModel.data.value!!.co_link_type
                    if (link.isNullOrEmpty() == false) {
                        if(link.startsWith("http")){
                            val packageManager: PackageManager = packageManager
                            if (isAppInstalled(getString(R.string.chrome_package), packageManager)) {

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                startActivity(intent)
                            } else {
                                if (isAppInstalled(getString(R.string.internet_package), packageManager)) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this,"크롬이나 삼성 인터넷이 설치되어 있지 않습니다", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }else{
                            if (link_type.isNullOrEmpty() == false) {
                                if (link_type.equals("공지")) {
                                    viewModel.getOneNoti(link)
                                }
                            }
                        }
                    }

                }
            }
        })
        viewModel.toast.observe(this, Observer { toast->
            if(!toast.isNullOrEmpty()){
                Toast.makeText(this,toast,Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.data.observe(this, Observer {it->
            if (it!= null){
                for (i in   it.coTextList()){
                    setAddView(i)
                }
                for (i in   it.Getcoinfo()){
                    setinfoview(i)
                }
            }
        })
        viewModel.select_bannernoti_data.observe(this, Observer { data->
            if (data != null){
                val i= Intent(this, PostNoticeActivity::class.java)

                i.putExtra("n_seq", data.n_seq)
                i.putExtra("u_seq", data.u_seq)
                i.putExtra("n_sday", data.n_sday)
                i.putExtra("n_title", data.n_title)
                i.putExtra("n_eday", data.n_eday)
                i.putExtra("n_type", data.n_type)
                i.putExtra("n_date",data.n_date)
                i.putExtra("n_click",data.n_click)
                application!!.htmlText = data.n_text!!
                startActivity(i)
            }
        })
    }
    private fun setAddView(s: String) {
        val txt1 = TextView(this)

        txt1.textSize = 13f
        txt1.visibility = View.VISIBLE
        txt1.text = "⦁  "+s
        var  typeface = ResourcesCompat.getFont(this, R.font.inter_medium);
        txt1.typeface = typeface
        txt1.setTextColor(ContextCompat.getColor(this, R.color.white_90))
        binding.llAddView.addView(txt1) // LinearLayout에 textView 추가
    }
    private fun setinfoview(s: String) {
        val txt1 = TextView(this)

        txt1.textSize = 13f
        txt1.visibility = View.VISIBLE
        txt1.text = "⦁  "+s
        var  typeface = ResourcesCompat.getFont(this, R.font.inter_medium);
        txt1.typeface = typeface
        txt1.setTextColor(ContextCompat.getColor(this, R.color.white_90))
        binding.llInfo.addView(txt1) // LinearLayout에 textView 추가
    }
    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }
    override fun finish() {
        super.finish()
      // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    override fun onDestroy() {
        super.onDestroy()

    }

    object BindingAdapters {
        @JvmStatic
        @BindingAdapter("visibleGone")
        fun showHide(view: View, text: String) {
            view.visibility = if (text.isEmpty()) View.GONE else View.VISIBLE
        }
    }
    fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }
}
