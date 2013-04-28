
package com.airbiquity.util_net;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.airbiquity.hap.A;

/**
 * Network request that contains: 
 * 1. URL.
 * 2. POST request content in JSON format (or NULL for GET requests).
 */
public class NetReq implements Parcelable
{
    public static final String KEY_REQ = "NetReq.KEY_REQ";
    
    /** Request URL */
    public final URL url;

    /** Response Message. */
    public final String post;
    
    public final LinkedList<HttpHeader> headers;  

    
    public int describeContents() { return 0; }

    public void writeToParcel(Parcel out, int flags) 
    {
        out.writeValue( url );
        out.writeString( post );
        out.writeList( headers );
    }

    public static final Parcelable.Creator<NetReq> CREATOR = new Parcelable.Creator<NetReq>() 
    {
        public NetReq createFromParcel(Parcel in) { return new NetReq(in); }
        public NetReq[] newArray(int size) { return new NetReq[size]; }
    };
    
    private NetReq(Parcel in) 
    {
        url =  (URL) in.readValue( URL.class.getClassLoader() );
        post = in.readString();
        headers = new LinkedList<HttpHeader> ();
        in.readList( headers, HttpHeader.class.getClassLoader() );
    }

    
    
    /**
     * Constructs a request with 1 header.
     * @param request_url : URL
     * @param post_content : POST request content in JSON format or null for GET requests.
     * @param header : 
     */
    public NetReq( URL request_url, String post_content, HttpHeader header )
    {
        url = request_url;
        post = post_content;
        headers = new LinkedList<HttpHeader>();
        headers.add( header );
    }

    /**
     * Constructs a request with given list of headers.
     * @param request_url : URL
     * @param post_content : POST request content in JSON format or null for GET requests.
     * @param http_headers : 
     */
    public NetReq( URL request_url, String post_content, List<HttpHeader> http_headers )
    {
        url = request_url;
        post = post_content;
        headers = new LinkedList<HttpHeader>( http_headers );        
    }

    /**
     * Constructs a request with headers: CONTENT_JSON, "Mip-Id", "Auth-Token" 
     * @param request_url : URL
     * @param post_content : POST request content in JSON format or null for GET requests.
     */
    public NetReq( URL request_url, String post_content )
    {
        url = request_url;
        post = post_content;
        headers = new LinkedList<HttpHeader>();
        headers.add( HttpHeader.CONTENT_JSON );
        headers.add( new HttpHeader( HttpHeader.NAME_MIP_ID    , A.getMipId()     ) );
        headers.add( new HttpHeader( HttpHeader.NAME_AUTH_TOKEN, A.getAuthToken() ) );
    }


    @Override
    public String toString()
    {
        return "NetReq: URL="+url+" POST="+post+" headers="+headers;
    }
}
