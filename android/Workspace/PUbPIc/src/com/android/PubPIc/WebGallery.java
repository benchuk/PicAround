package com.android.PubPIc;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebGallery extends Activity
{
	public void onCreate(Bundle v)
	   {
	      super.onCreate(v);
	      //myWebView.loadUrl("http://picaround.azurewebsites.net/Home/Images");
	      setContentView(R.layout.webgallery);
//	      WebView webview = (WebView) findViewById(R.id.webgalleryview);
//	      webview.getSettings().setJavaScriptEnabled(true);
//	      webview.setWebViewClient(new GalleryWebViewClient());
//	      webview.loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery/");
	   
	   }
	
	public class GalleryWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }
	}
}
