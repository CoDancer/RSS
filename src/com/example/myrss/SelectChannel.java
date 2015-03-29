package com.example.myrss;

import java.util.ArrayList;
import java.util.List;
import com.rss.data.ChannelDataHelper;

import android.R.integer;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectChannel  extends ListActivity {

	private static final int MENU_ADD = Menu.FIRST;
	private static final int MENU_QUIT = MENU_ADD + 1;
	
	ChannelAdapter mAdapter;
	ChannelDataHelper mChannel;
	
	List<String> channelList = new ArrayList<String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mChannel = new ChannelDataHelper(this);
		channelList = mChannel.getChannelList();
		mAdapter = new ChannelAdapter(this);
		
		if (mAdapter.getCount() == 0)
			Toast.makeText(this, "尚未添加任何频道", Toast.LENGTH_LONG).show();
			setTitle("频道选择");
			
			setListAdapter(mAdapter);
			
	}
	

	class ChannelAdapter extends BaseAdapter {
		private LayoutInflater  mInflator;
		
		public ChannelAdapter (Context context) {
			this.mInflator = LayoutInflater.from(context);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return channelList.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return channelList.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder vRss = null;
			final int row = position;
			
			vRss = new ViewHolder();
			convertView = mInflator.inflate(R.layout.item, null);
			
			vRss.textLayout = (LinearLayout)convertView.findViewById(R.id.textLayout);
			
			vRss.channel = (TextView)convertView.findViewById(R.id.title);
			vRss.delBtn = (Button)convertView.findViewById(R.id.del_btn);
			Log.e("quojs",channelList.get(row));
			
			vRss.textLayout.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String ChannelName = channelList.get(row);
					Intent it = new Intent();
					it.putExtra("channel",mChannel.getUrlByChannel(ChannelName));
					it.setClass(SelectChannel.this, ActivityMain.class);
					
					startActivity(it);
				}
			});
			
			vRss.delBtn.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					delRssInfo();
				}
				private void delRssInfo() {
					if(-1 != mChannel.DelChannelInfo(channelList.get(row))) {
						Toast.makeText(SelectChannel.this, "删除成功", Toast.LENGTH_LONG).show();
						channelList.remove(row);
						mAdapter.notifyDataSetChanged();
					} else {
					Toast.makeText(SelectChannel.this, "删除失败", Toast.LENGTH_LONG).show();
					
					}
				}
			});

			return convertView;
		}
		
	}
	
	final class ViewHolder {
		public LinearLayout textLayout;
		public TextView channel;
		public Button delBtn;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_ADD, 0, R.string.add_rss);
		menu.add(0, MENU_QUIT, 1, R.string.rss_quit);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case MENU_ADD:
			Intent intent = new Intent();
			intent.setClass(SelectChannel.this, AddRss.class);
			startActivity(intent);
			return true;
		case MENU_QUIT:
			SelectChannel.this.finish();

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
