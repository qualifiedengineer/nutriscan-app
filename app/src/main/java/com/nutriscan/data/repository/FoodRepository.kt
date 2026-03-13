package com.nutriscan.data.repository

import com.nutriscan.data.api.ApiClient
import com.nutriscan.data.api.OpenFoodFactsProduct
import com.nutriscan.data.api.USDAFood
import com.nutriscan.data.database.FoodDao
import com.nutriscan.data.model.Food
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FoodRepository(private val foodDao: FoodDao) {
    
    fun getAllFoods(): Flow<List<Food>> = foodDao.getAllFoods()
    
    fun searchFoods(query: String): Flow<List<Food>> = foodDao.searchFoods(query)
    
    suspend fun getFoodById(id: Long): Food? = foodDao.getFoodById(id)
    
    suspend fun getFoodByBarcode(barcode: String): Food? = foodDao.getFoodByBarcode(barcode)
    
    suspend fun insertFood(food: Food): Long = foodDao.insertFood(food)
    
    suspend fun deleteFood(food: Food) = foodDao.deleteFood(food)
    
    // Search USDA API and cache results
    suspend fun searchUSDA(query: String): List<Food> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.usdaApi.searchFoods(
                query = query,
                apiKey = ApiClient.USDA_API_KEY,
                pageSize = 10
            )
            
            response.foods.map { usdaFood ->
                convertUSDAToFood(usdaFood)
            }.also { foods ->
                // Cache in local DB
                foodDao.insertFoods(foods)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    // Lookup barcode in Open Food Facts and cache
    suspend fun lookupBarcode(barcode: String): Food? = withContext(Dispatchers.IO) {
        // Check local DB first
        getFoodByBarcode(barcode)?.let { return@withContext it }
        
        // Query Open Food Facts API
        try {
            val response = ApiClient.openFoodFactsApi.getProductByBarcode(barcode)
            if (response.status == 1 && response.product != null) {
                val food = convertOpenFoodFactsToFood(response.product, barcode)
                foodDao.insertFood(food)
                food
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    private fun convertUSDAToFood(usdaFood: USDAFood): Food {
        val nutrients = usdaFood.foodNutrients.associateBy { it.nutrientName.lowercase() }
        
        return Food(
            name = usdaFood.description,
            barcode = null,
            caloriesPer100g = nutrients["energy"]?.value?.toInt() ?: 0,
            proteinG = nutrients["protein"]?.value ?: 0f,
            carbsG = nutrients["carbohydrate, by difference"]?.value ?: 0f,
            fatG = nutrients["total lipid (fat)"]?.value ?: 0f,
            fiberG = nutrients["fiber, total dietary"]?.value ?: 0f,
            sugarG = nutrients["sugars, total including nlea"]?.value ?: 0f,
            cholesterolMg = nutrients["cholesterol"]?.value ?: 0f,
            sodiumMg = nutrients["sodium"]?.value ?: 0f,
            source = "USDA"
        )
    }
    
    private fun convertOpenFoodFactsToFood(product: OpenFoodFactsProduct, barcode: String): Food {
        val n = product.nutriments
        
        return Food(
            name = product.product_name ?: "Unknown Product",
            barcode = barcode,
            caloriesPer100g = n?.`energy-kcal_100g`?.toInt() ?: 0,
            proteinG = n?.proteins_100g ?: 0f,
            carbsG = n?.carbohydrates_100g ?: 0f,
            fatG = n?.fat_100g ?: 0f,
            fiberG = n?.fiber_100g ?: 0f,
            sugarG = n?.sugars_100g ?: 0f,
            cholesterolMg = (n?.cholesterol_100g ?: 0f) * 1000f, // Convert g to mg
            sodiumMg = (n?.sodium_100g ?: 0f) * 1000f, // Convert g to mg
            source = "OPEN_FOOD_FACTS"
        )
    }
}
