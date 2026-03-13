package com.nutriscan.data.database

import androidx.room.*
import com.nutriscan.data.model.Food
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM foods ORDER BY name ASC")
    fun getAllFoods(): Flow<List<Food>>
    
    @Query("SELECT * FROM foods WHERE id = :id")
    suspend fun getFoodById(id: Long): Food?
    
    @Query("SELECT * FROM foods WHERE barcode = :barcode LIMIT 1")
    suspend fun getFoodByBarcode(barcode: String): Food?
    
    @Query("SELECT * FROM foods WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchFoods(query: String): Flow<List<Food>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(food: Food): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoods(foods: List<Food>)
    
    @Update
    suspend fun updateFood(food: Food)
    
    @Delete
    suspend fun deleteFood(food: Food)
    
    @Query("DELETE FROM foods")
    suspend fun deleteAllFoods()
}
