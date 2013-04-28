package com.airbiquity.util_net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.airbiquity.hap.A;
import com.airbiquity.hap.BrandConst;
import com.airbiquity.hap.MetaCarSub;

/**
 * Set of methods that make JSON messages.
 */
public class JsonMaker
{
    private static final String TAG = "JsonMaker";


    /**
     * Make "register" message.
        {
            "firstName": "b2cuser",
            "lastName": "b2cuserlast",
            "userId": "test@testemail.com",
            "password": "test",
            "countryCode": "us",
            "languageCode": "en",
            "brandId": 0,
            "termsConditionsInfo": {
                "termsConditionsAccepted": true,
                "termsConditionsRead": true
            }
        }
     * 
     * @param username
     * @param passwd
     * @return
     */
    public static String makeReg( String username, String passwd, String nameFirst, String nameLast, String zip )
    {
        try
        {
            String idCountry = A.getCountryId();

            JSONObject jsonTnCInfo = new JSONObject();
            jsonTnCInfo.put( "termsConditionsAccepted" , true );
            jsonTnCInfo.put( "termsConditionsRead"     , true );
            
            JSONObject jsonReq = new JSONObject(); 
            jsonReq.put( "userId", username );
            jsonReq.put( "password", passwd );
            jsonReq.put( "firstName", nameFirst );
            jsonReq.put( "lastName", nameLast );
            jsonReq.put( "countryCode", idCountry );            
            jsonReq.put( "languageCode", "en" );    // TODO: get real language.
            jsonReq.put( "brandId", BrandConst.BRAND_ID );
            jsonReq.put( "termsConditionsInfo", jsonTnCInfo );
            if( zip.length() > 0 )
                jsonReq.put( "postalCode", zip );
            
            return jsonReq.toString();        
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
            return ""+e.getMessage();
        }
    }
    

    /**
     * Make Sign-In message.
        "userId": "test1@test",
        "password": "test",
        "clientTypeId": 2,
        "countryCode": "us",
        "brandId": 1,
        "hapDeviceInfo": {
            "make":     "SAMSUNG",
            "model":    "GALAXY S3",
            "uniqueId": "1234567890124212",
            "carrier":  "ATT",
            "osId":     1,
            "osVersion": "1.0",
            "hapVersion": "1.0"
     * 
     * @param username
     * @param passwd
     * @return
     */
    public static String makeSignin( String username, String passwd )
    {
        try
        {
            TelephonyManager teleMgr = (TelephonyManager) A.getContext().getSystemService( Context.TELEPHONY_SERVICE );
            PackageManager manager = A.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo( A.getContext().getPackageName(), 0 );
            String idCountry = A.getCountryId();
            String androidId = "" + android.provider.Settings.Secure.getString(A.getContext().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

            JSONObject jsonDeviceInfo = new JSONObject();
            jsonDeviceInfo.put( "make"      , Build.MANUFACTURER );
            jsonDeviceInfo.put( "model"     , Build.MODEL );
            jsonDeviceInfo.put( "uniqueId"  , androidId );
            jsonDeviceInfo.put( "carrier"   , teleMgr.getNetworkOperatorName() );
            jsonDeviceInfo.put( "osId"      , 1 );
            jsonDeviceInfo.put( "osVersion" , ""+Build.VERSION.SDK_INT );
            jsonDeviceInfo.put( "hapVersion", ""+info.versionCode );
            
            JSONObject jsonReq = new JSONObject(); 
            jsonReq.put( "userId", username );
            jsonReq.put( "password", passwd );
            jsonReq.put( "clientTypeId", 3 );
            jsonReq.put( "countryCode", idCountry );
            jsonReq.put( "brandId", BrandConst.BRAND_ID );
            jsonReq.put( "hapDeviceInfo", jsonDeviceInfo );
            return jsonReq.toString();        
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
            return ""+e.getMessage();
        }
    }

//    /**
//     * Make JSON request message for "Get App List" (user and HU agnostic)
//     * @return
//     */
//    public static String makeAppList()
//    {
//        try
//        {            
//            JSONObject jsonReq = new JSONObject();
//            jsonReq.put( "client_type", 3 );
//            jsonReq.put( "countryCode", A.getCountry() );
//            jsonReq.put( "languageCode", A.getUserLanguage() );
//            return jsonReq.toString();        
//        }
//        catch( JSONException e )
//        {
//            Log.e( TAG, " ", e );
//            return ""+e.getMessage();
//        }
//    }
    
    
    public static String makeHuProfile()
    {
        try
        {
            PackageManager manager = A.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo( A.getContext().getPackageName(), 0 );

            JSONObject hupInfo = new JSONObject();
            hupInfo.put( "huID", "4310810133553172531L" );
            hupInfo.put( "huType", "DA2.2" );
            hupInfo.put( "hupPlatformVersion", "1.0" );
            hupInfo.put( "hupPlatformName", "Panasonic" );

            JSONObject handSetInfo = new JSONObject();
            handSetInfo.put( "osName"      , "ANDROID" );
            handSetInfo.put( "osVersion" , Build.VERSION.SDK_INT );
            handSetInfo.put( "hapVersion", info.versionCode );
            handSetInfo.put( "make"      , Build.MANUFACTURER );
            handSetInfo.put( "model"     , Build.MODEL );
            
            JSONArray appsList = new JSONArray();    // TODO: Put apps here. 
            
            JSONObject jsonReq = new JSONObject(); 
            
            jsonReq.put( "hupInfo", hupInfo );
            jsonReq.put( "handSetInfo", handSetInfo );
            jsonReq.put( "appList", appsList );  
            
            return jsonReq.toString();        
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
            return ""+e.getMessage();
        }
    }

    
    /**
     * Make JSON request for "Add Vehicle".
     * @param vin      
     * @param huType
     * @param nick
     * @param model
     * @param year
     * @return JSON.
     */
    public static String makeAddCar(  int year, String model, String nick, String vin )
    {
        try
        {
            JSONObject jsonReq = new JSONObject(); 
            
            jsonReq.put( "modelYear", ""+year   );  
            jsonReq.put( "model"    , model     );  
            jsonReq.put( "nickname" , nick  );  
            jsonReq.put( "VIN"      , vin       );
            
            return jsonReq.toString();        
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
            return ""+e.getMessage();
        }
    }
    
    /**
     * Make JSON request for "Request Subscription Cancellation and Vehicle Removal"
     * @param vehicleId
     * @return
     */
    public static String makeSubDeleteReq( int vehicleId )
    {
        try
        {
            JSONObject jsonReq = new JSONObject();             
            jsonReq.put( "vehicleId", ""+vehicleId   );  
            return jsonReq.toString();        
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
            return ""+e.getMessage();
        }
    }
    
    
    /**
     * Make JSON request for "Vehicle Activation".
     * @param huid      : Head Unit ID.
     * @param huType
     * @param nickname
     * @param model
     * @param year
     * @return JSON.
     */
    public static String makeActivateCar( String huid, String huType, String nickname, String model, int year )
    {
        try
        {
            JSONObject jsonReq = new JSONObject(); 
            
            jsonReq.put( "huID"     , huid );
            jsonReq.put( "huType"   , huType );
            jsonReq.put( "nickname" , nickname );  
            jsonReq.put( "model"    , model );  
            jsonReq.put( "modelYear", ""+year );  
            
            return jsonReq.toString();        
        }
        catch( Exception e )
        {
            Log.e( TAG, " ", e );
            return ""+e.getMessage();
        }
    }
    

    public static byte[] getLogoutMessage( Context context )
    {
        String mipId = A.getMipId();
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "mipId", mipId );
        }
        catch( JSONException e )
        {
            Log.e( TAG, "", e );
        }
        return reqJson.toString().getBytes();
    }
    

    /**
     * Get location.
     * @return location or "" if not available.
     */
    public static String getLocation()
    {
        Location loc = A.getLocation();
        if( null == loc )
            return "";
        JSONObject reqJson = new JSONObject();
        try
        {
            reqJson.put( "latitude", loc.getLatitude() );
            reqJson.put( "longitude", loc.getLongitude() );
        }
        catch( JSONException e )
        {
            Log.e( TAG, "", e );
        }
        return reqJson.toString();
    }
    
    /**
     * Make the Vehicle Activation Completion Event.
     * @param resultCode : vehicle activation result code
     * @return JSON string 
     */
    public static String makeVehicleActivationCompletionEvent(String resultCode)
    {
    	if( null == resultCode ) {
    		return "";
    	}
    	
    	JSONObject jsonObj = new JSONObject();
    	try
    	{
    		jsonObj.put( "event", "vehicleActivation" );
    		jsonObj.put( "result", resultCode );
    	}
        catch( JSONException e )
        {
            Log.e( TAG, "", e );
        }
        return jsonObj.toString();
    }
}
