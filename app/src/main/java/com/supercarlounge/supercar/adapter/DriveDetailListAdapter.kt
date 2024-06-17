package com.supercarlounge.supercar.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.GradientDrawable
import com.supercarlounge.supercar.MainApplication
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.DriveAwayData
import com.supercarlounge.supercar.data.SlidePidData
import com.supercarlounge.supercar.databinding.ItemDriveDetail
import com.supercarlounge.supercar.databinding.PopUpWindowBinding
import com.supercarlounge.supercar.dialog.*
import com.supercarlounge.supercar.enumset.DrivePopMenuStatus
import com.supercarlounge.supercar.enumset.PidDialogType
import com.supercarlounge.supercar.enumset.PopTypeStatus
import com.supercarlounge.supercar.enumset.RecDriveDialogType
import com.supercarlounge.supercar.ui.activity.DriveWritingActivity
import com.supercarlounge.supercar.ui.activity.ProfileCompanyActivity
import com.supercarlounge.supercar.viewmodel.DriveDetailViewModel


class DriveDetailListAdapter(
    val con: Context,
    val owner: LifecycleOwner,
    val fm: FragmentManager,
    var data: ArrayList<DriveAwayData>,
    val model: DriveDetailViewModel,
    val itemClick: (DriveAwayData, Int,String,String) -> Unit
) : RecyclerView.Adapter<DriveDetailListAdapter.ViewHolder>() {
    var application :MainApplication?=null
    var selectindex = 0
    var popupset: BlockDialog2? = null
    var popupset2: PoliceDialog2? = null
    var popupset3: DeleteDilaog? = null
    var drivepop1: ProfileDriveLikeFriendDialog? = null
    var drivepop2: ProfileDriveLikeBaseDialog? = null
    var menupopup:PopupWindow? = null
    init {
        application = con.applicationContext as MainApplication
        model.isedit.observe(owner, Observer {
            if (it) {
                var datas = model.editdata.value
                var index = model.editindex.value
                data.get(index!!).pc_ok = "M"
                notifyItemChanged(index)
            }
        })
        model.drivePopMenuStatus.observe(owner, Observer { status ->
            when (status) {
                DrivePopMenuStatus.PROFILE_WAIT_DIALOG -> {

                }
                DrivePopMenuStatus.PROFILE_SET -> {

                }
                DrivePopMenuStatus.COMPANY_PROFILE->{
                    var data = model.editdata.value
                    val i = Intent(con, ProfileCompanyActivity::class.java)
                    i.putExtra("sub_seq", data!!.u_seq.toString())
                    con.startActivity(i)
                }
                DrivePopMenuStatus.NOT_FRIEND_POPUP -> {
                    drivepop2 = ProfileDriveLikeBaseDialog(
                        con,
                        "",
                        PopTypeStatus.DRIVE_NOT_FRIEND
                    ) { type, onoff ->
                        if (onoff) {
                            var data = model.editdata.value
                            var index = model.editindex.value
                            model.ProfileRead(data!!, index!!, 3, "")

                        }
                        drivepop2?.dismiss()
                    }
                    drivepop2?.show(fm, "")
                }
                DrivePopMenuStatus.FRIEND_POPUP -> {
                    drivepop1 = ProfileDriveLikeFriendDialog(
                            con,
                            "",
                            PopTypeStatus.DRIVE_FRIEND
                        ) { type, onoff, text ->
                            if (onoff) {
                                var data = model.editdata.value
                                var index = model.editindex.value
                                model.ProfileRead(data!!, index!!, 120, text)
                            }
                            drivepop1?.dismiss()
                        }
                    drivepop1?.show(fm, "")
                }
                DrivePopMenuStatus.DEFAULT -> {

                }

            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data!!.size
    }

    fun setdata(datalist: ArrayList<DriveAwayData>) {
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }

    fun setselect(index: Int) {
        selectindex = index
        notifyDataSetChanged()
    }

    fun setindex(index: Int ,indexData:DriveAwayData) {
        data.removeAt(index)
        data.add(index,indexData)
        notifyItemChanged(index)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_drive_detail, parent, false)
    ) {
        private val binding: ItemDriveDetail? = DataBindingUtil.bind(itemView)

        fun bind(data: DriveAwayData, position: Int) {
            binding?.dataset = data
            Log.d("어뎁터 그리기","${data.pc_status}, $position")
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner
            binding!!.pidpager.adapter = SlidePidPagerAdapter(
                con, con as LifecycleOwner,
                getpidtype(position,application?.userData?.u_gender.toString())!!
            ) { feeddata, position ->
                var istip = feeddata.istip
                if(!istip){
                   if (application!!.CheckManager() == false) {
                       var rectype: RecDriveDialogType? = RecDriveDialogType.NOT_REC_DIRVE
                       if (application!!.userData?.u_feed.equals("1")) {
                           var piddialog = PidDialog(con, feeddata.type) { event, isok ->
                               if (isok) {
                                   model.FeedProfileFeeedCehck(feeddata.type)
                               }
                           }
                           piddialog.show(fm, "")
                       } else {
                           var did = RecDriveDialog(con, rectype!!) { type, okcancel ->
                               if (type == RecDriveDialogType.PROFILE_OPEN) {

                               }
                           }
                           did.show(fm!!, "")
                       }
                   }else{
                       model.toast.value = con.getString(R.string.company_profile_open_text)
                   }
                }
            }
            binding!!.pidpager.setPageMargin(20);
            binding!!.pidpager.currentItem = checkpage(position)
            var isactive = (data.pc_ok.equals("M") && data.u_seq == (model.my_seq.value!!).toInt()) || (data.pc_status == 1 && data.heart_seq ==  (model.my_seq.value!!).toInt())
            Log.d("ddd", data?.pc_ok.toString())

            if (data.pc_ok.equals("Y") || data.b_profile.equals("Y")) {
                Glide.with(binding.ivLock).load(R.drawable.ic_ic_unlock_24x24).into(binding.ivLock)
            } else {

                Glide.with(binding.ivLock).load(R.drawable.lock_24x24).into(binding.ivLock)
            }
            Log.d("데이터", data.toString())


            if (model.my_seq.value == data.u_seq.toString()){
                Glide.with(binding.ivDrive).load(R.drawable.ic_frame_2412).into(binding.ivDrive)
            }else{
                if (isactive){
                    binding.tvDrive.setTextColor(ContextCompat.getColor(con, R.color.white_50))
                    Glide.with(binding.ivDrive).load(R.drawable.ic_ic_unactdraive_20x20).into(binding.ivDrive)
                }else{
                    binding.tvDrive.setTextColor(ContextCompat.getColor(con, R.color.main_color))
                    Glide.with(binding.ivDrive).load(R.drawable.icon_drive_active).into(binding.ivDrive)
                }
            }

            Log.d("어뎁터 그리기2", "${model.my_seq.value},${data.u_seq}")
            if (!model.my_seq.value!!.equals(data.u_seq)) {
                binding!!.llSet.visibility = View.VISIBLE
                binding!!.llSet.background = getbgdrawable(con, isactive)
                binding!!.llMap.setOnClickListener {
                    itemClick(data, position, "맵", "")
                }

                binding!!.llSet.setOnClickListener {
                    if (application!!.userData!!.u_staff_yn.equals("C")== false) {
                    Log.d("어뎁터 그리기3", "${model.my_seq.value},${data.u_seq}")
                    if (!model.my_seq.value!!.equals(data.u_seq.toString())) {
                        Log.d("data.pc_status", data.pc_status.toString())
                        when (data.pc_status) {
                            0 -> {
                                var pcok = data.pc_ok
                                var b_profile = data.b_profile
                                if (b_profile.equals("Y")) {

                                    itemClick(data, position, "프로필", "열람오픈")
                                } else {
                                    if (pcok.equals("N")) {

                                        model.ProfileReadCheck(data, position)

                                    } else if (pcok.equals("M")) {
                                        itemClick(data, position, "프로필", "대기중")
                                    } else {
                                        itemClick(data, position, "프로필", "")
                                    }
                                }

                            }
                            1 -> {
                                var pcok = data.pc_ok
                                var b_profile = data.b_profile
                                if (pcok.equals("Y") || b_profile.equals("Y")) {
                                    if (b_profile.equals("Y")) {
                                        itemClick(data, position, "프로필", "열람오픈")
                                    } else {
                                        itemClick(data, position, "프로필", "")
                                    }

                                }
                            }
                            2 -> {

                            }
                            3 -> {

                            }
                        }
                    } else {
                        itemClick(data, position, "", "")
                    }
                }else{
                    model.toast.value = con.getString(R.string.company_profile_open_text)
                         
                     }
                }
            } else {
                binding!!.llSet.visibility = View.GONE
            }

            model.deleteCall.observe(con,Observer{
                if (it) {
                    itemClick(data, position, "삭제","")
                }
            })


            // Glide.with(con).load(data.b_cover_gray).centerInside().into(binding!!.ivImage)
            binding!!.drvieMore.setOnClickListener {
            //    var wrapper = ContextThemeWrapper(con, R.style.Popupset)

            //    var popup = PopupMenu(wrapper, binding!!.drvieMore)

             //   popup.menuInflater.inflate(R.menu.drive_pop_menu_my,popup.menu)
                if (data.u_seq.toString().equals(model.my_seq.value!!) || application!!.CheckManager()) {
                    setpopup(con,binding!!.drvieMore,1,data,position)
                   // popup.inflate(R.menu.drive_pop_menu_my)
                } else {
                    setpopup(con,binding!!.drvieMore,0,data,position)
                  //  popup.inflate(R.menu.drive_pop_menu)
                }

//
//                popup.setOnMenuItemClickListener { it ->
//                    when (it.itemId) {
//                        R.id.decelerate -> {
//                            popupset2 = PoliceDialog2(
//                                con,
//                                PopTypeStatus.DRIVE_POLICE
//                            ) { type, okcancel, text ->
//                                if (okcancel) {
//                                    if (text.isNullOrEmpty()) {
//                                        model.toast.value = "신고 사유를 입력해주세요."
//                                        return@PoliceDialog2
//                                    }
//                                    model.PoliceInsert(text, data.u_seq.toString())
//                                    popupset2!!.dismiss()
//                                } else {
//                                    popupset2!!.dismiss()
//                                }
//                            }
//                            popupset2!!.show(fm, "")
//                        }
//
//                        R.id.block -> {
//                            popupset = BlockDialog2(
//                                con,
//                                PopTypeStatus.DRIVE_BLOCK
//                            ) { type, okcancel ->
//                                if (okcancel) {
//                                    model.PostBlockInsert("", data.u_seq.toString())
//                                    popupset!!.dismiss()
//                                } else {
//                                    popupset!!.dismiss()
//                                }
//                            }
//                            popupset!!.show(fm, "")
//                        }
//                        R.id.delete -> {
//                            popupset3 =
//                                DeleteDilaog(con, PopTypeStatus.DRIVE_DELETE) { type, isonoff ->
//                                    if (isonoff) {
//                                        model.delete_Drive(data)
//
//                                    }
//                                }
//                            popupset3!!.show(fm, "")
//                        }
//                        R.id.edit -> {
//                            var i = Intent(con, DriveWritingActivity::class.java)
//                            i.putExtra("data", data)
//                            con.startActivity(i)
//                        }
//
//                    }
//                    return@setOnMenuItemClickListener false
//                }
//                popup.show()
            }



            binding!!.likecheck.setOnClickListener {
                var check = !data.like_check.equals("1")
                model.likedrive(data.b_seq, check, position)
            }
//            itemView.setOnClickListener { itemClick(data, position,"") }

        }


    }

    fun getbgdrawable(con: Context, check: Boolean): Drawable {
        var d: Drawable? = null
        if (check) {
            d = GradientDrawable(
                con.getColor(R.color.unselect_cooper),
                con.getColor(R.color.unselect_cooper),
                con.getColor(
                    R.color.white_4
                ),
                con.getColor(R.color.white_4),
                2f,
                10f,
                GradientDrawable.Direction.TL_BR,
                GradientDrawable.Direction.TL_BR
            )
        } else {
            d = GradientDrawable(
                con.getColor(R.color.cooper_gradi_st),
                con.getColor(R.color.cooper_gradi_ed),
                con.getColor(
                    R.color.white_4
                ),
                con.getColor(R.color.white_4),
                2f,
                10f,
                GradientDrawable.Direction.TL_BR,
                GradientDrawable.Direction.TL_BR
            )
        }
        return d!!
    }

    fun setdatas(d: DriveAwayData, index: Int) {
//        data.set(index,d)
//        notifyItemChanged(index)
    }

    //    fun setlike(){
//        var dd = data.get(index)
//        dd.like_check =  ischeck
//        data.set(index,dd)
//        notifyItemChanged(index)
//    }
    companion object {
        @SuppressLint("SuspiciousIndentation")
        @BindingAdapter("app:detaildrives")
        @JvmStatic
        fun setItems(recyclerView: RecyclerView, items: ArrayList<DriveAwayData>) {
            if (recyclerView.adapter != null) {
                var adapter = (recyclerView.adapter as DriveDetailListAdapter)
                items?.let {
                    adapter.data = it
                    adapter.notifyDataSetChanged()

                }
            }
        }


        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("drivewhen","drivewhen2")
        fun setdrivewhen(view: TextView, index: Int?,data:DriveAwayData) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (index != null) {
                var result = ""
                when (index) {
                    0 -> {
                        result = "연락 후 날짜를 조율해요!"
                    }
                    else-> {
                        if (data.meeting_day.isNullOrEmpty() == false) {
                            var date = data.meeting_day
                            var list = date?.split("-", " ", ":")
                            if (list?.size == 5) {
                                var year = list[0]
                                var month = list[1]
                                var day = list[2]
                                var hour = list[3]
                                var min = list[4]
                                var ampm = "오전"
                                if (hour.toInt() > 11) {
                                    if (hour.toInt() > 12) {
                                        hour = (hour.toInt() - 12).toString()
                                    }
                                    ampm = "오후"
                                }

                                result = "${year}년 ${month}월 ${day}일 ${ampm} ${hour}시 ${min}분 "
                            }

                        }else{
                            result = "드라이브할 시간을 조율해요!"
                        }
                    }
                }
                view.text = result
            }
        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("drivehow")
        fun setdrivehow(view: TextView, index: Int?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (index != null) {
                var result = ""
                when (index) {
                    0 -> {
                        result = "누구든 드라이브는 다 좋아요!"
                    }
                    1 -> {
                        result = "차량 여러대가 만나는 드라이브!"
                    }
                    2 -> {
                        result = "설레는 드라이브를 하고 싶어요!"
                    }
                    3 -> {
                        result = "오늘은 차 없이 술 한 잔 어때요?"
                    }
                    4 -> {
                        result = "편하게 커피 한 잔 좋아요!"
                    }
                    5-> {
                        result = "2:2, 3:3 친구들과 같이 만나요!"
                    }
                    6 -> {
                        result = "맛집 같이 가요!"
                    }


                }
                view.text = result
            }

        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("drivestatus")
        fun setdrivestatus(view: TextView, index: Int?) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            if (index != null) {
                var result = ""
                when (index) {
                    0 -> {
                        result = "프로필 열람하기"
                    }
                    1 -> {
                        result = "프로필 열람 신청완료"
                    }
                    2 -> {
                        result = "호감 보내기"
                    }
                    3 -> {
                        result = "호감을 보냈습니다."
                    }

                }
                view.text = result
            }

        }

        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("okstatus","myseq","vipType")
        fun setokstatus(view: TextView, data: DriveAwayData,my_seq : String,driveType: String) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            Log.d("setokstatus", data.toString())
            var pcok = data.pc_ok
            var status = data.pc_status
            var u_seq = data.u_seq
            var b_profile = data.b_profile
            var b_heart_seq = data.heart_seq
            var result = ""
            when (status) {
                0 -> {
                    if (my_seq == u_seq.toString()){
                        if (driveType.equals("vip")){
                            result = "드라이브 확인하기"
                        }else{
                            result = "드라이브 등록하기"
                        }

                    }else{

                        if (b_profile.equals("Y")){
                            result = "호감 보내고 연락처 확인하기"
                        }else{
                            if (pcok.equals("N")) {
                                result = "드라이브 신청하기"
                            } else if (pcok.equals("M")) {
                                result = "드라이브 신청완료"

                            } else if (pcok.equals("Y")) {
                                result = "호감 보내고 연락처 확인하기"
                            }
                        }

                    }

                }
                1 -> {
                    Log.d("확인 하트seq", b_heart_seq.toString())
                    Log.d("확인 my_seq", my_seq.toString())
//                    if (b_heart_seq.equals(\"0\")){\n" +
//                                "                        result = \"호감을 보냈습니다"
//                    }else{
                        if (b_heart_seq.toString() == my_seq){
                            Log.d("확인", "setokstatus: 1")
                            if (b_profile.equals("M")){
                                result = "호감 보내고 연락처 확인하기"
                                Log.d("확인", "setokstatus: 2")
                            }else{
                                result = "호감을 보냈습니다"
                                Log.d("확인", "setokstatus:3")
                            }
                        }else{
                            result = "호감 보내고 연락처 확인하기"
                            Log.d("확인", "setokstatus: 4")
                        }
//                    }




                }
                2 -> {
                    result = "연락처가 공개되었습니다."
                }
                3 -> {
                    result = "RPM 평가하기"
                }

            }
            view.text = result
        }
        @SuppressLint("SuspiciousIndentation")
        @JvmStatic
        @BindingAdapter("text")
        fun settext(view: TextView, data: DriveAwayData) {
            // This methods should not have any return type, = declaration would make it return that object declaration.
            var pcok = data.pc_ok
            var status = data.pc_status
            var b_profile = data.b_profile
            Log.d("드라이브 데이터", data.toString())

            var result = ""
            if (pcok.equals("Y")) {
                result = "호감을 보내서 드라이브를 시작해 보세요!"
                view.text = result

            }else {
                if (b_profile == "Y"){
                    result = "프로필이 공개된 게시글입니다."
                }else{
                    result = "드라이브 신청후 프로필 확인이 가능합니다."

                }

                view.text = result

            }





        }
    }

    fun checkpage(position: Int):Int{
        var result = 0
        var set = position%22
            when (set) {
                0,2,11 -> {
                    result = 0
                }
                1,4,12 -> {
                    result = 1
                }
                3,6,14 -> {
                    result = 2
                }
                5,8,16 -> {
                    result = 3
                }
                7,10,18 -> {
                    result = 4
                }
                9,13->{
                    result = 5
                }
                15->{
                    result = 6
                }
                17->{
                    result = 7
                }
                19->{
                    result = 8
                }
                21->{
                    result = 9
                }



        }
        return result
    }

    fun setEditItem(rdata: DriveAwayData,position: Int){
        data.set(position,rdata)
        notifyItemChanged(position)
    }
    fun getpidtype(position: Int,sex:String):ArrayList<SlidePidData>? {
        var data:ArrayList<SlidePidData>?=null
        var set = position%11
        when (set) {
            0,1,3,5,7,9 -> {
                data = setpid(0,sex)
            }
            2,4,6,8,10,11->{
                data = setpid(1,sex)
            }
            else->{
                data = setpid(1,sex)
            }
        }
        return data
    }

    fun setpid(type:Int,sex: String): ArrayList<SlidePidData> {
        var list: ArrayList<SlidePidData> = arrayListOf()
        if(type == 0){
            if (sex.equals("MALE")) {
                list.add(SlidePidData(PidDialogType.FRIEND, false, "나와 맞는 짝꿍 찾기! \uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68", "짝꿍과 함께 신나는 드라이브!"))
                list.add(SlidePidData(PidDialogType.MAN_GLAMMER, false, "글래머,볼륨있는 여성 회원 만나기 \uD83D\uDD25", "이상형과 함께하는 설레는 드라이브!") )
                list.add(SlidePidData(PidDialogType.NEW_USER, false, "최근 가입한 신입회원 만나기 \uD83D\uDC25", "신입 회원과 즐거운 드라이브!"))
                list.add( SlidePidData(PidDialogType.AGE_20, false, "20대 드라이버,패신저 만나기 \uD83D\uDC66\uD83D\uDC67", "PASS인증을 통한 실제 나이를 인증한 회원!"))
                list.add( SlidePidData(PidDialogType.LIKE_MUSIC, false, "음악취향 비슷한 상대 만나기 \uD83C\uDFB6", "음악취향도 같으면 더 신나는 드라이브!"))
                list.add(SlidePidData(PidDialogType.SUPER_USE, false, "슈퍼카 오너 만나기 \uD83D\uDE98", "본인 소유의 차량 인증을 한 슈퍼카 오너!"))
            }else{
                list.add(SlidePidData(PidDialogType.FRIEND, false, "나와 맞는 짝꿍 찾기! \uD83D\uDC69\u200D❤️\u200D\uD83D\uDC68", "짝꿍과 함께 신나는 드라이브!"))
                list.add(SlidePidData(PidDialogType.SUPER_USE, false, "슈퍼카 오너 만나기 \uD83D\uDE98", "본인 소유의 차량 인증을 한 슈퍼카 오너!"))
                list.add( SlidePidData(PidDialogType.AGE_20, false, "20대 드라이버,패신저 만나기 \uD83D\uDC66\uD83D\uDC67", "PASS인증을 통한 실제 나이를 인증한 회원!"))
                list.add(SlidePidData(PidDialogType.FM_10PER, false, "상위 10% 이성 회원 만나기 \uD83D\uDE0E", "호감을 제일 많이 받은 회원!"))
                list.add( SlidePidData(PidDialogType.LIKE_MUSIC, false, "음악취향 비슷한 상대 만나기 \uD83C\uDFB6", "음악취향도 같으면 더 신나는 드라이브!"))
                list.add(SlidePidData(PidDialogType.NEW_USER, false, "최근 가입한 신입회원 만나기 \uD83D\uDC25", "신입 회원과 즐거운 드라이브!"))
            }
        }else {

                list.add(SlidePidData(PidDialogType.TIP, true, "내가 쓴 게시글이 상단 노출되는", "VIP DRIVE를 알고 계시나요?", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "내 게시글을 열람한 사람을", "알 수 있는 드라이브 패스권!", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "드라이브 패스권은 1일 보다", "30일이 저렴해요!", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "슈라는 매칭에 성공하면", "안심번호가 생성됩니다.", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "매너점수를 알 수 있는", "RPM 게이지 확인하셨나요?", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "진심을 가득 담아 호감을 보내세요", "드라이브 신청을 받아줄 거에요!", istop = true))
                list.add(SlidePidData(PidDialogType.TIP, true, "컨시어지를 확인하세요!", "혜택이 가득 담겨있답니다!", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "드라이브 같이 갈 사람을", "추천해 주는 드라이브 추천 기능!", istop = false))
                list.add(SlidePidData(PidDialogType.TIP, true, "미니 프로필은 드라이브 패스권", "구매 시 확인 가능해요!", istop = true))
                list.add(SlidePidData(PidDialogType.TIP, true, "안심 번호는 전화뿐만 아니라", "문자도 가능하다는 사실!", istop = false))
        }
        return list
    }

    fun setpopup(con:Context,view:View,type:Int,data:DriveAwayData,position:Int){

        var bindding:PopUpWindowBinding   = DataBindingUtil.inflate(LayoutInflater.from(con), R.layout.popup_window, null, false)
        bindding!!.type = type
        if(type == 0) {
            bindding!!.btnBlock.setOnClickListener {
                popupset = BlockDialog2(
                    con,
                    PopTypeStatus.DRIVE_BLOCK
                ) { type, okcancel ->
                    if (okcancel) {
                        model.PostBlockInsert("", data.u_seq.toString())
                        popupset!!.dismiss()
                    } else {
                        popupset!!.dismiss()
                    }
                }
                popupset!!.show(fm, "")

                menupopup!!.dismiss()
            }
            bindding!!.btnPolice.setOnClickListener {
                popupset2 = PoliceDialog2(
                    con,
                    PopTypeStatus.DRIVE_POLICE
                ) { type, okcancel, text ->
                    if (okcancel) {
                        if (text.isNullOrEmpty()) {
                            model.toast.value = "신고 사유를 입력해주세요."
                            return@PoliceDialog2
                        }
                        model.PoliceInsert(text, data.u_seq.toString())
                        popupset2!!.dismiss()
                    } else {
                        popupset2!!.dismiss()
                    }
                }
                popupset2!!.show(fm, "")
                menupopup!!.dismiss()
            }
        }else {
            bindding!!.btnDelete.setOnClickListener {
                popupset3 = DeleteDilaog(con, PopTypeStatus.DRIVE_DELETE) { type, isonoff ->
                        if (isonoff) {
                            model.delete_Drive(data)

                        }
                    }
                popupset3!!.show(fm, "")
                menupopup!!.dismiss()
            }
            bindding!!.btnEdit.setOnClickListener {
                itemClick(data, position, "수정", "")
//                var i = Intent(con, DriveWritingActivity::class.java)
//                i.putExtra("data", data)
//                con.startActivity(i)
                menupopup!!.dismiss()
            }
        }
        menupopup= PopupWindow(bindding.root,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,true)
        menupopup!!.showAsDropDown(view,-100,0)

    }


}