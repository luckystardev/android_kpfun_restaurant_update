package com.kpfunusa.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpfunusa.R;
import com.kpfunusa.bean.Gifts;
import com.kpfunusa.loadimage.ImageLoader;

public class GiftsAdapter extends ArrayAdapter<Gifts>{
	Context context;
	int layoutResourceId;
	ArrayList<Gifts> data=new ArrayList<Gifts>();
	public GiftsAdapter(Context context,  int layoutResourceId, ArrayList<Gifts> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
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
			holder.company_name = (TextView)row.findViewById(R.id.tvNameGifts);
			holder.title = (TextView)row.findViewById(R.id.tvGifts);
			holder.expiration = (TextView)row.findViewById(R.id.tvDateGifts);
			holder.logo = (ImageView) row.findViewById(R.id.ivLogoGifts);
			row.setTag(holder);
		}
		else
		{
			holder = (ImageHolder)row.getTag();
		}
		Gifts gifts = data.get(position);
		holder.company_name.setText("@"+gifts.getCompany_name());
		holder.title.setText(gifts.getTitle());
		holder.expiration.setText("From " + gifts.getName());
		// Set the results into ImageView
		String urlImg = "http://kpfun.com";
		if (holder.logo != null) {
			/*new ImageDownloaderTask(holder.logo).execute(urlImg
					+ gifts.getDeal_img());*/
			int loader = R.drawable.loader;
	        ImageLoader imgLoader = new ImageLoader(getContext());
	        imgLoader.DisplayImage(urlImg+ gifts.getDealImg(), loader, holder.logo, false);
		}
		
		return row;
	}
	
	static class ImageHolder
	{
		TextView company_name;
		TextView title;
		TextView expiration;
		ImageView logo;
	}
}
