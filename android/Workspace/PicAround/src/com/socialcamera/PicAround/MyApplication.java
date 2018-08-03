package com.socialcamera.PicAround;

import org.acra.*;
import org.acra.annotation.*;
import com.socialcamera.PicAround.R;
import android.app.Application;

//@ReportsCrashes(formKey = "", // will not be used
//mailTo = "benchuk@gmail.com",
//mode = ReportingInteractionMode.TOAST,
//resToastText = R.string.crash_toast_text)
//@ReportsCrashes(formKey = "1W9T23GfAjovWHAjt_jwJBiAQ4mBC4bpatBbxRdVK_OI")

@ReportsCrashes(formKey = "", // will not be used
formUri = "http://picaround.azurewebsites.net/Logs/log",
mode = ReportingInteractionMode.TOAST,
resToastText = R.string.crash_toast_text)
public class MyApplication extends Application {
	 @Override
	  public void onCreate() {
	      super.onCreate();

	      // The following line triggers the initialization of ACRA
	      ACRA.init(this);
	  }
}