package com.kpfunusa.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kpfunusa.R;
import com.kpfunusa.ActivityLogin;
import com.kpfunusa.bean.AddFriends;

public class AddFriendsAdapter extends ArrayAdapter<AddFriends> {
	Context context;
	int layoutResourceId;
	ArrayList<AddFriends> data = new ArrayList<AddFriends>();
	private OnWidgetItemClicked listener;
	public AddFriendsAdapter(Context context, int layoutResourceId, ArrayList<AddFriends> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}
	public void setListener(OnWidgetItemClicked listener){
		this.listener = listener;
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
			holder.acc = (TextView) row.findViewById(R.id.tvAccount);

			holder.check = (ImageView) row.findViewById(R.id.ivCheckFriend);
			row.setTag(holder);
		} else {
			holder = (ImageHolder) row.getTag();
		}
		AddFriends add = data.get(position);
		holder.acc.setText(add.getAccount());
		// Set the results into ImageView
		holder.check.setImageResource(add.getState());
		holder.check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(data.get(position).getState()==R.drawable.friend_false){
					AddFriend(data.get(position).getId().toString());
					data.get(position).setState(R.drawable.friend_true);
					
				}else{
					RemoveFriend(data.get(position).getId().toString());
					data.get(position).setState(R.drawable.friend_false);
				}
				ImageView iv = (ImageView) v.findViewById(R.id.ivCheckFriend);
				iv.setImageResource(data.get(position).getState());
				if(listener != null) listener.onCheckClicked(v, position);
			}
		});

		return row;
	}

	static class ImageHolder {
		TextView acc;
		ImageView check;
	}
	public void AddFriend(final String friend_id){
		RequestQueue queue = Volley.newRequestQueue(getContext());
		String url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
		    new Response.Listener<String>() 
		    {
		        @Override
		        public void onResponse(String response) {
		            // response
		        	try {
						JSONObject json = new JSONObject(response);
						String er = json.getString("error");
						if(!er.equalsIgnoreCase("false"))
							Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }, 
		    new Response.ErrorListener() 
		    {
		         @Override
		         public void onErrorResponse(VolleyError error) {
		             // error
		        	 //Log.i("Error.Response",error.getMessage());
		       }
		    }
		) {     
		    @Override
		    protected Map<String, String> getParams() 
		    {  
		            Map<String, String>  params = new HashMap<String, String>();  
		            params.put("token", ActivityLogin.token);
                    params.put("mode", "addFriend");
                    params.put("user_id",  ActivityLogin.id);
                    params.put("friend_id", friend_id);
		            return params;  
		           
		    }
		};
		queue.add(postRequest);
	}
	
	public void RemoveFriend(final String removefriend_id){
		RequestQueue queue = Volley.newRequestQueue(getContext());
		String url = ActivityLogin.webServiceURL;
		StringRequest postRequest = new StringRequest(Request.Method.POST, url, 
		    new Response.Listener<String>() 
		    {
		        @Override
		        public void onResponse(String response) {
		            // response
		        	try {
						JSONObject json = new JSONObject(response);
						String er = json.getString("error");
						if(!er.equalsIgnoreCase("false"))
							Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    }, 
		    new Response.ErrorListener() 
		    {
		         @Override
		         public void onErrorResponse(VolleyError error) {
		             // error
		        	 //Log.i("Error.Response",error.getMessage());
		       }
		    }
		) {     
		    @Override
		    protected Map<String, String> getParams() 
		    {  
		            Map<String, String>  params = new HashMap<String, String>();  
		            params.put("token", ActivityLogin.token);
                    params.put("mode", "removeFriend");
                    params.put("user_id", ActivityLogin.id);
                    params.put("friend_id", removefriend_id);
		            return params;  
		           
		    }
		};
		queue.add(postRequest);
	}
	public interface OnWidgetItemClicked{
		void onCheckClicked(View v, int position);
	}
}
