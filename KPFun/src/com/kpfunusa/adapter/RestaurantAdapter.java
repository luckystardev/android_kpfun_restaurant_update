package com.kpfunusa.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kpfunusa.R;
import com.kpfunusa.bean.Restaurant;
import com.kpfunusa.loadimage.ImageLoader;

public class RestaurantAdapter extends ArrayAdapter<Restaurant>{
	Context context;
	int layoutResourceId;
	ArrayList<Restaurant> data=new ArrayList<Restaurant>();
	public RestaurantAdapter(Context context,  int layoutResourceId, ArrayList<Restaurant> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	//get id of list
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ImageHolder holder = null;
		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ImageHolder();
			holder.name = (TextView)row.findViewById(R.id.tvName);
			holder.distance = (TextView)row.findViewById(R.id.tvDistance);
			holder.img = (ImageView) row.findViewById(R.id.imageLogo);
			holder.arrow = (ImageView) row.findViewById(R.id.ivRightArrow);
			holder.rl = (RelativeLayout) row.findViewById(R.id.row_bg);
			row.setTag(holder);
		}
		else
		{
			holder = (ImageHolder)row.getTag();
		}
		Restaurant restaurant = data.get(position);
		holder.name.setText(restaurant.getCompany_name());
		holder.distance.setText(restaurant.getDistance());
		// Set the results into ImageView
		String urlImg = "http://kpfun.com";
		String theHolder=restaurant.getImg_thumb();
		if (theHolder.equals("restaurant")) {
			holder.img.setImageResource(R.drawable.ic_restaurant);
		}else if (theHolder.equals("bar")) {
			holder.img.setImageResource(R.drawable.ic_bar);
		}else if (theHolder.equals("ad")) {
			holder.img.setBackgroundColor(0xFF000000);
			holder.img.setImageResource(R.drawable.ic_ad);
		}else if (theHolder.equals("loadMore")) {
			holder.img.setVisibility(4); //Invisible
			holder.arrow.setVisibility(4);
		}else if (holder.img != null && !theHolder.equals("")) {
			//new ImageDownloaderTask(holder.img).execute(urlImg+restaurant.getImg());
			int loader = R.drawable.loader;
	        ImageLoader imgLoader = new ImageLoader(getContext());
	        holder.img.setImageResource(android.R.color.transparent); 
	        imgLoader.DisplayImage(urlImg+restaurant.getImg_thumb(), loader, holder.img, false);
		}else{
			holder.img.setImageResource(R.drawable.ic_restaurant);
			/*
			int loader = R.drawable.ic_restaurant;
			ImageLoader imgLoader = new ImageLoader(getContext());
		    imgLoader.DisplayImage("", loader, holder.img, false);
			*/
			
		}
		
		//holder.img.setImageResource(restaurant.getImg());
		
		return row;
	}
	
	static class ImageHolder
	{
		TextView name;
		TextView distance;
		ImageView img;
		ImageView arrow;
		RelativeLayout rl;
	}
}