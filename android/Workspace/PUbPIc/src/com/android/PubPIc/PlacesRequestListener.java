package com.android.PubPIc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


    /*
     * Callback after places are fetched.
     */
    public class PlacesRequestListener extends BaseRequestListener {
  
    	
    	
    	
    	public PlacesRequestListener(ProgressDialog dialog,Context con)
       {
    		
	      super();
	      _dialog = dialog;
	      _con=con;
	
       }
    	private Context _con;
    	private ProgressDialog _dialog;
        private JSONArray jsonArray;
        private Handler mHandler = new Handler();

		@Override
        public void onComplete(final String response, final Object state) {
            Log.d("Facebook-FbAPIs", "Got response: " + response);
        //    dialog.dismiss();

            try {
                jsonArray = new JSONObject(response).getJSONArray("data");
                
                if (jsonArray == null) {
            //        showToast("Error: nearby places could not be fetched");
                    return;
                }
            } catch (JSONException e) {
              //  showToast("Error: " + e.getMessage());
                return;
            }
            mHandler .post(new Runnable() {
               // private ListView placesList;

				@Override
                public void run() {
					Toast.makeText(_con, response, Toast.LENGTH_LONG).show();
                  //  placesList = (ListView) findViewById(R.id.places_list);
                 //   placesList.setOnItemClickListener(Places.this);
                  //  placesList.setAdapter(new PlacesListAdapter(Places.this));
					
                }
            });

        }
}
    
