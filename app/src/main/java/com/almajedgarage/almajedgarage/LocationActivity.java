package com.almajedgarage.almajedgarage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LocationActivity extends AppCompatActivity {
WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        String latitude = "26.1826146",longitude="50.5308112";
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewController());
        webView.getSettings().getJavaScriptEnabled();
        webView.getSettings().setDomStorageEnabled(true);
//        String url = "http://maps.google.com";
//        webView.loadUrl(url);
        webView.loadUrl("http://www.almajed-garage.com/androidapp/androidmap.html");
    }
    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
