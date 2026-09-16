package com.ferhatozcelik.webviewbridge

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.jsoup.Jsoup

/**
 * Bridges a [WebView]'s HTML content with native Kotlin code.
 *
 * The helper parses the supplied HTML and rewrites the `onclick` attribute of the
 * elements you are interested in, so that clicks inside the web page are forwarded
 * to a [WebClickInterface] on the Android side.
 *
 * ```kotlin
 * val bridge = AndroidWebViewBridgeHelper(html, webView)
 * bridge.setClickEvent("a", "buy-button") {
 *     Toast.makeText(this, "Purchased!", Toast.LENGTH_SHORT).show()
 * }
 * webView.loadDataWithBaseURL(null, bridge.getHTML(), "text/html", "UTF-8", null)
 * ```
 *
 * @param html The initial HTML document.
 * @param webView The target [WebView]. JavaScript is enabled automatically.
 */
@SuppressLint("SetJavaScriptEnabled")
class AndroidWebViewBridgeHelper(
    html: String,
    private val webView: WebView,
) {

    private var html: String = html
    private val clickListeners = LinkedHashMap<String, WebClickInterface>()
    private var bridgeRegistered = false

    init {
        webView.settings.javaScriptEnabled = true
    }

    /**
     * Registers a click listener for the element identified by [elementId].
     *
     * The first element matching [elementTag] whose `id` equals [elementId] gets an
     * `onclick` handler that calls back into [listener] when clicked.
     */
    fun setClickEvent(elementTag: String, elementId: String, listener: WebClickInterface) {
        clickListeners[elementId] = listener

        val document = Jsoup.parse(html)
        document.getElementsByTag(elementTag)
            .firstOrNull { it.id().replace("\\\"", "") == elementId }
            ?.attr("onclick", "$BRIDGE_NAME.onClicked('$elementId')")
        html = document.html()

        registerBridge()
    }

    /** Returns the (possibly rewritten) HTML that should be loaded into the [WebView]. */
    fun getHTML(): String = html

    private fun registerBridge() {
        if (bridgeRegistered) return
        webView.addJavascriptInterface(JsBridge(), BRIDGE_NAME)
        bridgeRegistered = true
    }

    private inner class JsBridge {
        @JavascriptInterface
        fun onClicked(elementId: String) {
            clickListeners[elementId]?.onWebClick()
        }
    }

    companion object {
        const val BRIDGE_NAME = "AndroidWebViewBridge"
    }
}
