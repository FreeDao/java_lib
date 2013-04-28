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
package com.airbiquity.application.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.airbiquity.application.model.AppInfo;
import com.airbiquity.application.model.BackendInfo;
import com.airbiquity.application.model.HandsetInfo;
import com.airbiquity.application.model.HandsetProfile;
import com.airbiquity.application.model.UserInfo;
import com.airbiquity.hap.A;
import com.airbiquity.util_net.UrlMaker;

/**
 * Handset Profile Manager .
 */
public class HandsetProfileManager
{
    //private static final String TAG = "HandsetProfileManager";

	// TODO: Temp hardcoded Choreo URLs.  These need to be retrieved from Choreo based on region.
	//private static final String CHOREO_CLIENT_GATEWAY_URL = "https://mipportallab.airbiquity.com:9022";
	private static final String CHOREO_MIMIC_TETHERING_URL = "https://nissanmiptestsocks.airbiquity.com:9020";

    /**
     * Get handset profile.
     * @return HandsetProfile
     */
    public static HandsetProfile getHandsetProfile()
    {
        UserInfo userInfo = new UserInfo( A.getAuthToken(), A.getMipId() );                
        HandsetInfo handsetInfo = new HandsetInfo();
        BackendInfo backendInfo = new BackendInfo( UrlMaker.getBaseURL(), CHOREO_MIMIC_TETHERING_URL );
        HandsetProfile handsetProfile = new HandsetProfile( userInfo, handsetInfo, getAppList(), backendInfo );
        return handsetProfile;
    }


    /**
     * Get list of registered apps.
     * @return list of registered apps.
     */
    public static List<AppInfo> getAppList()
    {
        List<AppInfo> appList = new ArrayList<AppInfo>();
        ApplicationManager appMangr = ApplicationManager.getInstance();
        Collection<RegisteredApplication> apps = appMangr.getApps();
        for( RegisteredApplication app : apps )
        {
            AppInfo ai = new AppInfo( app.getName(), app.getVersion(), null, null, null );
            appList.add( ai );
        }
        return appList;
    }

}
