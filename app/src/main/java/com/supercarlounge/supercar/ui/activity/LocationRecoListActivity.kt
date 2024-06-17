package com.supercarlounge.supercar.ui.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.adapter.LocationSpotAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.AcitivtyLocationRecoBinding
import com.supercarlounge.supercar.viewmodel.LocationRecoViewModel


class LocationRecoListActivity: BaseActivitiy() {
    private lateinit var binding: AcitivtyLocationRecoBinding
    private lateinit var viewModel: LocationRecoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationRecoViewModel::class.java)
        binding = AcitivtyLocationRecoBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        setadapter()
        setclicks()
    }

fun setadapter(){
    binding!!.rvLocationMain.adapter = LocationSpotAdapter(this,viewModel.recodata.value!!){ data,type, index->

    }
    binding!!.rvLocationMain.layoutManager = LinearLayoutManager(this)
}
fun setclicks(){
    binding!!.ivBack.setOnClickListener {
        finish()
    }
}

}