package com.socialcamera.PicAround;
//package com.android.PicAround;
//
//import com.android.PicAround.R;
//
//import android.app.TabActivity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TabHost;
//import android.widget.TabHost.TabSpec;
//
//public class MainTabsActivity extends TabActivity
//{
//	 @Override
//	    public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        setContentView(R.layout.main_tabs);
//	 
//	     
//	        
//	        
//	        TabHost tabHost = getTabHost();
//	 
//	        // Tab for places
//	        TabSpec places = tabHost.newTabSpec("1");
//	        // setting Title and Icon for the Tab
//	        places.setIndicator("", getResources().getDrawable(R.drawable.icon_1_tab));
//	        Intent placesIntent = new Intent(this, PUbPIcActivity.class);
//	        places.setContent(placesIntent);
//	 
//	        // Tab for events
//	        TabSpec events = tabHost.newTabSpec("2");
//	        events.setIndicator("", getResources().getDrawable(R.drawable.icon_2_tab));
//	        Intent eventsIntent = new Intent(this, TabNavigation.class);
//	        events.setContent(eventsIntent);
//	 
//	        // Tab for camera
//	        TabSpec camera = tabHost.newTabSpec("3");
//	        camera.setIndicator("", getResources().getDrawable(R.drawable.icon_3_tab));
//	        Intent cameraIntent = new Intent(this, CameraIntent.class);
//	        camera.setContent(cameraIntent);
//	        
//	        // Tab for gallery
//	        TabSpec gallery = tabHost.newTabSpec("4");
//	        gallery.setIndicator("", getResources().getDrawable(R.drawable.icon_4_tab));
//	        Intent galleryIntent = new Intent(this, WebGallery.class);
//	        gallery.setContent(galleryIntent);
//	 
//	        // Adding all TabSpec to TabHost
//	        tabHost.addTab(places); 
//	        tabHost.addTab(events); 
//	        tabHost.addTab(camera); 
//	        tabHost.addTab(gallery);
//	        tabHost.setCurrentTab(2);
//	    }
//}
