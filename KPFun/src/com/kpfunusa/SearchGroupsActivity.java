package com.kpfunusa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class SearchGroupsActivity extends TabGroupActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startChildActivity("RestaurantSearchActivity", new Intent(this.getApplicationContext(),ActivityRestaurantSearch.class));
    }
    @SuppressWarnings("deprecation")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    String test = "This was called";
    	if(resultCode == Activity.RESULT_OK)
           {
           ActivitySharing activity = (ActivitySharing) getLocalActivityManager().getCurrentActivity();
           activity.onActivityResult(requestCode, resultCode, data);
           }
    }
    
}
