package com.kpfunusa;

import java.io.InputStream;
import java.net.URL;
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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.adapter.RewardsAdapter;
import com.kpfunusa.bean.Rewards;
import com.kpfunusa.loadimage.ImageLoader;

public class ActivityYourGift extends Activity {
	String terms;
	TextView tv_restaurantRewardsTitle;
	TextView tvGift, tvFrom, tvCaption, tvCompanyName, tvAddress, tvAddress2, tvDealerId, tvTrackingId, tvTerms, tvClose;
	ImageView ivUserImg, ivImageView;
	ImageView ivDealImg;
	String urlImg, user_img;
	public String user_id_restaurant;
	private TextView tvTerms_desc;
	private TextView tvValidUsername;
	private ImageView mbtnGiftMap;
	private String address;
	private String city;
	private String zip_code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.your_gift_layout);
		tv_restaurantRewardsTitle =(TextView)findViewById(R.id.tv_restaurantRewardsTitle);
		/*set font for view*/
		//setFont();
		Intent in = getIntent();
		String restaurantName = in.getStringExtra("company_name");
		String deal_img = in.getStringExtra("deal_img");
		address = in.getStringExtra("address");
		city = in.getStringExtra("city");
		String voucher = in.getStringExtra("voucher");
		String state = in.getStringExtra("state");
		zip_code = in.getStringExtra("zip_code");
		String name = in.getStringExtra("name");
		String title = in.getStringExtra("title");
		user_id_restaurant = in.getStringExtra("user_id");
		String distance = in.getStringExtra("distance");
		terms = in.getStringExtra("terms");
		user_img = in.getStringExtra("user_img");
		String caption = in.getStringExtra("caption");
		
		
		tvCompanyName = (TextView)findViewById(R.id.company_name);
		tvAddress = (TextView)findViewById(R.id.address);
		//tvAddress2 = (TextView)findViewById(R.id.address2);
		tvGift = (TextView)findViewById(R.id.gift);
		tvFrom = (TextView)findViewById(R.id.from);
		tvCaption = (TextView)findViewById(R.id.caption);
		tvDealerId = (TextView)findViewById(R.id.dealer_id);
		tvTrackingId = (TextView)findViewById(R.id.tracking_id);
//		tvTerms = (TextView)findViewById(R.id.terms);
		tvTerms_desc = (TextView)findViewById(R.id.terms_desc);
		tvValidUsername = (TextView)findViewById(R.id.valid_username);
		
		ivDealImg = (ImageView)findViewById(R.id.deal_img);
		ivUserImg = (ImageView)findViewById(R.id.user_img);
		
		
		tvCompanyName.setText(restaurantName);
		tvAddress.setText(address + "\n" + city+", "+state+" "+zip_code);
		//tvAddress2.setText(city+", "+state+" "+zip_code);
		tvGift.setText(title);
		tvFrom.setText(name + " sent you this gift!");
		if (caption.length() == 0){
			tvCaption.setText("Test");
		}else{
			tvCaption.setText(caption);
		}
		tvTerms_desc.setText(terms);
		tvValidUsername.setText("Valid for " + name);
		tvDealerId.setText("");
		tvTrackingId.setText("Tracking: " + voucher);
		//tvTerms.setText(terms);
		
		
		urlImg = "http://kpfun.com";
		
		/*try {
			BGImageLoader task = new BGImageLoader(ivDealImg);
			task.execute(urlImg+deal_img);
		}
		catch (Exception e) {
		    // handle it
			String error = e.getMessage();
			String test = "nothing";
		}*/
		
		if(ivDealImg!=null){
			//new ImageDownloaderTask(mIvLogo).execute(urlImg+logo);
			int loader = R.drawable.loader;
	        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
	        imgLoader.DisplayImage(urlImg+deal_img, loader, ivDealImg, true);
	        
        }
		
		if(ActivityLogin.has_restaurant=="no" && Double.parseDouble(distance) < 0.1){
			showAlertDialogClaimRestaurant(getParent(), "Additional Rewards!", "Would you like to subscribe to this restaurants loyalty reward program for additional deals and savings?", true);
		}
		
		if(ivUserImg!=null){
			//new ImageDownloaderTask(mIvLogo).execute(urlImg+logo);
			int loader = R.drawable.loader;
	        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
	        imgLoader.DisplayImage(urlImg+user_img, loader, ivUserImg, false);
        }
//		tvTerms.setOnClickListener(new OnClickListener() {
//			public void onClick(View view){
//				showAlertTerms(getParent(), "Reward Terms",terms, true);	
//			}
//		});
		
		mbtnGiftMap = (ImageView)findViewById(R.id.iv_map_gift);
		mbtnGiftMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mapintent = new Intent(ActivityYourGift.this, RestaurantMap.class);
				
				mapintent.putExtra("zipCode", zip_code);
				mapintent.putExtra("CityInfo", city);
				mapintent.putExtra("AddressInfo", address);
				Log.d("zipcode and city and address in gift", zip_code + city + address);
				
				startActivity(mapintent);
			}
		});
//		
		ivUserImg.setOnClickListener(new OnClickListener() {
			public void onClick(View view){
				setContentView(R.layout.image_view);
				ivImageView = (ImageView)findViewById(R.id.image_view_tab_icon);
				/*tvClose = (TextView)findViewById(R.id.close);
				tvClose.setOnClickListener(new OnClickListener() {
					public void onClick(View view){
						setContentView(R.layout.your_gift_layout);
						
					}
				});*/
				if(ivImageView!=null){
					//new ImageDownloaderTask(mIvLogo).execute(urlImg+logo);
					int loader = R.drawable.loader;
			        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
			        imgLoader.DisplayImage(urlImg+user_img, loader, ivImageView, true);
		        }
			}
				
			
		});
		
	}
	
	/* dialog check member */
	@SuppressWarnings("deprecation")
	public void showAlertDialogClaimRestaurant(Context context, String title,
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
				ClaimRestaurant();
				
				
				
			}

		});
		// Showing Alert Message
		alertDialog.show();
	}
	
	@SuppressWarnings("unused")
	private void ClaimRestaurant() {
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//NO RESPONSE
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						//Log.i("Error.Response", error.getMessage());
						
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "claimRestaurant");
				params.put("user_id", ActivityLogin.id);
				params.put("user_id_restaurant", user_id_restaurant);
				return params;
			}
		};
		queue.add(postRequest);
	}
	
	/* set font for view */
	@SuppressWarnings("unused")
	private void setFont() {
		// Font path
		String fontPathTitle = "fonts/MYRIADPRO-REGULAR.OTF";		
		// Loading Font Face
		Typeface tfTitle = Typeface.createFromAsset(getAssets(), fontPathTitle);		
		// Applying font
		tv_restaurantRewardsTitle.setTypeface(tfTitle);	
	}
	/*alert to exit*/
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
//			showAlertDialogExit(ActivityYourGift.this, "KpFun alert!",
//					"Do you want to exit KpFun ?", false);
//			return false;
			ActivityYourGift.this.finish();
		}

		return super.onKeyDown(keyCode, event);
	}
	/* dialog check member */
	@SuppressWarnings("deprecation")
	public void showAlertTerms(Context context, String title,
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
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}

		});

		
		// Showing Alert Message
		alertDialog.show();
	}
	
}
