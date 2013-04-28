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


/**
 * This class contains main attributes of HTTP response such as
 * code (200, 404, ...), phrase ("OK", "Not Found", ...), content type,
 * data, is the last part of multi-part response and if only the data
 * should be sent (not the first part of multi-part response) 
 */
public class Response {
	public final String Phrase;
	public final int Code;
	public final String ContentType;
	public final byte[] Data;
	public final boolean IsLast;
	public boolean SendOnlyData = false;
	public String[] AdditionalHeaders = null;
	
	/**
	 * Constructs new instance of HTTP response
	 * @param phrase
	 * @param code
	 * @param contentType
	 * @param data
	 * @param isLast
	 */
	public Response(String phrase, int code, String contentType, byte[] data, boolean isLast) {
		this.Phrase = (phrase != null) ? phrase : "";
		this.Code = code;
		this.ContentType = (contentType != null) ? contentType : "";
		this.Data = (data != null) ? data : new byte[0];
		this.IsLast = isLast;
	}
}
