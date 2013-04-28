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

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * HandsetProfile for HU.
 */
public class HandsetProfile
{
    public final UserInfo userInfo;
    public final HandsetInfo handsetInfo;
    public final List<AppInfo> appList;
    public final BackendInfo backendInfo;


    /**
     * Handset Profile.
     * @param userInfo
     * @param handsetInfo
     * @param appList
     * @param backendInfo
     */
    public HandsetProfile( UserInfo userInfo, HandsetInfo handsetInfo, List<AppInfo> appList, BackendInfo backendInfo )
    {
        this.userInfo = userInfo;
        this.handsetInfo = handsetInfo;
        this.appList = appList;
        this.backendInfo = backendInfo;
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
            JSONArray list = new JSONArray();
            for( AppInfo ai : appList )
                list.put( ai.toJson() );
            j.put( "userInfo", userInfo.toJson() );
            j.put( "handsetInfo", handsetInfo.toJson() );
            j.put( "appList", list );
            j.put( "backendInfo", backendInfo.toJson() );
        }
        catch( JSONException e )
        {
            return null;
        }
        return j;
    }
}
