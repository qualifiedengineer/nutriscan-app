# NutriScan - Privacy-First Nutrition Tracker

[![Build APK](https://github.com/YOUR_USERNAME/nutriscan-app/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/nutriscan-app/actions/workflows/build.yml)

A Kotlin Android app for tracking nutrition with on-device ML (barcode scanning, OCR). **100% free, privacy-first, no cloud required.**

![NutriScan](https://img.shields.io/badge/Android-8.0%2B-green)
![ML Kit](https://img.shields.io/badge/ML%20Kit-FREE-blue)
![Cost](https://img.shields.io/badge/Cost-%240-success)

## 🎯 Features

### ✅ On-Device Scanning (FREE)
- 📸 **Barcode Scanner** - ML Kit Barcode API (Google, free forever)
- 🔍 **Nutrition Label OCR** - ML Kit Text Recognition (Google, free forever)
- 🖼️ **Photo Analysis** - Parse calories, protein, sugar, cholesterol from labels
- 🔒 **Privacy-First** - All ML processing happens on your phone, no image uploads

### ✅ Smart Tracking
- 📊 **Daily Dashboard** - Real-time calorie, protein, sugar, cholesterol tracking
- ⚠️ **Smart Indicators** - Warnings for excessive intake (high sugar, cholesterol, sodium)
- 🥗 **Meal History** - View and edit logged meals
- 💾 **Local Storage** - Room SQLite database, everything stays on device

### ✅ Free APIs
- **USDA FoodData Central** - 750K+ foods, free API key included
- **Open Food Facts** - 3M+ products with barcodes, no key needed
- **Cached Forever** - Food lookups saved locally

## 🏗️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **ML**: ML Kit Vision (on-device)
- **API**: Retrofit + USDA/Open Food Facts
- **Async**: Kotlin Coroutines + Flow

## 📱 Build & Install

### Quick Start (GitHub Actions)

1. **Fork this repo**
2. **Enable Actions** (Settings → Actions → Allow all actions)
3. **Push/trigger workflow** - APK builds automatically
4. **Download APK** from Actions artifacts
5. **Install on phone** - Enable "Unknown sources" in settings

### Local Build

```bash
# Requires Java 17
./gradlew assembleDebug

# APK output
app/build/outputs/apk/debug/app-debug.apk
```

### Android Studio

1. Open project in Android Studio
2. Build → Build Bundle(s) / APK(s) → Build APK(s)
3. APK appears in `app/build/outputs/apk/debug/`

## 🔒 Privacy

- ✅ No user accounts
- ✅ No cloud uploads
- ✅ All data stored locally (Room DB)
- ✅ ML processing 100% on-device
- ✅ No analytics or tracking
- ✅ Open source

## 💰 Cost

| Component | Monthly Cost |
|-----------|-------------|
| ML Kit Vision (OCR) | $0 (FREE) |
| ML Kit Barcode | $0 (FREE) |
| USDA API | $0 (5K req/hr) |
| Open Food Facts API | $0 (unlimited) |
| **TOTAL** | **$0** |

## 📊 Nutrition Indicators

The app analyzes foods and shows:
- ✅ **HIGH_PROTEIN** - ≥10g per 100 cal
- ✅ **HIGH_FIBER** - ≥3g per serving
- ⚠️ **HIGH_SUGAR** - ≥15g per serving
- ⚠️ **HIGH_CHOLESTEROL** - ≥60mg per serving
- ⚠️ **HIGH_SODIUM** - ≥400mg per serving

## 🎨 Screenshots

(Add screenshots here after first build)

## 📱 Requirements

- Android 8.0+ (API 26)
- Camera permission
- 100MB storage
- Internet (first use only, for food lookups)

## 🤝 Contributing

PRs welcome! Areas for improvement:
- [ ] TensorFlow Lite food photo recognition
- [ ] Weekly/monthly trends charts
- [ ] Export to CSV/PDF
- [ ] Samsung Flip 7 flex mode optimizations
- [ ] Wear OS companion app

## 📄 License

MIT License - Feel free to use for personal or commercial projects

## 🙏 Credits

Built with:
- [ML Kit](https://developers.google.com/ml-kit) (Google)
- [USDA FoodData Central](https://fdc.nal.usda.gov/)
- [Open Food Facts](https://world.openfoodfacts.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)

---

**Made with ❤️ by [Your Name]**
