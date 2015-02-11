package com.kpfunusa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.bean.Restaurant;
import com.kpfunusa.gps.GPSTracker;
import com.kpfunusa.loadimage.ImageLoader;

public class ActivityRestaurantDetails extends Activity {
	ProgressDialog pdialog;
	GPSTracker gpstracker;

	ArrayList<Restaurant> arraylist = ActivityRestaurantSearch.arraylist;
	
	Button btnGetReward;
	
	TextView mTvName, mTvAddress, mTvCity, mTvDistance2, mTvReward, mTvTerms;
	ImageView mIvImg;
	String position;
	static Restaurant res = new Restaurant();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		position = i.getStringExtra("position");
		getRestaurant();
	}

	/* dialog check member */
	@SuppressWarnings("deprecation")
	public void showAlertDialogCheckMember(Context context, String title,
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
				String url = "https://kpfun.com/membership.php";
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			}

		});
		// Showing Alert Message
		alertDialog.show();
	}
	
	
	@SuppressWarnings("unused")
	private void getRestaurant() {
		pdialog = ProgressDialog.show(getParent(), "", "Loading...");
		RequestQueue queue = Volley.newRequestQueue(this);

		final String str_latitude = String.valueOf(ActivityRestaurantSearch.latitude);
		final String str_longitude = String.valueOf(ActivityRestaurantSearch.longitude);
		// Toast.makeText(getParent(),
		// "lat and long"+str_latitude+" "+str_longitude,Toast.LENGTH_LONG).show();

		final String str_zipcode = ActivityLogin.zip_code;

		StringRequest myReq = new StringRequest(Method.POST,
				ActivityLogin.webServiceURL,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Restaurant restaurant = arraylist.get(Integer.parseInt(position));
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "getDetails");
				params.put("latitude", restaurant.getLat());
				params.put("longitude", restaurant.getLng());
				params.put("company_name", restaurant.getCompany_name());
				params.put("user_lat", String.valueOf(ActivityRestaurantSearch.latitude));
				params.put("user_lng", String.valueOf(ActivityRestaurantSearch.longitude));
				return params;
			};
		};
		queue.add(myReq);
	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			private TextView mTvGift_Details;
			private TextView mTvTerms_Desc;
			private ImageView mbtnMap;

			@Override
			public void onResponse(String response) {
				try {
					JSONObject json = new JSONObject(response);
					JSONObject item = json.getJSONObject("restaurant");
					String sub = json.getString("sub");
					String sub_message = json.getString("sub_message");
						res.setCompany_name(item.optString("company_name"));
						res.setAddress(item.optString("street_address"));
						res.setCity(item.optString("city"));
						res.setDistance("(" + item.optString("distance") + " miles)");
						res.setImg(item.optString("img"));
						res.setImg_thumb(item.optString("img_thumb"));
						res.setId(item.optString("user_id"));
						res.setState(item.optString("state"));
						res.setZip_code(item.optString("zip_code"));
						res.setTerms(item.optString("terms"));
						res.setPercent(item.optString("percent"));
						res.setDescription(item.optString("description"));
						res.setReference(item.optString("reference"));
						res.setWebsite(item.optString("website"));
						res.setReward(item.optString("reward"));
						res.setVideo(item.optString("video"));
						res.setButton(item.optString("button"));
						
						Log.d("adview input value", res.getCompany_name());
						Log.d("adview input value", res.getDescription());
						Log.d("adview input value", res.getReward());
						
						//IF res.getReward == "" SHOW 'ad view'
						if (res.getReward() == ""){
							GlobalVariable.globalvariable = false;
							Intent adview = new Intent(ActivityRestaurantDetails.this, ActivityAdView.class);
//							Intent adview = new Intent(getParent(), ActivityAdView.class);
//							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							
							adview.putExtra("companyname", res.getCity());
							adview.putExtra("description", res.getDescription());
							adview.putExtra("video_id", res.getVideo());
							
							Log.d("adview input value", res.getCompany_name());
							Log.d("adview input value", res.getDescription());
							
//							ActivityRestaurantDetails.this.finish();
							startActivity(adview);
//							parentActivity.startChildActivity("ActivityAdview", adview);
							GlobalVariable.globalvariable = true;
						}
						//ELSE show Restaurant Details (this is what shows currently - needs interface update)
						else{
							setContentView(R.layout.restaurant_details);
							mTvName = (TextView) findViewById(R.id.tvNameDetail);
							mTvAddress = (TextView) findViewById(R.id.tvAddressDetail);
							//mTvCity = (TextView) findViewById(R.id.tvCity);
							mIvImg = (ImageView) findViewById(R.id.ivLogoDetail);
							mTvDistance2=(TextView)findViewById(R.id.distance2);
							mTvReward=(TextView) findViewById(R.id.reward2);
//							mTvTerms=(TextView) findViewById(R.id.terms2);
							mTvTerms_Desc = (TextView)findViewById(R.id.terms_desc);
							mTvGift_Details = (TextView)findViewById(R.id.gift_rewards_details);
							
							mTvName.setText(res.getCompany_name());
							
							
							
							if(res.getPercent().equals("Receive 0% off your entire bill")){
								mTvGift_Details.setText("Hello");
							}else{
								mTvGift_Details.setText("Receive " + res.getPercent() + "% off your entire bill");
							}
//							if(res.getPercent().equals("")){
//								mTvName.setText(res.getCompany_name());
//							}else{
//								mTvName.setText(res.getCompany_name() + " (" + res.getPercent() + "% Reward)");
//							}
							if(res.getAddress().equals("")){
								mTvAddress.setVisibility(4);
							}else{
								mTvAddress.setText(res.getAddress() + "\n" + res.getCity() + ", " + res.getState() + " " + res.getZip_code());	
							}
							mTvTerms_Desc.setText(res.getTerms());
							//mTvCity.setText(city + ", " + state + " " + zip_code);
							Log.d("Distance TextView Tag", res.getDistance());
							String Distance = res.getDistance().replace("(", "");
							Distance =Distance.replace(")", "");
							Log.d("Distance TextView Tag", Distance);
							mTvDistance2.setText(Distance);
							if(res.getDistance().length()<10){
								mTvDistance2.setVisibility(4);//invisible
							}
							mTvReward.setText(res.getDescription());
							
							String urlImg = "http://kpfun.com";
							if (mIvImg != null) {
								int loader = R.drawable.loader; 
								ImageLoader imgLoader = new ImageLoader(getApplicationContext());
								imgLoader.DisplayImage(urlImg+res.getImg(), loader, mIvImg, true);
							}
							mbtnMap = (ImageView)findViewById(R.id.iv_map_reward);
							mbtnMap.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent mapintent = new Intent(ActivityRestaurantDetails.this, RestaurantMap.class);
									
									mapintent.putExtra("zipCode", res.getZip_code());
									mapintent.putExtra("CityInfo", res.getCity());
									mapintent.putExtra("AddressInfo", res.getAddress());
									Log.d("zipcode and city and address in Details", res.getZip_code() + res.getCity() + res.getAddress());
									
									startActivity(mapintent);
								}
							});
							btnGetReward = (Button) findViewById(R.id.btnGetReward);
							if(res.getId().equals("ad")){
								btnGetReward.setText("Learn More");
							}
							btnGetReward.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									if(res.getId().equals("ad")){
										String url = res.getWebsite();
										Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
										startActivity(intent);
									}else if (ActivityLogin.dealer_id.toString().equalsIgnoreCase("0")) {
										showAlertDialogCheckMember(getParent(), "KpFun alert!", "Loyalty Rewards are exclusive to paid members. Do you want to purchase membership?", true);
										return;
									} else {
										Intent intent = new Intent(getParent(), ActivitySharing.class);
										TabGroupActivity parentActivity = (TabGroupActivity) getParent();
										parentActivity.startChildActivity("ActivitySharing", intent);
										return;
									}
									
								}
							});
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//adapter.notifyDataSetChanged();
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
	
	
	/* dialog check terms */
//	@SuppressWarnings("deprecation")
//	public void showAlertTerms(Context context, String title,
//			String message, Boolean status) {
//		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
//
//		// Setting Dialog Title
//		alertDialog.setTitle(title);
//
//		// Setting Dialog Message
//		alertDialog.setMessage(message);
//
//		if (status != null)
//			// Setting alert dialog icon
//			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
//
//		// Setting OK Button
//		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//			}
//
//		});
//
//		
//		// Showing Alert Message
//		alertDialog.show();
//	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityRestaurantDetails.this.finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		
		return true;
	}
		
	
}
