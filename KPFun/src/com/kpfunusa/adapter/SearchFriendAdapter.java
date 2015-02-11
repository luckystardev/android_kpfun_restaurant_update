package com.kpfunusa.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kpfunusa.ActivityLogin;
import com.kpfunusa.R;
import com.kpfunusa.bean.SearchFriends;

public class SearchFriendAdapter extends ArrayAdapter<SearchFriends> {
	Context context;
	int layoutResourceId;
	ArrayList<SearchFriends> data = new ArrayList<SearchFriends>();
	//List<SearchFriends> listSearchFriend = new Lis

	public SearchFriendAdapter(Context context, int layoutResourceId,
			ArrayList<SearchFriends> data) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ImageHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ImageHolder();
			holder.name = (TextView) row.findViewById(R.id.tvNameContact);
			holder.phone = (TextView) row.findViewById(R.id.tvPhoneNumber);
			holder.mail = (ImageView)row.findViewById(R.id.ivMail);
			row.setTag(holder);
		} else {
			holder = (ImageHolder) row.getTag();
		}
		SearchFriends search = data.get(position);
		holder.name.setText(search.getName());
		holder.phone.setText(search.getPhone());
		holder.mail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content ="Hey, put this app on your phone and register, so I can send you free food. http://kpfun.com/apps Use code: " + ActivityLogin.code;
				String number =data.get(position).getPhone();
				sendSMS(content,number);
			}
		});
		return row;
	}

	static class ImageHolder {
		TextView name;
		TextView phone;
		ImageView mail;

	}
	private void sendSMS(String content,String numberPhone){
		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		sendIntent.putExtra("sms_body", content);
		sendIntent.putExtra("address", numberPhone);
		sendIntent.setType("vnd.android-dir/mms-sms");
		getContext().startActivity(sendIntent);
	}
}
