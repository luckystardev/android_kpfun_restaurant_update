package com.kpfunusa;

import com.kpfunusa.R;
import com.kpfunusa.dialog.AlertDialogManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityMore extends Activity implements OnClickListener {

	TextView tv_moreTitle;
	Button btn_exit_keeper, btn_purchase_membership, btn_visit_website,
			btn_about_us, btn_terms_of_use, btn_privacy_policy, btn_fb_login, btn_logout;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_layout);
		cd = new ConnectionDetector(getApplicationContext());

		tv_moreTitle = (TextView) findViewById(R.id.tv_moreTitle);
		btn_purchase_membership = (Button) findViewById(R.id.btn_purchase_membership);
		btn_visit_website = (Button) findViewById(R.id.btn_visit_website);
		btn_about_us = (Button) findViewById(R.id.btn_about_us);
		btn_terms_of_use = (Button) findViewById(R.id.btn_terms_of_use);
		btn_privacy_policy = (Button) findViewById(R.id.btn_privacy_policy);
		btn_logout = (Button) findViewById(R.id.btn_logout);
		//btn_fb_login = (Button) findViewById(R.id.btn_fb_login);
		// btn_exit_keeper = (Button) findViewById(R.id.btn_exit_keeper);

		/* set font for views */
		// setFont();
		/* set onclik views */
		btn_purchase_membership.setOnClickListener(this);
		btn_visit_website.setOnClickListener(this);
		btn_about_us.setOnClickListener(this);
		btn_terms_of_use.setOnClickListener(this);
		btn_privacy_policy.setOnClickListener(this);
		btn_logout.setOnClickListener(this);
		//btn_fb_login.setOnClickListener(this);
		// btn_exit_keeper.setOnClickListener(this);

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
		tv_moreTitle.setTypeface(tfTitle);
		btn_purchase_membership.setTypeface(tfButton);
		btn_visit_website.setTypeface(tfButton);
		btn_about_us.setTypeface(tfButton);
		btn_terms_of_use.setTypeface(tfButton);
		btn_privacy_policy.setTypeface(tfButton);
		btn_logout.setTypeface(tfButton);
		//btn_fb_login.setTypeface(tfButton);
		// btn_exit_keeper.setTypeface(tfButton);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_purchase_membership: {
			if (!cd.isConnectingToInternet()) {
				alert.showAlertDialog(ActivityMore.this, "KpFun alert!",
						"No network, Please check your data connection!", false);
				return;
			}

			String url = "http://kpfun.com/membership.php";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		}
			break;
		case R.id.btn_visit_website: {
			if (!cd.isConnectingToInternet()) {
				alert.showAlertDialog(ActivityMore.this, "KpFun alert!",
						"No network, Please check your data connection!", false);
				return;
			}

			String url = "http://kpfun.com";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		}
			break;
		case R.id.btn_about_us: {
			if (!cd.isConnectingToInternet()) {
				alert.showAlertDialog(ActivityMore.this, "KpFun alert!",
						"No network, Please check your data connection!", false);
				return;
			}
			String url = "http://kpfun.com/about.php";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);

		}
			break;
		case R.id.btn_terms_of_use: {
			if (!cd.isConnectingToInternet()) {
				alert.showAlertDialog(ActivityMore.this, "KpFun alert!",
						"No network, Please check your data connection!", false);
				return;
			}
			String url = "http://kpfun.com/terms_of_use.html";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);

		}
			break;
		case R.id.btn_privacy_policy: {
			if (!cd.isConnectingToInternet()) {
				alert.showAlertDialog(ActivityMore.this, "KpFun alert!",
						"No network, Please check your data connection!", false);
				return;
			}
			String url = "http://kpfun.com/privacy_policy.html";
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);

		}
			break;
		case R.id.btn_logout: {
			SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
			SharedPreferences.Editor Ed=sp.edit();
			Ed.putString("un","");              
			Ed.putString("pw","");   
			Ed.commit();
			
			Intent i = new Intent(ActivityMore.this, ActivityLogin.class);
			startActivity(i);

			// close this activity
			finish();

		}
			break;
		/*case R.id.btn_fb_login: {
			setContentView(R.layout.social_connect_layout);

		}
			break;*/
		}

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
