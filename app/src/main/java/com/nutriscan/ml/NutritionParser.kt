package com.nutriscan.ml

import com.nutriscan.data.model.Food

class NutritionParser {
    
    fun parseNutritionLabel(text: String): Food? {
        try {
            val lines = text.lines().map { it.trim() }
            
            // Look for nutrition facts patterns
            val calories = extractCalories(lines)
            val protein = extractNutrient(lines, listOf("protein", "protéines"))
            val carbs = extractNutrient(lines, listOf("carbohydrate", "glucides", "carbs"))
            val fat = extractNutrient(lines, listOf("fat", "lipides", "total fat"))
            val fiber = extractNutrient(lines, listOf("fiber", "fibre", "dietary fiber"))
            val sugar = extractNutrient(lines, listOf("sugar", "sucres", "sugars"))
            val cholesterol = extractNutrient(lines, listOf("cholesterol", "cholestérol"))
            val sodium = extractNutrient(lines, listOf("sodium"))
            
            // Need at least calories to be valid
            if (calories == null) return null
            
            return Food(
                name = "Scanned Product",
                barcode = null,
                caloriesPer100g = calories,
                proteinG = protein ?: 0f,
                carbsG = carbs ?: 0f,
                fatG = fat ?: 0f,
                fiberG = fiber ?: 0f,
                sugarG = sugar ?: 0f,
                cholesterolMg = cholesterol ?: 0f,
                sodiumMg = sodium ?: 0f,
                source = "OCR"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
    
    private fun extractCalories(lines: List<String>): Int? {
        val caloriePatterns = listOf(
            Regex("calories[:\\s]+(\\d+)", RegexOption.IGNORE_CASE),
            Regex("(\\d+)\\s*cal", RegexOption.IGNORE_CASE),
            Regex("energy[:\\s]+(\\d+)", RegexOption.IGNORE_CASE)
        )
        
        for (line in lines) {
            for (pattern in caloriePatterns) {
                val match = pattern.find(line)
                if (match != null) {
                    return match.groupValues[1].toIntOrNull()
                }
            }
        }
        return null
    }
    
    private fun extractNutrient(lines: List<String>, keywords: List<String>): Float? {
        for (keyword in keywords) {
            val pattern = Regex("$keyword[:\\s]+(\\d+\\.?\\d*)\\s*g", RegexOption.IGNORE_CASE)
            val mgPattern = Regex("$keyword[:\\s]+(\\d+\\.?\\d*)\\s*mg", RegexOption.IGNORE_CASE)
            
            for (line in lines) {
                // Try grams first
                pattern.find(line)?.let {
                    return it.groupValues[1].toFloatOrNull()
                }
                
                // Try milligrams
                mgPattern.find(line)?.let {
                    return it.groupValues[1].toFloatOrNull()
                }
            }
        }
        return null
    }
}
