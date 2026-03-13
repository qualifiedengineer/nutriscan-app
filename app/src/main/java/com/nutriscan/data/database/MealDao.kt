package com.nutriscan.data.database

import androidx.room.*
import com.nutriscan.data.model.Meal
import com.nutriscan.data.model.MealType
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meals ORDER BY timestamp DESC")
    fun getAllMeals(): Flow<List<Meal>>
    
    @Query("SELECT * FROM meals WHERE timestamp >= :startOfDay AND timestamp < :endOfDay ORDER BY timestamp DESC")
    fun getMealsForDay(startOfDay: Long, endOfDay: Long): Flow<List<Meal>>
    
    @Query("SELECT * FROM meals WHERE mealType = :mealType AND timestamp >= :startOfDay AND timestamp < :endOfDay")
    fun getMealsByType(mealType: MealType, startOfDay: Long, endOfDay: Long): Flow<List<Meal>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal): Long
    
    @Update
    suspend fun updateMeal(meal: Meal)
    
    @Delete
    suspend fun deleteMeal(meal: Meal)
    
    @Query("DELETE FROM meals WHERE timestamp < :timestamp")
    suspend fun deleteOldMeals(timestamp: Long)
    
    @Query("DELETE FROM meals")
    suspend fun deleteAllMeals()
}
