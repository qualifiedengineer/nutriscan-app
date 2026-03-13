package com.nutriscan.util

import com.nutriscan.data.model.Food
import com.nutriscan.data.model.NutritionIndicator
import org.junit.Assert.*
import org.junit.Test

class NutritionAnalyzerTest {

    @Test
    fun `analyzeFood detects high protein food`() {
        // Chicken breast: 165 cal, 31g protein per 100g
        val chickenBreast = Food(
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
        
        val indicators = NutritionAnalyzer.analyzeFood(chickenBreast, 100f)
        
        assertTrue("Should detect high protein", indicators.contains(NutritionIndicator.HIGH_PROTEIN))
        assertFalse("Should not flag high sugar", indicators.contains(NutritionIndicator.HIGH_SUGAR))
    }

    @Test
    fun `analyzeFood detects high sugar warning`() {
        // Soda: 41 cal, 11g sugar per 100ml
        val soda = Food(
            name = "Cola",
            caloriesPer100g = 41,
            proteinG = 0f,
            carbsG = 11f,
            fatG = 0f,
            fiberG = 0f,
            sugarG = 11f,
            cholesterolMg = 0f,
            sodiumMg = 9f
        )
        
        // 250ml serving = 27.5g sugar (>15g threshold)
        val indicators = NutritionAnalyzer.analyzeFood(soda, 250f)
        
        assertTrue("Should detect high sugar", indicators.contains(NutritionIndicator.HIGH_SUGAR))
        assertTrue("Should be a warning indicator", NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_SUGAR))
    }

    @Test
    fun `analyzeFood detects high cholesterol`() {
        // Egg: 143 cal, 373mg cholesterol per 100g
        val egg = Food(
            name = "Whole Egg",
            caloriesPer100g = 143,
            proteinG = 12.6f,
            carbsG = 0.7f,
            fatG = 9.5f,
            fiberG = 0f,
            sugarG = 0.4f,
            cholesterolMg = 373f,
            sodiumMg = 124f
        )
        
        // 50g egg = 186.5mg cholesterol (>60mg threshold)
        val indicators = NutritionAnalyzer.analyzeFood(egg, 50f)
        
        assertTrue("Should detect high cholesterol", indicators.contains(NutritionIndicator.HIGH_CHOLESTEROL))
    }

    @Test
    fun `analyzeFood detects high sodium warning`() {
        // Instant ramen: 448 cal, 1820mg sodium per 100g
        val ramen = Food(
            name = "Instant Ramen",
            caloriesPer100g = 448,
            proteinG = 9.1f,
            carbsG = 61.8f,
            fatG = 17.8f,
            fiberG = 2.3f,
            sugarG = 2.5f,
            cholesterolMg = 0f,
            sodiumMg = 1820f
        )
        
        val indicators = NutritionAnalyzer.analyzeFood(ramen, 100f)
        
        assertTrue("Should detect high sodium", indicators.contains(NutritionIndicator.HIGH_SODIUM))
        assertTrue("High sodium should be warning", NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_SODIUM))
    }

    @Test
    fun `analyzeFood detects high fiber`() {
        // Black beans: 132 cal, 8.7g fiber per 100g
        val blackBeans = Food(
            name = "Black Beans",
            caloriesPer100g = 132,
            proteinG = 8.9f,
            carbsG = 23.7f,
            fatG = 0.5f,
            fiberG = 8.7f,
            sugarG = 0.3f,
            cholesterolMg = 0f,
            sodiumMg = 2f
        )
        
        val indicators = NutritionAnalyzer.analyzeFood(blackBeans, 100f)
        
        assertTrue("Should detect high fiber", indicators.contains(NutritionIndicator.HIGH_FIBER))
        assertFalse("High fiber is not a warning", NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_FIBER))
    }

    @Test
    fun `analyzeFood detects low calorie food`() {
        // Cucumber: 15 cal per 100g
        val cucumber = Food(
            name = "Cucumber",
            caloriesPer100g = 15,
            proteinG = 0.7f,
            carbsG = 3.6f,
            fatG = 0.1f,
            fiberG = 0.5f,
            sugarG = 1.7f,
            cholesterolMg = 0f,
            sodiumMg = 2f
        )
        
        val indicators = NutritionAnalyzer.analyzeFood(cucumber, 100f)
        
        assertTrue("Should detect low calorie", indicators.contains(NutritionIndicator.LOW_CALORIE))
    }

    @Test
    fun `analyzeFood scales correctly with serving size`() {
        val food = Food(
            name = "Test Food",
            caloriesPer100g = 200,
            proteinG = 10f,
            carbsG = 20f,
            fatG = 5f,
            fiberG = 2f,
            sugarG = 8f,
            cholesterolMg = 40f,
            sodiumMg = 200f
        )
        
        // 200g serving should double nutrients
        val indicators200g = NutritionAnalyzer.analyzeFood(food, 200f)
        
        // 16g sugar @ 200g > 15g threshold
        assertTrue("200g serving should trigger high sugar", indicators200g.contains(NutritionIndicator.HIGH_SUGAR))
        
        // 80mg cholesterol @ 200g > 60mg threshold
        assertTrue("200g serving should trigger high cholesterol", indicators200g.contains(NutritionIndicator.HIGH_CHOLESTEROL))
        
        // 400mg sodium @ 200g = 400mg threshold
        assertTrue("200g serving should trigger high sodium", indicators200g.contains(NutritionIndicator.HIGH_SODIUM))
    }

    @Test
    fun `analyzeFood returns empty for neutral food`() {
        // Neutral food with no extreme values
        val food = Food(
            name = "Plain Rice",
            caloriesPer100g = 130,
            proteinG = 2.7f,
            carbsG = 28f,
            fatG = 0.3f,
            fiberG = 0.4f,
            sugarG = 0.1f,
            cholesterolMg = 0f,
            sodiumMg = 1f
        )
        
        val indicators = NutritionAnalyzer.analyzeFood(food, 100f)
        
        assertTrue("Neutral food should have no indicators or only low calorie", 
            indicators.isEmpty() || indicators == listOf(NutritionIndicator.LOW_CALORIE))
    }

    @Test
    fun `getIndicatorDescription returns correct messages`() {
        assertEquals(
            "Great source of protein for muscle building and satiety",
            NutritionAnalyzer.getIndicatorDescription(NutritionIndicator.HIGH_PROTEIN)
        )
        
        assertTrue(
            "High sugar description should include warning",
            NutritionAnalyzer.getIndicatorDescription(NutritionIndicator.HIGH_SUGAR).contains("⚠️")
        )
    }

    @Test
    fun `isWarning correctly identifies warning indicators`() {
        assertTrue(NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_SUGAR))
        assertTrue(NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_CHOLESTEROL))
        assertTrue(NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_SODIUM))
        assertTrue(NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_SAT_FAT))
        
        assertFalse(NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_PROTEIN))
        assertFalse(NutritionAnalyzer.isWarning(NutritionIndicator.HIGH_FIBER))
        assertFalse(NutritionAnalyzer.isWarning(NutritionIndicator.LOW_CALORIE))
    }
}
