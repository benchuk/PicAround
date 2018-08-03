//package WebHelpers;
//
//import Services.ContainerService;
//import android.graphics.Bitmap;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//public class GalleryWebViewClient extends WebViewClient
//{
//	private ContainerService dataService = ContainerService.getInstance();
//	@Override
//	public boolean shouldOverrideUrlLoading(WebView view, String url)
//	{
//		view.loadUrl(url);
//		return true;
//	}
//	
//	@Override
//	public void onPageStarted(WebView view, String url, Bitmap favicon)
//	{
//		// TODO Auto-generated method stub
//		super.onPageStarted(view, url, favicon);
//		//this.getSherlock().setProgressBarIndeterminate(true);
//	}
//	@Override
//	public void onPageFinished(WebView view, String url)
//	{
//		// TODO Auto-generated method stub
//		super.onPageFinished(view, url);
//		//this.getSherlock().setProgressBarIndeterminate(false);
//	}
//
//}
//
//
//
//// setContentView(R.layout.webgallery);
//// View view = inflater.inflate(R.layout.webgallery, container, false);
//// if (webview == null)
//// {
//// webview = (WebView) findViewById(R.id.webgalleryview);
//// webview.getSettings().setJavaScriptEnabled(true);
//// webview.setWebViewClient(new GalleryWebViewClient());
////
//// webview.loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery?eventID="
//// + cs.SelectedEventID);
//// }
//// if (savedInstanceState != null)
//// {
//// ((WebView)
//// view.findViewById(R.id.webgalleryview)).restoreState(savedInstanceState);
//// }
//// else
//// {
//// ((WebView)
//// view.findViewById(R.id.webgalleryview)).loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery/");
//// }
//// ((WebView)
//// view.findViewById(R.id.webgalleryview)).loadUrl("http://picaround.azurewebsites.net/Mobile/Gallery/");
//// webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);