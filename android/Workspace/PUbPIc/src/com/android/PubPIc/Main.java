package com.android.PubPIc;

import Services.ContainerService;
import Services.FacebookGetIdAsync;
import Services.FacebookService;
import Services.LocationService;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Main extends Activity
{
	private final int SPLASH_DISPLAY_LENGTH = 1000;
	private int _notificationID = 12358;
	private FacebookService fs;
	private ContainerService cs;
	
	private boolean isFacebookSessionValid;
	
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //Clear old (stacked) notifications from notification bar if any, might happen if we crash.
        NotificationManager _notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		_notificationManager.cancel(_notificationID);
		cs = ContainerService.getInstance();
		cs.Main = this;
		
		
//	   Intent eventsIntent2= new Intent(Main.this, TabNavigation.class);
//	   startActivityForResult(eventsIntent2,200);
//      
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		fs.AuthorizeCallback(requestCode, resultCode, data);
		ShowMainTabs();
	}

	public void ShowMainTabs()
	{
		new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //Finish the splash activity so it can't be returned to.
            	Main.this.finish();
                // Create an Intent that will start the main activity.
                //Intent tabsIntent = new Intent(Main.this, TabNavigation.class);
            	Intent tabsIntent = new Intent(Main.this, TabNavigation.class);
                Main.this.startActivity(tabsIntent);
            }
        }, SPLASH_DISPLAY_LENGTH);
	}
    
	
    @Override
    protected void onResume()
    {
    	super.onResume();
    	
    	
         
    	fs = FacebookService.getInstance();
    	
    	isFacebookSessionValid = fs.LoadService(this);
    	
        if (isFacebookSessionValid)
        {
        	fs.GetFacebookIdAsync();
        	fs.ExtendAccessTokenIfNeeded(this);
     		ShowMainTabs();
        }
    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//       this.finish();
//    }
}
