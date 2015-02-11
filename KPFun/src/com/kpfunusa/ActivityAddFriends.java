package com.kpfunusa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.adapter.AddFriendsAdapter;
import com.kpfunusa.adapter.SearchFriendAdapter;
import com.kpfunusa.bean.AddFriends;
import com.kpfunusa.bean.SearchFriends;

public class ActivityAddFriends extends Activity implements OnClickListener,
		AddFriendsAdapter.OnWidgetItemClicked {

	ImageButton btn_addFriends, btn_searchFriends;
	ImageView iv_downAddFriendsLeft, iv_downAddFriendsRight;
	LinearLayout layout_addFriends, layout_searchFriends;
	ListView listviewSearch, listviewAdd;
	com.kpfunusa.views.BSSEditText etSearch;
	SearchFriendAdapter searchAdapter;
	AddFriendsAdapter addAdapter;
	ArrayList<SearchFriends> arraylistSearch = new ArrayList<SearchFriends>();
	ArrayList<AddFriends> arraylistAdd = new ArrayList<AddFriends>();
	ProgressDialog pdialog = null;
	String contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friends_layout);

		/* Init view */
		btn_addFriends = (ImageButton) findViewById(R.id.btn_addFriends);
		btn_searchFriends = (ImageButton) findViewById(R.id.btn_searchFriends);

		iv_downAddFriendsLeft = (ImageView) findViewById(R.id.iv_dropAddFriendsLeft);
		iv_downAddFriendsRight = (ImageView) findViewById(R.id.iv_dropAddFriendsRight);
		
		layout_addFriends = (LinearLayout) findViewById(R.id.layout_addFriends);
		layout_searchFriends = (LinearLayout) findViewById(R.id.layout_searchFriends);
		etSearch = (com.kpfunusa.views.BSSEditText) findViewById(R.id.etSearch);

		/* set onclick Button */
		btn_addFriends.setOnClickListener(this);
		btn_searchFriends.setOnClickListener(this);
		

		// set data
		listviewSearch = (ListView) findViewById(R.id.lv_searchFriends);
		searchAdapter = new SearchFriendAdapter(this,
				R.layout.friend_search_item, arraylistSearch);
		getContacts();
		listviewSearch.setAdapter(searchAdapter);

		// data add friends
		listviewAdd = (ListView) findViewById(R.id.lv_addFriends);
		if (contacts != "") {
			GetFriends();
		} else
			Toast.makeText(getApplicationContext(), "NO contact",
					Toast.LENGTH_LONG).show();
		addAdapter = new AddFriendsAdapter(this, R.layout.add_friend_item,
				arraylistAdd);
		addAdapter.setListener(this);
		listviewAdd.setAdapter(addAdapter);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!s.equals("")) {
					searching(etSearch.getText().toString());
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_addFriends: {
			/* visible or invisible layout addFriends and add search friends */
			layout_addFriends.setVisibility(View.VISIBLE);
			layout_searchFriends.setVisibility(View.INVISIBLE);

			/* visible or invisible Image down addFriends and add search friends */
			iv_downAddFriendsLeft.setVisibility(View.VISIBLE);
			iv_downAddFriendsRight.setVisibility(View.INVISIBLE);

			/* Change background Button tab addFriends and add search friends */
			btn_addFriends.setBackgroundColor(Color.parseColor("#15bcdd"));
			btn_searchFriends.setBackgroundColor(Color.parseColor("#ffffff"));

			/* Change icon button tab addFriends and add search friends */

			btn_addFriends
					.setImageResource(R.drawable.ic_action_user_add_while);
			btn_searchFriends
					.setImageResource(R.drawable.ic_action_search_gray);
		}
			break;

		case R.id.btn_searchFriends: {
			/* visible or invisible layout Rewards and gfits */
			layout_addFriends.setVisibility(View.INVISIBLE);
			layout_searchFriends.setVisibility(View.VISIBLE);

			/* visible or invisible Image down Rewards and gfits */
			iv_downAddFriendsLeft.setVisibility(View.INVISIBLE);
			iv_downAddFriendsRight.setVisibility(View.VISIBLE);

			/* Change background Button tab Rewards and gifts */
			btn_searchFriends.setBackgroundColor(Color.parseColor("#15bcdd"));
			btn_addFriends.setBackgroundColor(Color.parseColor("#ffffff"));

			/* Change TextColor Button tab Rewards and gifts */
			btn_searchFriends.setImageResource(R.drawable.ic_action_search_while);
			btn_addFriends.setImageResource(R.drawable.ic_action_user_add_gray);
		}
			break;
		
		}

	}

	public void getContacts() {

		String phoneNumber = null;
		/* String email = null; */

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		/*
		 * Uri EmailCONTENT_URI =
		 * ContactsContract.CommonDataKinds.Email.CONTENT_URI; String
		 * EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		 * String DATA = ContactsContract.CommonDataKinds.Email.DATA;
		 */

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = getContentResolver();

		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				null);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {
					output.append("," + name);
					SearchFriends search = new SearchFriends();
					search.setName(name);

					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						output.append("," + phoneNumber);
						search.setPhone(phoneNumber);

					}

					phoneCursor.close();

					arraylistSearch.add(search);
				}
				output.append("|");
				searchAdapter.notifyDataSetChanged();

			}
			contacts = output.substring(0, output.length() - 1);
		}
	}

	private void searching(String text) {
		text = text.toLowerCase(Locale.getDefault());
		ArrayList<SearchFriends> arraySearch = new ArrayList<SearchFriends>();
		for (SearchFriends search : arraylistSearch) {
			if (search.getName().toLowerCase(Locale.getDefault())
					.contains(text))
				arraySearch.add(search);
		}
		searchAdapter = new SearchFriendAdapter(this,
				R.layout.friend_search_item, arraySearch);
		searchAdapter.notifyDataSetChanged();
		listviewSearch.setAdapter(searchAdapter);
	}

	public void GetFriends() {

		pdialog = ProgressDialog.show(getParent(), "", "Loading...");
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						try {
							JSONObject json = new JSONObject(response);
							JSONArray items = json.getJSONArray("friends");
							int check;

							arraylistAdd = new ArrayList<AddFriends>();

							for (int i = 0; i < items.length(); i++) {
								JSONObject item = items.getJSONObject(i);
								AddFriends friend = new AddFriends();
								friend.setId(item.optString("user_id"));
								friend.setAccount(item.optString("first_name") + " " + item.optString("last_name"));
								if (Integer.parseInt(item.optString("friends")) == 0)
									check = R.drawable.friend_false;
								else
									check = R.drawable.friend_true;
								friend.setState(check);
								arraylistAdd.add(friend);

							}
							// load data to listview
							addAdapter = new AddFriendsAdapter(
									ActivityAddFriends.this,
									R.layout.add_friend_item, arraylistAdd);
							addAdapter.setListener(ActivityAddFriends.this);
							listviewAdd.setAdapter(addAdapter);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						addAdapter.notifyDataSetChanged();
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
				params.put("mode", "getFriends");
				params.put("user_id", ActivityLogin.id);
				params.put("contacts", contacts);
				return params;

			}
		};
		queue.add(postRequest);
	}

	public void AddFriends(final String friend_id) {
		pdialog = ProgressDialog.show(getParent(), "", "Procesing...");
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						try {
							JSONObject json = new JSONObject(response);
							String er = json.getString("error");
							if (er.equalsIgnoreCase("false")) {
								Toast.makeText(getApplicationContext(),
										"success", Toast.LENGTH_LONG).show();
								pdialog.dismiss();
							} else {
								Toast.makeText(getApplicationContext(),
										"error", Toast.LENGTH_LONG).show();
								pdialog.dismiss();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
				params.put("mode", "getFriends");
				params.put("user_id", "15");
				params.put("friend_id", friend_id);
				return params;

			}
		};
		queue.add(postRequest);
	}

	@Override
	public void onCheckClicked(View v, int position) {
		// TODO Auto-generated method stub
		// GetFriends();
		if (contacts != "") {
			//GetFriends();
		} else
			Toast.makeText(getApplicationContext(), "NO contact",
					Toast.LENGTH_LONG).show();

	}
	
	
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
			TabActivity tabs = (TabActivity) getParent();
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
