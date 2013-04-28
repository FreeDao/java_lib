package com.airbiquity.hap;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.airbiquity.util_net.Json;

/**
 * Country's metadata.
 */
public class MetaCountry implements Comparable<MetaCountry>
{
    public static final String ID_COUNTRY_ID        = "country";            // ISO 3166-1 2-letter country code.
    public static final String ID_COUNTRY_NAME      = "countryName";        // Name of the country in the language specified in the request.
    public static final String ID_LANGS             = "languages";          // List of supported language codes. Each element is an ISO-639-1 2-letter code for language (e.g. en for English)
    public static final String ID_IS_NOP            = "isCountryNOP";       // Is this country served by a NOP?
    public static final String ID_IS_LINK_AWAY      = "isCountryLinkAway";  // Is this country served by a mobile portal accessed by a linkawayURL?
    public static final String ID_IS_USER_ID_EMAIL  = "isUserIdEmail";      // Is the userId an email address? In some NOPs userId is not an email address.
    public static final String ID_IS_NEED_ZIP       = "isZipRequired";      // TRUE if postal code is required for creating an account.
    
    public static final String KEY_META = "com.airbiquity.hap.meta_country";
    public final String id;
    public final String name;
    public final String langs;
    public final boolean isNop;
    public final boolean isLinkAway;
    public final boolean isUserIdEmail;
    public final boolean isNeedZip;


    /**
     * Construct this object from JSONObject.
     * @param j : a JSONObject
     * @throws JSONException
     */
    public MetaCountry( JSONObject j )
    {
        id              = Json.gs( j, ID_COUNTRY_ID, "" ).toLowerCase(Locale.US);
        name            = Json.gs( j, ID_COUNTRY_NAME, "" );
        langs           = Json.gs( j, ID_LANGS, "" );
        isNop           = Json.gb( j, ID_IS_NOP, false );
        isLinkAway      = Json.gb( j, ID_IS_LINK_AWAY, false );
        isUserIdEmail   = Json.gb( j, ID_IS_USER_ID_EMAIL, false );
        isNeedZip       = Json.gb( j, ID_IS_NEED_ZIP, false );
    }


    /**
     * Parse a Country.
     * @param strJson : string that contains a Country in JSON format.
     * @return MetaCountry.
     * @throws JSONException if the content is not in a valid JSON format.
     */
    static public MetaCountry valueOf( String strJson ) throws JSONException
    {
        JSONObject obj = new JSONObject( strJson );
        return new MetaCountry( obj );
    }


    /**
     * Parse a list of countries.
     * @param strJson : string that contains a list of countries in JSON format.
     * @return List of MetaCountry objects.
     * @throws JSONException if the content is not in a valid JSON format.
     */
    static public ArrayList<MetaCountry> parseList( String strJson ) throws JSONException
    {
        JSONObject joCountries = new JSONObject( strJson );
        JSONArray jaCountries = joCountries.getJSONArray( "countries" ); 
        ArrayList<MetaCountry> metas = new ArrayList<MetaCountry>( jaCountries.length() );
        for( int i = 0; i < jaCountries.length(); i++ )
        {
            JSONObject jo = jaCountries.getJSONObject( i );
            MetaCountry meta = new MetaCountry( jo );
            metas.add( meta );
        }
        return metas;
    }


    @Override
    public int compareTo( MetaCountry u )
    {
        return id.compareTo( u.id );
    }


    @Override
    public String toString()
    {
        //return id + " " + name + " " + isNop + " " + isLinkAway + " " + isUserIdEmail;
        return name;
    }
}
