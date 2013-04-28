/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *
 *****************************************************************************/
package com.airbiquity.application.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.airbiquity.hap.A;

/**
 * Handset information.
 */
public class HandsetInfo
{
    private static final String TAG = "HandsetInfo";
    
    public final String make;
    public final String model;
    public final String osName;
    public final String osVersion;
    public final String hapVersion;


    /**
     * 
     * @param make
     * @param model
     * @param osName
     * @param osVersion
     * @param hapVersion
     */
    public HandsetInfo()
    {
        make = Build.MANUFACTURER;
        model =  Build.MODEL;
        osName = "Android";
        
        //osVersion = "2.3"; // Build.VERSION.RELEASE;
        osVersion = ""+Build.VERSION.SDK_INT;
        
        hapVersion = getAgentVersion();
    }
    

    /**
     * Retrieves the version of this application/agent/HAP from the OS.
     * 
     * @return The agent version is successfully determined from the OS, otherwise "UNKNOWN".
     */
    private String getAgentVersion()
    {
        try
        {
            PackageManager manager = A.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo( A.getContext().getPackageName(), 0 );
            return ""+info.versionCode;
        }
        catch( NameNotFoundException e )
        {
            Log.e( TAG, "", e );
        }
        return "UNKNOWN";
    }

    
    /**
     * Get JSON representation of this object.
     * @return
     */
    public JSONObject toJson()
    {
        JSONObject j = new JSONObject();
        try
        {
            j.put( "make", make );
            j.put( "model", model );
            j.put( "osName", osName );
            j.put( "osVersion", osVersion );
            j.put( "hapVersion", hapVersion );
        }
        catch( JSONException e )
        {
            return null;
        }
        return j;
    }
}
