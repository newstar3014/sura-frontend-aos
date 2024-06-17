package com.supercarlounge.supercar.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.adapter.LocationCateAdapter
import com.supercarlounge.supercar.adapter.LocationSpotAdapter
import com.supercarlounge.supercar.customview.BaseActivitiy
import com.supercarlounge.supercar.databinding.AcitivtyLocationAreaBinding
import com.supercarlounge.supercar.viewmodel.LocationAreaViewModel


class LocationAreaListActivity: BaseActivitiy() {
    private lateinit var binding: AcitivtyLocationAreaBinding
    private lateinit var viewModel: LocationAreaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LocationAreaViewModel::class.java)
        binding = AcitivtyLocationAreaBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        setContentView(binding.root)
        binding!!.setLifecycleOwner(this)
        setadapter()
        setclicks()
    }

fun setadapter(){
    binding!!.spinArea.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            Log.d("CLOSE","CLOSE1")
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.d("CLOSE","CLOSE2")
        }

    }

    binding!!.rvCate.adapter = LocationCateAdapter(this,viewModel.locationcategory.value!!){data,index ->

    }
    binding!!.rvCate.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

    binding!!.rvLocationMain.adapter = LocationSpotAdapter(this,viewModel.recodata.value!!){ data, type,index->

    }
    binding!!.rvLocationMain.layoutManager = LinearLayoutManager(this)
}
fun setclicks(){
    binding!!.ivBack.setOnClickListener {
        finish()
    }
}
    companion object {
        @BindingAdapter("entriesset")
        @JvmStatic
        fun Spinner.setEntries(entries: ArrayList<String>?) {
            entries?.run {
                val arrayAdapter = ArrayAdapter(context, R.layout.item_spinner_set,R.id.tv_spin, entries)
                arrayAdapter.setDropDownViewResource(R.layout.item_spinner_set)
                adapter = arrayAdapter
            }
        }
        @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
        @JvmStatic
        fun Spinner.getSelectedValue(): Any? {
            return selectedItem
        }

        @JvmStatic
// 위에것이 실행되고 아래의 bindingapdater가 실행된다.
        @BindingAdapter("selectedValueAttrChanged")
        fun Spinner.setInverseBindingListener(inverseBindingListener: InverseBindingListener?) {

            inverseBindingListener?.run {
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View,
                        position: Int,
                        id: Long
                    ) {
                        if (tag != position) {
                            inverseBindingListener.onChange()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
            }
        }

        @JvmStatic
        @BindingAdapter("selectedValue")
        fun Spinner.setSelectedValue(selectedValue: Any?) {
            adapter?.run {
                val position =
                    (adapter as ArrayAdapter<Any>).getPosition(selectedValue)
                setSelection(position, false)
                tag = position
            }
        }
    }
}