# 🚀 Push NutriScan to GitHub for Automatic APK Building

## ✅ Repository is Ready!

Git repository initialized at: `/home/shark/.openclaw/workspace/nutriscan-app/`
All files committed and ready to push.

## 📋 Step-by-Step Guide

### 1. Create GitHub Repository

1. Go to https://github.com/new
2. **Repository name**: `nutriscan-app` (or your choice)
3. **Description**: "Privacy-first nutrition tracker with ML Kit (Android)"
4. **Visibility**: Public or Private (your choice)
5. **DON'T initialize** with README (we already have one)
6. Click **Create repository**

### 2. Push to GitHub

GitHub will show commands. Use these:

```bash
cd /home/shark/.openclaw/workspace/nutriscan-app

# Add your GitHub repo as remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/nutriscan-app.git

# Rename branch to main (if needed)
git branch -M main

# Push to GitHub
git push -u origin main
```

**If prompted for credentials:**
- Username: Your GitHub username
- Password: Use a **Personal Access Token** (not your password)
  - Generate at: https://github.com/settings/tokens
  - Permissions: `repo` scope

### 3. Enable GitHub Actions

1. Go to your repo: `https://github.com/YOUR_USERNAME/nutriscan-app`
2. Click **Actions** tab
3. If prompted, click **"I understand my workflows, go ahead and enable them"**
4. The build will start automatically!

### 4. Download APK

After ~5 minutes:
1. Go to **Actions** tab
2. Click the latest workflow run
3. Scroll to **Artifacts**
4. Download **nutriscan-debug-apk**
5. Unzip and install `app-debug.apk` on your phone

## 🎯 Workflow Status

The GitHub Actions workflow (`.github/workflows/build.yml`) will:
- ✅ Set up Java 17 automatically
- ✅ Build debug APK
- ✅ Upload APK as artifact
- ✅ Run on every push/PR

## 🔄 Rebuilding

To trigger a new build after changes:
```bash
cd /home/shark/.openclaw/workspace/nutriscan-app
git add .
git commit -m "Your changes"
git push
```

Or click **"Run workflow"** in the Actions tab.

## 🐛 Troubleshooting

### "Actions are disabled"
- Go to Settings → Actions → General
- Select "Allow all actions and reusable workflows"

### "Push rejected"
- Make sure you used a Personal Access Token, not password
- Check repo URL is correct

### "Build failed"
- Click on the failed workflow
- Check logs for errors
- Most likely: dependency download timeout (just re-run)

## 📱 Installing APK on Samsung Flip 7

1. **Download APK** from GitHub Actions artifacts
2. **Transfer to phone** (USB, Google Drive, email, etc.)
3. **Enable Unknown Sources**:
   - Settings → Apps → Special access → Install unknown apps
   - Select your file manager → Allow
4. **Tap APK** in file manager
5. **Install** → Done!

## 🎉 Next Steps

Once built:
1. Install on phone
2. Grant camera permission
3. Scan a product barcode
4. Log your first meal
5. Check the daily dashboard

## 🔒 Privacy Note

Even though the repo is on GitHub:
- No user data ever leaves your phone
- All ML processing is on-device
- GitHub only builds the APK, doesn't see your usage

## 💡 Alternative: Private Repo

If you want complete privacy:
1. Make repo **Private** during creation
2. Only you can see the code
3. APK builds still work
4. Free for personal use

---

**Ready to push?** Follow steps 1-4 above and you'll have your APK in ~5 minutes!
