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
public interface IMCSConnectionAddress
{
    /**
     * 
     * @param address
     * @return
     */
    boolean isSameAs( IMCSConnectionAddress address );


    /**
     * 
     * @param address
     * @return
     */
    boolean isSubsetOf( IMCSConnectionAddress address );
}
