package com.kpfunusa;

import static com.kpfunusa.CommonUtilities.SENDER_ID;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;
import com.kpfunusa.R;
import com.kpfunusa.dialog.AlertDialogManager;
import com.kpfunusa.gps.GPSTracker;

public class ActivityRegister extends Activity implements OnClickListener {
	private EditText edt_username, edt_password, edt_email, edt_phone,
			edt_firstname, edt_lastname, edt_code;
	private ProgressDialog pdialog;
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	public String gcm_id;
	GPSTracker gpstracker;
	static double latitude, longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registration_layout);
		cd = new ConnectionDetector(getApplicationContext());
		Button register = (Button) findViewById(R.id.btn_Register);
		TextView backLogin = (TextView) findViewById(R.id.tv_Back_Login);

		edt_firstname = (EditText) findViewById(R.id.edt_firstNameRegister);
		edt_lastname = (EditText) findViewById(R.id.edt_lastNameRegister);
		edt_username = (EditText) findViewById(R.id.edt_userNameRegister);
		edt_password = (EditText) findViewById(R.id.edt_PasswordRegister);
		edt_phone = (EditText) findViewById(R.id.edt_phoneRegister);
		edt_email = (EditText) findViewById(R.id.edt_emailRegister);
		edt_code = (EditText) findViewById(R.id.edt_codeRegister);

		
		gpstracker = new GPSTracker(getApplicationContext());
		
		/* set onclick */
		register.setOnClickListener(this);
		backLogin.setOnClickListener(this);
		
		//registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		final String regId = GCMRegistrar.getRegistrationId(this);
		
		// Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM           
            GCMRegistrar.register(this, SENDER_ID);
            gcm_id=GCMRegistrar.getRegistrationId(this);
        } else {
        	gcm_id=regId;
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.              
                Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            }
        }
        

	}

	/* Login */
	private void Resgister() {
		RequestQueue queue = Volley.newRequestQueue(this);
		final String url = ActivityLogin.webServiceURL;
		final String token = ActivityLogin.token;
		final String mode = "register";

		final String first_name = edt_firstname.getText().toString();
		final String last_name = edt_lastname.getText().toString();
		final String username = edt_username.getText().toString();
		final String password = edt_password.getText().toString();
		final String phone = edt_phone.getText().toString();
		final String email = edt_email.getText().toString();
		final String gcm_regid = gcm_id;
		final String code = edt_code.getText().toString();
		
		latitude = gpstracker.getLatitude();
		longitude = gpstracker.getLongitude();

		final String str_latitude = String.valueOf(latitude);
		final String str_longitude = String.valueOf(longitude);

		/* dialog */
		pdialog = new ProgressDialog(this);
		pdialog.setCancelable(true);
		pdialog.setMessage("Checking ....");
		pdialog.show();

		/*
		 * try { Thread.sleep(12000); } catch (Exception e) { }
		 */
		StringRequest myReq = new StringRequest(Method.POST, url,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("mode", mode);
				params.put("code", code);
				params.put("first_name", first_name);
				params.put("last_name", last_name);
				params.put("username", username);
				params.put("password", password);
				params.put("email", email);
				params.put("phone", phone);
				params.put("gcm_regid", gcm_regid);
				params.put("latitude", str_latitude);
				params.put("longitude", str_longitude);
				return params;
			};
		};
		myReq.setRetryPolicy(new DefaultRetryPolicy(5000, 
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		queue.add(myReq);
	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// setTvResultText(response);
				try {
					JSONObject json = new JSONObject(response);
					String strError = json.getString("error");

					if (strError.equalsIgnoreCase("false")) {
						//alert.showAlertDialog(ActivityRegister.this, "KpFun alert!", "Registration Successful! ", true);
						/* SAVE LOGIN INFO */
						SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
						SharedPreferences.Editor Ed=sp.edit();
						Ed.putString("un",edt_username.getText().toString());              
						Ed.putString("pw",edt_password.getText().toString());   
						Ed.commit();
						
						// Start your app main activity
						Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
						startActivity(i);

						// close this activity
						finish();
						
						
						
						
						pdialog.dismiss();
						return;
					} else {
						alert.showAlertDialog(ActivityRegister.this,
								"KpFun alert!", strError, false);
						pdialog.dismiss();
						return;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				// mTvResult.setText(error.getMessage());
			}
		};
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_Register: {
			if (edt_firstname.getText().toString().equalsIgnoreCase("")) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert",
						"First name no empty!", false);
				edt_firstname.requestFocus();
				return;
			}
			if (edt_lastname.getText().toString().equalsIgnoreCase("")) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert",
						"Last name no empty!", false);
				edt_lastname.requestFocus();
				return;
			}
			if (edt_username.getText().toString().equalsIgnoreCase("")) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert",
						"Username no empty!", false);
				edt_username.requestFocus();
				return;
			}
			if (edt_password.getText().toString().equalsIgnoreCase("")) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert",
						"Password no empty!", false);
				edt_password.requestFocus();
				return;
			}
			if (edt_phone.getText().toString().equalsIgnoreCase("")) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert",
						"Phone no empty!", false);
				edt_phone.requestFocus();
				return;
			}
			if (edt_email.getText().toString().equalsIgnoreCase("")) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert",
						"Email no empty!", false);
				edt_email.requestFocus();
				return;
			}
			if (!cd.isConnectingToInternet()) {
				alert.showAlertDialog(ActivityRegister.this, "KpFun alert!",
						"No network, Please check your data connection!",
						false);
				return;
			}
			Resgister();

		}
			break;
		case R.id.tv_Back_Login: {
			Intent intentLogin = new Intent(ActivityRegister.this,
					ActivityLogin.class);
			startActivity(intentLogin);
			finish();
		}
			break;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
