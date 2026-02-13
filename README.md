# 🎡 Lucky Spin Wheel (Advanced)

[![Release](https://jitpack.io/v/sketchlibx/Lucky-Spin-Wheel.svg)](https://jitpack.io/#sketchlibx/Lucky-Spin-Wheel)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A powerful, highly customizable, and **premium-looking** Lucky Spin Wheel library for Android. Now supports **Icons**, **Custom Colors**, and comes with a built-in **Royal Gold & Dark Theme**.

<p align="center">
  <img src="screenshots/demo.gif" width="250" alt="Demo GIF"/>
  <img src="screenshots/preview.png" width="250" alt="Preview Image"/>
</p>

---

## ✨ Features (v1.0.0)

- 🎨 **Premium Default Theme:** Royal Dark Blue & Gold theme out of the box.
- 🖼️ **Icon Support:** Add images/icons along with text in wheel slices.
- 🌈 **Full Customization:** Change slice colors, text colors, and border styles.
- 👆 **Separate Cursor View:** A highly detailed, customizable pointer/cursor view.
- ⚡ **Smooth Physics:** Realistic deceleration animation with target selection.
- 📱 **Responsive:** Scales perfectly on all screen sizes.

---

## 📦 Installation

### Step 1. Add the JitPack repository

Add this to your project-level `settings.gradle` (or root `build.gradle`):

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url '[https://jitpack.io](https://jitpack.io)' }
    }
}
```

### Step 2. Add the dependency
Add this to your module-level build.gradle (usually app/build.gradle):

```gradle
dependencies {
    implementation 'com.github.sketchlibx:Lucky-Spin-Wheel:v0.1-alpha'
}
```

### 🚀 Usage
### 1. Add Views to XML Layout
We use two views: LuckyWheelView (the wheel) and CursorView (the pointer). This allows for a realistic overlay effect.

```xml
<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center">

    <sketchlib.lucky.spinwheel.LuckyWheelView
        android:id="@+id/wheel_view"
        android:layout_width="320dp"
        android:layout_height="320dp"
        android:layout_centerInParent="true"
        android:padding="10dp" />

    <sketchlib.lucky.spinwheel.CursorView
        android:id="@+id/cursor_view"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_alignTop="@id/wheel_view"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="-15dp" />

</RelativeLayout>
```

### 2. Setup in Java
You can configure the wheel in 3 modes.
Mode A: Default Premium Theme (Text Only)
Pass 0 as the color to use the built-in Royal Theme.

```java
List<LuckyItem> data = new ArrayList<>();

// text, icon, color (0 = default theme)
data.add(new LuckyItem("100", 0, 0)); 
data.add(new LuckyItem("200", 0, 0));
data.add(new LuckyItem("500", 0, 0));
data.add(new LuckyItem("0", 0, 0));

binding.wheelView.setData(data);
binding.wheelView.setRound(8); // Spin 8 times
```

Mode B: With Icons 🖼️
Pass a Drawable ID (e.g., R.drawable.coin) as the second parameter.

```java
data.add(new LuckyItem("100", R.drawable.ic_coin, 0));
data.add(new LuckyItem("Win", R.drawable.ic_trophy, 0));
```

Mode C: Full Custom Colors 🎨
Pass a Color.parseColor(...) as the third parameter to override the default theme.

```java
data.add(new LuckyItem("10", 0, Color.parseColor("#EF5350"))); // Red
data.add(new LuckyItem("20", 0, Color.parseColor("#FFA726"))); // Orange
```

### 3. Handle Spin Events

```java
binding.wheelView.setSpinListener(new LuckyWheelView.SpinListener() {
    @Override
    public void onRotateStart() {
        // Disable button
        binding.btnSpin.setEnabled(false);
    }

    @Override
    public void onRotateEnd(int resultIndex) {
        // Enable button & Show Result
        binding.btnSpin.setEnabled(true);
        String reward = data.get(resultIndex).topText;
        Toast.makeText(context, "You won: " + reward, Toast.LENGTH_SHORT).show();
    }
});

// Start Spinning (Random Winner)
int index = new Random().nextInt(data.size());
binding.wheelView.startLuckyWheelWithTargetIndex(index);
```

🎨 XML Attributes
You can customize the look and feel directly from XML layout.

LuckyWheelView
| Attribute | Format | Description |
|---|---|---|
| app:lsw_backgroundColor | Color | Background behind slices |
| app:lsw_textColor | Color | Default text color |
| app:lsw_borderColor | Color | Outer rim color |
| app:lsw_edgeWidth | Dimension | Width of the outer border |
| app:lsw_textSize | Dimension | Size of the text inside slices |
CursorView
| Attribute | Format | Description |
|---|---|---|
| app:lsw_cursor_bodyColor | Color | Main color of the pointer |
| app:lsw_cursor_borderColor | Color | Border color (Default: Gold Gradient) |
| app:lsw_cursor_centerColor | Color | Center dot color (Default: Gold Gradient) |

l
🤝 Contribution
Contributions are welcome!
 * Fork the Project
 * Create your Feature Branch
 * Commit your Changes
 * Push to the Branch
 * Open a Pull Request
📄 License
Copyright 2026 SketchLibX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

<p align="center">Made with ❤️ by <a href="https://www.google.com/search?q=https://github.com/sketchlibx">SketchLibX</a></p>
