package com.example.myfoodordering

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class DrinkFragment : Fragment(R.layout.fragment_drink) {
    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rg = view.findViewById<RadioGroup>(R.id.rgDrink)
        
        rg.setOnCheckedChangeListener { group, checkedId ->
            val rb = group.findViewById<RadioButton>(checkedId)
            vm.setDrink(rb.text.toString())
        }
    }
}