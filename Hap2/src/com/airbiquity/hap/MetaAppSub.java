package com.airbiquity.hap;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.Json;

/**
 * Metadata for app subscriptions. (from BE call "User’s Vehicle Subscription":AppSubscriptionInfo)
 */
public class MetaAppSub
{
    public static final int TYPE_BASIC   = 0;
    public static final int TYPE_PREMIUM = 1;    
    public static final int TYPE_NONE    = 3;    
    
    public static final String ID_TYPE      = "type";
    public static final String ID_PRICE     = "price";
    public static final String ID_CURRENCY  = "currency";
    public static final String ID_TAX_TOTL  = "taxesTotal";                                                                 
    public static final String ID_OVRL_TOTAL= "overallTotal";
    public static final String ID_TAXES     = "taxes";
    public static final String ID_RENEWAL   = "renewalInfo";
    
    
    //public static final String KEY_META = "com.airbiquity.hap.MetaAppSub";
    
    public final int        type        ;
    public final double     price       ;
    public final String     currency    ;
    public final double     taxTotl     ;
    public final double     ovrlTotl    ;
    public final String     taxes       ;
    public final String     expDate     ;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaAppSub( JSONObject j ) throws JSONException
    {
        type        = Json.gi( j, ID_TYPE       , 0     );
        price       = Json.gd( j, ID_PRICE      , 0.0   );
        currency    = Json.gs( j, ID_CURRENCY   , ""    );
        taxTotl     = Json.gd( j, ID_TAX_TOTL   , 0.0   );
        ovrlTotl    = Json.gd( j, ID_OVRL_TOTAL , 0.0   );
        taxes       = Json.gs( j, ID_TAXES      , ""    );
        JSONObject jRen = j.getJSONObject( ID_RENEWAL );
        expDate = Json.gs( jRen, "expirationDate", ""    ); // Date in format: "MM/DD/YYYY"
    }

//    public MetaAppSub( 
//            int        type     ,
//            double     price    ,
//            String     currency ,
//            String     renewal  ,
//            double     taxTotl  ,
//            double     ovrlTotl ,
//            String     taxes   
//            )
//    {
//        this.type       = type    ;
//        this.price      = price   ;
//        this.currency   = currency;
//        this.renewal    = renewal ;
//        this.taxTotl    = taxTotl ;
//        this.ovrlTotl   = ovrlTotl;
//        this.taxes      = taxes   ;
//    }
    

    /**
     * Parse string containing JSON representation of this object.
     * @param strJson : string that contains this object in JSON format.
     * @return this object.
     * @throws JSONException if the content is invalid JSON.
     */
    static public MetaAppSub valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaAppSub( obj );
    }


    /**
     * Parse a list of this objects.
     * @param strJson : string that contains a list of this objects in JSON format.
     * @return List of this objects.
     * @throws JSONException if the content is invalid JSON.
     */
    static public ArrayList<MetaAppSub> parseList( String strJson ) throws JSONException
    {
        JSONArray ja = new JSONArray( strJson );
        ArrayList<MetaAppSub> metas = new ArrayList<MetaAppSub>( ja.length() );
        for( int i = 0; i < ja.length(); i++ )
        {
            JSONObject jo = ja.getJSONObject( i );
            MetaAppSub meta = new MetaAppSub( jo );
            metas.add( meta );
        }
        return metas;
    }
}
