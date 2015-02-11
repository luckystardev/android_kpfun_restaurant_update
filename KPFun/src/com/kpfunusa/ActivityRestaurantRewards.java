package com.kpfunusa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpfunusa.R;
import com.kpfunusa.loadimage.ImageLoader;

public class ActivityRestaurantRewards extends Activity {
	String terms;
	TextView tv_restaurantRewardsTitle;
	TextView mTvRestaurantName,mTvAddress,mTvCity,mTvPercent,tv_restaurantRewardMemberId,tv_restaurantRewardTrackingId, mTvTerms;
	ImageView mIvLogo;
	private TextView mTvTerms_descrip;
	private ImageView mbtn_reward_Map;
	private String zip_code;
	private String city;
	private String address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_reward_layout);
		tv_restaurantRewardsTitle =(TextView)findViewById(R.id.tv_restaurantRewardsTitle);
		/*set font for view*/
		//setFont();
		Intent in = getIntent();
		String restaurantName = in.getStringExtra("company_name_rewards");
		String logo = in.getStringExtra("img_rewards");
		address = in.getStringExtra("address_rewards");
		city = in.getStringExtra("city_rewards");
		String voucher = in.getStringExtra("voucher");
		String state = in.getStringExtra("state_rewards");
		zip_code = in.getStringExtra("zip_code_rewards");
		String percent = in.getStringExtra("percent_rewards");
		String name = in.getStringExtra("name");
		String descrip = in.getStringExtra("description");
		terms = in.getStringExtra("terms");
		
		mTvRestaurantName = (TextView)findViewById(R.id.tv_restaurantRewardName);
		mTvAddress = (TextView)findViewById(R.id.tv_restaurantRewardAdress);
		//mTvCity = (TextView)findViewById(R.id.tv_restaurantRewardCity);
		mTvPercent = (TextView)findViewById(R.id.tv_reward_description);
		mIvLogo = (ImageView)findViewById(R.id.img_restaurantRewardLogo);
		tv_restaurantRewardMemberId =(TextView)findViewById(R.id.tv_restaurantRewardMemberId);
		tv_restaurantRewardTrackingId =(TextView)findViewById(R.id.tv_restaurantTrackingId);
//		mTvTerms =(TextView)findViewById(R.id.terms);
		mTvTerms_descrip = (TextView)findViewById(R.id.terms_descrip);
		
		mTvRestaurantName.setText(restaurantName);
		mTvAddress.setText(address + "\n" + city+", "+state+" "+zip_code);
		//mTvCity.setText(city+", "+state+" "+zip_code);
		mTvPercent.setText(descrip);
		mTvTerms_descrip.setText(terms);
		tv_restaurantRewardMemberId.setText("Valid for "+name+ " ("+ ActivityLogin.dealer_id +")");
		tv_restaurantRewardTrackingId.setText("Tracking ID: "+voucher);
		String urlImg = "http://kpfun.com";
		
		if(mIvLogo!=null){
			//new ImageDownloaderTask(mIvLogo).execute(urlImg+logo);
			int loader = R.drawable.loader;
	        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
	        imgLoader.DisplayImage(urlImg+logo, loader, mIvLogo, true);
        }
		
		mbtn_reward_Map = (ImageView)findViewById(R.id.iv_map_reward_layout);
		mbtn_reward_Map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mapRewardintent = new Intent(ActivityRestaurantRewards.this, RestaurantMap.class);
				
				mapRewardintent.putExtra("zipCode", zip_code);
				mapRewardintent.putExtra("CityInfo", city);
				mapRewardintent.putExtra("AddressInfo", address);
				Log.d("zipcode and city and address in reward", zip_code + city + address);
				
				startActivity(mapRewardintent);
			}
		});
//		mTvTerms.setOnClickListener(new OnClickListener() {
//			public void onClick(View view){
//				showAlertTerms(getParent(), "Reward Terms",terms, true);
//				
//			}
		//void showAlertDialogCheckMember(getParent(), "KpFun alert!","Do you want to purchase membership?", true);
				
//			
//		});
//		
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
			finish();
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		
		return true;
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
