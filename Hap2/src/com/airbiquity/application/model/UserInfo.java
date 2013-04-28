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
 * User information.
 */
public class UserInfo
{
    public final String auth_token;
    public final String mip_id;


    /**
     * constructor
     * @param mip_id : MIP ID.
     */
    public UserInfo( String authToken, String mipId )
    {
        auth_token = authToken;
        mip_id     = mipId;
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
            j.put( "authenticationToken", auth_token );
            j.put( "mipId", mip_id );
        }
        catch( JSONException e )
        {
            return null;
        }
        return j;
    }
}
