package com.airbiquity.util_net;

/**
 * Response to a network request that contains: 
 * 1. HTTP status code.
 * 2. Message.
 */
public class NetResp
{
    /** Response HTTP status code. */
    public final int code;

    /** Response Message. */
    public final byte[] data;


    /**
     * Constructor.
     * @param status_code : HTTP status (200=OK, 404=NotFound, ...)
     * @param response_data : response body.
     */
    public NetResp( int status_code, byte[] response_data )
    {
        code = status_code;
        data = response_data;
    }
    
    @Override
    public String toString()
    {
        return "NetResp: code="+code+" data="+ new String(data);
    }    
}
