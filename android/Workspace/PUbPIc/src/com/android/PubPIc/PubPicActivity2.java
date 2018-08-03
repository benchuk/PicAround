package com.android.PubPIc;

import com.actionbarsherlock.app.SherlockActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
public class PubPicActivity2 extends SherlockActivity 
{
	private ImageView imageView;

	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        //Used to put dark icons on light action bar
	        boolean isLight = true;

	        menu.add("Save")
	            .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

	        menu.add("Search")
	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	        menu.add("Refresh")
	            .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
	            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

	        return true;
	    }

	 
	public void onCreate(Bundle icicle)
	   {
	      super.onCreate(icicle);
	      setTheme(R.style.Theme_Sherlock_Light); //Used for theme switching in samples

	      setContentView(R.layout.pubpicactivity2);
	      final ImageAdapter a ;
	      Gallery gallery = (Gallery) findViewById(R.id.gallery);
	      gallery.setAdapter(a = new ImageAdapter(this));
	      
	      imageView = (ImageView)findViewById(R.id.ImageView2);
	      gallery.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView parent, View v, int position, long id) {
	        		Toast.makeText(getBaseContext(), 
							"You have selected picture " + (id+1) + " of your Images", 
							Toast.LENGTH_SHORT).show();
					imageView.setImageResource(a.mImageIds[position]); 	
	          }
	      }); 	
//	      Button b = (Button) findViewById(R.id.viewImages);
//	      b.setOnClickListener(new View.OnClickListener() {
//	         public void onClick(View arg0) {
//	         setResult(RESULT_OK);
//	         finish();
//	         } 
//	      });
	   }

}
