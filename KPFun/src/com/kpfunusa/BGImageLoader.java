package com.kpfunusa;

import java.io.InputStream;
import java.net.URL;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

public class BGImageLoader extends AsyncTask<String, Void, Drawable> {

    //ProgressDialog pd;
    ImageView imgView;
    public BGImageLoader (ImageView img){
        imgView=img;
    }
    protected Drawable doInBackground(String... url) {
           try {
        	   //pd = ProgressDialog.show(this, "Working..", "Getting image", true, false);
               InputStream is = (InputStream) new URL(url[0]).getContent();
               Drawable d = Drawable.createFromStream(is, "src name");
               return d;
           } catch (Exception e) {
               System.out.println("Exception = " + e);
               return null;
           }
    }

    protected void onProgressUpdate(Void... progress) {
    }

    protected void onPostExecute(Drawable result) {
        /* Image loaded...*/
       // pd.dismiss();
        imgView.setBackgroundDrawable(result);

    }
}
