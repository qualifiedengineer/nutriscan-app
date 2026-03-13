package com.nutriscan.data.repository

import com.nutriscan.data.database.FoodDao
import com.nutriscan.data.model.Food
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FoodRepositoryTest {

    @Mock
    private lateinit var mockFoodDao: FoodDao
    
    private lateinit var repository: FoodRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = FoodRepository(mockFoodDao)
    }

    @Test
    fun `getAllFoods returns flow from DAO`() = runTest {
        val expectedFoods = listOf(
            Food(
                id = 1,
                name = "Apple",
                caloriesPer100g = 52,
                proteinG = 0.3f,
                carbsG = 14f,
                fatG = 0.2f,
                fiberG = 2.4f,
                sugarG = 10.4f,
                cholesterolMg = 0f,
                sodiumMg = 1f
            )
        )
        
        whenever(mockFoodDao.getAllFoods()).thenReturn(flowOf(expectedFoods))
        
        repository.getAllFoods().collect { foods ->
            assertEquals(expectedFoods, foods)
        }
        
        verify(mockFoodDao).getAllFoods()
    }

    @Test
    fun `searchFoods delegates to DAO`() = runTest {
        val query = "chicken"
        val expectedFoods = listOf(
            Food(
                id = 1,
                name = "Chicken Breast",
                caloriesPer100g = 165,
                proteinG = 31f,
                carbsG = 0f,
                fatG = 3.6f,
                fiberG = 0f,
                sugarG = 0f,
                cholesterolMg = 85f,
                sodiumMg = 74f
            )
        )
        
        whenever(mockFoodDao.searchFoods(query)).thenReturn(flowOf(expectedFoods))
        
        repository.searchFoods(query).collect { foods ->
            assertEquals(1, foods.size)
            assertEquals("Chicken Breast", foods[0].name)
        }
        
        verify(mockFoodDao).searchFoods(query)
    }

    @Test
    fun `getFoodById returns single food`() = runTest {
        val foodId = 123L
        val expectedFood = Food(
            id = foodId,
            name = "Banana",
            caloriesPer100g = 89,
            proteinG = 1.1f,
            carbsG = 23f,
            fatG = 0.3f,
            fiberG = 2.6f,
            sugarG = 12f,
            cholesterolMg = 0f,
            sodiumMg = 1f
        )
        
        whenever(mockFoodDao.getFoodById(foodId)).thenReturn(expectedFood)
        
        val result = repository.getFoodById(foodId)
        
        assertEquals(expectedFood, result)
        verify(mockFoodDao).getFoodById(foodId)
    }

    @Test
    fun `getFoodById returns null when not found`() = runTest {
        val foodId = 999L
        
        whenever(mockFoodDao.getFoodById(foodId)).thenReturn(null)
        
        val result = repository.getFoodById(foodId)
        
        assertNull(result)
        verify(mockFoodDao).getFoodById(foodId)
    }

    @Test
    fun `getFoodByBarcode returns cached food`() = runTest {
        val barcode = "1234567890123"
        val cachedFood = Food(
            id = 1,
            name = "Coca Cola",
            barcode = barcode,
            caloriesPer100g = 42,
            proteinG = 0f,
            carbsG = 10.6f,
            fatG = 0f,
            fiberG = 0f,
            sugarG = 10.6f,
            cholesterolMg = 0f,
            sodiumMg = 11f,
            source = "OPEN_FOOD_FACTS"
        )
        
        whenever(mockFoodDao.getFoodByBarcode(barcode)).thenReturn(cachedFood)
        
        val result = repository.getFoodByBarcode(barcode)
        
        assertEquals(cachedFood, result)
        assertEquals("OPEN_FOOD_FACTS", result?.source)
        verify(mockFoodDao).getFoodByBarcode(barcode)
    }

    @Test
    fun `insertFood delegates to DAO and returns id`() = runTest {
        val newFood = Food(
            name = "Custom Food",
            caloriesPer100g = 100,
            proteinG = 5f,
            carbsG = 10f,
            fatG = 2f,
            fiberG = 1f,
            sugarG = 3f,
            cholesterolMg = 10f,
            sodiumMg = 50f,
            source = "USER"
        )
        val expectedId = 42L
        
        whenever(mockFoodDao.insertFood(newFood)).thenReturn(expectedId)
        
        val result = repository.insertFood(newFood)
        
        assertEquals(expectedId, result)
        verify(mockFoodDao).insertFood(newFood)
    }

    @Test
    fun `deleteFood delegates to DAO`() = runTest {
        val foodToDelete = Food(
            id = 10,
            name = "Junk Food",
            caloriesPer100g = 500,
            proteinG = 5f,
            carbsG = 60f,
            fatG = 30f,
            fiberG = 0f,
            sugarG = 40f,
            cholesterolMg = 100f,
            sodiumMg = 800f
        )
        
        repository.deleteFood(foodToDelete)
        
        verify(mockFoodDao).deleteFood(foodToDelete)
    }

    @Test
    fun `lookupBarcode checks local cache first`() = runTest {
        val barcode = "9876543210123"
        val cachedFood = Food(
            id = 5,
            name = "Snickers Bar",
            barcode = barcode,
            caloriesPer100g = 488,
            proteinG = 8.2f,
            carbsG = 60.3f,
            fatG = 24f,
            fiberG = 1.5f,
            sugarG = 54.5f,
            cholesterolMg = 10f,
            sodiumMg = 140f,
            source = "OPEN_FOOD_FACTS"
        )
        
        whenever(mockFoodDao.getFoodByBarcode(barcode)).thenReturn(cachedFood)
        
        val result = repository.lookupBarcode(barcode)
        
        assertEquals(cachedFood, result)
        // Should not hit API if cached
        verify(mockFoodDao).getFoodByBarcode(barcode)
    }
}
