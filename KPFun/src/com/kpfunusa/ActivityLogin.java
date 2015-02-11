package com.kpfunusa;

import static com.kpfunusa.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.kpfunusa.CommonUtilities.EXTRA_MESSAGE;
import static com.kpfunusa.CommonUtilities.SENDER_ID;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;
import com.kpfunusa.R;
import com.kpfunusa.GCMIntentService;
import com.kpfunusa.dialog.AlertDialogManager;
import com.kpfunusa.gps.GPSTracker;

public class ActivityLogin extends Activity {
	 // label to display gcm messages
    TextView lblMessage;
     
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;
    
    public static String webServiceURL="http://kpfun.com/ajax_ws2.php";
    public static String token="testingToken";
    public static String has_restaurant="no";
	private EditText edt_username, edt_password;
	public String gcm_id, un, pw, tab;
	public ProgressDialog pdialog;
	static int count = 0;
	/* id is get user id, dealer_id, zip_code when when user login */
	public static String id, dealer_id, zip_code, code;
	public String gcm_regid;
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences sp1;
	
	
	GPSTracker gpstracker;
	static double latitude, longitude;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent in = getIntent();
		tab = in.getStringExtra("tab");
		
		//Check for saved credentials.
		sp1=getSharedPreferences("Login",MODE_PRIVATE);
		un = sp1.getString("un", "");
		pw = sp1.getString("pw", "");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		gpstracker = new GPSTracker(getApplicationContext());
		
		// Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
		//GCMRegistrar.checkManifest(this);

		
		cd = new ConnectionDetector(getApplicationContext());

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
        
        
        if(un==""){
			setContentView(R.layout.login_layout);
			
			Button login = (Button) findViewById(R.id.btn_login);
			/* init EditText */
			edt_username = (EditText) findViewById(R.id.edt_userNameLogin);
			edt_password = (EditText) findViewById(R.id.edt_PasswordLogin);
			
			
			login.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (edt_username.getText().toString().equalsIgnoreCase("")) {

						alert.showAlertDialog(ActivityLogin.this, "KpFun alert!",
								"Username no empty!", false);
						edt_username.requestFocus();
						return;
					}
					if (edt_password.getText().toString().equalsIgnoreCase("")) {
						alert.showAlertDialog(ActivityLogin.this, "KpFun alert!",
								"Password no empty!", false);
						edt_password.requestFocus();
						return;
					}
					if (!cd.isConnectingToInternet()) {
						alert.showAlertDialog(ActivityLogin.this, "KpFun alert!",
								"No network, Please check your data connection!",
								false);
						return;
					}

					Login();

				}
			});
			TextView register = (TextView) findViewById(R.id.tv_Register);
			register.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent inRegister = new Intent(ActivityLogin.this,
							ActivityRegister.class);
					startActivity(inRegister);
					// finish();

				}
			});
        
        
        }else{
			Login();
			
		}
        
		

		
	}
	
	
	/* Login */
	private void Login() {
		
			
		RequestQueue queue = Volley.newRequestQueue(this);
		final String token = ActivityLogin.token;
		final String mode = "login";
		gcm_regid = gcm_id.toString();
		final String username, password;
		pdialog = new ProgressDialog(this);
		pdialog.setCancelable(true);
		latitude = gpstracker.getLatitude();
		longitude = gpstracker.getLongitude();

		final String str_latitude = String.valueOf(latitude);
		final String str_longitude = String.valueOf(longitude);
		
		if(gcm_regid==""){
			gcm_regid=GCMRegistrar.getRegistrationId(this);
			
		}
		
		if(un==""){
			username = edt_username.getText().toString();
			password = edt_password.getText().toString();
			un=username;
			pw=password;
			/* dialog */
			
			
			pdialog.setMessage("Checking ....");
			pdialog.show();

		
		}else{
			username = un;
			password = pw;
		}
		
		/* delay */
		/*
		 * try { Thread.sleep(12000); } catch (Exception e) { }
		 */
		StringRequest myReq = new StringRequest(Method.POST, ActivityLogin.webServiceURL,
				createMyReqSuccessListener(), createMyReqErrorListener()) {

			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("mode", mode);
				params.put("username", username);
				params.put("password", password);
				params.put("latitude", str_latitude);
				params.put("longitude", str_longitude);
				if(gcm_regid!=""){
					params.put("gcm_regid", gcm_regid);
				}
				return params;
			};
		};
		queue.add(myReq);

	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				// setTvResultText(response);
				try {
					JSONObject json = new JSONObject(response);
					String err = json.getString("error");
					id = json.getString("user_id");
					dealer_id = json.getString("dealer_id");
					zip_code = json.getString("zip_code");
					code = json.getString("code");
					if (err.equalsIgnoreCase("false")) {
						
						/* SAVE LOGIN INFO */
						SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
						SharedPreferences.Editor Ed=sp.edit();
						Ed.putString("un",un);              
						Ed.putString("pw",pw);   
						Ed.commit();
						
						
						Intent intentTab = new Intent(ActivityLogin.this, TabManager.class);
						intentTab.putExtra("tab", tab);
						startActivity(intentTab);
						finish();
						
							pdialog.dismiss();
						
						return;
					} else {
						alert.showAlertDialog(ActivityLogin.this,
								"KpFun alert", err, false);
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
				String theError=error.toString();
				pdialog.dismiss();
			}
		};
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
			showAlertDialogExit(ActivityLogin.this, "KpFun alert!",
					"Do you want to exit KpFun ?", false);
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
}
