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
package com.airbiquity.mcs.common;

/**
 * 
 */
public interface IMCSMultiPointLayer
{
    /**
     * 
     * @param fromAddress
     * @param toAddress
     * @return
     * @throws MCSException
     */
    McsLayer createConnectionPoint( IMCSConnectionAddress fromAddress, IMCSConnectionAddress toAddress ) throws MCSException;


    /**
     * 
     * @param connectionPoint
     * @throws MCSException
     */
    void closeConnection( McsLayer connectionPoint ) throws MCSException;


    /**
     * Register given listener (usually upper layer) to be notified about events: connection-requested, connection-closed.
     */
    void addListener( IMcsConListener listener );


    /**
     * Unregister given listener.
     */
    void removeListener( IMcsConListener notification );


    /**
     * Set the lower layer and start listening to it.
     * If there is a lower layer set already - unset it first. 
     * @param lower_layer : the lower layer (aka data layer, aka transport layer)
     */
    void setLowerLayer( McsLayer dataLayer );


}
