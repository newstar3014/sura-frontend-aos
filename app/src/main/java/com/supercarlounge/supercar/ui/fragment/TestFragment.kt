package com.supercarlounge.supercar.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.supercarlounge.supercar.R
import com.supercarlounge.supercar.databinding.FragTextBinding


class TestFragment : Fragment() {
    val LogTag = "BoardImageDetailFragment"
    var binding: FragTextBinding? = null
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var view = DataBindingUtil.inflate<FragTextBinding>(inflater, R.layout.frag_text, container, false)!!.root

        return view
    }

    fun newInstance(): TestFragment {
        val fragment = TestFragment()
        return fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)
        binding!!.setLifecycleOwner(this)
    }


}