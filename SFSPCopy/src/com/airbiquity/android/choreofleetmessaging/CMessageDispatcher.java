package com.airbiquity.android.choreofleetmessaging;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Messenger;
import android.util.SparseArray;


public class CMessageDispatcher {
	
	private static final String TAG = "CMessageDispatcher";
private static CMessageDispatcher minstance = null;
	
	private ArrayList<Handler> mHandlers = new ArrayList<Handler>();
	private SparseArray<int[]> mAccepts = new SparseArray<int[]>();
	private Context mContext = null;
	private boolean mIsBound = false;
	private Messenger mService = null;
	private int mServiceState = CMessageBrokerService.STATE_NOT_STARTED;
	private DBInstance mDb = null;
	
}