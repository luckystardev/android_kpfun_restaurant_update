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
import com.kpfunusa.bean.Rewards;
import com.kpfunusa.loadimage.ImageLoader;

public class RewardsAdapter extends ArrayAdapter<Rewards> {
	Context context;
	int layoutResourceId;
	ArrayList<Rewards> data = new ArrayList<Rewards>();

	public RewardsAdapter(Context context, int layoutResourceId,
			ArrayList<Rewards> data) {
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
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ImageHolder();
			holder.name = (TextView) row.findViewById(R.id.tvNameRewards);
			holder.reward = (TextView) row.findViewById(R.id.tvRewards);
			holder.date = (TextView) row.findViewById(R.id.tvDate);
			holder.logo = (ImageView) row.findViewById(R.id.ivLogoRewards);
			row.setTag(holder);
		} else {
			holder = (ImageHolder) row.getTag();
		}
		Rewards rewards = data.get(position);
		holder.name.setText(rewards.getCompany_name());
		holder.reward.setText(rewards.getPercent()+"% Off");
		holder.date.setText(rewards.getExpiration());
		// Set the results into ImageView

		String urlImg = "http://kpfun.com";
		if (holder.logo != null) {
			/*new ImageDownloaderTask(holder.logo).execute(urlImg
					+ rewards.getImg());*/
			int loader = R.drawable.loader;
	        ImageLoader imgLoader = new ImageLoader(getContext());
	        imgLoader.DisplayImage(urlImg+ rewards.getImg(), loader, holder.logo, false);
		}

		return row;
	}

	static class ImageHolder {
		TextView name;
		TextView reward;
		TextView date;
		ImageView logo;
	}
}
