package com.kpfunusa;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.kpfunusa.R;

public class ActivitySocialConnect extends Activity {
	// Your Facebook APP ID
//	private static String APP_ID = "512263202185430"; // Replace with your App
	private static String APP_ID = "641360709231346";
	// Instance of Facebook Class
	private Facebook facebook = new Facebook(APP_ID);
	private AsyncFacebookRunner mAsyncRunner;
	String FILENAME = "AndroidSSO_data";
	private SharedPreferences mPrefs;
	LinearLayout layout_socialBeforConnect, layout_socialAfterConnect;
	ImageView btn_backScreen;

	// Buttons
	Button btnFacebookLogout;
	Button btnFacebookShareToWall;

	ImageButton btnFacebookLogin;
	ImageView pic;
	TextView tv_afterConnectToFacebook;
	TextView welcome,tv_socialConnect;
	Bitmap picProfile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_connect_layout);

		btnFacebookLogin = (ImageButton) findViewById(R.id.btn_loginFacebook);
		btnFacebookLogout = (Button) findViewById(R.id.btn_logoutFacebook);
		btnFacebookShareToWall = (Button) findViewById(R.id.btn_shareFacebook);
		pic = (ImageView) findViewById(R.id.pic_facebook);
		welcome = (TextView) findViewById(R.id.tv_facebookNameOfUser);

		layout_socialBeforConnect = (LinearLayout) findViewById(R.id.layout_socialBeforeConnect);
		layout_socialAfterConnect = (LinearLayout) findViewById(R.id.layout_socialAfterConnect);
		btn_backScreen = (ImageView) findViewById(R.id.iv_backScreenSocialConnect);

		tv_socialConnect = (TextView) findViewById(R.id.tv_socialConnect);
		tv_afterConnectToFacebook =(TextView)findViewById(R.id.tv_afterConnectToFacebook);
		/* set font for views */
		//setFont();

		mAsyncRunner = new AsyncFacebookRunner(facebook);
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
		updateButtonImage();
		btnFacebookLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (facebook.isSessionValid()) {
					try {
						facebook.logout(getApplicationContext());
						Editor editor = mPrefs.edit();
						editor.clear();
						editor.commit();
						updateButtonImage();

						/* set visible or invisible layout */
						layout_socialBeforConnect.setVisibility(View.VISIBLE);
						layout_socialAfterConnect.setVisibility(View.INVISIBLE);

						welcome.setText("Wellcome!...");
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnFacebookLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("Image Button", "button Clicked");
				loginToFacebook();
			}
		});
		/* back screen */
		btn_backScreen.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				layout_socialBeforConnect.setVisibility(View.VISIBLE);
				layout_socialAfterConnect.setVisibility(View.INVISIBLE);
			}
		});

		/**
		 * Posting to Facebook Wall
		 * */
		btnFacebookShareToWall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				postToWall();
			}
		});
	}

	/**
	 * Function to login into facebook
	 * */
	public void loginToFacebook() {
		mPrefs = getPreferences(MODE_PRIVATE);
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);

		if (access_token != null) {
			facebook.setAccessToken(access_token);

			/*
			 * set visible or invisible layout social After Connect and social
			 * Befor Connect
			 */
			layout_socialAfterConnect.setVisibility(View.VISIBLE);
			layout_socialBeforConnect.setVisibility(View.INVISIBLE);
			//Log.d("FB Sessions", "" + facebook.isSessionValid());
		}

		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}

		if (!facebook.isSessionValid()) {
			facebook.authorize(this,
					new String[] { "email", "publish_stream" },
					new DialogListener() {

						@Override
						public void onCancel() {
							// Function to handle cancel event
						}

						@Override
						public void onComplete(Bundle values) {
							// Function to handle complete event
							// Edit Preferences and update facebook acess_token
							SharedPreferences.Editor editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();
							updateButtonImage();
							layout_socialBeforConnect
									.setVisibility(View.INVISIBLE);
							layout_socialAfterConnect
									.setVisibility(View.VISIBLE);
						}

						@Override
						public void onError(DialogError error) {
							// Function to handle error

						}

						@Override
						public void onFacebookError(FacebookError fberror) {
							// Function to handle Facebook errors

						}

					});
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		facebook.authorizeCallback(requestCode, resultCode, data);
	}

	public Bitmap getUserPic(String userID) {
		String imageURL;
		Bitmap bitmap = null;

		imageURL = "http://graph.facebook.com/" + userID
				+ "/picture?type=large";
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL)
					.getContent());
		} catch (Exception e) {
			//Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * Function to post to facebook wall
	 * */
	public void postToWall() {
		// post on user's wall.
		facebook.dialog(this, "feed", new DialogListener() {

			@Override
			public void onFacebookError(FacebookError e) {
			}

			@Override
			public void onError(DialogError e) {
			}

			@Override
			public void onComplete(Bundle values) {
			}

			@Override
			public void onCancel() {
			}
		});

	}

	/**
	 * Function to //Logout user from Facebook
	 * */
	public void logoutFromFacebook() {
		mAsyncRunner.logout(this, new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				//Log.d("Logout from Facebook", response);
				if (Boolean.parseBoolean(response) == true) {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// make Login button visible

						}

					});

				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}

	/**
	 * Get Profile information by making request to Facebook Graph API
	 * */
	public void getProfileUserInfo() {
		mAsyncRunner.request("me", new RequestListener() {
			@Override
			public void onComplete(String response, Object state) {
				//Log.d("Profile", response);
				String json = response;
				try {
					// Facebook Profile JSON data
					JSONObject profile = new JSONObject(json);

					// getting name of the user
					final String name = profile.getString("name");

					// getting email of the user
					final String id = profile.getString("id");
					picProfile = getUserPic(id);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// Toast.makeText(getApplicationContext(), "Name: "
							// + name + "\nEmail: " + id,
							// Toast.LENGTH_LONG).show();
							welcome.setText(name);
							pic.setImageBitmap(picProfile);
						}

					});

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onIOException(IOException e, Object state) {
			}

			@Override
			public void onFileNotFoundException(FileNotFoundException e,
					Object state) {
			}

			@Override
			public void onMalformedURLException(MalformedURLException e,
					Object state) {
			}

			@Override
			public void onFacebookError(FacebookError e, Object state) {
			}
		});
	}

	private void updateButtonImage() {
		if (facebook.isSessionValid()) {
			getProfileUserInfo();
		} else {
			pic.setImageResource(R.drawable.no_image);
		}
	}

	/* set font for view */
	@SuppressWarnings("unused")
	private void setFont() {
		// Font path
		String fontPathTitle = "fonts/MYRIADPRO-REGULAR.OTF";
		// Loading Font Face
		Typeface tfTitle = Typeface.createFromAsset(getAssets(), fontPathTitle);
		// Applying font
		tv_socialConnect.setTypeface(tfTitle);
		tv_afterConnectToFacebook.setTypeface(tfTitle);
		
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
			showAlertDialogExit(ActivitySocialConnect.this, "KpFun alert!",
					"Do you want to exit KpFun ?", false);
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
}
