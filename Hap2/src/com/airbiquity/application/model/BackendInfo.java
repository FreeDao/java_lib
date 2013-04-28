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
 * Backend (Choreo) info.
 */
public class BackendInfo
{
    public final String clientGatewayUrl;
    public final String mimicTetheringUrl;


    /**
     * constructor
     * @param clientGatewayUrl : Choreo Client Gateway URL.
     * @param mimicTetheringUrl : Choreo Mimic Tethering URL
     */
    public BackendInfo( String clientGatewayUrl, String mimicTetheringUrl )
    {
        this.clientGatewayUrl = clientGatewayUrl;
        this.mimicTetheringUrl = mimicTetheringUrl;
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
            j.put( "clientGatewayUrl", clientGatewayUrl );
            j.put( "mimicTetheringUrl", mimicTetheringUrl );
        }
        catch( JSONException e )
        {
            return null;
        }
        return j;
    }
}
