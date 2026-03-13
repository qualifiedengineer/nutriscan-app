package com.nutriscan.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nutriscan.data.model.NutritionSummary
import com.nutriscan.data.model.UserProfile
import com.nutriscan.data.repository.MealRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(mealRepository: MealRepository) {
    val nutritionSummary by mealRepository.getTodaysNutritionSummary().collectAsState(
        initial = NutritionSummary(0, 0f, 0f, 0f, 0f)
    )
    
    val userProfile = UserProfile() // Default goals
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Today's Nutrition",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Calories Card
        NutritionCard(
            title = "Calories",
            current = nutritionSummary.totalCalories,
            goal = userProfile.dailyCalorieGoal,
            unit = "kcal",
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Protein Card
        NutritionCard(
            title = "Protein",
            current = nutritionSummary.totalProtein.toInt(),
            goal = userProfile.dailyProteinGoal,
            unit = "g",
            color = Color(0xFF4CAF50)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Sugar Card
        NutritionCard(
            title = "Sugar",
            current = nutritionSummary.totalSugar.toInt(),
            goal = userProfile.dailySugarLimit,
            unit = "g",
            color = Color(0xFFFF9800),
            isWarning = nutritionSummary.totalSugar > userProfile.dailySugarLimit
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Cholesterol Card
        NutritionCard(
            title = "Cholesterol",
            current = nutritionSummary.totalCholesterol.toInt(),
            goal = userProfile.dailyCholesterolLimit,
            unit = "mg",
            color = Color(0xFFF44336),
            isWarning = nutritionSummary.totalCholesterol > userProfile.dailyCholesterolLimit
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Fiber Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Fiber",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${nutritionSummary.totalFiber.toInt()}g",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF8BC34A)
                )
            }
        }
    }
}

@Composable
fun NutritionCard(
    title: String,
    current: Int,
    goal: Int,
    unit: String,
    color: Color,
    isWarning: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isWarning) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "$current / $goal $unit",
                    style = MaterialTheme.typography.headlineSmall,
                    color = color
                )
                
                val percentage = if (goal > 0) (current.toFloat() / goal * 100).toInt() else 0
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.titleMedium,
                    color = color
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = if (goal > 0) (current.toFloat() / goal).coerceIn(0f, 1f) else 0f,
                modifier = Modifier.fillMaxWidth(),
                color = color
            )
            
            if (isWarning) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⚠️ Over daily limit",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFF44336)
                )
            }
        }
    }
}
