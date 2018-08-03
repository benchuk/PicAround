package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import com.actionbarsherlock.app.SherlockFragment;
//import com.android.PicAround.R;
//import Services.ContainerService;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//public class GalleryFragment extends SherlockFragment
//{
//
//	private WebView webview;
//	private ContainerService cs;
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		super.onCreate(savedInstanceState);
//		cs = ContainerService.getInstance();
//	}
//
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState)
//	{
//		super.onActivityCreated(savedInstanceState);
//	}
//		
//	@Override
//	public void onSaveInstanceState(Bundle outState)
//	{	
//		//webview.saveState(outState);
//	};
//	
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//	{
//		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		View view = inflater.inflate(R.layout.webgallery, container, false);
////		if (webview == null)
////		{
//			//webview = (WebView) view.findViewById(R.id.webgalleryview);
//			webview.getSettings().setJavaScriptEnabled(true);
//			webview.setWebViewClient(new GalleryWebViewClient());
//			
//			webview.loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery?eventID=" + cs.SelectedEventID);
////		}
////		if (savedInstanceState != null)
////		{
////			((WebView) view.findViewById(R.id.webgalleryview)).restoreState(savedInstanceState);
////		}
////		else
////		{
////			((WebView) view.findViewById(R.id.webgalleryview)).loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery/");
////		}
//		//((WebView) view.findViewById(R.id.webgalleryview)).loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery/");
//		// webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//
//		return view;
//	}
//
//	private class GalleryWebViewClient extends WebViewClient
//	{
//		private ContainerService dataService = ContainerService.getInstance();
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url)
//		{
//			view.loadUrl(url);
//			return true;
//		}
//		
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon)
//		{
//			// TODO Auto-generated method stub
//			super.onPageStarted(view, url, favicon);
//			 getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
//		}
//		@Override
//		public void onPageFinished(WebView view, String url)
//		{
//			// TODO Auto-generated method stub
//			super.onPageFinished(view, url);
//			 getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		}
//	
//	}
//
//}