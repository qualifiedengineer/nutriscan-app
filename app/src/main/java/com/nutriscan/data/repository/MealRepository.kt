package com.nutriscan.data.repository

import com.nutriscan.data.database.MealDao
import com.nutriscan.data.database.FoodDao
import com.nutriscan.data.model.Meal
import com.nutriscan.data.model.MealType
import com.nutriscan.data.model.NutritionSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar

class MealRepository(
    private val mealDao: MealDao,
    private val foodDao: FoodDao
) {
    
    fun getAllMeals(): Flow<List<Meal>> = mealDao.getAllMeals()
    
    fun getTodaysMeals(): Flow<List<Meal>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis
        
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis
        
        return mealDao.getMealsForDay(startOfDay, endOfDay)
    }
    
    fun getTodaysNutritionSummary(): Flow<NutritionSummary> {
        return combine(getTodaysMeals(), foodDao.getAllFoods()) { meals, foods ->
            val foodMap = foods.associateBy { it.id }
            
            var totalCalories = 0
            var totalProtein = 0f
            var totalSugar = 0f
            var totalCholesterol = 0f
            var totalFiber = 0f
            
            meals.forEach { meal ->
                val food = foodMap[meal.foodId] ?: return@forEach
                val multiplier = meal.quantityGrams / 100f // Nutrition is per 100g
                
                totalCalories += (food.caloriesPer100g * multiplier).toInt()
                totalProtein += food.proteinG * multiplier
                totalSugar += food.sugarG * multiplier
                totalCholesterol += food.cholesterolMg * multiplier
                totalFiber += food.fiberG * multiplier
            }
            
            NutritionSummary(
                totalCalories = totalCalories,
                totalProtein = totalProtein,
                totalSugar = totalSugar,
                totalCholesterol = totalCholesterol,
                totalFiber = totalFiber
            )
        }
    }
    
    suspend fun insertMeal(meal: Meal): Long = mealDao.insertMeal(meal)
    
    suspend fun deleteMeal(meal: Meal) = mealDao.deleteMeal(meal)
    
    suspend fun deleteOldMeals(daysToKeep: Int = 90) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysToKeep)
        mealDao.deleteOldMeals(calendar.timeInMillis)
    }
}
