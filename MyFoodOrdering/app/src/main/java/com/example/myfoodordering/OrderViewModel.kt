package com.example.myfoodordering

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Order(
    val mainMeal: String = "",
    val sideDishes: Set<String> = emptySet(),
    val drink: String = ""
) {
    fun isComplete(): Boolean {
        return mainMeal.isNotEmpty() && sideDishes.isNotEmpty() && drink.isNotEmpty()
    }
}

class OrderViewModel : ViewModel() {
    private val _order = MutableStateFlow(Order())
    val order: StateFlow<Order> = _order.asStateFlow()

    fun setMainMeal(meal: String) {
        _order.update { it.copy(mainMeal = meal) }
    }

    fun toggleSideDish(side: String, isSelected: Boolean) {
        _order.update { currentOrder ->
            val updatedSides = currentOrder.sideDishes.toMutableSet()
            if (isSelected) {
                updatedSides.add(side)
            } else {
                updatedSides.remove(side)
            }
            currentOrder.copy(sideDishes = updatedSides)
        }
    }

    fun setDrink(drink: String) {
        _order.update { it.copy(drink = drink) }
    }

    fun clearOrder() {
        _order.value = Order()
    }
}