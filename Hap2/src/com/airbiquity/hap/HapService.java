package com.airbiquity.hap;

import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.airbiquity.application.manager.ApplicationManager;
import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.connectionmgr.coonmgr.android.ConnectionManager;


/**
 * HAP (Handset Application Proxy) Service.
 *
 */
public class HapService extends Service
{
	private static final String TAG = "HAP Service";
	static final String ACTION_FOREGROUND = "com.airbiquity.hap.FOREGROUND";
	static final String ACTION_BACKGROUND = "com.airbiquity.hap.BACKGROUND";

    private ApplicationManager appManager;
    //private CommandHandler commandHandler = null;
	//private Object mLock;                                  // Lock object to synchronize what?
	//private BtConManager btConMngr = null;                 // Our bluetooth connection manager that connects to Head Unit.
	

	/**
	 * Constructor.
	 */
	public HapService() 
	{
		//Log.d(HAP, "HapService()");
        appManager = ApplicationManager.getInstance();
	}

	/**
	 * Called by the system when the service is first created.
	 */
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		Log.d(TAG, "onCreate()");
		A.a().addHuConStateListener( conListener );
		appManager.initialize();

		// TODO: Use NotificationManager to enable Bluetooth if not already done
	}


	@Override
	public IBinder onBind(Intent intent) 
	{
		Log.d(TAG, "onBind()");

		// Select the interface to return.		
		if (IHandsetApplicationProxy.class.getName().equals(intent.getAction())) 
		{
			Log.d(TAG, "onBind IHandsetApplicationProxy");
			return mHapBinder;
		}

		return null;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
	    Log.d(TAG, "onStartCommand()");

		if( !ConnectionManager.isConnected() && A.getMipId() != "")
		{
	        ConnectionManager.connect();
		}
		
		// Broadcast intent to nomadic apps to notify that the Agent has started. 
		// The nomadic app should connect to Agent if not connected yet.
		A.getContext().sendBroadcast( new Intent( "com.airbiquity.hap.ServiceStarted" ) );		

		if( null != intent )
		    setForegroundMode( ACTION_FOREGROUND.equals(intent.getAction()) );
		
		return Service.START_STICKY;
	}

	private void setForegroundMode( boolean isForeground )
	{
        if( isForeground )
        {
          // In this sample, we'll use the same text for the ticker and the expanded notification
          CharSequence text = getText( R.string.foreground_service_started );

          // Set the icon, scrolling text and timestamp
          Notification notification = new Notification( R.drawable.ic_stat, text, System.currentTimeMillis() );

          // The PendingIntent to launch our activity if the user selects this notification
          Intent i = new Intent( this, ActHome.class );
          i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
          PendingIntent contentIntent = PendingIntent.getActivity( this, 0, i, 0 );

          // Set the info for the views that show in the notification panel.
          notification.setLatestEventInfo( this, getText( R.string.app_name ), text, contentIntent );

          startForeground( R.string.foreground_service_started, notification );
        }
        else
        {
          stopForeground( true );
        }	    
	}
	
	@Override
	public void onDestroy() 
	{
		// stop the connection
		Log.e("--->", "stop service");
	    A.a().removeHuConStateListener( conListener );
	    setConState( false );
	    ConnectionManager.disconnect();
		appManager.deinitialize();
        super.onDestroy();
	}

	
	/** Listener for the changes in connection state. */
	private HuConStateListener conListener = new HuConStateListener()
	{
        @Override public void conStateChanged( boolean s ){ setConState( s ); }
    };
	
    
	/**
     * Binder for IHandsetApplicationProxy interface.
     * This interface is for registering nomadic apps with Agent.
     * Upon registration a nomadic app provide callback so that Agent can forward messages from HU to the nomadic app. 
     * 
     * The IHandsetApplicationProxy is defined through IDL. When implementing these methods, be aware
     * of the following:
     * 1) All exceptions will remain local to the implementing process. They will not be propagated
     *    to the calling application.
     * 2) All IPC calls are synchronous. If you know the process is likely to be time-consuming, you
     *    should consider wrapping the synchronous call in an asynchronous wrapper or moving the
     *    process on the receiver side into a background thread.
     */
    private final IHandsetApplicationProxy.Stub mHapBinder = new IHandsetApplicationProxy.Stub() 
    {

        /**
         * Register a nomadic app with the Handset Application Proxy (HAP).  
         * A single nomadic app can call this function multiple times, each time a different
         * connection ID will be returned.  In essence, calling this more than once is viewed
         * as the application re-registering.  Any previous connection ID assigned to the
         * handset application is considered to be released. 
         *
         * @param appName Unique handset application name.  
         * Java package naming conventions (e.g. com.company.product) are recommended.
         *
         * @param schemaVer Version of the MIP schema used by the handset application.  The
         * MIP schema version is a version maintained by the handset application vendor that changes
         * when new MIP messages/features are exposed in the handset application.
         *
         * @param actName Full name of the base activity of the handset application that
         * HAP can use to start the handset application.
         *
         * @param callback Callback interface used by HAP to pass messages to the handset application.
         *
         * @return Upon success, a unique connection ID that HAP uses to identify the handset
         * application, -1 otherwise.
         */        
        public int aqHapInit( String appName, String schemaVer, String actName, IHapCallback callback) throws RemoteException 
        {

            //synchronized(mLock) 
            //{
                
                int connectionId = -1;
                Log.d(TAG, "appName="+appName +" mipSchemaVer="+schemaVer+" ActName="+actName+" app="+appManager);

                if( (null==appName)   || (appName.length()   < 1) ||
                    (null==schemaVer) || (schemaVer.length() < 1) ||
                    (null==actName)   || (actName.length()   < 1) ||
                    (null==callback) ) {
                    Log.e(TAG, "aqHapInit-bad arguments" );
                    return -1;
                }
                                    
                if(null==appManager){
                    Log.d(TAG, "applicationManager is null " );
                    return -1;
                }
                    
                connectionId = appManager.addApplication( appName.toLowerCase(Locale.US), schemaVer, actName, callback);
                Log.d(TAG, "connectionId = " + connectionId );

                sendCurrentConnectionStateToCallback(callback);
                
                return connectionId;
            //}
        }
        
        /**
         * Sends a message from nomadic app to head unit.
         * @param connectionId Connection ID provided to the handset application when it registered with HAP.
         * @param sequenceNumber
         * @param payload
         * @param contentType
         */        
        public boolean aqSendMsg( int connectionId, int sequenceNumber, byte[] payload, String contentType ) throws RemoteException
        {
            Log.d( TAG, "aqSendMsg() connectionId: " + connectionId + ", sequenceNumber: " + sequenceNumber + ", contentType: " + contentType );
            if( (null!=payload) && (null!=contentType) && (contentType.endsWith("json") || contentType.endsWith("xml")) )
                Log.d( TAG, "payload: " + new String( payload ) );

            String appName = appManager.getApplicationName( connectionId );
            ApplicationMessage msg = new ApplicationMessage( appName, sequenceNumber, payload, contentType );
            Log.d( TAG, "Msg="+msg );
                    
            try
            {
                if( 0 == sequenceNumber)
                    A.a().qLongPolling.put( msg );
                else
                {
                    LinkedBlockingQueue<ApplicationMessage> q = A.a().mapQueues.get(sequenceNumber);
                    if( null != q )
                        q.put(msg);
                    else
                        Log.e(TAG, "Invalid Sequance Number:"+sequenceNumber );
                }
                return true;//commandHandler.processMessageFromApplication( msg );
            }
            catch( Exception e )
            {
                Log.e(TAG, " ", e);
                return false;
            }
        }
    };
    
    
    /**
    * Send current HUP<->HAP (bluetooth) connection state to given nomadic app. 
    * @param callback : nomadic app's callback to use to call onHapConnectionStateChange(state) 
    */
    private void sendCurrentConnectionStateToCallback( IHapCallback callback )
    {
        if( null == callback )
        {
            Log.e( TAG, "sendCurrentConnectionStateToCallback(), null callback" );
            return;
        }
            
        try
        {
            int state = A.a().getHuConState() ? 0 : 1;  // (0=connected, 1=disconnected)
            Log.d( TAG, "Sent connection state: " + state );     
            callback.onHapConnectionStateChange( state );   // TODO: sometimes we get an exception here: thread is already started.
        }
        catch( Exception e )
        {
            Log.e( TAG, "", e );
        }
    }

	
	/**
     * Tell all nomadic apps that Agent-HU connection state has changed.
     * @param is_connected : new BT connection state to the Head Unit as defined by BtConManager.STATE_
     */
    private void setConState( boolean is_connected )
    {
        // Notify all nomadic apps.
        for( IHapCallback c : appManager.getAllCallbacks() )
            sendCurrentConnectionStateToCallback( c );
    }
}
