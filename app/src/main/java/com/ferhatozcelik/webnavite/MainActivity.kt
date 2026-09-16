package com.ferhatozcelik.webnavite

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ferhatozcelik.webnavite.databinding.ActivityMainBinding
import com.ferhatozcelik.webviewbridge.AndroidWebViewBridgeHelper

/**
 * Sample app demonstrating how to use the `:webviewbridge` library.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    // Button id inside the demo HTML.
    private val buttonId = "buttontest"

    // Simulated product id.
    private val productId = "proId"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.apply {
            val bridge = AndroidWebViewBridgeHelper(Cons.HTML, webView)

            bridge.setClickEvent("a", buttonId) {
                Toast.makeText(applicationContext, "Purchased: $productId", Toast.LENGTH_SHORT).show()
            }

            webView.loadDataWithBaseURL(null, bridge.getHTML(), "text/html", "UTF-8", null)
        }
    }
}
