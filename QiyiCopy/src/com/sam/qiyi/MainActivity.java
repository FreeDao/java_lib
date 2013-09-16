package com.sam.qiyi;

import com.sam.qiyi.account.AccountActivity;
import com.sam.qiyi.channel.ChannelActivity;
import com.sam.qiyi.home.HomeActivity;
import com.sam.qiyi.more.MoreActivity;
import com.sam.qiyi.search.SearchActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private TabHost tabhost;
	private Intent mHomeIntent;
	private Intent mChannelIntent;
	private Intent mSearchIntent;
	private Intent mAccountIntent;
	private Intent mMoreIntent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		prepareIntent();
		initClickFunctions();
		setupIntent();

	}

	private void initClickFunctions() {
		findViewById(R.id.channel1).setOnClickListener(this);
		findViewById(R.id.channel2).setOnClickListener(this);
		findViewById(R.id.channel3).setOnClickListener(this);
		findViewById(R.id.channel4).setOnClickListener(this);
		findViewById(R.id.channel5).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG, "----------tab was clicked:"+v.getId());
		switch (v.getId()) {
		case R.id.channel1:
			tabhost.setCurrentTabByTag(TabConstance.TAB_TAGHOME);
			break;
		case R.id.channel2:
			tabhost.setCurrentTabByTag(TabConstance.TAB_TAG_CHANNEL);
			break;
		case R.id.channel3:
			tabhost.setCurrentTabByTag(TabConstance.TAB_TAG_SEARCH);
			break;
		case R.id.channel4:
			tabhost.setCurrentTabByTag(TabConstance.TAB_TAG_ACCOUNT);
			break;
		case R.id.channel5:
			tabhost.setCurrentTabByTag(TabConstance.TAB_TAG_MORE);
			break;
		}

	}

	private void setupIntent(){
		tabhost = getTabHost();
		tabhost.addTab(tabhost.newTabSpec(TabConstance.TAB_TAGHOME).setIndicator("home",getResources().getDrawable(R.drawable.icon_1_n)).setContent(mHomeIntent));
		tabhost.addTab(tabhost.newTabSpec(TabConstance.TAB_TAG_CHANNEL).setIndicator("channel",getResources().getDrawable(R.drawable.icon_2_n)).setContent(mChannelIntent));
		tabhost.addTab(tabhost.newTabSpec(TabConstance.TAB_TAG_SEARCH).setIndicator("search",getResources().getDrawable(R.drawable.icon_3_n)).setContent(mSearchIntent));
		tabhost.addTab(tabhost.newTabSpec(TabConstance.TAB_TAG_ACCOUNT).setIndicator("account",getResources().getDrawable(R.drawable.icon_4_n)).setContent(mAccountIntent));
		tabhost.addTab(tabhost.newTabSpec(TabConstance.TAB_TAG_MORE).setIndicator("moremore",getResources().getDrawable(R.drawable.icon_5_n)).setContent(mMoreIntent));
	}
	
	private void prepareIntent(){
		mHomeIntent = new Intent(this, HomeActivity.class);
		mChannelIntent = new Intent(this, ChannelActivity.class);
		mAccountIntent = new Intent(this, AccountActivity.class);
		mSearchIntent = new Intent(this, SearchActivity.class);
		mMoreIntent = new Intent(this, MoreActivity.class);
	}
	
}
