#!/bin/bash
# NutriScan Android App Builder
set -e

echo "🏗️  Building NutriScan Android APK..."

PROJECT_DIR="/home/shark/.openclaw/workspace/nutriscan-app"
cd "$PROJECT_DIR"

# Copy gradle wrapper from Pokedex (working reference)
echo "📦 Setting up Gradle wrapper..."
cp -r /home/shark/.openclaw/workspace/pokedex-app/gradle ./
cp /home/shark/.openclaw/workspace/pokedex-app/gradlew ./
cp /home/shark/.openclaw/workspace/pokedex-app/gradlew.bat ./
chmod +x gradlew

# Create necessary directories
mkdir -p app/src/main/res/{values,mipmap-hdpi,mipmap-mdpi,mipmap-xhdpi,mipmap-xxhdpi,mipmap-xxxhdpi,xml}
mkdir -p app/src/main/java/com/nutriscan/{data/{database,model,repository,api},ui/{home,scanner,meals},util}

echo "✅ Project structure created"
echo "📱 Next steps:"
echo "1. Complete model/database files"
echo "2. Add Compose UI screens"
echo "3. Implement ML Kit scanning"
echo "4. Build with: ./gradlew assembleDebug"
echo ""
echo "💡 ML Kit & TensorFlow Lite are FREE (no API costs)"
