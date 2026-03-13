#!/bin/bash
# Run comprehensive tests for NutriScan

set -e

PROJECT_DIR="/home/shark/.openclaw/workspace/nutriscan-app"
cd "$PROJECT_DIR"

echo "🧪 NutriScan Test Suite Runner"
echo "================================"
echo ""

# Check if Android SDK is available
if [ -z "$ANDROID_HOME" ] && [ ! -d "$HOME/Android/Sdk" ]; then
    echo "⚠️  Android SDK not found. Skipping local build."
    echo "ℹ️  Tests will run via GitHub Actions instead."
    LOCAL_BUILD=false
else
    LOCAL_BUILD=true
    if [ -z "$ANDROID_HOME" ]; then
        export ANDROID_HOME="$HOME/Android/Sdk"
    fi
fi

# Function to run tests
run_unit_tests() {
    echo "📝 Running Unit Tests..."
    if $LOCAL_BUILD; then
        ./gradlew test --stacktrace || {
            echo "❌ Unit tests failed"
            exit 1
        }
        echo "✅ Unit tests passed"
        echo "📊 Report: app/build/reports/tests/testDebugUnitTest/index.html"
    else
        echo "⏭️  Skipping (requires Android SDK)"
    fi
}

run_lint() {
    echo "🔍 Running Lint Checks..."
    if $LOCAL_BUILD; then
        ./gradlew lint --stacktrace || {
            echo "⚠️  Lint issues found (non-blocking)"
        }
        echo "📊 Report: app/build/reports/lint-results-debug.html"
    else
        echo "⏭️  Skipping (requires Android SDK)"
    fi
}

run_coverage() {
    echo "📊 Generating Coverage Report..."
    if $LOCAL_BUILD; then
        ./gradlew testDebugUnitTestCoverage --stacktrace || {
            echo "⚠️  Coverage generation failed (non-blocking)"
        }
        if [ -f "app/build/reports/coverage/test/debug/index.html" ]; then
            echo "📊 Coverage Report: app/build/reports/coverage/test/debug/index.html"
        fi
    else
        echo "⏭️  Skipping (requires Android SDK)"
    fi
}

run_instrumented_tests() {
    echo "📱 Checking for connected devices..."
    DEVICES=$(adb devices | grep -w "device" | wc -l)
    
    if [ "$DEVICES" -gt 0 ]; then
        echo "✅ Found $DEVICES device(s)"
        echo "🧪 Running instrumented tests..."
        ./gradlew connectedAndroidTest --stacktrace || {
            echo "❌ Instrumented tests failed"
            exit 1
        }
        echo "✅ Instrumented tests passed"
    else
        echo "⏭️  No devices connected. Skipping instrumented tests."
        echo "ℹ️  Connect device or start emulator to run UI tests."
    fi
}

run_monkey_test() {
    echo "🐒 Running Monkey Stress Test..."
    DEVICES=$(adb devices | grep -w "device" | wc -l)
    
    if [ "$DEVICES" -gt 0 ] && [ -n "$(adb shell pm list packages | grep com.nutriscan)" ]; then
        echo "Running 500 random UI events..."
        adb shell monkey -p com.nutriscan -v 500 2>&1 | tee monkey_output.log
        
        if grep -q "crashed" monkey_output.log; then
            echo "❌ App crashed during monkey testing"
            exit 1
        else
            echo "✅ Monkey test passed (no crashes)"
        fi
    else
        echo "⏭️  App not installed or no device. Skipping monkey test."
    fi
}

take_screenshot() {
    echo "📸 Taking screenshot for visual regression..."
    if [ -n "$(adb devices | grep -w device)" ]; then
        mkdir -p screenshots
        adb exec-out screencap -p > screenshots/$(date +%Y%m%d_%H%M%S).png
        echo "✅ Screenshot saved to screenshots/"
    else
        echo "⏭️  No device connected"
    fi
}

# Parse arguments
SKIP_UNIT=false
SKIP_LINT=false
SKIP_INSTRUMENTED=false
SKIP_MONKEY=false

for arg in "$@"; do
    case $arg in
        --skip-unit) SKIP_UNIT=true ;;
        --skip-lint) SKIP_LINT=true ;;
        --skip-instrumented) SKIP_INSTRUMENTED=true ;;
        --skip-monkey) SKIP_MONKEY=true ;;
        --all) ;; # Run everything (default)
        *)
            echo "Usage: $0 [--skip-unit] [--skip-lint] [--skip-instrumented] [--skip-monkey]"
            exit 1
            ;;
    esac
done

# Run test suite
echo "Starting test execution at $(date)"
echo ""

$SKIP_UNIT || run_unit_tests
echo ""

$SKIP_LINT || run_lint
echo ""

run_coverage
echo ""

$SKIP_INSTRUMENTED || run_instrumented_tests
echo ""

$SKIP_MONKEY || run_monkey_test
echo ""

take_screenshot
echo ""

echo "🎉 Test suite completed!"
echo "================================"
echo "Summary:"
echo "  ✅ Unit Tests: $([ $SKIP_UNIT = false ] && echo 'Run' || echo 'Skipped')"
echo "  ✅ Lint: $([ $SKIP_LINT = false ] && echo 'Run' || echo 'Skipped')"
echo "  ✅ Instrumented Tests: $([ $SKIP_INSTRUMENTED = false ] && echo 'Attempted' || echo 'Skipped')"
echo "  ✅ Monkey Test: $([ $SKIP_MONKEY = false ] && echo 'Attempted' || echo 'Skipped')"
echo ""
echo "📁 Reports generated in: $PROJECT_DIR/app/build/reports/"
echo ""

if ! $LOCAL_BUILD; then
    echo "💡 TIP: Tests will run automatically on GitHub push!"
    echo "   Check: https://github.com/YOUR_USERNAME/nutriscan-app/actions"
fi
