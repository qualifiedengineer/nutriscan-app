#!/bin/bash
# Build NutriScan APK

set -e

echo "🏗️  Building NutriScan APK..."

PROJECT_DIR="/home/shark/.openclaw/workspace/nutriscan-app"
cd "$PROJECT_DIR"

# Check if gradlew exists
if [ ! -f "gradlew" ]; then
    echo "❌ Error: gradlew not found. Run the setup script first."
    exit 1
fi

# Make gradlew executable
chmod +x gradlew

echo "📦 Building debug APK..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    echo ""
    echo "📱 APK Location:"
    echo "   $PROJECT_DIR/app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "📲 Install on device:"
    echo "   adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "🎉 NutriScan is ready to use!"
else
    echo ""
    echo "❌ Build failed. Check the errors above."
    exit 1
fi
