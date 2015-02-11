package com.kpfunusa;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.kpfunusa.dialog.AlertDialogManager;
import com.kpfunusa.loadimage.ImageLoader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAdView extends YouTubeBaseActivity implements
YouTubePlayer.OnInitializedListener, OnClickListener{
	
	private Button mbtnButtonText;
	private TextView mTvCompanyName;
	private TextView mTvRewardsDesc;
	private String company_name;
	private String company_desc;
	private YouTubePlayer YPlayer;
    private static final String YoutubeDeveloperKey = "AIzaSyBlCj4gdzv922EJ0AE9mPPwJ2uBtCYesLI";
    private static final int RECOVERY_DIALOG_REQUEST = 1;
	
	
	AlertDialogManager alert = new AlertDialogManager();
	ConnectionDetector cd;
	private Intent ad_intent;
	private String video_id;
	private ImageView mImgBackground;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_ad_view);
		cd = new ConnectionDetector(getApplicationContext());
		init_ui_handle();
		YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(YoutubeDeveloperKey, this);
	}
	
	public void init_ui_handle(){
		
		/***
		 * Set up UI handle of ad view activity 
		 */
		mbtnButtonText = (Button)findViewById(R.id.btnButtonText);
		mTvCompanyName = (TextView)findViewById(R.id.tvCompanyNameDetail);
		mTvRewardsDesc = (TextView)findViewById(R.id.reward_descrip);
		mImgBackground = (ImageView)findViewById(R.id.iv_BackgroundImg);
		
		mbtnButtonText.setOnClickListener(ActivityAdView.this);
		mImgBackground.setVisibility(View.INVISIBLE);
		/***
		 * intent parameter set up
		 */
		Intent adviewintent = getIntent();
		company_name = adviewintent.getStringExtra("companyname");
		company_desc = adviewintent.getStringExtra("description");
		video_id = adviewintent.getStringExtra("video_id");
		
		mTvCompanyName.setText(company_name);
		mTvRewardsDesc.setText(company_desc);
	}
	
	/***
	 * video stream function.
	 */
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		Toast.makeText(ActivityAdView.this, "aaaaaaaaaaaaaaaaaaaaaaaa", Toast.LENGTH_SHORT).show();
		switch (v.getId()){
			case R.id.btnButtonText:
			{
				if (!cd.isConnectingToInternet()) {
					alert.showAlertDialog(ActivityAdView.this, "KpFun alert!",
							"No network, Please check your data connection!", false);
					return;
				}

				String url = "http://kpfun.com";
				ad_intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				ActivityAdView.this.startActivity(ad_intent);
			}

			break;
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			GlobalVariable.globalvariable = true;
			ActivityAdView.this.finish();
			
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		
		return true;
	}

	 @Override
	    public void onInitializationFailure(YouTubePlayer.Provider provider,
	            YouTubeInitializationResult errorReason) {
	        if (errorReason.isUserRecoverableError()) {
	            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
	            mImgBackground.setVisibility(View.VISIBLE);
	            String urlImg = "http://img.youtube.com/vi/Aci1hPNhQ6Q/0.jpg";
	            if ( mImgBackground != null) {
					int loader = R.drawable.loader; 
					ImageLoader imgLoader = new ImageLoader(getApplicationContext());
					imgLoader.DisplayImage(urlImg, loader, mImgBackground, true);
				}
	        } else {
			            String errorMessage = String.format(
			                    "There was an error initializing the YouTubePlayer",
			                    errorReason.toString());
			            mImgBackground.setVisibility(View.VISIBLE);
			            
			            String urlImg = "http://img.youtube.com/vi/Aci1hPNhQ6Q/0.jpg";
			            if ( mImgBackground != null) {
							int loader = R.drawable.loader; 
							ImageLoader imgLoader = new ImageLoader(getApplicationContext());
							imgLoader.DisplayImage(urlImg, loader, mImgBackground, true);
						}
			            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
	        }
	    }
	 
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        if (requestCode == RECOVERY_DIALOG_REQUEST) {
	            // Retry initialization if user performed a recovery action
	            getYouTubePlayerProvider().initialize(YoutubeDeveloperKey, this);
	        }
	    }
	 
	    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
	        return (YouTubePlayerView) findViewById(R.id.youtube_view);
	    }
	 
	    @Override
	    public void onInitializationSuccess(Provider provider,
	            YouTubePlayer player, boolean wasRestored) {
	        if (!wasRestored) {
	            YPlayer = player;
	            /*
	             * Now that this variable YPlayer is global you can access it
	             * throughout the activity, and perform all the player actions like
	             * play, pause and seeking to a position by code.
	             */
	            YPlayer.cueVideo(video_id);
	        }
	    }
	
}
