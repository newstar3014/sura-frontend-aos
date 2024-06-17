package com.supercarlounge.supercar.ui.activity


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.PopInfoPagerAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.PopUpinfoData
import com.supercarlounge.supercar.databinding.ActivityPopInfoBinding
import com.supercarlounge.supercar.dialog.GuideDialog
import com.supercarlounge.supercar.enumset.GuidePopType
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.viewmodel.PopInfoViewModel
import kotlinx.coroutines.Job


class PopInfoActivity : BaseActivitiy() {
    var application:MainApplication?=null
    private lateinit var binding: ActivityPopInfoBinding
    private lateinit var viewModel: PopInfoViewModel
    var pager: PopInfoPagerAdapter? = null
    private var job : Job? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.background_trn);

        viewModel = ViewModelProvider(this).get(PopInfoViewModel::class.java)
        binding = ActivityPopInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData?.u_seq.toString()
        binding!!.viewModel = viewModel
        if (intent.getSerializableExtra("popUpinfoData") != null) {
            viewModel.popUpinfoData.value = intent.getSerializableExtra("popUpinfoData") as ArrayList<PopUpinfoData>
        }
        viewModel.popUpinfoData_size.value = intent.getIntExtra("popUpinfoDataSize",0)
        observer()
        clickListeners()
        setDialogWindowSize()
        pager = PopInfoPagerAdapter(
            this, viewModel.popUpinfoData.value!!
        ) { data, position ->
            if (data.p_link.isNullOrEmpty() == false) {
                viewModel.viewLogBanner(data.p_seq,"POPUP")
                if (data.p_link.startsWith("https:")) {
                    if (data.p_link.startsWith("https://www.supercarlounge.com/wv/event")){
                        var i = Intent(this,WebViewActivity::class.java)
                        var linkset = data.p_link!!+"?u_seq="+application!!.userData!!.u_seq
                        i.putExtra("url",linkset)
                        resultLauncherPopUp.launch(i)
                    }else {
                        val url = data.p_link
                        val packageManager: PackageManager = packageManager
                        if (isAppInstalled(getString(R.string.chrome_package), packageManager)) {

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        } else {
                            if (isAppInstalled(
                                    getString(R.string.internet_package),
                                    packageManager
                                )
                            ) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this,
                                    "크롬이나 삼성 인터넷이 설치되어 있지 않습니다",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                } else if (data.p_link.equals("HEARTSHOP")){
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    val i= Intent(this, HeartsShopActivity::class.java)
                    startActivity(i)
                }else if (data.p_link.equals("HEARTFRIEND")){
                    viewModel!!.viewLogBanner(data.p_seq,"POPUP")
                    val i= Intent(this, HeartsShopActivity::class.java)
                    i.putExtra("index",1)
                    startActivity(i)
                }else if (data.p_link.equals("MYCARWRITE")){
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    var i = Intent(this, MyCarBoardWritingActivity::class.java)
                    startActivity(i)
                }else if (data.p_link.equals("BOARDWRITE")){
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    var i = Intent(this, PostWritingActivity::class.java)
                    startActivity(i)
                }
                else if (data.p_link.equals("DRIVEWRITE")){
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    var i = Intent(this, NewDriveWritingActivity::class.java)
                    startActivity(i)
                }else if (data.p_link.equals("USEREDIT")){
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    val i = Intent(this, ProfileCorrectionActivity::class.java)
                    i.putExtra("u_seq", viewModel.my_seq.value)
                    i.putExtra("view", "수정")
                    startActivity(i)
                }else if(data.p_link.equals("MYPAGE")){
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    setResult(Constans.MYPAGEMOVE)
                    super.finish()
                }else if (data.p_link.equals("FRIEND")) {
                    viewModel.viewLogBanner(data.p_seq,"POPUP")
                    viewModel.GetInviteCode()
                }else {
                    if (data.p_link.isNullOrEmpty() == false) {
                        viewModel.viewLogBanner(data.p_seq,"POPUP")
                        viewModel.getOneNoti(data.p_link)
                    }
                }
            }
        }
        binding!!.pager.adapter = pager
        binding!!.pager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                // Check if this is the page you want.


                viewModel.position.value = position+1


            }
        })
    }

    var mCurrAngle:Double = 0.0
    @SuppressLint("DefaultLocale", "IntentReset")
    private fun clickListeners(){

        binding.tvCancel1.setOnClickListener {
            application!!.SavePopUpDate()
            finish()

        }
        binding.tvCancel2.setOnClickListener {

            finish()
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

        @SuppressLint("SuspiciousIndentation")
    fun observer(){
            viewModel.inviteset.observe(this, Observer {
                if(it){
                    application!!.onDynamicLinkClick(this@PopInfoActivity,viewModel.invite_code.value!!)
                }
            })
        viewModel.popUpinfoData.observe(this,Observer{ it->
            if (it != null){

            }
        })
            viewModel!!.select_bannernoti_data.observe(this, androidx.lifecycle.Observer { data->
                if (data != null){
                    if (!data.n_seq.isNullOrEmpty()){
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
                    overridePendingTransition(R.anim. fadein, R.anim.fadeout)
                }

            })
    }
    //다이얼로그 사이즈 초기화
    private fun setDialogWindowSize() {

        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels
        val displayPH = metrisc.heightPixels

        var x=0
        var y=0


                x = displayPW
                y = displayPW

        var params = RelativeLayout.LayoutParams(x, y)
        binding!!.pager.layoutParams = params
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim. fadein, R.anim.fadeout)
    }
    override fun finish() {
        var i = Intent()
        setResult(Activity.RESULT_OK, i)
        super.finish()
        overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

 var   resultLauncherPopUp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
         if (result.resultCode == Constans.BRIDGE_MOVE_NOTICE){
            setResult(Constans.BRIDGE_MOVE_NOTICE)
             super.finish()
        }
    }
}
