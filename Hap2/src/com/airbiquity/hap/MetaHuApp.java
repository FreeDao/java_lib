package com.airbiquity.hap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.Json;

/**
 * HU app metadata.
 */
public class MetaHuApp implements Comparable<MetaHuApp>
{
    public static final String ID_APP_NAME = "appName";
    public static final String ID_APP_VER = "appVersion";
    public static final String ID_APP_DOWN_URL = "applicationDownloadURI";
    public static final String ID_ICON_DOWN_URL = "applicationIconDownloadURI";
    public static final String ID_CRC = "crc";
    public static final String ID_TYPE = "type";
    public static final String ID_ACTION = "action";
    public static final String ID_HAS_NOMADIC = "handsetAppProvided";
    
    public static final String KEY_META = "MetaHuApp.key";
    private final JSONObject j; // JSON Object that contains all the metadata for this asset.
    
    public final String name;
    public final String ver;
    public final String urlDownApp;
    public final String urlDownIcon;
    public final long   crc;
    public final String type;
    public final String action;
    public final boolean hasNomadic;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaHuApp( JSONObject jsonObj )
    {
        j = jsonObj;
        name = Json.gs( j, ID_APP_NAME, "" );
        ver = Json.gs( j, ID_APP_VER, "" );
        urlDownApp = Json.gs( j, ID_APP_DOWN_URL, "" );
        urlDownIcon = Json.gs( j, ID_ICON_DOWN_URL, "" );
        crc = Json.gl( j, ID_CRC, 0L );
        type = Json.gs( j, ID_TYPE, "" );
        action = Json.gs( j, ID_ACTION, "" );
        hasNomadic = Json.gb( j, ID_HAS_NOMADIC, false );
    }


    /**
     * Encodes this object as a compact JSON string.
     * @return representation of this object as a compact JSON string.
     */
    public String asString()
    {
        return j.toString();
    }


    /**
     * Parse JSON representation of this object.
     * @param strJson : string that contains this object in JSON format.
     * @return this object.
     * @throws JSONException if the content is not in a valid JSON format.
     */
    static public MetaHuApp valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaHuApp( obj );
    }


    /**
     * Parse a list of objects.
     * @param strJson : string that contains a list of objects in JSON format.
     * @return List of objects.
     * @throws JSONException if the content is not in a valid JSON format.
     */
    static public ArrayList<MetaHuApp> parseList( String strJson ) throws JSONException
    {
        JSONObject joList = new JSONObject( strJson );
        JSONArray jaList = joList.getJSONArray( "appList" ); 
        ArrayList<MetaHuApp> metas = new ArrayList<MetaHuApp>( jaList.length() );
        for( int i = 0; i < jaList.length(); i++ )
        {
            JSONObject jo = jaList.getJSONObject( i );
            MetaHuApp meta = new MetaHuApp( jo );
            metas.add( meta );
        }
        return metas;
    }


    @Override
    public int compareTo( MetaHuApp u )
    {
        return name.compareTo( u.name );
    }


    @Override
    public String toString()
    {
        return name;
    }
}
