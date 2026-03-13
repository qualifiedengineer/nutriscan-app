package com.nutriscan.ui.meals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nutriscan.data.model.Food
import com.nutriscan.data.model.Meal
import com.nutriscan.data.repository.FoodRepository
import com.nutriscan.data.repository.MealRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsScreen(
    mealRepository: MealRepository,
    foodRepository: FoodRepository
) {
    val scope = rememberCoroutineScope()
    val meals by mealRepository.getTodaysMeals().collectAsState(initial = emptyList())
    val allFoods by foodRepository.getAllFoods().collectAsState(initial = emptyList())
    val foodMap = remember(allFoods) { allFoods.associateBy { it.id } }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Today's Meals",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (meals.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No meals logged today.\nUse the scanner to add food!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(meals, key = { it.id }) { meal ->
                    val food = foodMap[meal.foodId]
                    food?.let {
                        MealCard(
                            meal = meal,
                            food = it,
                            onDelete = {
                                scope.launch {
                                    mealRepository.deleteMeal(meal)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealCard(
    meal: Meal,
    food: Food,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }
    val timeString = dateFormat.format(Date(meal.timestamp))
    
    val multiplier = meal.quantityGrams / 100f
    val calories = (food.caloriesPer100g * multiplier).toInt()
    val protein = (food.proteinG * multiplier)
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${meal.quantityGrams.toInt()}g • $timeString",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "$calories kcal",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${protein.toInt()}g protein",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete meal",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
