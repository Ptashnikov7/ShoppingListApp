package com.example.shoppinglistapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import com.example.shoppinglistapp.model.ShoppingItem

class ShoppingViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items.asStateFlow()

    private val _newItemName = MutableStateFlow("")
    val newItemName: StateFlow<String> = _newItemName.asStateFlow()

    val itemsToBuyCount: StateFlow<Int> = _items
        .map { list -> list.count { !it.isBought } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun onNameChange(newName: String) {
        _newItemName.value = newName
    }

    fun addItem() {
        val name = _newItemName.value.trim()
        if (name.isNotEmpty()) {
            _items.value = _items.value + ShoppingItem(name = name)
            _newItemName.value = ""
        }
    }

    fun toggleStatus(item: ShoppingItem) {
        _items.value = _items.value.map {
            if (it.id == item.id) it.copy(isBought = !it.isBought) else it
        }
    }
}