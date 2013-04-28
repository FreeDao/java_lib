package com.airbiquity.hap;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.webkit.WebView;
import android.widget.Toast;

import com.airbiquity.application.manager.ApplicationMessage;
import com.airbiquity.connectionmgr.msp.PanAppManager;
import com.airbiquity.db.DbApps;
import com.airbiquity.db.DbCars;
import com.airbiquity.util_net.TaskOnHuCon;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;


/**
 * This is a preferred pattern to be used instead of regular singletons, because regular singletons don't work reliably on Android.
 * Naming Application class "A" is a common Android practice. (Similar to the "R" class) It is short and used in many places.  
 *
 */
public class A extends Application
{
    private static final String TAG = "A";
    private static final String KEY_CUR_COUNTRY_ID          = "A.KEY_CUR_COUNTRY_ID";
    private static final String KEY_CUR_COUNTRY_NEED_ZIP    = "A.KEY_CUR_COUNTRY_NEED_ZIP";
    private static final String KEY_CUR_COUNTRY_IS_NOP      = "A.KEY_CUR_COUNTRY_IS_NOP";
    private static final String KEY_CUR_COUNTRY_IS_LINKAWAY = "A.KEY_CUR_COUNTRY_IS_LINKAWAY";
    private static final String KEY_MIP_ID           = "A.KEY_MIP_ID";
    private static final String KEY_AUTH_TOKEN       = "A.KEY_AUTH_TOKEN";
    public static final String KEY_COUNTRIES        = "A.KEY_COUNTRIES";
    //public static final String KEY_APP_LIST         = "A.KEY_APP_LIST";
    public static final String KEY_MISC             = "A.KEY_MISC";    
    public static final String KEY_TERMS            = "A.KEY_TERMS";
    public static final String KEY_TERMS_ACCEPTED   = "A.KEY_TERMS_ACCEPTED";
    
    /** 1 hour worth of milliseconds. */
    public static final long MILS_HOUR = 1000 * 60 * 60;  

    /** 1 day worth of milliseconds. */
    public static final long MILS_DAY = MILS_HOUR * 24;  
    
    //private static final String GA_ID = "UA-35935879-1"; // TODO: Change this before production.
    private static A inst;  // Instance.
    public SharedPreferences.Editor  cfgEdProg; // Program preferences.
    public SharedPreferences         cfgProg;   // Program preferences.
    
    public DbApps dbApps;
    public DbCars dbSubs;
    
    private HashSet<HuConStateListener> huConStateListeners = new HashSet<HuConStateListener>(); 
    
    /** Current Bluetooth connection state to the Head Unit (true=connected, false=disconnected) */  
    private boolean connected;

    /** For Panasonic only: If we just connected to HU but didn't update the screen yet. */
    public boolean isPanHuJustConnected;

    /** True if user just answered YES to "Are you the owner" dialog. */
    public boolean isOwner;

    /** Font for this brand: Bold */
    Typeface fontBold;
    
    /** Font for this brand: Regular */
    Typeface fontNorm;

    /** 
     * Map of queues for sending reply messages form nomadic apps to back to the head unit.
     * The key of the map is the sequence number and the value is the queue.
     * Each queue is used to send one reply message form a nomadic app back to the head unit.
     * For each request from the HU we create a new empty queue, forward the request to nomadic app 
     * and get blocked on the queue until a reply message from the nomadic app appears in the queue, 
     * then we send the message to HU and delete the queue.
     * */
    public final ConcurrentHashMap<Integer, LinkedBlockingQueue<ApplicationMessage>> mapQueues = new ConcurrentHashMap<Integer, LinkedBlockingQueue<ApplicationMessage>>();
    
    /** Queue for sending asynchronous messages form nomadic apps to head unit using long-polling. */
    public final LinkedBlockingQueue<ApplicationMessage> qLongPolling = new LinkedBlockingQueue<ApplicationMessage>();

    /** WebView that runs our JS apps. */ 
    public WebView webView;

    /** Panasonic App Manager */
    public PanAppManager panAppManager;

    /** Currently connected Head Unit information. */
    public MetaHuInfo curHuInfo;

    /** Information about the last connected Head Unit. */
    public MetaHuInfo lastHuInfo;
    
    /** Google Analytics EasyTracker */
    public Tracker tr;
    
    /** Handler to run JS on UI thread. */
    private Handler h;

        

    @Override
    public void onCreate()
    {
        super.onCreate();
        inst = this;
        
        h = new Handler();
        
        fontNorm = Typeface.createFromAsset(A.getContext().getAssets(), "fonts/"+BrandConst.fontNorm );
        fontBold = Typeface.createFromAsset(A.getContext().getAssets(), "fonts/"+BrandConst.fontBold );

        // EasyTracker needs a context before calls can be made.
        EasyTracker.getInstance().setContext( getApplicationContext() );
        tr = EasyTracker.getTracker();
        tr.trackEvent("MyCategory", "MyAction", "MyLabel", 0L );

        
        // Create configuration objects.
        cfgProg = getSharedPreferences( "ProgPref", Activity.MODE_PRIVATE );
        cfgEdProg = cfgProg.edit();        
        
        dbApps = new DbApps( getContext() );
        dbSubs = new DbCars( getContext() );
        
        webView = new WebView( getApplicationContext() ); // Create it here because it needs to be created on UI thread. Or is it?
        panAppManager = new PanAppManager();

        // Start the MIP service.
        // we will start service when user successfully login
        // startService( new Intent( "com.airbiquity.hap.AgentService" ) );
    }

    /**
     * Get the Application class instance.
     * Naming it "a" is a common Android practice. It is short and used in many places.
     */
    public static A a()
    {
        return inst; 
    }

    /**
     * Get App context.
     * This is a prefered context if you don't need an activity context.
     * See http://stackoverflow.com/questions/987072/using-application-context-everywhere for details.
     * @return App context.
     */
    public static Context getContext()
    {
        return inst.getApplicationContext();
    }

    
    /**
     * Get application folder.
     * @return application folder.
     */
    public static File getAppDir()
    {
        return getContext().getFilesDir();         
    }
    
    
    /**
     * Show a toast.
     * @param msg : text to show.
     */
    public static void toast( String msg )
    {
        Toast.makeText( getContext(), msg, Toast.LENGTH_LONG ).show();
    }


    /**
     * Show a toast.
     * @param idMsg : ID of the string to show.
     */
    public static void toast( int idMsg )
    {
        String msg = inst.getString( idMsg );
        Toast.makeText( getContext(), msg, Toast.LENGTH_LONG ).show();
    }

    
    /**
     * Gets the language code for default Locale or "en" if no language was set.
     * @return it.
     */
    public static String getUserLanguage()
    {
        //String langDefault = Locale.getDefault().getLanguage();
        
    	// TODO: change me back once Choreo supports other languages
    	// String langDefault = inst.getResources().getConfiguration().locale.getLanguage();
        // return langDefault.length() == 0 ? "en" : langDefault;
    	return "en";
    }

    
    /**
     * Get user's Google emails.
     * @return
     */
    public static LinkedList<String> getUserEmails()
    {
        AccountManager manager = AccountManager.get( getContext() );
        Account[] accounts = manager.getAccountsByType( "com.google" );
        LinkedList<String> emails = new LinkedList<String>();
        
        for( Account account : accounts )
        {
            if( Patterns.EMAIL_ADDRESS.matcher( account.name ).matches() )
                emails.add( account.name );
        }        
        
        return emails;
    }

    
    /**
     * Get user's ISO 3166-1 2-letter country code from the phone.
     * See http://stackoverflow.com/questions/3659809/where-am-i-get-country
     * @return
     */
    public static String getDefaultCountry()
    {
        // From TelephonyManager -----------------------------------------------------------------
        TelephonyManager tm = (TelephonyManager)inst.getSystemService(Context.TELEPHONY_SERVICE);
        String c_from_tel = tm.getSimCountryIso();
        Log.i( TAG, "country from TelephonyManager = "+c_from_tel );
        if( null != c_from_tel  &&  2 == c_from_tel.length()  )
            return c_from_tel;

        // LocationManager -----------------------------------------------------------------------
        try
        {
            Location loc = getLocation();
            if( null != loc )
            {
                Geocoder geocoder = new Geocoder(inst, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation( loc.getLatitude(), loc.getLongitude(), 1);
                String c_from_loc = addresses.get(0).getCountryCode().toLowerCase(Locale.US);
                Log.i( TAG, "country from LocationManager = "+c_from_loc );
                if( null != c_from_loc  &&  2 == c_from_loc.length()  )
                    return c_from_loc;
            }
        }
        catch( Exception e )
        {
            Log.e( TAG, "", e );
        }
        
        // From locale ---------------------------------------------------------------------------
        String c_from_locale = inst.getResources().getConfiguration().locale.getCountry().toLowerCase(Locale.US);
        Log.i( TAG, "country from locale = "+c_from_locale );
        return c_from_locale;      
    }

    
    /**
     * Returns a Location indicating the data from the last known location fix.  
     * This can be done without starting the provider.  
     * Note that this location could be out-of-date, for example if the device was turned off and
     * moved to another location.
     * @return Location or null if provider is disabled.
     */
    public static Location getLocation()
    {
        LocationManager locationManager = (LocationManager) inst.getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        return loc;
    }
    
    
    /**
     * Save info that we get upon login: MIP-ID, Auth-Token.
     * @param mipId
     * @param authToken
     */
    public static void setLoginInfo( String mipId, String authToken )
    {
        inst.cfgEdProg.putString( KEY_MIP_ID, mipId );
        inst.cfgEdProg.putString( KEY_AUTH_TOKEN, authToken );
        inst.cfgEdProg.commit();
    }

    
    /**
     * Get MIP ID from SharedPreferences.
     * @return MIP ID or "" if it's not set yet.
     */
    public static String getMipId()
    {
        return inst.cfgProg.getString( KEY_MIP_ID, "" );
    }

    
    /**
     * Get Auth-Token from SharedPreferences.
     * @return Auth-Token or "" if it's not set yet.
     */
    public static String getAuthToken()
    {
        return inst.cfgProg.getString( KEY_AUTH_TOKEN, "" );
    }

    
    /**
     * Set user-selected country.
     * @param idCountry : 2-letter country ID.
     * @param isNeedZip : true if country requires Zip code to create account.
     */
    public static void setCountry( String idCountry, boolean isNeedZip, boolean isNop, boolean isLinkAway )
    {
        inst.cfgEdProg.putString( KEY_CUR_COUNTRY_ID            , idCountry );
        inst.cfgEdProg.putBoolean( KEY_CUR_COUNTRY_NEED_ZIP     , isNeedZip );
        inst.cfgEdProg.putBoolean( KEY_CUR_COUNTRY_IS_NOP       , isNop     );
        inst.cfgEdProg.putBoolean( KEY_CUR_COUNTRY_IS_LINKAWAY  , isLinkAway);
        inst.cfgEdProg.commit();
    }
    
    
    /**
     * Get user-selected country.
     * @return ISO 3166-1 2-letter country code or "" if not set yet.
     */
    public static String getCountryId()
    {
        return inst.cfgProg.getString( KEY_CUR_COUNTRY_ID, "" );
    }

    
    /**
     * Check if current country requires Zip code to create account.
     * @return true if so.
     */
    public static boolean isCountryNeedZip()
    {
        return inst.cfgProg.getBoolean( KEY_CUR_COUNTRY_NEED_ZIP, false );
    }
    
    /**
     * Check if current country has NOP.
     * @return true if so.
     */
    public static boolean isCountryNop()
    {
        return inst.cfgProg.getBoolean( KEY_CUR_COUNTRY_IS_NOP, false );
    }
    
    /**
     * Check if current country is Link-Away.
     * @return true if so.
     */
    public static boolean isCountryLinkAway()
    {
        return inst.cfgProg.getBoolean( KEY_CUR_COUNTRY_IS_LINKAWAY, false );
    }
    
    
    /**
     * Set current HU connection state and notify all the listeners.
     * @param is_connected : true if the Agent is currently connected to HU.
     */
    public void setHuConState( boolean is_connected )
    {
        // Check if the connection state has really changed. 
        if( connected == is_connected )
        {
            Log.d( TAG, "False alarm. Connection state is still: " + connected );
            return;
        }

        connected = is_connected;
        Log.d( TAG, "Agent<->HU connection state has changed to: " + connected );
        for( HuConStateListener l : huConStateListeners )
            l.conStateChanged( is_connected );
        
        if( connected )
        {
            TaskOnHuCon task = new TaskOnHuCon();
            task.execute();
        }
        else
        {
            curHuInfo = null;
        }
        
        // create queue for long polling events upon connection, and kill it otherwise
        if( connected )
        {
        	// clear long polling queue upon connection to clear out potentially
        	// stale events
        	qLongPolling.clear();
        } else {
        	// TODO: kind of hack-ish right now.  This was added to fix the case where
        	// a long polling handler was blocked on the queue after the Bluetooth
        	// connection went down.  Inserting this "poison" element should cause
        	// a blocked consumer to exit, but needs to be fixed by cleanly canceling
        	// any blocking threads upon disconnect with the head unit.
        	qLongPolling.offer( ApplicationMessage.createInvalidApplicationMessage() );
        }

        //startService( new Intent( "com.airbiquity.hap.AgentService" ) );
        // Set service state.
        String mode = connected ? HapService.ACTION_FOREGROUND : HapService.ACTION_BACKGROUND ;
        Intent intent = new Intent( mode );
        intent.setClass( this, HapService.class );
        startService( intent );
    }
    
    public boolean getHuConState()
    {
        return connected;
    }
    
    public void addHuConStateListener( HuConStateListener l )
    {
        huConStateListeners.add( l );
    }
    
    public void removeHuConStateListener( HuConStateListener l )
    {
        huConStateListeners.remove( l );
    }
    
    /**
     * TODO put this into A 
     * Load WebView URL on UI thread.
     * @param url : URL to load.
     */
    public void loadUrlOnUiThread( final String url )
    {
        Runnable r = new Runnable() { public void run() { A.a().webView.loadUrl( url ); } };
        h.post( r );
    }
}
