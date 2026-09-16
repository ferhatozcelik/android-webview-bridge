# Android WebView Bridge

A tiny, dependency-light Android library that lets a web page inside a `WebView`
call back into native Kotlin code. Point it at an element (by tag + id), and its
`onclick` handler is wired to a Kotlin callback — perfect for hybrid apps that render
HTML but need native behaviour on a specific button.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## Features

- Attach native click listeners to elements in any HTML string.
- Automatic JavaScript enabling and bridge registration.
- Supports multiple bridged elements, each with its own callback.
- Small footprint — the only runtime dependency is [jsoup](https://jsoup.org/).
- Written in Kotlin, ships as a standard Android library (AAR).

## Modules

| Module | Description |
| --- | --- |
| `:webviewbridge` | The reusable library. This is what you depend on. |
| `:app` | Sample application that demonstrates the library with an inline HTML "buy" button. |

## Installation

```kotlin
dependencies {
    implementation("com.ferhatozcelik:webviewbridge:1.0.0")
}
```

Or, when working inside this repository, depend on the module directly:

```kotlin
dependencies {
    implementation(project(":webviewbridge"))
}
```

## Usage

```kotlin
class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webView)

        val bridge = AndroidWebViewBridgeHelper(html = myHtml, webView = webView)

        bridge.setClickEvent("a", "buy-button") {
            Toast.makeText(this, "Purchased!", Toast.LENGTH_SHORT).show()
        }

        webView.loadDataWithBaseURL(null, bridge.getHTML(), "text/html", "UTF-8", null)
    }
}
```

Given HTML like:

```html
<a id="buy-button" href="#">Buy</a>
```

the library rewrites the element's `onclick` attribute to call into the registered
Kotlin callback.

## API

| Member | Description |
| --- | --- |
| `AndroidWebViewBridgeHelper(html, webView)` | Creates the bridge and enables JavaScript. |
| `setClickEvent(elementTag, elementId, listener)` | Wires `listener` to the first element with the given tag and id. |
| `getHTML()` | Returns the rewritten HTML to load into the `WebView`. |
| `WebClickInterface` | Functional interface with a single `onWebClick()` method. |

## Requirements

| Tool | Version |
| --- | --- |
| minSdk | 21 |
| compileSdk / targetSdk | 36 |
| Gradle | 8.14.5 |
| Android Gradle Plugin | 8.13.2 |
| Kotlin | 2.2.21 |
| JDK | 17 |

## Building

```bash
./gradlew :webviewbridge:assembleRelease   # build the AAR
./gradlew :app:assembleDebug                # build the sample app
```

## License

```
Copyright 2024 Ferhat OZCELIK

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0
```

## Author

**Ferhat OZCELIK**

- GitHub: [@ferhatozcelik](https://github.com/ferhatozcelik)
- LinkedIn: [ferhatozcelik](https://www.linkedin.com/in/ferhatozcelik/)
