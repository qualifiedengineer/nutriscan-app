# NutriScan Android App - Complete Project Specification

## Executive Summary
**Status**: Project scaffolded, needs full implementation  
**Estimated Build Time**: 5-7 days for MVP  
**Free ML Models**: ML Kit Vision (Google, on-device, zero cost)

## What's Done ✅
- Project structure created
- Gradle build files configured
- Data models defined (Food, Meal, UserProfile)
- Dependencies added (Room, ML Kit, Compose)
- Gradle wrapper copied from working Pokedex project

## What's Needed 🚧

### 1. Database Layer (2-3 hours)
**Files to create:**
```
app/src/main/java/com/nutriscan/data/database/
├── NutriDatabase.kt         (Room database setup)
├── FoodDao.kt               (CRUD operations for foods)
├── MealDao.kt               (CRUD operations for meals)
└── UserProfileDao.kt        (User settings)
```

**Reference**: `/home/shark/.openclaw/workspace/pokedex-app/app/src/main/java/com/pokedex/data/database/`

### 2. Repository Layer (2 hours)
```
app/src/main/java/com/nutriscan/data/repository/
├── FoodRepository.kt        (Food lookup logic)
├── MealRepository.kt        (Meal logging logic)
└── UserRepository.kt        (Settings management)
```

### 3. API Integration (2-3 hours)
```
app/src/main/java/com/nutriscan/data/api/
├── USDAApi.kt              (Retrofit interface)
├── OpenFoodFactsApi.kt     (Barcode lookup)
└── ApiModels.kt            (Response DTOs)
```

**USDA API Key** (already provided): `YCozANSyjR9C5wsiD1tNWgDqUMj5QdhuUZAFwtds`

### 4. ML Kit Integration (4-5 hours)
```
app/src/main/java/com/nutriscan/ml/
├── BarcodeScanner.kt       (ML Kit barcode scanning)
├── TextRecognizer.kt       (OCR for nutrition labels)
└── NutritionParser.kt      (Extract calories/protein from text)
```

**ML Kit is FREE**: Runs on-device, no API costs.

### 5. UI Screens (8-10 hours)
```
app/src/main/java/com/nutriscan/ui/
├── MainActivity.kt                  (Navigation host)
├── home/
│   ├── HomeScreen.kt               (Daily summary dashboard)
│   └── HomeViewModel.kt
├── scanner/
│   ├── ScannerScreen.kt            (Camera + ML Kit)
│   └── ScannerViewModel.kt
├── meals/
│   ├── MealsScreen.kt              (Meal history list)
│   └── MealsViewModel.kt
└── detail/
    ├── FoodDetailScreen.kt         (Nutrition facts + indicators)
    └── FoodDetailViewModel.kt
```

### 6. Resources (1 hour)
```
app/src/main/res/
├── values/
│   ├── strings.xml
│   ├── colors.xml
│   └── themes.xml
└── xml/
    ├── backup_rules.xml
    └── data_extraction_rules.xml
```

## Build Commands

```bash
cd /home/shark/.openclaw/workspace/nutriscan-app

# Debug APK (for testing)
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk

# Release APK (for distribution)
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

## Cost Breakdown 💰

| Component | Cost |
|-----------|------|
| ML Kit Vision (OCR) | **FREE** (Google) |
| ML Kit Barcode | **FREE** (Google) |
| TensorFlow Lite | **FREE** (Google) |
| USDA API | **FREE** (5K requests/hour) |
| Open Food Facts API | **FREE** (unlimited) |
| **TOTAL** | **$0** |

## Samsung Flip 7 Features

### Flex Mode Support
```kotlin
// Half-folded phone: camera on top, results on bottom
WindowManager.isFoldable() → Enable split-screen UI
```

### On-Device AI
- **NPU acceleration** for ML Kit (automatic)
- **Camera2 API** for better image quality
- **Foldable display** optimizations

## Quick Start Options

### Option A: Finish the Build (Recommended)
I can complete the full app in stages:
1. Database + Repository (2 hours)
2. ML Kit scanning (3 hours)
3. Compose UI (6 hours)
4. Testing + APK (2 hours)

**Total**: ~13 hours of focused work

### Option B: Use Existing Apps
- **MyFitnessPal** (free, has barcode scanning)
- **Cronometer** (privacy-focused, local storage)
- **Lose It!** (simple UI)

### Option C: Simplified MVP
Strip down to core features:
- Barcode scanner only (no OCR)
- Manual meal logging (no camera)
- Simple list view (no dashboard)
**Build time**: 4-5 hours

## Next Steps

**Do you want me to:**
1. ✅ **Complete the full app** (Option A) - Best solution
2. 🚀 **Build simplified MVP** (Option C) - Fastest APK
3. 📦 **Package current scaffold** for you to finish later
4. 🔄 **Recommend alternative approach** (PWA, React Native)

Let me know your preference and I'll proceed!
