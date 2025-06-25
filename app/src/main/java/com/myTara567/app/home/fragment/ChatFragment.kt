package com.myTara567.app.home.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.myTara567.app.R
import com.myTara567.app.serverApi.controller

class ChatFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private var fileChooserCallback: ValueCallback<Array<Uri>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)

        webView = rootView.findViewById(R.id.webView)
        progressBar = rootView.findViewById(R.id.progressBar)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE // Show progress bar when loading starts
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE // Hide progress bar when loading finishes
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100) {
                    progressBar.visibility = View.GONE // Hide progress bar when page is fully loaded
                } else {
                    progressBar.visibility = View.VISIBLE // Show progress bar during page load
                    progressBar.progress = newProgress
                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                fileChooserCallback = filePathCallback
                openFileChooser()
                return true
            }
        }

        val urlToLoad = controller.chatsupport
        webView.loadUrl(urlToLoad)

        return rootView
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // Accept all file types, or specify a type like "image/*"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Choose a file"), REQUEST_FILE_CHOOSER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_FILE_CHOOSER) {
            if (resultCode == Activity.RESULT_OK) {
                val result = data?.data?.let { arrayOf(it) }
                fileChooserCallback?.onReceiveValue(result)
            } else {
                fileChooserCallback?.onReceiveValue(null)
            }
            fileChooserCallback = null
        }
    }

    companion object {
        const val ARG_PARAM1 = "param1"
        const val ARG_PARAM2 = "param2"
        private const val REQUEST_FILE_CHOOSER = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

