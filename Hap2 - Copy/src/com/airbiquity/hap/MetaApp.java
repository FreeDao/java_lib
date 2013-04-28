package com.airbiquity.hap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.airbiquity.util_net.Json;

/**
 * HU app metadata.
 */
public class MetaApp implements Comparable<MetaApp>
{
    public static final String ID_APP_ID        = "appId";
    public static final String ID_APP_NAME      = "appName";
    public static final String ID_COMPANY       = "companyName";
    public static final String ID_APP_DOWN_URLS = "downloadUrls";
    public static final String ID_ICON_DOWN_URL = "iconURL";
    public static final String ID_DISCRIPTION   = "description";
    public static final String ID_TYPE          = "subscriptionTypeId";
    public static final String ID_PACKAGE_NAME  = "appStoreCommand";
    public static final String ID_HAS_NOMADIC   = "downloadApp";
    public static final String ID_ICON          = "icon";
    public static final String ID_IS_ON         = "is_on";
    
    //public static final String KEY_META = "MetaApp.key";
    //private final JSONObject j; // JSON Object that contains all the metadata for this asset.
    
    public final long    id;
    public final String  name;
    public final String  company;
    public final String  urlDownApp;
    public final String  urlDownIcon;
    public final String  discript;
    public final int     type;
    public final String  packageName;
    public final boolean hasNomadic;
    public final Bitmap  icon;
    public final boolean isOn;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaApp( JSONObject j )
    {
        //j           = jsonObj;
        id          = Json.gl( j, ID_APP_ID         , 0  );
        name        = Json.gs( j, ID_APP_NAME       , "" );
        company     = Json.gs( j, ID_COMPANY        , "" );
        urlDownApp  = Json.gs( j, ID_APP_DOWN_URLS  , "" );
        urlDownIcon = Json.gs( j, ID_ICON_DOWN_URL  , "" );
        discript    = Json.gs( j, ID_DISCRIPTION    , "" );
        type        = Json.gi( j, ID_TYPE           , 0  );
        packageName = Json.gs( j, ID_PACKAGE_NAME   , "" );
        hasNomadic  = Json.gb( j, ID_HAS_NOMADIC    , false );
        icon = null;
        isOn        = Json.gb( j, ID_IS_ON          , false );
    }

    public MetaApp( 
            long    id           ,
            String  name         ,
            String  company      ,
            String  urlDownApp   ,
            String  urlDownIcon  ,
            String  discript     ,
            int     type         ,
            String  packageName  ,
            boolean hasNomadic   ,
            Bitmap  icon         ,
            boolean isOn         
            )
    {
        this.id          = id          ;
        this.name        = name        ;
        this.company     = company     ;
        this.urlDownApp  = urlDownApp  ;
        this.urlDownIcon = urlDownIcon ;
        this.discript    = discript    ;
        this.type        = type        ;
        this.packageName = packageName ;
        this.hasNomadic  = hasNomadic  ;
        this.icon        = icon        ;
        this.isOn        = isOn        ;
    }

    /**
     * Encodes this object as a compact JSON string.
     * @return representation of this object as a compact JSON string.
     */
    public String asString()
    {
        return "id="+id+" name="+name;
    }


    /**
     * Parse JSON representation of this object.
     * @param strJson : string that contains this object in JSON format.
     * @return this object.
     * @throws JSONException if the content is not in a valid JSON format.
     */
    static public MetaApp valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaApp( obj );
    }


    /**
     * Parse a list of objects.
     * @param strJson : string that contains a list of objects in JSON format.
     * @return List of objects.
     * @throws JSONException if the content is not in a valid JSON format.
     */
    static public ArrayList<MetaApp> parseList( String strJson ) throws JSONException
    {
        JSONArray jaList = new JSONArray( strJson );
        //JSONArray jaList = joList.getJSONArray( "appList" ); 
        ArrayList<MetaApp> metas = new ArrayList<MetaApp>( jaList.length() );
        for( int i = 0; i < jaList.length(); i++ )
        {
            JSONObject jo = jaList.getJSONObject( i );
            MetaApp meta = new MetaApp( jo );
            metas.add( meta );
        }
        return metas;
    }


    @Override
    public int compareTo( MetaApp u )
    {
        return name.compareTo( u.name );
    }


    @Override
    public String toString()
    {
        return name;
    }
}
