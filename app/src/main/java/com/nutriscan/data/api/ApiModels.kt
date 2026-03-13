package com.nutriscan.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// USDA FoodData Central API
interface USDAApi {
    @GET("v1/foods/search")
    suspend fun searchFoods(
        @Query("query") query: String,
        @Query("api_key") apiKey: String,
        @Query("pageSize") pageSize: Int = 10
    ): USDASearchResponse
    
    @GET("v1/food/{fdcId}")
    suspend fun getFoodDetails(
        @Path("fdcId") fdcId: String,
        @Query("api_key") apiKey: String
    ): USDAFoodDetail
}

// Open Food Facts API
interface OpenFoodFactsApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String
    ): OpenFoodFactsResponse
}

// USDA Response Models
data class USDASearchResponse(
    val foods: List<USDAFood>
)

data class USDAFood(
    val fdcId: String,
    val description: String,
    val foodNutrients: List<USDANutrient>
)

data class USDANutrient(
    val nutrientId: Int,
    val nutrientName: String,
    val value: Float,
    val unitName: String
)

data class USDAFoodDetail(
    val fdcId: String,
    val description: String,
    val foodNutrients: List<USDANutrient>
)

// Open Food Facts Response Models
data class OpenFoodFactsResponse(
    val status: Int,
    val product: OpenFoodFactsProduct?
)

data class OpenFoodFactsProduct(
    val product_name: String?,
    val brands: String?,
    val nutriments: OpenFoodFactsNutriments?,
    val code: String
)

data class OpenFoodFactsNutriments(
    val `energy-kcal_100g`: Float?,
    val proteins_100g: Float?,
    val carbohydrates_100g: Float?,
    val fat_100g: Float?,
    val fiber_100g: Float?,
    val sugars_100g: Float?,
    val cholesterol_100g: Float?,
    val sodium_100g: Float?
)
