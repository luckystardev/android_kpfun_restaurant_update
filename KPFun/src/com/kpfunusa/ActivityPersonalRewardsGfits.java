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
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.adapter.GiftsAdapter;
import com.kpfunusa.adapter.RewardsAdapter;
import com.kpfunusa.bean.Gifts;
import com.kpfunusa.bean.Rewards;
import com.kpfunusa.dialog.AlertDialogManager;

public class ActivityPersonalRewardsGfits extends Activity implements
		OnClickListener {
	static String tab;
	Button btn_gfits, btn_rewards;
	ImageView iv_downLeft, iv_downRight;
	LinearLayout layout_gfits, layout_rewards;
	TextView tv_persionalRewardGiftTitle;
	ListView listReward, listGift;
	RewardsAdapter rewardsadapter;
	GiftsAdapter giftsadapter;
	ArrayList<Gifts> arraylistGifts = new ArrayList<Gifts>();
	ArrayList<Rewards> arrayRewards = new ArrayList<Rewards>();
	ProgressDialog pdialog;
	AlertDialogManager alert = new AlertDialogManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_reward_gifts);

		/* Init view */
		btn_gfits = (Button) findViewById(R.id.btn_gifts);
		btn_rewards = (Button) findViewById(R.id.btn_rewards);

		iv_downLeft = (ImageView) findViewById(R.id.iv_dropLeft);
		iv_downRight = (ImageView) findViewById(R.id.iv_dropRight);
		
		layout_gfits = (LinearLayout) findViewById(R.id.layout_gfits);
		layout_rewards = (LinearLayout) findViewById(R.id.layout_rewards);
		tv_persionalRewardGiftTitle = (TextView) findViewById(R.id.tv_persionalRewardGiftTitle);

		/* set onclick Button */
		btn_gfits.setOnClickListener(this);
		btn_rewards.setOnClickListener(this);
		// iv_backScreenPersonalRewardGift.setOnClickListener(this);
		
		/* set Font for views */
		// setFont();

		// data rewards
		//Reward();
		// data gifts
		if(tab=="rewards"){
			loadRewards();
		}else{
			Gifts();
		}
		// Pass results to ListViewAdapter Class
		giftsadapter = new GiftsAdapter(this, R.layout.persional_gifts_items, arraylistGifts);
		rewardsadapter = new RewardsAdapter(this, R.layout.persional_rewards_items, arrayRewards);
		listGift = (ListView) findViewById(R.id.lv_gfits);
		listReward = (ListView) findViewById(R.id.lv_rewards);
		// Binds the Adapter to the ListView
		//listReward.setAdapter(rewardsadapter);
		listGift.setAdapter(giftsadapter);
		listReward.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long id) {
				Rewards rewards = arrayRewards.get(position);
				Intent intent = new Intent(getParent(),
						ActivityRestaurantRewards.class);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				intent.putExtra("company_name_rewards", rewards.getCompany_name());
				intent.putExtra("img_rewards", rewards.getImg());
				intent.putExtra("address_rewards", rewards.getAddress());
				intent.putExtra("city_rewards", rewards.getCity());
				intent.putExtra("state_rewards", rewards.getState());
				intent.putExtra("voucher", rewards.getVoucher());
				intent.putExtra("zip_code_rewards", rewards.getZip_code());
				intent.putExtra("percent_rewards", rewards.getPercent());
				
				intent.putExtra("name", rewards.getName());
				intent.putExtra("terms", rewards.getTerms());

				parentActivity.startChildActivity("ActivityRestaurantRewards", intent);
			}
		});
		listGift.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
				Gifts gifts = arraylistGifts.get(position);
				Intent intent = new Intent(getParent(), ActivityYourGift.class);
				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				intent.putExtra("company_name", gifts.getCompany_name());
				intent.putExtra("deal_img", gifts.getDealImg());
				intent.putExtra("address", gifts.getAddress());
				intent.putExtra("city", gifts.getCity());
				intent.putExtra("state", gifts.getState());
				intent.putExtra("zip_code", gifts.getZipCode());
				
				intent.putExtra("name", gifts.getName());
				intent.putExtra("voucher", gifts.getVoucher());
				intent.putExtra("terms", gifts.getTerms());
				intent.putExtra("title", gifts.getTitle());
				
				intent.putExtra("user_img", gifts.getUserImg());
				intent.putExtra("caption", gifts.getCaption());
				
				intent.putExtra("distance", gifts.getDistance());
				intent.putExtra("user_id", gifts.getUserId());

				parentActivity.startChildActivity("ActivityYourGift", intent);
			}
		});

	}

	private void Reward() {
		tab="rewards";
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = ActivityLogin.webServiceURL;
		pdialog = ProgressDialog.show(getParent(), "", "Loading...");
		pdialog.setCancelable(true);
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						try {
							JSONObject json = new JSONObject(response);
							JSONArray items = json.getJSONArray("rewards");
							arrayRewards = new ArrayList<Rewards>();
							final String error = json.getString("error");
							if (error.toString().equalsIgnoreCase("false")) {
								for (int i = 0; i < items.length(); i++) {
									JSONObject item = items.getJSONObject(i);

									Rewards rew = new Rewards();
									rew.setCompany_name(item
											.optString("company_name"));
									rew.setExpiration("Expires "
											+ item.optString("expiration"));
									rew.setImg(item.optString("img"));
									rew.setVoucher(item.optString("voucher"));
									rew.setPercent(item.optString("percent"));
									rew.setAddress(item
											.optString("street_address"));
									rew.setCity(item.optString("city"));
									rew.setState(item.optString("state"));
									rew.setZip_code(item.optString("zip_code"));
									rew.setName(item.optString("name"));
									rew.setTerms(item.optString("terms"));

									arrayRewards.add(rew);
								}

								rewardsadapter = new RewardsAdapter(
										ActivityPersonalRewardsGfits.this,
										R.layout.persional_rewards_items,
										arrayRewards);
								// Reward();
								listReward.setAdapter(rewardsadapter);
							} else {
								alert.showAlertDialog(getParent(),
										"KpFun alert!", error, false);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						rewardsadapter.notifyDataSetChanged();
						pdialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						//Log.i("Error.Response", error.getMessage());
						pdialog.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "getRewards");
				params.put("user_id", ActivityLogin.id);
				params.put("latitude", String.valueOf(ActivityLogin.latitude));
				params.put("longitude", String.valueOf(ActivityLogin.longitude));
				return params;
			}
		};
		queue.add(postRequest);
	}

	private void Gifts() {
		/* dialog */
		tab="gifts";
		String test = getParent().toString();
		pdialog = ProgressDialog.show(getParent(), "", "Loading...");
		pdialog.setCancelable(true);
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						try {
							JSONObject json = new JSONObject(response);
							JSONArray items = json.getJSONArray("rewards");
							ActivityLogin.has_restaurant = json.getString("has_restaurant");
							arraylistGifts = new ArrayList<Gifts>();
							final String error = json.getString("error");
							if (error.toString().equalsIgnoreCase("false")) {
								for (int i = 0; i < items.length(); i++) {
									JSONObject item = items.getJSONObject(i);

									Gifts gif = new Gifts();
									gif.setCompany_name(item.optString("company_name"));
									gif.setExpiration("Expires " + item.optString("expiration"));
									gif.setDeal_img(item.optString("deal_img"));
									gif.setTitle(item.optString("title"));
									gif.setName(item.optString("name"));
									gif.setUserImg(item.optString("user_img"));
									gif.setCaption(item.optString("caption"));
									gif.setTerms(item.optString("terms"));
									gif.setAddress(item.optString("street_address") + " " + item.optString("street_address2"));
									gif.setCity(item.optString("city"));
									gif.setState(item.optString("state"));
									gif.setZipCode(item.optString("zip_code"));
									gif.setVoucher(item.optString("voucher"));
									gif.setDistance(item.optString("distance"));
									gif.setUserId(item.optString("user_id"));
									arraylistGifts.add(gif);

								}

								giftsadapter = new GiftsAdapter(
										ActivityPersonalRewardsGfits.this,
										R.layout.persional_gifts_items,
										arraylistGifts);
								// Gifts();
								listGift.setAdapter(giftsadapter);
							} else {
								alert.showAlertDialog(getParent(),
										"KpFun alert!", error, false);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						giftsadapter.notifyDataSetChanged();
						pdialog.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						//Log.i("Error.Response", error.getMessage());
						pdialog.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "getGifts");
				params.put("user_id", ActivityLogin.id);
				params.put("latitude", String.valueOf(ActivityLogin.latitude));
				params.put("longitude", String.valueOf(ActivityLogin.longitude));
				return params;
			}
		};
		queue.add(postRequest);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gifts: {
			
			/* visible or invisible layout Rewards and gifts */
			layout_gfits.setVisibility(View.VISIBLE);
			layout_rewards.setVisibility(View.INVISIBLE);

			/* visible or invisible Image down Rewards and gifts */
			iv_downLeft.setVisibility(View.VISIBLE);
			iv_downRight.setVisibility(View.INVISIBLE);

			/* Change background Button tab Rewards and gifts */
			btn_gfits
					.setBackgroundResource(R.color.activity_bar_button_selected);
			btn_rewards
					.setBackgroundResource(R.color.activity_bar_button_unselected);

			/* Change TextColor Button tab Rewards and gifts */
			btn_gfits.setTextColor(Color.parseColor("#FFFFFF"));
			btn_rewards.setTextColor(Color.parseColor("#15bcdd"));

			// load when click
			Gifts();

		}
			break;

		case R.id.btn_rewards: {
			
			loadRewards();

		}
			break;
		/*
		 * case R.id.iv_backScreenPersonalRewardGift:{
		 * showAlertDialogExit(ActivityPersonalRewardsGfits.this,
		 * "KpFun alert!", "Do you want to exit KpFun ?", false); }break;
		 */
		}
	}
	private void loadRewards(){
		tab="rewards";
		/* visible or invisible layout Rewards and gifts */
		layout_rewards.setVisibility(View.VISIBLE);
		layout_gfits.setVisibility(View.INVISIBLE);

		/* visible or invisible Image down Rewards and gifts */
		iv_downLeft.setVisibility(View.INVISIBLE);
		iv_downRight.setVisibility(View.VISIBLE);

		/* Change background Button tab Rewards and gifts */
		btn_rewards
				.setBackgroundResource(R.color.activity_bar_button_selected);
		btn_gfits
				.setBackgroundResource(R.color.activity_bar_button_unselected);

		/* Change TextColor Button tab Rewards and gifts */
		btn_rewards.setTextColor(Color.parseColor("#FFFFFF"));
		btn_gfits.setTextColor(Color.parseColor("#15bcdd"));

		// load when click

		Reward();
		
		
	}
	/* set font for view */
	@SuppressWarnings("unused")
	private void setFont() {
		// Font path
		String fontPathTitle = "fonts/MYRIADPRO-REGULAR.OTF";
		String fontPathButton = "fonts/HelveticaNeueBold.ttf";
		// Loading Font Face
		Typeface tfTitle = Typeface.createFromAsset(getAssets(), fontPathTitle);
		Typeface tfButton = Typeface.createFromAsset(getAssets(),
				fontPathButton);
		// Applying font
		tv_persionalRewardGiftTitle.setTypeface(tfTitle);
		btn_rewards.setTypeface(tfButton);
		btn_gfits.setTypeface(tfButton);
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
			@SuppressWarnings("deprecation")
			TabActivity tabs = (TabActivity) getParent().getParent();
		    //tabs.getTabHost().setCurrentTab(4);
		    tabs.getTabHost().setCurrentTabByTag("tab1");
		    //return false;
		}
	    
		//return super.onKeyDown(keyCode, event);
		
		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		return true;
	}
}
