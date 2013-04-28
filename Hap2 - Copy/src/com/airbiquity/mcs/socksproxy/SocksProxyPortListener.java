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
package com.airbiquity.mcs.socksproxy;

import android.util.Log;

import com.airbiquity.mcs.common.IMCSConnectionAddress;
import com.airbiquity.mcs.common.McsLayer;
import com.airbiquity.mcs.common.IMCSMultiPointLayer;
import com.airbiquity.mcs.common.IMcsConListener;
import com.airbiquity.mcs.common.MCSException;
import com.airbiquity.mcs.tcpip.TCPIPAddress;


/**
 * SocksProxyPortListener
 */
public class SocksProxyPortListener implements IMcsConListener
{
    private static final String TAG = "SocksProxyPortListener";
    private static final int PORT = 1080;
    private IMCSMultiPointLayer lowerLayer;


    public SocksProxyPortListener()
    {
    }


    /**
     * Set the lower layer and start listening to it.
     * If there is a lower layer set already - unset it first. 
     * @param lower_layer : the lower layer which supports TCPIP connections.
     */
    public void setLowerLayer( IMCSMultiPointLayer lower_layer )
    {
        if( lowerLayer != null )
            lowerLayer.removeListener( this );
        
        lowerLayer = lower_layer;
        if( lowerLayer != null )
            lowerLayer.addListener( this );
    }


    /**
     * Handles requesting of new connection by client.
     */
    public void newConnectionRequested( IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress, byte[] payload )
    {
        if( lowerLayer != null && toAddress instanceof TCPIPAddress && fromAddress instanceof TCPIPAddress )
        {
            TCPIPAddress addrFrom = (TCPIPAddress) fromAddress;
            TCPIPAddress addrTo = (TCPIPAddress) toAddress;
            if( addrFrom.getPortType() == TCPIPAddress.ProtocolTypeTCP && addrTo.getPortType() == TCPIPAddress.ProtocolTypeTCP && (addrTo.getPortNumber() == PORT) )
            {
                try
                {
                    // Create new connection point
                    McsLayer layer = lowerLayer.createConnectionPoint( addrFrom, addrTo );
                    SocksProxyLayer socksLayer = new SocksProxyLayer();
                    socksLayer.init();
                    socksLayer.setLowerLayer( layer );
                }
                catch( MCSException ex )
                {
                    Log.e( TAG, "", ex );
                }
            }
        }
    }


    /**
     * Handles closing of connection
     */
    public void onConnectionClosed( McsLayer connection, IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress )
    {
    }
}
