package com.dpower4.mobac;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
 
public class MobacWebView extends Activity {
 
	private WebView webView;
 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobacwebview);
 
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("http://mobac.dpower4.com");
 
	}
 
}