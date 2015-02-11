package com.kpfunusa;

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kpfunusa.bean.Restaurant;
import com.kpfunusa.gps.GPSTracker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 * <p>
 * Notice how we deal with the possibility that the Google Play services APK is not
 * installed/enabled/updated on a user's device.
 */
public class RestaurantMap extends FragmentActivity {
    /**
     * Note that this may be null if the Google Play services APK is not available.
     */
    private GoogleMap mMap;
	private String zip_code;
	private String city_info;
	private String addr_info;
	
	GPSTracker gpstracker;
	private Geocoder geocoder = new Geocoder(this);
	private double restaurant_lat;
	private double restaurant_lng;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_viw);
        
        init_Map_UI();
        
        get_latlng_zipcode();
        
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void init_Map_UI(){
    	
    	Intent map = getIntent();
    	zip_code = map.getStringExtra("zipCode");
    	city_info = map.getStringExtra("CityInfo");
    	addr_info = map.getStringExtra("AddressInfo");
    	Log.d("Get Extra Infor in Restaurant Map", zip_code + ":" + city_info + ":" + addr_info);
    	
    	//get restaurant position information.    	
    }
    
   public void get_latlng_zipcode(){
	   try {
		      List<Address> addresses = geocoder.getFromLocationName(zip_code, 1);
		      if (addresses != null && !addresses.isEmpty()) {
		        Address address = addresses.get(0);
		        // Use the address as needed
		        String message = String.format("Latitude: %f, Longitude: %f", 
		        restaurant_lat = address.getLatitude(), 
		        restaurant_lng = address.getLongitude());
		        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
		      } else {
		        // Display appropriate message when Geocoder services are not available
		        Toast.makeText(this, "Unable to geocode zipcode", Toast.LENGTH_LONG).show(); 
		      }
		    } catch (IOException e) {
		      // handle exception
		    }
   }
    
    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
    	 // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Show Sydney
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(restaurant_lat,restaurant_lng), 10));
        
        mMap.addMarker(new MarkerOptions()
        .position(new LatLng(restaurant_lat,restaurant_lng))
        .title(city_info + addr_info)
        .snippet("Population:" + zip_code));
    }
    
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			RestaurantMap.this.finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		return true;
	}
}
