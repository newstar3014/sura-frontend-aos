package com.supercarlounge.supercar.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.supercarlounge.supercar.GlideImageGetter
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.google.firebase.dynamiclinks.ktx.*
import com.google.firebase.ktx.Firebase
import com.supercarlounge.supercar.Constans
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.data.NotiData
import com.supercarlounge.supercar.databinding.ActivityPostNoticeBinding
import com.supercarlounge.supercar.viewmodel.PostNoticeViewModel

class PostNoticeActivity: BaseActivitiy() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPostNoticeBinding
    private lateinit var viewModel: PostNoticeViewModel
    private lateinit var application:MainApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        with(window) {
//            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//            // set an slide transition
//            enterTransition = Slide(Gravity.END)
//            exitTransition = Slide(Gravity.START)
//        }
        viewModel = ViewModelProvider(this).get(PostNoticeViewModel::class.java)
        binding = ActivityPostNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData?.u_seq.toString()
        binding!!.setLifecycleOwner(this)
        binding!!.viewModel = viewModel

        val i= intent

        var type =i.getStringExtra("type")

        if (type.equals("배너")){

        }else{

        }


//        viewModel.anonymousForumNotiData.value = i.getParcelableExtra("data")
//        viewModel.subText.value = i.getStringExtra("testText")

        var n_seq =i.getStringExtra("n_seq")
        var u_seq = i.getStringExtra("u_seq")
        var n_sday = i.getStringExtra("n_sday")
        var n_title =  i.getStringExtra("n_title")
        var n_eday = i.getStringExtra("n_eday")
        var n_type =  i.getStringExtra("n_type")
        var n_date =  i.getStringExtra("n_date")
        var n_click =  i.getStringExtra("n_click")
        var n_text = application.htmlText
        var data =NotiData(n_date = n_date, n_seq = n_seq, n_text = n_text, n_eday = n_eday, n_sday = n_sday, n_title = n_title, n_type = n_type, u_seq = u_seq,n_click = n_click)
        viewModel.anonymousForumNotiData.value = data
        var g: GlideImageGetter = GlideImageGetter(this, binding!!.tvSub);
        var d = Html.fromHtml(n_text, 1, g, null)
        binding!!.tvSub.text = d
//        Log.d("텍스트", viewModel.subText.value.toString())
        Log.d("PostNoticeActivity anonymousForumNotiData:",  viewModel.anonymousForumNotiData.value.toString())

        viewModel.titleText.value = "공지사항"
        clickListeners()
        observes()

    }

    private fun observes() {
        viewModel.inviteset.observe(this, Observer {
            if(it){
                application!!.onDynamicLinkClick(this@PostNoticeActivity,viewModel.invite_code.value!!)
            }
        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
        binding!!.tvSub.setOnClickListener {
           if (it.isLongClickable == false) {
                   var click = viewModel.anonymousForumNotiData.value!!.n_click
                   if (click.isNullOrEmpty() == false) {
                       if(click.startsWith("https:")){
                           if (click.startsWith("https://www.supercarlounge.com/wv/event")){
                               var i = Intent(this,WebViewActivity::class.java)
                               var linkset = click!!+"?u_seq="+application!!.userData!!.u_seq
                               i.putExtra("url",linkset)
                               resultLauncherPopUp.launch(i)
                           }else {
                               val url = click
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
                       }else if (click.equals("HEARTSHOP")){
                           val i= Intent(this, HeartsShopActivity::class.java)
                           startActivity(i)
                       }else if (click.equals("HEARTFRIEND")){
                           val i= Intent(this, HeartsShopActivity::class.java)
                           i.putExtra("index",1)
                           startActivity(i)
                       }else if (click.equals("MYCARWRITE")){
                           var i = Intent(this, MyCarBoardWritingActivity::class.java)
                           startActivity(i)
                       }else if (click.equals("BOARDWRITE")){
                           var i = Intent(this, PostWritingActivity::class.java)
                           startActivity(i)
                       }
                       else if (click.equals("DRIVEWRITE")){
                           var i = Intent(this, NewDriveWritingActivity::class.java)
                           startActivity(i)
                       }else if (click.equals("USEREDIT")){
                           val i = Intent(this, ProfileCorrectionActivity::class.java)
                           i.putExtra("u_seq", viewModel.my_seq.value)
                           i.putExtra("view", "수정")
                           startActivity(i)
                       }
                       else if (click.equals("FRIEND")) {
                           viewModel.GetInviteCode()
                       }else {

                       }
                   }
               }
        }
    }

    override fun finish() {
        super.finish()
      //  overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }

    fun isAppInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (ex: PackageManager.NameNotFoundException) {
            false
        }
    }
    var   resultLauncherPopUp = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Constans.BRIDGE_MOVE_NOTICE){

            super.finish()
        }
    }
}
