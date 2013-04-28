package com.airbiquity.application.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.airbiquity.hap.A;
import com.airbiquity.hap.IHapCallback;

/**
 * Manages nomadic apps. An app is registered by addApplication().
 * Once registered the app will be persistently stored in a database. 
 * Callbacks are not persistently stored because they become invalid after service restart.
 */
public class ApplicationManager
{
    private static final String HAP = "HAP";
    private final Context context;
    private static ApplicationManager instance = null;

    // TODO: use only one app storage instead of the 2 below:
    
    /** Non-persistent Map of the Registered Applications. Map key is the connection ID. 
     * (Callbacks are not persistently stored because they become invalid after service restart.) */
    private Map<Integer, RegisteredApplication> registeredApplications = new ConcurrentHashMap<Integer, RegisteredApplication>();

    /** Persistent database of registered apps. */
    private ApplicationDatabaseAdapter databaseAdapter = null;


    /**
     * 
     */
    private ApplicationManager()
    {
        context = A.getContext();
    }


    public static ApplicationManager getInstance()
    {
        if( null == instance )
            instance = new ApplicationManager();
        return instance;
    }


    public void initialize()
    {
        databaseAdapter = new ApplicationDatabaseAdapter( context );
        databaseAdapter.open();
    }


    public void deinitialize()
    {
        databaseAdapter.close();
        databaseAdapter = null;
    }


    /**
     * Adds (registers) an application.
     * @param name : name of the application to be added.
     * @param version : MIP Schema Version
     * @param actName : Activity to be started to launch the nomadic app.
     * @param callback : callback to the nomadic app.
     * @return valid connection ID if the application was successfully added, otherwise -1.
     */
    public int addApplication( String name, String version, String actName, IHapCallback callback )
    {
        Integer connectionId = Integer.valueOf( -1 );
        RegisteredApplication newApplication = new RegisteredApplication( name, version, actName, callback );
        if( newApplication.isValid() )
        {
            // check if the application is already registered
            for( Map.Entry<Integer, RegisteredApplication> entry : registeredApplications.entrySet() )
            {
                RegisteredApplication application = entry.getValue();
                if( (application != null) && application.isValid() )
                {
                    if( application.getName().equalsIgnoreCase( newApplication.getName() ) )
                    {
                        // this application is already registered, overwrite it in case the
                        // other parameters (e.g. version) changed and return the same connection ID
                        entry.setValue( newApplication );
                        connectionId = entry.getKey();
                        break;
                    }
                }
            }
            // if it wasn't already registered, add it
            if( connectionId.intValue() == -1 )
            {
                connectionId = getNextConnectionId();
                registeredApplications.put( connectionId, newApplication );
            }
            // now add it to the database, it will be overwritten if it already exists
            Log.d("TAG", newApplication.getName() + newApplication.getVersion()+ newApplication.getBaseActivity() );
            databaseAdapter.insert( newApplication.getName(), newApplication.getVersion(), newApplication.getBaseActivity() );
        }
        return connectionId.intValue();
    }


    /* *
     * Removes the application if it exists.
     * @param name name of the application to remove
     * /
    public void removeApplication( String name )
    {
        if( (name != null) && (name.length() > 0) )
        {
            Integer removeKey = Integer.valueOf( -1 );
            // check if the application is registered
            for( Map.Entry<Integer, RegisteredApplication> entry : registeredApplications.entrySet() )
            {
                RegisteredApplication application = entry.getValue();
                if( (application != null) && application.isValid() )
                {
                    if( application.getName().equalsIgnoreCase( name ) )
                    {
                        removeKey = entry.getKey();
                        break;
                    }
                }
            }
            // remove the application if it is registered
            if( removeKey.intValue() != -1 )
            {
                registeredApplications.remove( removeKey );
            }
            databaseAdapter.remove( name );
        }
    }*/


    /* *
     * 
     * @param connectionId
     * @return
     * /
    public IHapCallback getCallback( int connectionId )
    {
        RegisteredApplication application = registeredApplications.get( Integer.valueOf( connectionId ) );
        return ((null != application) && application.isValid()) ? application.getCallback() : null;
    }*/


    /**
     * Get callback by app name.
     * @param name : app name.
     * @return callback.
     */
    public IHapCallback getCallback( String name )
    {
        IHapCallback callback = null;
        // check if the application is registered
        for( Map.Entry<Integer, RegisteredApplication> entry : registeredApplications.entrySet() )
        {
            RegisteredApplication application = entry.getValue();
            if( (application != null) && application.isValid() )
            {
                if( application.getName().equalsIgnoreCase( name ) )
                {
                    callback = application.getCallback();
                    break;
                }
            }
        }
        return callback;
    }


    public ArrayList<IHapCallback> getAllCallbacks()
    {
        ArrayList<IHapCallback> allCallbacks = new ArrayList<IHapCallback>();
        // check if the application is registered
        for( Map.Entry<Integer, RegisteredApplication> entry : registeredApplications.entrySet() )
        {
            RegisteredApplication application = entry.getValue();
            if( (application != null) && application.isValid() )
            {
                allCallbacks.add( application.getCallback() );
            }
        }
        return allCallbacks;
    }


    /**
     * Get all registered apps.
     * @return Collection of all registered apps.
     */
    public Collection<RegisteredApplication> getApps()
    {
        return registeredApplications.values();
    }


    /**
     * Returns the registered application with the specified connection ID.
     * @param connectionId Connection ID to search for.
     * @return The registered application if found, otherwise null.
     */
    public RegisteredApplication getApplication( int connectionId )
    {
        return registeredApplications.get( Integer.valueOf( connectionId ) );
    }


    /* *
     * Searches for the registered application with the specified name.
     * @param name Application name to search for.
     * @return The registered application if found, otherwise null.
     * /
    public RegisteredApplication getApplication( String name )
    {
        // check if the application is registered
        for( Map.Entry<Integer, RegisteredApplication> entry : registeredApplications.entrySet() )
        {
            RegisteredApplication application = entry.getValue();
            if( (application != null) && application.isValid() && application.getName().equalsIgnoreCase( name ) )
            {
                return application;
            }
        }
        return null;
    }*/


    /**
     * Returns the next connection ID available for use in the collection of registered applications.
     * @return next available connection ID
     */
    private Integer getNextConnectionId()
    {
        // find the largest connection ID (key) in the collection of registered applications
        // and increment it, or 1 if the collection is empty
        if( registeredApplications.isEmpty() )
        {
            return Integer.valueOf( 1 );
        }
        else
        {
            Set<Integer> keys = registeredApplications.keySet();
            Integer[] connectionIds = (Integer[]) keys.toArray( new Integer[keys.size()] );
            Arrays.sort( connectionIds );
            return Integer.valueOf( connectionIds[connectionIds.length - 1].intValue() + 1 );
        }
    }


    /**
     * Start a nomadic app by its name.
     * @param name : name of the nomadic app to launch.
     * @return true on OK.
     */
    public boolean launchApplication( String name )
    {
    	Log.d("TAG", "application name = "+name.toLowerCase(Locale.US));
    	name = name.toLowerCase(Locale.US);
        boolean retVal = false;
        String baseActivity = databaseAdapter.getBaseActivity( name );
        if( (null != baseActivity) && (baseActivity.length() > 0) )
        {
            Intent intent = new Intent();
            String packageName = getPackageName( name );
            if( packageName == null )
            {
                packageName = getPackageName( baseActivity );
            }
            Log.d("TAG", "packageName = " + packageName);
            Log.d("TAG", "baseActivity = " + baseActivity);
            intent.setClassName( packageName, baseActivity );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            try
            {
                Log.i( HAP, "Starting Application: name=" + name );
                context.startActivity( intent );
                retVal = true;
            }
            catch( ActivityNotFoundException e )
            {
                Log.e( "HAP", "Error Starting Application: name=" + name );
                databaseAdapter.remove( name );
            }
        }
        return retVal;
    }


    /**
     * Extract the package name from the base activity name.
     * @param baseActivity Fully qualified base activity class name
     * @return packageName
     */
    private String getPackageName( String baseActivity )
    {
        int index = baseActivity.lastIndexOf( "." );
        String packageName = null;
        if( index > 0 )
        {
            packageName = baseActivity.substring( 0, index );
        }
        return packageName;
    }


    public String getApplicationName( int connectionId )
    {
        RegisteredApplication application = getApplication( connectionId );
        return (null != application) ? application.getName() : null;
    }
}
