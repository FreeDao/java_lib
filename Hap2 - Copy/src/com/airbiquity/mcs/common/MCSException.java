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

import android.util.Log;

/**
 * MCSException is thrown by variety of methods in MCS packages, 
 * for example when no enough memory exists to accomplish MCS initialization.
 */
public class MCSException extends Exception
{
    private static final long serialVersionUID = 6808755760512918196L;


    public MCSException()
    {
        super();
    }


    /**
     * Constructor.
     * @param message
     */
    public MCSException( String message )
    {
        super( message );
        Log.e( "MCSException", message );
    }
    /*
     * public MCSException(String message, Throwable cause) { super(message, cause); } public MCSException(Throwable cause)
     * { super(cause); }
     */
}
