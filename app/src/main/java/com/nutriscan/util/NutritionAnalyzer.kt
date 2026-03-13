package com.nutriscan.util

import com.nutriscan.data.model.Food
import com.nutriscan.data.model.NutritionIndicator

object NutritionAnalyzer {
    
    fun analyzeFood(food: Food, servingGrams: Float = 100f): List<NutritionIndicator> {
        val indicators = mutableListOf<NutritionIndicator>()
        val multiplier = servingGrams / 100f
        
        val calories = food.caloriesPer100g * multiplier
        val protein = food.proteinG * multiplier
        val fiber = food.fiberG * multiplier
        val sugar = food.sugarG * multiplier
        val fat = food.fatG * multiplier
        val cholesterol = food.cholesterolMg * multiplier
        val sodium = food.sodiumMg * multiplier
        
        // High protein: ≥10g per 100 cal
        if (calories > 0 && (protein / (calories / 100)) >= 10) {
            indicators.add(NutritionIndicator.HIGH_PROTEIN)
        }
        
        // High fiber: ≥3g per serving
        if (fiber >= 3f) {
            indicators.add(NutritionIndicator.HIGH_FIBER)
        }
        
        // High sugar: ≥15g per serving
        if (sugar >= 15f) {
            indicators.add(NutritionIndicator.HIGH_SUGAR)
        }
        
        // High saturated fat: ≥5g per serving (using total fat as proxy)
        if (fat >= 5f) {
            indicators.add(NutritionIndicator.HIGH_SAT_FAT)
        }
        
        // High cholesterol: ≥60mg per serving
        if (cholesterol >= 60f) {
            indicators.add(NutritionIndicator.HIGH_CHOLESTEROL)
        }
        
        // High sodium: ≥400mg per serving
        if (sodium >= 400f) {
            indicators.add(NutritionIndicator.HIGH_SODIUM)
        }
        
        // Low calorie: <100 cal per serving
        if (calories < 100) {
            indicators.add(NutritionIndicator.LOW_CALORIE)
        }
        
        return indicators
    }
    
    fun getIndicatorDescription(indicator: NutritionIndicator): String {
        return when (indicator) {
            NutritionIndicator.HIGH_PROTEIN -> "Great source of protein for muscle building and satiety"
            NutritionIndicator.HIGH_FIBER -> "High fiber content aids digestion and promotes fullness"
            NutritionIndicator.HIGH_SUGAR -> "⚠️ High in sugar - limit intake to avoid blood sugar spikes"
            NutritionIndicator.HIGH_SAT_FAT -> "⚠️ High in fat - moderate consumption recommended"
            NutritionIndicator.HIGH_CHOLESTEROL -> "⚠️ High cholesterol content - monitor daily intake"
            NutritionIndicator.HIGH_SODIUM -> "⚠️ High sodium - may affect blood pressure"
            NutritionIndicator.LOW_CALORIE -> "Low in calories - good for weight management"
        }
    }
    
    fun isWarning(indicator: NutritionIndicator): Boolean {
        return when (indicator) {
            NutritionIndicator.HIGH_SUGAR,
            NutritionIndicator.HIGH_SAT_FAT,
            NutritionIndicator.HIGH_CHOLESTEROL,
            NutritionIndicator.HIGH_SODIUM -> true
            else -> false
        }
    }
}
