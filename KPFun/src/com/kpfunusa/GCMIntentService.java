package com.kpfunusa;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.kpfunusa.R;

import static com.kpfunusa.CommonUtilities.SENDER_ID;
import static com.kpfunusa.CommonUtilities.displayMessage;
 
public class GCMIntentService extends GCMBaseIntentService {
 
    private static final String TAG = "GCMIntentService";
 
    public GCMIntentService() {
        super(SENDER_ID);
    }
 
    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) 
    {
    	
    	//Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        //Log.d("NAME", MainActivity.name);
        //ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);
    }
 
    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        //Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, registrationId);
    }
 
    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        //Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");
        String url="";
        String check=message.substring(0,4);
        if(message.substring(0,4).equals("http")){
        	int breakPoint=message.indexOf('|');
        	url=message.substring(0,breakPoint);
        	breakPoint=breakPoint+1;
        	message=message.substring(breakPoint);
        }
        
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message,url);
    }
 
    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        //Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message, "");
    }
 
    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        //Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }
 
    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        //Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }
 
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, String url) {
        
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        String appname = context.getResources().getString(R.string.app_name);
        
        
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        
        
        Notification notification;
        Intent in;
        if(url.equals("")){
	        in=new Intent(context, SplashScreen.class);
	        in.putExtra("tab", "tab4");
        }else{
        	in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,in, 0);

        
        notification = new Notification(icon, message, 0);
        notification.setLatestEventInfo(context, appname, message, contentIntent);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);

        
        
 
    }
 
}
