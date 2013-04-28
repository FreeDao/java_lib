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

/**
 * Application Information.
 */
public class AppInfo
{
    public final String appName;
    public final String appVersion;
    public final String appType;
    public final String pkgName;
    public final String downloadUrl;


    /**
     * Constructor for application information.
     * 
     * @param appName application name.
     * @param appVersion application version.
     * @param appType application type, handset or headunit.
     * @param pkgName application package name.
     * @param downloadUrl application download url.
     */
    public AppInfo( String appName, String appVersion, String appType, String pkgName, String downloadUrl )
    {
        this.appName = appName;
        this.appVersion = appVersion;
        this.appType = appType;
        this.pkgName = pkgName;
        this.downloadUrl = downloadUrl;
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
            j.put( "appName"    , appName );
            j.put( "appVersion" , appVersion );
            j.put( "appType"    , appType );
            j.put( "pkgName"    , pkgName );
            j.put( "downloadUrl", downloadUrl );
        }
        catch( JSONException e )
        {
            return null;
        }
        return j;
    }
}
