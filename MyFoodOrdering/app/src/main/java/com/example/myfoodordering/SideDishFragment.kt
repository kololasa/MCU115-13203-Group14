package com.example.myfoodordering

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class SideDishFragment : Fragment(R.layout.fragment_side_dish) {
    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val cbFries = view.findViewById<CheckBox>(R.id.cbFries)
        val cbSalad = view.findViewById<CheckBox>(R.id.cbSalad)
        val cbSoup = view.findViewById<CheckBox>(R.id.cbSoup)

        val listener = { view: View ->
            if (view is CheckBox) {
                vm.toggleSideDish(view.text.toString(), view.isChecked)
            }
        }

        cbFries.setOnClickListener(listener)
        cbSalad.setOnClickListener(listener)
        cbSoup.setOnClickListener(listener)
    }
}