package com.kpfunusa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.adapter.RestaurantAdapter;
import com.kpfunusa.bean.Restaurant;
import com.kpfunusa.dialog.AlertDialogManager;
import com.kpfunusa.gps.GPSTracker;

public class ActivityRestaurantSearch extends Activity {
	ProgressDialog pdialog;
	AlertDialogManager alert = new AlertDialogManager();
	ListView list;
	public RestaurantAdapter adapter;
	EditText editsearch;
	private ImageView refresh;
	static ArrayList<Restaurant> arraylist = new ArrayList<Restaurant>();
	
	String pageToken;
	GPSTracker gpstracker;
	static double latitude, longitude;

	ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_search);
		cd = new ConnectionDetector(getApplicationContext());
		// Locate the ListView in listview_main.xml
		list = (ListView) findViewById(R.id.lvResearch);
		/* get latitude && longitude */
		gpstracker = new GPSTracker(getApplicationContext());
		refresh = (ImageView) findViewById(R.id.refresh);
		refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	arraylist.clear();
            	Search();
            }
        });
		
		if (!cd.isConnectingToInternet()) {
			showAlertDialogCheckNetwork(getParent(), "KpFun alert!",
					"No network, Please check your data connection!"
							+ " Exit and Login again ? ", false);
			return;
		} else {
			if (GlobalVariable.globalvariable){
				Search();	
			}
			// Pass results to ListViewAdapter Class
			adapter = new RestaurantAdapter(this,
					R.layout.restaurant_search_row, arraylist);
			list.setAdapter(adapter);
		}
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {

				Restaurant restaurant = arraylist.get(position);
				if(restaurant.getCompany_name().equals("Load More")){
					arraylist.remove(position);
					LoadMore();
					
					
				}else{
					Intent in = new Intent(getParent(), ActivityRestaurantDetails.class);
					TabGroupActivity parentActivity = (TabGroupActivity) getParent();
					
					in.putExtra("position", String.valueOf(position));
					
					parentActivity.startChildActivity("RestaurantDetailsActivity", in);
				}
			}
		});
		/* set onclick for iv_backScreenRestaurantSearch 
		iv_backScreenRestaurantSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showAlertDialogExit(getParent(), "KpFun alert!",
						"Do you want to exit KpFun ?", false);
			}
		});*/
	}

	@SuppressWarnings("unused")
	private void Search() {
		pdialog = ProgressDialog.show(getParent(), "", "Retrieving Restaurants...");
		RequestQueue queue = Volley.newRequestQueue(this);
		/* latitude && longitudeg */
		latitude = gpstracker.getLatitude();
		longitude = gpstracker.getLongitude();

		final String str_latitude = String.valueOf(latitude);
		final String str_longitude = String.valueOf(longitude);
		// Toast.makeText(getParent(),
		// "lat and long"+str_latitude+" "+str_longitude,Toast.LENGTH_LONG).show();

		final String str_zipcode = ActivityLogin.zip_code;

		StringRequest myReq = new StringRequest(Method.POST,
				ActivityLogin.webServiceURL,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "search");
				
				Double lat=Double.parseDouble(str_latitude);
				
				if(lat==0){
					String test = str_zipcode.toString();
					//String test2 = "123";
					if(test.toString()=="null"){
						params.put("zip code", "76109");
					}else if(test==null){
						params.put("zip code", "76109");
					}else{
						params.put("zip code", str_zipcode);
					}
					
				}else{
					params.put("latitude",str_latitude);
					  params.put("longitude",str_longitude);
					  params.put("user_id", ActivityLogin.id);
				}
				  
				 
				//params.put("zip code", "76120");

				/*
				 * We use zip code = 76120 to test app :D please delete under
				 * line code and replace with code 1 or 2
				 */
				//

				/*
				 * if y
				 * 
				 * ou want to search restaurant with latitude and longitude 1=>
				 */
				/* params.put("zip code", str_zipcode); */

				/*
				 * if you want to search restaurant with latitude and longitude
				 * 2=>
				 */

				/*
				 * params.put("latitude",str_latitude);
				 * params.put("longitude",str_longitude);
				 */

				return params;
			};
		};
		myReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(myReq);

	}
	
	@SuppressWarnings("unused")
	private void LoadMore() {
		pdialog = ProgressDialog.show(getParent(), "", "Retrieving Restaurants...");
		RequestQueue queue = Volley.newRequestQueue(this);
		/* latitude && longitudeg */
		latitude = gpstracker.getLatitude();
		longitude = gpstracker.getLongitude();

		final String str_latitude = String.valueOf(latitude);
		final String str_longitude = String.valueOf(longitude);
		// Toast.makeText(getParent(),
		// "lat and long"+str_latitude+" "+str_longitude,Toast.LENGTH_LONG).show();

		final String str_zipcode = ActivityLogin.zip_code;

		StringRequest myReq = new StringRequest(Method.POST,
				ActivityLogin.webServiceURL,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "loadMore");
				params.put("page_token", pageToken);
				params.put("user_id", ActivityLogin.id);
				
				Double lat=Double.parseDouble(str_latitude);
				
				if(lat==0){
					String test = str_zipcode.toString();
					//String test2 = "123";
					if(test.toString()=="null"){
						params.put("zip code", "76109");
					}else if(test==null){
						params.put("zip code", "76109");
					}else{
						params.put("zip code", str_zipcode);
					}
					
				}else{
					params.put("latitude",str_latitude);
					  params.put("longitude",str_longitude);
				}
				

				return params;
			};
		};
		myReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(myReq);

	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONArray items = json.getJSONArray("restaurants");
					pageToken=json.getString("next_page");
					for (int i = 0; i < items.length(); i++) {
						JSONObject item = items.getJSONObject(i);
						Restaurant res = new Restaurant();
						res.setCompany_name(item.optString("company_name"));
						res.setAddress(item.optString("street_address"));
						res.setCity(item.optString("city"));
						if(item.optString("distance").length()<15){
							res.setDistance("(" + item.optString("distance") + " miles)");
						}else{
							res.setDistance(item.optString("distance"));
						}
						res.setImg(item.optString("img"));
						res.setImg_thumb(item.optString("img_thumb"));
						res.setId(item.optString("user_id"));
						res.setState(item.optString("state"));
						res.setZip_code(item.optString("zip_code"));
						res.setTerms(item.optString("terms"));
						res.setPercent(item.optString("percent"));
						res.setDescription(item.optString("description"));
						res.setLat(item.optString("lat"));
						res.setLng(item.optString("lng"));
						arraylist.add(res);

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(!pageToken.equals("null")){
					Restaurant loadMore=new Restaurant();
					loadMore.setCompany_name("Load More");
					loadMore.setDistance("");
					loadMore.setImg_thumb("loadMore");
					arraylist.add(loadMore);
				}
				adapter.notifyDataSetChanged();
				pdialog.dismiss();
				
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				pdialog.dismiss();
			}
		};
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialogCheckNetwork(Context context, String title,
			String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog
					.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}

		});

		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				System.exit(0);
			}

		});
		// Showing Alert Message
		alertDialog.show();
	}

	/* alert to exit */
	@SuppressWarnings("deprecation")
	public void showAlertDialogExit(Context context, String title,
			String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog
					.setIcon((status) ? R.drawable.success : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton2("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}

		});

		alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		});
		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showAlertDialogExit(getParent(), "KpFun alert!",
					"Do you want to exit KpFun ?", false);
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		return true;
	}
}
