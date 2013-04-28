/*****************************************************************************
 *
 *                      AIRBIQUITY PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to Airbiquity
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from Airbiquity.
 *
 *            Copyright (c) 2012 by Airbiquity.  All rights reserved.
 *            
 *                   @author 
 *                   Jack Li
 *
 *****************************************************************************/
package com.airbiquity.mcs.http.android;

import java.util.Locale;

/**
 * Utility class for manipulating HTTP requests and responses
 */
public class HttpUtils {

	/**
	 * Retrieves URL path from a URL removing leading "http://" and host name 
	 * @param url full URL containing host name and "http://"
	 * @return
	 */
	public static String extractUrlPath(String url) {
		if( url != null ) {
			String uri = url.toLowerCase(Locale.US);
			if( uri.startsWith("http://") ) {
				uri = uri.substring( 7 );
				int idx = uri.indexOf('/');
				if( idx > 0 ) {
					uri = uri.substring(idx);
					return uri;
				}
			}
		}
		return url;
	}
	
	/**
	 * Converts a response into array of bytes
	 * @param resp HTTP response
	 * @return the response as an array of bytes
	 */
	public static byte[] getBytes(Response resp) {
		if( resp.SendOnlyData ) {
			byte[] data = resp.Data;
			return data;
		}
		
		// Build HTTP text
		StringBuilder bld = new StringBuilder("HTTP/1.0 ");
		
		// Get response line
		bld.append( resp.Code );
		bld.append(' ');
		bld.append(resp.Phrase);
		bld.append("\r\n");
		
		// Get header lines
		if( resp.ContentType.length() > 0 ) {
			bld.append("Content-Type: ");
			bld.append(resp.ContentType);
			bld.append("\r\n");
		}
		if( resp.IsLast ) {
			bld.append("Content-Length: ");
			bld.append(resp.Data.length + "\r\n");
		}
		if( resp.AdditionalHeaders != null ) {
			for(int i = 0; i < resp.AdditionalHeaders.length; i++) {
				bld.append(resp.AdditionalHeaders[i]);
				bld.append("\r\n");
			}
		}

		bld.append("\r\n");
		String header = bld.toString();
		byte[] hdr = header.getBytes();
		
		// Get content
		if( resp.Data.length < 1 ) {
			// No contents - return the header
			return hdr;
		}
		int totalLen = hdr.length + resp.Data.length;
		byte[] res = new byte[totalLen];
		System.arraycopy(hdr, 0, res, 0, hdr.length);
		System.arraycopy(resp.Data, 0, res, hdr.length, resp.Data.length);
		return res;
	}

}
