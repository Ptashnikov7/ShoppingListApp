package com.example.shoppinglistapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppinglistapp.viewmodel.ShoppingViewModel

@Composable
fun ShoppingScreen(viewModel: ShoppingViewModel) {
    val items by viewModel.items.collectAsStateWithLifecycle()
    val newItemName by viewModel.newItemName.collectAsStateWithLifecycle()
    val count by viewModel.itemsToBuyCount.collectAsStateWithLifecycle()

    val pendingItems = items.filter { !it.isBought }
    val boughtItems = items.filter { it.isBought }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Список покупок",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )
            }
            Text(
                text = "Залишилось купити: ",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "$count",
                color = Color(0xFFFFA000), // Orange
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Input Field Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newItemName,
                onValueChange = { viewModel.onNameChange(it) },
                placeholder = { Text("Молоко 1л", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF1F3F4),
                    unfocusedContainerColor = Color(0xFFF1F3F4),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = { viewModel.addItem() },
                enabled = newItemName.isNotBlank(),
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF9800), // Orange
                    disabledContainerColor = Color(0xFFFF9800).copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "Додати",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        // List Section
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (pendingItems.isNotEmpty()) {
                item {
                    SectionHeader("ПОТРІБНО КУПИТИ")
                }
                items(pendingItems, key = { it.id }) { item ->
                    ShoppingItemCard(item = item, onToggle = { viewModel.toggleStatus(item) })
                }
            }

            if (boughtItems.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader("КУПЛЕНО")
                }
                items(boughtItems, key = { it.id }) { item ->
                    ShoppingItemCard(item = item, onToggle = { viewModel.toggleStatus(item) })
                }
            }
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        ),
        color = Color.LightGray,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}