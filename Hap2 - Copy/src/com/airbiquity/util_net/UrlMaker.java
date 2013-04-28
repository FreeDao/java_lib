package com.airbiquity.util_net;

import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.airbiquity.hap.A;

/**
 * Set of methods that makes URLs for access Choreo.
 */
public class UrlMaker
{
    private static final String TAG = "UrlMaker";
    
    public static final String KEY_SERVER_URL = "KEY_SERVER_URL";
    
    public static final String DEFAULT_SERVER_URL = "http://nissanmip-mipgw-pre.viaaq.net:80/"; // pre-prod, HTTP, no security
    // public static final String DEFAULT_SERVER_URL = "https://nissanmip-mipgw-pre.viaaq.net:443/"; // pre-prod, HTTPS, full security
    // public static final String DEFAULT_SERVER_URL = "http://nissan-mipgw-test.viaaq.net:9022/"; // test, HTTP, no security
    // public static final String DEFAULT_SERVER_URL = "https://nissan-mipgw-test.viaaq.net:9025/"; // test, HTTPS, ? security
    // public static final String DEFAULT_SERVER_URL = "http://nissanmipdevgw.airbiquity.com:9018/"; // dev, HTTP, no security
    

    /**
     * Get Choreo URL for "Register".
     * @return URL.
     */
    public static URL getUrlForReg()
    {
        String strUrl = getBaseURL() + "account_services/api/1.0/account";
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Login".
     * @return URL.
     */
    public static URL getUrlForLogin()
    {
        String strUrl = getBaseURL() + "account_services/api/1.0/account/login";
        return str2url( strUrl );
    }
    
    /**
     * Get Choreo URL for "Logout".
     * @return URL.
     */
    public static URL getUrlForLogout()
    {
        String strUrl = getBaseURL() + "account_services/api/1.0/account/logout";
        return str2url( strUrl );
    }


    
    /**
     * Get Choreo URL for "GetCountries".
     * @return URL.
     */
    public static URL getUrlForGetCountries()
    {
        // The language is hard-coded to "en" because it's unused anyway.
        String strUrl = getBaseURL() + "account_services/api/1.0/static_content/countries/client_type/3/language/en";
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Miscellaneous Content".
     * @return URL.
     */
    public static URL getUrlForMisc()
    {
        String idCountry = A.getCountryId();       
        String strUrl = getBaseURL() + "account_services/api/1.0/static_content/misc/client_type/3/country/"+idCountry;
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Terms and Conditions".
     * @return URL.
     */
    public static URL getUrlForTerms()
    {
        String idCountry = A.getCountryId();        
        String idLang    = A.getUserLanguage();        
        String strUrl = getBaseURL() + "account_services/api/1.0/static_content/terms_and_conditions/client_type/3/country/"+idCountry+"/language/"+idLang;
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Get App List" (user and HU agnostic)
     * @return URL.
     */
    public static URL getUrlForGetAppList()
    {
        String idCountry = A.getCountryId();        
        String idLang    = A.getUserLanguage();        
        String strUrl = getBaseURL() +"account_services/api/1.0/static_content/apps/client_type/3/country_code/"+idCountry+"/language_code/"+idLang;
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Get User’s Vehicle Subscriptions"
     * @return URL.
     */
    public static URL getUrlForGetUsersVehicleSubscriptions()
    {
        String strUrl = getBaseURL() +"account_services/api/1.0/subscription/vehicle_subscriptions";
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Get Vehicle Information"
     * @param huid : Head Unit ID.
     * @return URL.
     */
    public static URL getUrlForGetCarInfo( String huid )
    {
        String strUrl = getBaseURL() +"account_services/api/1.0/connected_account_services/vehicle/hu_id/"+huid;
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "Add Vehicle"
     * @return URL.
     */
    public static URL getUrlForAddCar()
    {
        String strUrl = getBaseURL() +"account_services/api/1.0/vehicle";
        return str2url( strUrl );
    }
    
    
    /**
     * Get Choreo URL for "Request Subscription Cancellation and Vehicle Removal"
     * @return URL.
     */
    public static URL getUrlForRequestSubCancel()
    {
        String strUrl = getBaseURL() +"account_services/api/1.0/subscription/subscription_and_vehicle/cancellation_request";
        return str2url( strUrl );
    }
    
    
    /**
     * Get Choreo URL for "Vehicle Activation"
     * @return URL.
     */
    public static URL getUrlForActivateCar()
    {
        String strUrl = getBaseURL() +"account_services/api/1.0/connected_account_services/vehicle_activation";
        return str2url( strUrl );
    }

    
    /**
     * Get Choreo URL for "HU Profile Update".
     * @return URL.
     */
    public static URL getUrlForHuProfile()
    {
        String strUrl = getBaseURL() + "mip_services/core/api/1.0/profile";
        return str2url( strUrl );
    }

    
    /**
     * Convert string to URL
     * @param strUrl
     * @return URL
     * @throws IllegalArgumentException if given string could not be parsed as a URL.
     */
    public static URL str2url( String strUrl )
    {
        try
        {
            URL url = new URL( strUrl );
            return url;
        }
        catch( MalformedURLException e )
        {
            Log.e( TAG, "", e );
            throw new IllegalArgumentException( "str2url", e );
        }        
    }
    

    /**
     * Get base Choreo URL.
     */
    public static String getBaseURL()
    {
        String savedUrl = A.a().cfgProg.getString( KEY_SERVER_URL, "" );
        return savedUrl.length() > 0 ? savedUrl : DEFAULT_SERVER_URL;
    }
    
    /**
     * 
     * @param language
     * @param accept_format
     * @return
     */
    public static URL getUrlForTextToSpeech(String language,String accept_format){
    	
    	 String strUrl = getBaseURL() + "mip_services/core/api/1.0/speech/text_to_speech";
    	 StringBuilder urlBuilder = new StringBuilder();
    	 //TODO hardcode device id here, later will be changed
    	 urlBuilder.append( strUrl ).append( "?" ).append( "device_id=" )
         .append( "1234567890" ).append( "&language=" ).append( language )
         .append( "&accept_format=" ).append( accept_format );
         return str2url( urlBuilder.toString() );
    	
    }
    
    /**
     * 
     * @param language
     * @param accept_format
     * @return
     */
    public static URL getUrlForSpeechToText(String language,String audio_format, boolean isMultipleResults){
    	String strUrl = getBaseURL() + "mip_services/core/api/1.0/speech/speech_to_text";
    	StringBuilder urlBuilder = new StringBuilder();
    	urlBuilder.append(strUrl).append("?")
				  .append("device_id=").append("1234567890")
		          .append("&audio_content_type=").append(audio_format)
		          .append("&accept_language=").append(language)
		          .append("&multiple_results=").append(isMultipleResults);
    	
         return str2url(urlBuilder.toString() );
    	
    }

    /**
     * Get Choreo URL for "Time Sync".
     * @return
     */
	public static URL getUrlForTimeSync() {
		String strUrl = getBaseURL() + "mip_services/core/api/1.0/miscellaneous/time";
		return str2url(strUrl);
	}
}
