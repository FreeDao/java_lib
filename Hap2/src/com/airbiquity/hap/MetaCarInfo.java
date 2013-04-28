package com.airbiquity.hap;

import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.Json;

/**
 * Metadata for Car Info.
 */
public class MetaCarInfo
{
    public static final String ID_HUID          = "hu_id";
    public static final String ID_IS_ACTIVATED  = "isActivated";                                                                 
    public static final String ID_MODEL         = "model";
    public static final String ID_YEAR          = "modelYear";
    
    public static final String KEY_META = "com.airbiquity.hap.MetaCarInfo";
    private final JSONObject j; // JSON Object that contains all the metadata for this object.
    
    public final String     modl;
    public final int        year;
    public final boolean    isActivated;
    public final String     huid;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaCarInfo( JSONObject jsonObj )
    {
        j = jsonObj;
        huid        = Json.gs( j, ID_HUID           , ""    );
        isActivated = Json.gb( j, ID_IS_ACTIVATED   , false );
        modl        = Json.gs( j, ID_MODEL          , ""    );
        year        = Json.gi( j, ID_YEAR           , 0     );
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
     * Parse string containing JSON representation of this object.
     * @param strJson : string that contains this object in JSON format.
     * @return this object.
     * @throws JSONException if the content is invalid JSON.
     */
    static public MetaCarInfo valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaCarInfo( obj );
    }


    @Override
    public String toString()
    {
        //return id + " " + name + " " + isNop + " " + isLinkAway + " " + isUserIdEmail;
        return huid;
    }
}
