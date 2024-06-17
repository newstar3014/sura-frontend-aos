package com.supercarlounge.supercar.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log

import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.*
import java.util.regex.Pattern


class BlockAcquaintancesActivity: BaseActivitiy() {

    var application:MainApplication?=null
    private lateinit var binding: ActivityBlockAcquaintancesBinding
    private lateinit var viewModel: BlockAcquaintancesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BlockAcquaintancesViewModel::class.java)
        binding = ActivityBlockAcquaintancesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        application = applicationContext as MainApplication
        viewModel.my_seq.value = application!!.userData!!.u_seq.toString()
        binding!!.viewModel = viewModel

        val i= intent
        viewModel.titleText.value =i.getStringExtra("titleText")
        viewModel.mainText.value =i.getStringExtra("mainText")
        viewModel.subText.value = i.getStringExtra("subText")

//        binding.tvTest.text

        viewModel.titleText.value = "초대코드"
        clickListeners()
        observes()
        initLoader()
        viewModel.GetTerm()
        permissionCheck()
        viewModel.FriendList()
        Glide.with(binding!!.ivLoading).load(R.raw.loading_anim).override(200,200).centerInside().into(binding!!.ivLoading)
    }

    private fun permissionCheck(){
        val status = ContextCompat.checkSelfPermission(this,"android.permission.READ_CONTACTS")
        if (status == PackageManager.PERMISSION_GRANTED){
            Log.d("퍼미션", "suc: 1")
            getInformation()
        }else{
            ActivityCompat.requestPermissions(this, arrayOf<String>("android.permission.READ_CONTACTS"), 100)
            Log.d("퍼미션", "suc: 1")
        }
    }


    //퍼미션 콜백
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.d("퍼미션", "suc: ")
            getInformation()
        }else{
            Log.d("퍼미션", "not: ")
        }
    }

    private fun initLoader() {
        //companion object 영역에 선언
        var LOADER_ID = 12345//Unique id you want


//처음 로더를 호출할 경우
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, loaderCallback)

//한번 만들어진 로더를 다른 전화번호를 검색하기 위해 다시 사용할 경우 restartLoader를 사용
        LoaderManager.getInstance(this).restartLoader(LOADER_ID, null, loaderCallback)

//만들어진 로더를 다 사용하고 날리기 위한 경우
        LoaderManager.getInstance(this).destroyLoader(LOADER_ID)
    }

    private val loaderCallback = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                this@BlockAcquaintancesActivity,
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode("010-7720-1730")),
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY),//projection : 원하는 column만 가져오기 위해 설정. null일 경우 모든 항목 가져옴
                null,//selection
                null,//selectionArgs
                null//sortOrder
            )
            Log.d("퍼미션", "onCreateLoader: ")

        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            data?.apply {
                moveToFirst()
                while (!isAfterLast) {
                    getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME).takeIf { it >= 0 }?.let {
                        getString(it)
                    }?.let {
                        Log.d("contactDisplayName", it)
                    }
                    moveToNext()
                }
                close()
            }
            Log.d("퍼미션", "onLoadFinished: ")
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            Log.d("퍼미션", "onLoaderReset: ")
        }
    }

    @SuppressLint("Range")
    private fun getInformation(){
        var numbook :ArrayList<String> = arrayListOf()
        val cr = contentResolver
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        if (cur!!.count > 0) {
            viewModel.apicall.value = true
            var line = ""
            while (cur!!.moveToNext()) {
                val id = cur!!.getInt(cur!!.getColumnIndex(ContactsContract.Contacts._ID))
                //line = String.format("%4d", id)
                val name =
                    cur!!.getString(cur!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

              //  line += " $name"
                if ("1" == cur!!.getString(cur!!.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id.toString()),
                        null
                    )
                    var i = 0
                    val pCount = pCur!!.count
                    val phoneNum = arrayOfNulls<String>(pCount)
                    val phoneType = arrayOfNulls<String>(pCount)
                    while (pCur!!.moveToNext()) {
                        phoneNum[i] =
                            pCur!!.getString(pCur!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        var pp = phoneNum[i]!!.replace("-", "")
                        if(pp.length>=11) {
                            var pnum = pp.substring(0, 11)
                           // var setnum = pnum.convertNumberToPhoneNumber()
                            line = pnum
                            numbook.add(line)
                           // Log.d("phoneset",pnum)
                        }

                        phoneType[i] =
                            pCur!!.getString(pCur!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
                        i++
                    }
                }
                if(!line.isNullOrEmpty())

                    line = ""
            }
            viewModel.apicall.value = false
            Log.d("리스트","+"+numbook.size.toString())
            viewModel.SetList.value = numbook
            viewModel.Setcontacts.value = "+"+numbook.size.toString()
        }

    }
    private fun getContactDisplayName(context: Context, phoneNumber: String): String? = context.contentResolver.query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber)),
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
            null,
            null,
            null
        )?.run {
            var contactName: String? = null
            moveToFirst()
            while (!isAfterLast) {
                getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME).takeIf { it >= 0 }?.let {
                    contactName = getString(it)
                }
                moveToNext()
            }
            close()
            contactName
        }

    private fun observes() {
        viewModel.sendon.observe(this,Observer{
            if(it){
                viewModel.SendFriend()
            }
        })
        viewModel.toast.observe(this, Observer {text->
            if(!text.isNullOrEmpty()){
                Toast.makeText(this@BlockAcquaintancesActivity,text,Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.apicall.observe(this, Observer {
            if (it){

            }
        })
    }

    private fun clickListeners() {
        binding!!.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
       // overridePendingTransition(com.supercarlounge.supercar.R.anim. fadein, com.supercarlounge.supercar.R.anim.fadeout)
    }
    fun String.convertNumberToPhoneNumber(): String {
        return try {
            val regexString = "(\\d{2,3})(\\d{3,4})(\\d{4})"
           // val regexString = "(\\d{3})(\\d{3,4})(\\d{4})"
            return if (!Pattern.matches(regexString, this)) this else Regex(regexString).replace(
                this,
                "$1-$2-$3"
            )
        } catch (e: ParseException) {
            e.printStackTrace()
            this
        }
    }
}
