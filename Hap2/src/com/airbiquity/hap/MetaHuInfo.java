package com.airbiquity.hap;

import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.Json;

/**
 * Metadata for Head Unit information which is sent from HU.
 */
public class MetaHuInfo
{
    public static final String ID_HU_TYPE      = "headUnitType";
    public static final String ID_HU_SN        = "headUnitSerialNumber";
    public static final String ID_MAKE         = "vehicleMake";
    
    public static final String KEY_META = "com.airbiquity.hap.MetaUserVehicleSubscription";
    private final JSONObject j; // JSON Object that contains all the metadata for this object.
    
    public final String     type;
    public final String     sn;
    public final String     make;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaHuInfo( JSONObject jsonObj )
    {
        j = jsonObj;
        type  = Json.gs( j, ID_HU_TYPE       , ""    );
        sn    = Json.gs( j, ID_HU_SN         , ""    );
        make  = Json.gs( j, ID_MAKE          , ""    );
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
    static public MetaHuInfo valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaHuInfo( obj );
    }


    @Override
    public String toString()
    {
        return type + " " + sn + " " + make;
    }
}
