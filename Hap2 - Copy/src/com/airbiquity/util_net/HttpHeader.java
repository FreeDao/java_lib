
package com.airbiquity.util_net;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * HTTP Header. 
 */
public class HttpHeader implements Parcelable
{
    public static final String NAME_CONTENT_TYPE = "Content-Type";
    public static final String NAME_MIP_ID = "Mip-Id";
    public static final String NAME_AUTH_TOKEN = "Auth-Token";
    public static final String NAME_ACCESS_KEY_ID = "Access-Key-Id";
    public static final String NAME_APP_TOKEN = "App-Token";
    
    public static final String VAL_TEXT = "text/plain";
    public static final String VAL_JSON = "application/json";
    public static final String VAL_BIN  = "application/octet-stream";
    
    public static final HttpHeader CONTENT_JSON = new HttpHeader(NAME_CONTENT_TYPE, VAL_JSON);
    public static final HttpHeader CONTENT_BIN  = new HttpHeader(NAME_CONTENT_TYPE, VAL_BIN );
    public static final HttpHeader CONTENT_TEXT = new HttpHeader(NAME_CONTENT_TYPE, VAL_TEXT);
    
    public final String name;
    public final String val;


    /**
     * Constructor.
     * @param header_name  : name  (like "Content-Type" )
     * @param header_value : value (like "application/json" )
     */
    public HttpHeader( String header_name, String header_value )
    {
        name = header_name;
        val = header_value;
    }

    @Override public int describeContents() { return 0; }

    @Override 
    public void writeToParcel(Parcel out, int flags) 
    {
        out.writeString( name );
        out.writeString( val  );
    }

    public static final Parcelable.Creator<HttpHeader> CREATOR = new Parcelable.Creator<HttpHeader>() 
    {
        public HttpHeader createFromParcel(Parcel in) { return new HttpHeader(in); }
        public HttpHeader[] newArray(int size) { return new HttpHeader[size]; }
    };
    
    private HttpHeader( Parcel in ) 
    {
        name = in.readString();
        val  = in.readString();
    }

    @Override
    public String toString()
    {
        return name+":"+val;
    }
}
