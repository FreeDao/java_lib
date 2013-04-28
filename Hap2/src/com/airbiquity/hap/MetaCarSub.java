package com.airbiquity.hap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.Json;

/**
 * Metadata for car and its subscriptions. (from BE call "User’s Vehicle Subscription")
 */
public class MetaCarSub
{
    public static final String ID_VEHICLE_ID    = "vehicleId";
    public static final String ID_NICKNAME      = "nickname";
    public static final String ID_MODEL         = "model";
    public static final String ID_YEAR          = "year";
    public static final String ID_IS_ACTIVATED  = "isActivated";                                                                 
    public static final String ID_HUID          = "huID";
    public static final String ID_VIN           = "VIN";
    public static final String ID_SUBSCRS       = "appSubscriptions";
    
    
    //public static final String KEY_META = "com.airbiquity.hap.MetaUserVehicleSubscription";
    
    public final int        id          ;
    public final String     nickname    ;
    public final String     model       ;
    public final int        year        ;
    public final boolean    isActivated ;
    public final String     huid        ;
    public final String     vin         ;
    public final int        subType     ;
    public final String     expDate     ;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaCarSub( JSONObject j ) throws JSONException
    {
        id          = Json.gi( j, ID_VEHICLE_ID     , 0     );
        nickname    = Json.gs( j, ID_NICKNAME       , ""    );
        model       = Json.gs( j, ID_MODEL          , ""    );
        year        = Json.gi( j, ID_YEAR           , 0     );
        isActivated = Json.gb( j, ID_IS_ACTIVATED   , false );
        huid        = Json.gs( j, ID_HUID           , ""    );
        vin         = Json.gs( j, ID_VIN            , ""    );
        String jSubs= Json.gs( j, ID_SUBSCRS        , ""    );
        
        ArrayList<MetaAppSub> subs = MetaAppSub.parseList( jSubs );
        MetaAppSub sub = null;
        for( MetaAppSub s : subs )
        {
            if( s.type == MetaAppSub.TYPE_PREMIUM || null == sub )
                sub = s;
        }
        if( null != sub )
        {
            subType = sub.type;
            expDate = sub.expDate;            
        }
        else
        {
            subType = MetaAppSub.TYPE_NONE;
            expDate = null;
        }
    }

    public MetaCarSub( 
            int        id          ,
            String     nickname    ,
            String     modl        ,
            int        year        ,
            boolean    isActivated ,
            String     huid        ,
            String     vin         ,
            int        subType     ,
            String     expDate     
            )
    {
        this.id          = id         ;
        this.nickname    = nickname   ;
        this.model        = modl       ;
        this.year        = year       ;
        this.isActivated = isActivated;
        this.huid        = huid       ;
        this.vin         = vin        ;
        this.subType     = subType    ;
        this.expDate     = expDate    ;
    }
    

    /**
     * Parse string containing JSON representation of this object.
     * @param strJson : string that contains this object in JSON format.
     * @return this object.
     * @throws JSONException if the content is invalid JSON.
     */
    static public MetaCarSub valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaCarSub( obj );
    }


    /**
     * Parse a list of this objects.
     * @param strJson : string that contains a list of this objects in JSON format.
     * @return List of this objects.
     * @throws JSONException if the content is invalid JSON.
     */
    static public ArrayList<MetaCarSub> parseList( String strJson ) throws JSONException
    {
        JSONObject joCountries = new JSONObject( strJson );
        JSONArray jaCountries = joCountries.getJSONArray( "vehicleSubscriptions" ); 
        ArrayList<MetaCarSub> metas = new ArrayList<MetaCarSub>( jaCountries.length() );
        for( int i = 0; i < jaCountries.length(); i++ )
        {
            JSONObject jo = jaCountries.getJSONObject( i );
            MetaCarSub meta = new MetaCarSub( jo );
            metas.add( meta );
        }
        return metas;
    }


    @Override
    public String toString()
    {
        //return id + " " + name + " " + isNop + " " + isLinkAway + " " + isUserIdEmail;
        return nickname;
    }
}
