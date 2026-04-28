package com.example.shoppinglistapp.model
import java.util.UUID

data class ShoppingItem(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val isBought: Boolean = false
)