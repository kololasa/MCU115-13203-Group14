package com.example.myfoodordering

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ConfirmFragment : Fragment(R.layout.fragment_confirm) {
    private val vm: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val tvOrderSummary = view.findViewById<TextView>(R.id.tvOrderSummary)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.order.collect { order ->
                val summary = "Main Course:\n  ${order.mainMeal}\n\n" +
                              "Side Dishes:\n  ${if (order.sideDishes.isEmpty()) "None" else order.sideDishes.joinToString("\n  ")}\n\n" +
                              "Beverage:\n  ${order.drink}"
                tvOrderSummary.text = summary
            }
        }

        btnConfirm.setOnClickListener {
            val order = vm.order.value
            if (!order.isComplete()) {
                Toast.makeText(
                    requireContext(),
                    "Please select a main meal, at least one side dish, and a drink.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Submit Order")
                .setMessage(
                    "Main: ${order.mainMeal}\n" +
                    "Sides: ${order.sideDishes.joinToString(", ")}\n" +
                    "Drink: ${order.drink}\n\nReady to submit?"
                )
                .setPositiveButton("Submit") { d, _ ->
                    d.dismiss()
                    Toast.makeText(requireContext(), "Order submitted!", Toast.LENGTH_SHORT).show()
                    
                    val activity = requireActivity() as MainActivity
                    activity.showFinalResult(order)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}