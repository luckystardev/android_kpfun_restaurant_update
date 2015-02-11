/*
 * Copyright (C) 2012 Muhammad Tayyab Akram <dear_tayyab@yahoo.com>
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

package com.kpfunusa;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.kpfunusa.R;
import com.kpfunusa.graphic.TabBitmap;

@SuppressWarnings("deprecation")
public class TabManager extends TabActivity {
	
	private static final String TAG_1 = "tab1";
	private static final String TAG_2 = "tab2";
	private static final String TAG_3 = "tab3";
	private static final String TAG_4 = "tab4";
	private static final String TAG_5 = "tab5";
	
	TabHost mTabHost;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Intent in = getIntent();
		String tab = in.getStringExtra("tab");
		if(tab ==null){
			tab="tab1";
		}
		
		setTabs();
		
		mTabHost.setCurrentTabByTag(tab);
		
	}

	private void setTabs() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		addTab(getResources().getString(R.string.search), TAG_1, createTabDrawable(R.drawable.search), SearchGroupsActivity.class);
		addTab(getResources().getString(R.string.friends), TAG_2, createTabDrawable(R.drawable.friends), ActivityAddFriends.class);
		//addTab(getResources().getString(R.string.connect), TAG_3, createTabDrawable(R.drawable.connect),ActivitySocialConnect.class);
		addTab(getResources().getString(R.string.rewards), TAG_4, createTabDrawable(R.drawable.rewards), FriendGroupActivity.class);
		addTab(getResources().getString(R.string.more), TAG_5, createTabDrawable(R.drawable.more), ActivityMore.class);
		
	}
	
	private Drawable createTabDrawable(int resId) {
		Resources res = getResources();
		StateListDrawable states = new StateListDrawable();

		final Options options = new Options();
		options.inPreferredConfig = Config.ARGB_8888;
		
		Bitmap icon = BitmapFactory.decodeResource(res, resId, options);
		
		Bitmap unselected = TabBitmap.createUnselectedBitmap(res, icon);
		Bitmap selected = TabBitmap.createSelectedBitmap(res, icon);
		
		icon.recycle();
		
		states.addState(new int[] { android.R.attr.state_selected }, new BitmapDrawable(res, selected));
		states.addState(new int[] { android.R.attr.state_enabled }, new BitmapDrawable(res, unselected));
		
		return states;
	}
	
	private View createTabIndicator(String label, Drawable drawable) {
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, mTabHost.getTabWidget(), false);

		TextView txtTitle = (TextView) tabIndicator.findViewById(R.id.text_view_tab_title);
		txtTitle.setText(label);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) txtTitle.getLayoutParams();
		txtTitle.setLayoutParams(params);

		ImageView imgIcon = (ImageView) tabIndicator.findViewById(R.id.image_view_tab_icon);
		imgIcon.setImageDrawable(drawable);
		
		return tabIndicator;
	}
	
	private void addTab(String label, String tag, Drawable drawable,  Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = mTabHost.newTabSpec(tag);
		spec.setIndicator(createTabIndicator(label, drawable));
		spec.setContent(intent);

		mTabHost.addTab(spec);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event){
		return true;
	}
}