package com.nutriscan.data.database

import androidx.room.TypeConverter
import com.nutriscan.data.model.MealType

class Converters {
    @TypeConverter
    fun fromMealType(value: MealType): String {
        return value.name
    }
    
    @TypeConverter
    fun toMealType(value: String): MealType {
        return MealType.valueOf(value)
    }
}
