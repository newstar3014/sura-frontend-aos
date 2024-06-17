package com.supercarlounge.supercar.adapter


import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.data.*
import com.supercarlounge.supercar.databinding.*
import com.supercarlounge.supercar.viewmodel.MyCarInformationModel


class MyCarListAdapter(val resources : Resources ,val con: Context, val data: ArrayList<UserInformationCarData>, val model: MyCarInformationModel, val itemClick: (UserInformationCarData, Int) -> Unit) : RecyclerView.Adapter<MyCarListAdapter.ViewHolder>() {
    var selectindex = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }


    override fun getItemCount(): Int {
        return data.size
    }

    fun setdataList(datalist:ArrayList<UserInformationCarData>){
        data.clear()
        data.addAll(datalist)
        notifyDataSetChanged()
    }
    fun setselect(index:Int){
        selectindex=index
        notifyDataSetChanged()
    }

    fun setdata(index:Int, newData:UserInformationCarData){
        data.set(index,newData)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), position)
    }

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_my_car_list, parent, false)
    ) {
        private val binding: ItemMyCarList? = DataBindingUtil.bind(itemView)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(data: UserInformationCarData, position: Int) {
            binding?.dataset = data
            binding?.viewModel = model
            binding?.posi = position
            binding!!.executePendingBindings()
            binding.lifecycleOwner = con as LifecycleOwner

            initView(resources,binding)
            Log.d("체크", data.uc_car_main_yn.toString())
            Log.d("대표차량인지", data.uc_check.toString())
            Log.d("사진있는지",  data.carb_image.toString())
            Log.d("사진있는지 포지션",  position.toString())

            if (data.uc_check.equals("Y")){
                binding.tvSub.text =  "대표차량"
                binding.tvSub.background = ContextCompat.getDrawable(con,R.drawable.coner_round_filter_bg_s)
                binding.tvSub.setTextColor(ContextCompat.getColor(con, R.color.main_color))


            } else{
                 binding.tvSub.text =  "삭제하기"
                binding.tvSub.background = ContextCompat.getDrawable(con,R.drawable.coner_round_edit_box)
                                          binding.tvSub.setTextColor(ContextCompat.getColor(con, R.color.white))
            }

            if (data.carb_image != null){
                binding.itemIv.visibility = View.VISIBLE

                if (data.carb_name.equals("브랜드 선택")){
                    Glide.with(binding.itemIv).load(R.drawable.ic_plus_gray_34x34).centerCrop().into(binding.itemIv)
                    binding.tvTitle.setTextColor(ContextCompat.getColor(con, R.color.edit_text_color_hint))
                    binding.itemIv.setPadding(100,100,100,100)
                }else{
                    binding.tvTitle.setTextColor(ContextCompat.getColor(con, R.color.white))
                    Glide.with(binding.itemIv).load(data.carb_image).centerCrop().into(binding.itemIv)
                    binding.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, con.resources.getDimension(
                        com.supercarlounge.supercar.R.dimen._10sdp))
                    val typeface = resources.getFont(R.font.inter_bold)
                    binding.tvTitle.typeface = typeface
                    binding.itemIv.setPadding(0,0,0,0)

                }
            }else{
                binding.itemIv.visibility = View.INVISIBLE
            }

//            if (data.carm_name != null){
//                binding.tvTitle.text = data.carm_name
//            }else{
//                binding.tvTitle.text = data.uc_model
//            }



            binding.tvSub.setOnClickListener { itemClick(data, position) }
//            Glide.with(binding!!.itemIv).load(data!!.src).centerInside().into(binding!!.)
        }
    }
    private fun initView(resources: Resources,binding: ItemMyCarList){
        val metrisc: DisplayMetrics = resources.displayMetrics
        val displayPW = metrisc.widthPixels.toInt()
        val displayPH = metrisc.heightPixels

        val x = (displayPW/3)

        var params = FrameLayout.LayoutParams(x,binding.item.height)

        Log.d("핸드폰 전체넓이", displayPW.toString())
        Log.d("핸드폰 3분의 넓이", x.toString())
        Log.d("핸드폰 높이", binding.item.height.toString())
        binding.item.layoutParams.width = x

    }


}