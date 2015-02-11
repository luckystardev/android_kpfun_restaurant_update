package com.kpfunusa;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.bean.Restaurant;
import com.kpfunusa.bean.Rewards;
import com.kpfunusa.dialog.AlertDialogManager;
import com.kpfunusa.loadimage.ImageLoader;

public class ActivitySharing extends Activity {
	private static final int CAMERA_PIC_REQUEST = 1337;
	  
	private ImageView mIvImg;
	private String url;
	static String id_deal;
	static String title;
	
	private TextView mTvTitle;
	private ImageView mIvLogo, mIvAddPhoto;
	private Button mBtSend;
	private ProgressDialog pd;
	private EditText mEtMessage;
	private String pathImage="";
	static final int SELECT_IMG = 100;
	Bitmap bmp = null;
	AlertDialogManager alert = new AlertDialogManager();

	/* GCM */
	// Asyntask
	AsyncTask<Void, Void, Void> mRegisterTask;

	private TextView btnPersonalize;

	public static String token;
	public static String mode;
	public static String user_id;
	public static String deal_id;
	public static String user_id_company;
	public static String user_img;
	public static String caption;
	public static String company_name;
	public static int CAMERA = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.sharing_layout);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(
				R.layout.sharing_layout, null);
		this.setContentView(viewToLoad);

		mTvTitle = (TextView) findViewById(R.id.tvTitleBonus);
		mIvImg = (ImageView) findViewById(R.id.ivLogoSharing);
		mBtSend = (Button) findViewById(R.id.btnSend);
		mIvAddPhoto = (ImageView) findViewById(R.id.btnAddPhoto);
		mEtMessage = (EditText) findViewById(R.id.txtMessage);
//		btnPersonalize = (TextView)findViewById(R.id.personalize);
//		btnPersonalize.setVisibility(1);
		
		mTvTitle.setText("Share");
		String urlImg = "http://kpfun.com";
		
		
		
		Bonus();
		mTvTitle = (TextView)findViewById(R.id.tvTitleBonus);
		//Button next = (Button)findViewById(R.id.btnNext);
		
		
		/*Button addFriend =(Button)findViewById(R.id.btnAddFriends);
		
		addFriend.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intentAdd = new Intent(getParent(), ActivityAddFriendsForSearchRestaurants.class);
				TabGroupActivity parentActivity = (TabGroupActivity)getParent();
				parentActivity.startChildActivity("ActivityAddFriendsForSearchRestaurants", intentAdd);
				
			}
		});
		*/
		

		mBtSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				 Sharing();
			}
		});
		mIvAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showAlertCamera(getParent(), "Share Photo","Take a new photo or select one from your gallery.", true);
				
			}
		});

	}
	public void Sharing() {
		
		pd = ProgressDialog.show(getParent(), null, "Please Wait...");
		final String caption = mEtMessage.getText().toString();
		RequestQueue queue = Volley.newRequestQueue(this);
		String url = ActivityLogin.webServiceURL;
		String random=UUID.randomUUID().toString();
		final String rando= random.replace("-", "");
		StringRequest postRequest = new StringRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						//alert.showAlertDialog(getParent(), "KpFun alert!", response, false);
						// response
						try {
							JSONObject json = new JSONObject(response);
							String voucher=json.getString("voucher");
							String percent=json.getString("percent");
							String er = json.getString("error");
							String name = json.getString("name");
//							String descrip = json.getString("description");
//							 String voucher = json.getString("voucher");
							if (er.toString().equalsIgnoreCase("false")) {
								Intent intent = new Intent(getParent(), ActivityRestaurantRewards.class);
								TabGroupActivity parentActivity = (TabGroupActivity) getParent();
								
								intent.putExtra("company_name_rewards", ActivityRestaurantDetails.res.getCompany_name());
								intent.putExtra("img_rewards", ActivityRestaurantDetails.res.getImg());
								intent.putExtra("address_rewards", ActivityRestaurantDetails.res.getAddress());
								intent.putExtra("city_rewards", ActivityRestaurantDetails.res.getCity());
								intent.putExtra("state_rewards", ActivityRestaurantDetails.res.getState());
								intent.putExtra("voucher", voucher);
								intent.putExtra("zip_code_rewards", ActivityRestaurantDetails.res.getZip_code());
								intent.putExtra("percent_rewards", percent);
								intent.putExtra("name", name);
								intent.putExtra("terms", ActivityRestaurantDetails.res.getTerms());
								intent.putExtra("description", ActivityRestaurantDetails.res.getDescription());
								
								parentActivity.startChildActivity("ActivityRestaurantRewards", intent);
								
								
								//alert.showAlertDialog(getParent(), "KpFun alert!", "Sharing success!", true);
							} else {
								String theError="Sharing Fail! Error message: " +er;
								alert.showAlertDialog(getParent(), "KpFun alert!", theError, false);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pd.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						Log.i("Error.Response", error.getMessage());
						pd.dismiss();
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", ActivityLogin.token);
				params.put("mode", "createVoucher");
				params.put("user_id", ActivityLogin.id);
				params.put("deal_id", ActivitySharing.id_deal);
				params.put("user_id_company", ActivityRestaurantDetails.res.getId());
				params.put("user_img", pathImage);
				params.put("caption", caption);
				params.put("rando", rando);
				return params;

			}
		};
		queue.add(postRequest);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_IMG && resultCode == RESULT_OK && data != null) {
			Uri selectedImg = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImg, filePathColumn, null, null, null);

			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

				pathImage = cursor.getString(columnIndex);
				//Log.d("check photo", pathImage);

				Bitmap bmp = loadImage(pathImage);
				

				if(bmp.getWidth()>800 || bmp.getHeight()>800){
					int width=bmp.getWidth();
					int height=bmp.getHeight();
					int newWidth=800;
					int newHeight=800;
					if(width > height){
						newHeight=(height*newWidth)/width;
					}else{
						newWidth=(width*newHeight)/height;
					}
					
					bmp=Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
				}
				// new loadPictureAsyn().execute();
				mIvAddPhoto.setImageBitmap(bmp);
				
				// Bitmap bitmap =
				// BitmapFactory.decodeResource(getResources(),R.drawable.logo);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);
				byte[] byte_arr = stream.toByteArray();
				pathImage = Base64.encodeToString(byte_arr, Base64.DEFAULT);
			}
		}else if(requestCode == CAMERA_PIC_REQUEST){
			
			Bundle extras = data.getExtras();
		    Bitmap mImageBitmap = (Bitmap) extras.get("data");
		    mIvAddPhoto.setImageBitmap(mImageBitmap);
		    
		    ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
			byte[] byte_arr = stream.toByteArray();
			pathImage = Base64.encodeToString(byte_arr, Base64.DEFAULT);
		    
		}
	}

	private Bitmap loadImage(String imgPath) {
		BitmapFactory.Options options;
		try {
			options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void Bonus(){
        //Intent in = getIntent();		
		RequestQueue queue = Volley.newRequestQueue(this);
		url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
		    new Response.Listener<String>() 
		    {
		        @Override
		        public void onResponse(String response) {
		            // response
		        	try {
						JSONObject json = new JSONObject(response);
						title = json.getString("title");
						id_deal = json.getString("id");
						String urlImg = "http://kpfun.com";
						// error in here
						if(mIvImg!=null){
							int loader = R.drawable.loader;
					        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
					        imgLoader.DisplayImage(urlImg+json.getString("deal_img"), loader, mIvImg, true);
						}
				        //new ImageDownloaderTask(mIvImg).execute(urlImg+json.getString("deal_img"));
						mTvTitle.setText(title);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	 //pd.dismiss();
		        }
		    }, 
		    new Response.ErrorListener() 
		    {
		         @Override
		         public void onErrorResponse(VolleyError error) {
		             // error
		        	 //Log.i("Error.Response",error.getMessage());
		       }
		    }
		) {     
		    @Override
		    protected Map<String, String> getParams() 
		    {  
		            Map<String, String>  params = new HashMap<String, String>();  
		            params.put("token", ActivityLogin.token);
                    params.put("mode", "bonus");
                    params.put("user_id", ActivityRestaurantDetails.res.getId());
		            return params;  
		           
		    }
		};
		queue.add(postRequest);
		
	}
	
	/* dialog photos */
	@SuppressWarnings("deprecation")
	public void showAlertCamera(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog
					.setIcon((status) ? R.drawable.camera : R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton2("Camera", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				
				String test=getParent().toString();
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
				getParent().startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
				
			}

		});

		alertDialog.setButton("Gallery", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				getParent().startActivityForResult(i, SELECT_IMG);
				
				
				
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
}
