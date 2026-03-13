package com.nutriscan.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val barcode: String? = null,
    val caloriesPer100g: Int,
    val proteinG: Float,
    val carbsG: Float,
    val fatG: Float,
    val fiberG: Float,
    val sugarG: Float,
    val cholesterolMg: Float,
    val sodiumMg: Float,
    val source: String = "USER" // USDA, OPEN_FOOD_FACTS, USER
)

@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodId: Long,
    val timestamp: Long,
    val quantityGrams: Float,
    val mealType: MealType
)

enum class MealType {
    BREAKFAST, LUNCH, DINNER, SNACK
}

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val dailyCalorieGoal: Int = 2000,
    val dailyProteinGoal: Int = 150,
    val dailyCholesterolLimit: Int = 300,
    val dailySugarLimit: Int = 50
)

data class NutritionSummary(
    val totalCalories: Int,
    val totalProtein: Float,
    val totalSugar: Float,
    val totalCholesterol: Float,
    val totalFiber: Float
)

enum class NutritionIndicator {
    HIGH_PROTEIN,    // ≥10g per 100 cal
    HIGH_FIBER,      // ≥3g per serving
    HIGH_SUGAR,      // ≥15g per serving
    HIGH_SAT_FAT,    // ≥5g per serving
    HIGH_CHOLESTEROL, // ≥60mg per serving
    HIGH_SODIUM,     // ≥400mg per serving
    LOW_CALORIE      // <100 cal per serving
}
