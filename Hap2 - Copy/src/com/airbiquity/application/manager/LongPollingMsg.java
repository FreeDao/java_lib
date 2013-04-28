package com.airbiquity.application.manager;

import java.util.List;

/**
 * A message that contain: app name and list of event IDs.
 */
public class LongPollingMsg
{
    /** App name */
    public final String appName;
        
    /** List of event IDs. */
    public final List<String> ids;


    /**
     * Constructs a message using the given arguments.
     * 
     * @param app_name : App name
     * @param listOfEventIds : list of event IDs.
     */
    public LongPollingMsg( String app_name, List<String> listOfEventIds )
    {
        appName = app_name;
        ids = listOfEventIds;
    }
}
