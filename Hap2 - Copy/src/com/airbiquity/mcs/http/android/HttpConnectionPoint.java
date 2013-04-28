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


import android.util.Log;

import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.McsLayerListener;

/**
 * Android platform implementation of IHttpConnectionPoint
 */
public class HttpConnectionPoint implements McsLayerListener, IHttpConnectionPoint {
    private static final String TAG = "HttpConnectionPoint";
	private McsLayer m_connection;
	private IHttpLayer m_httpLayer;
	
	private static final int BufSize = 16*1024;
	private byte[] m_readBuffer = new byte[BufSize];
	private byte[] m_requestBuffer = new byte[BufSize];
	private int m_requestBufSize = BufSize;
	private int m_requestBufPos = 0;
	private int m_id = -1;
	
	/**
	 * Constructs new instance
	 * @param dataLayer
	 * @param httpLayer
	 */
	public HttpConnectionPoint(McsLayer dataLayer, IHttpLayer httpLayer) {
		m_httpLayer = httpLayer;
		m_connection = dataLayer;
		m_connection.addListener( this );
		
		//MCSLogger.log("HttpConnectionPoint", "\n\n\nNEW  HTTP  CONNECTION POINT\n\n=========================\n\n");
	}

	/**
	 * Retrieves underlying data layer.
	 */
	public McsLayer getDataLayer() {
		return m_connection;
	}

	/**
	 * Handles notification of incoming data from the lower layer.
	 * Keep adding the data into the local buffer until we have a complete HTTP request, 
	 * then process it and clear the local buffer.
	 */
	public void onDataReceived() {
		// Read and parse data
		Request req = null;
		synchronized(this) {
			if( m_connection == null )
				return;
			
			int newDataLen = 0;
			while( ( newDataLen = m_connection.readData(m_readBuffer, BufSize) ) > 0 ) {
				addData( newDataLen );
			}
			try {
				req = HttpHelper.parseRequest(m_requestBuffer, m_requestBufPos);
				
				if( req != null )           // Check if we have a complete HTTP request,
					m_requestBufPos = 0;    // then clear the buffer,
				                            // otherwise wait for more data.
			} catch (Exception e) {
			    Log.e( TAG, "", e );
			}
		}
		
		// Process request
		if( req != null ) {
			processRequest(req);
		}
	}

	/**
	 * Processes HTTP request
	 * @param req
	 */
	private void processRequest(Request req) {
		// Find available handler
		IRequestHandler handler = null;
		synchronized(this) {
			if( m_httpLayer != null )
				handler = m_httpLayer.findHandler(req);
			else
				return;
		}
		
		if( handler != null ) {
		// Get response
    		Response resp = handler.processRequest(req, this.m_id);
    		if( null != resp )    // If response==null that means we will send an async response later.
    		    sendResponse(resp);
		}
		else {
		    Response notFoundResp = new Response("Not Found", 404, null, null, true);
		    sendResponse(notFoundResp);
		}
	}

	/**
	 * Utilitty method used while parsing incoming HTTP request
	 * @param newDataLen
	 */
	private void addData(int newDataLen) {
		if( m_requestBufPos + newDataLen > m_requestBufSize ) {
			// Increase buffer size
			m_requestBufSize += BufSize;
			byte[] tmp = new byte[m_requestBufSize];
			System.arraycopy(m_requestBuffer, 0, tmp, 0, m_requestBufPos);
			m_requestBuffer = tmp;
		}
		System.arraycopy(m_readBuffer, 0, m_requestBuffer, m_requestBufPos, newDataLen);
		m_requestBufPos += newDataLen;
	}

	/**
	 * Handles closing of underlying connection
	 */
	public void onConnectionClosed() {
		synchronized(this) {
			if( m_connection == null ) {
				return;
			}
			m_connection = null;
		}
		m_httpLayer.onConnectionClosed( this );
		m_httpLayer = null;
		m_readBuffer = null;
		m_requestBuffer = null;
	}

	/**
	 * Sets connection ID
	 * @param seqNo connection ID
	 */
	public void setID(int seqNo) {
		m_id = seqNo;
	}
	
	/**
	 * Retieves connection ID
	 */
	public int getID() {
		return m_id;
	}

	/**
	 * Serializes and sends a response to the underlying data layer 
	 */
	public void sendResponse(Response resp) {
		if( resp != null ) {
			byte[] buf = HttpUtils.getBytes(resp);
			
			// Write response
			McsLayer dataLayer = null;
			synchronized(this) {
				dataLayer = m_connection;
			}
			if( dataLayer != null ) {
				dataLayer.writeData(buf, buf.length);
			}
		}
	}
}
