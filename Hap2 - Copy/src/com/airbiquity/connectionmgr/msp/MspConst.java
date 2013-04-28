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
 *****************************************************************************/
package com.airbiquity.connectionmgr.msp;

/**
 * Constants for MIP Serial Protocol.
 */
public interface MspConst
{
    public final static int START_SIGNATURE = 0x4841700D;     // Start signature of MSP segment.
    
    // Command types.
    public final static int CMD_TYPE_APP_DATA      = 0x0;
    public final static int CMD_TYPE_AUD_DATA      = 0x1;
    public final static int CMD_TYPE_TEMPLATE_DATA = 0x2;
    public final static int CMD_TYPE_CONTROL       = 0x8;    
        
    // Control Message Type
    public final short INIT_CONNECTION       = (short) 0x8001;
    public final short INIT_CONNECTION_RSP   = (short) 0x8101;
    public final short TER_CONNECTION        = (short) 0x8002;
    public final short TER_CONNECTION_RSP    = (short) 0x8102;
    public final short KEEPALIVE             = (short) 0x8003;
    public final short KEEPALIVE_RSP         = (short) 0x8103;

    // App Data Messages
    public final short SET_LANGUAGE                      = (short) 0x0001;
    public final short SET_LANGUAGE_RSP                  = (short) 0x0101;
    public final short EXIT_APPLICATION_MODE             = (short) 0x0003;
    public final short EXIT_APPLICATION_MODE_RSP         = (short) 0x0103;
    public final short DISPLAY_CHANGE_INDICATION         = (short) 0x0004;
    public final short DISPLAY_CHANGE_END_INDICATION     = (short) 0x0005;
    public final short HARD_KEY_OPERATION_INDICATION     = (short) 0x0006;
    public final short SOFT_KEY_OPERATION_INDICATION     = (short) 0x0007;
    public final short HANDSFREE_INTERRUPT_INDICATION    = (short) 0x0008;
    public final short AUDIO_INDICATION                  = (short) 0x000A;
    public final short AUDIO_PERMIT_INDICATION           = (short) 0x000B;
    public final short TTS_REQUEST                       = (short) 0x000C;
    public final short TTS_RESPONSE                      = (short) 0x010C;
    public final short HANDSFREE_DIAL					 = (short) 0x0010;
    public final short HANDSFREE_DIAL_RSP		 		 = (short) 0x0110;
    public final short OFFBOARD_VR                       = (short) 0x0011;
    public final short OFFBOARD_VR_RSP                   = (short) 0x0111;
    public final short RESUME_APP            			 = (short) 0x0012;
    public final short RESUME_APP_RSP					 = (short) 0x0112;
    public final short HEAD_UNIT_INFORMATION_INDICATION  = (short) 0x0014;
    public final short TEMPLATE_UPDATE					 = (short) 0x0015;
    public final short TEMPLATE_UPDATE_RSP				 = (short) 0x0115;
    public final short AUDIO_STATUS_INDICATION			 = (short) 0x0016;
    public final short DISPLAY_SYSTEM_SCREEN_INDICATION  = (short) 0x0017;
    public final short RESUME_AUDIO_APP		 		 	 = (short) 0x0018;
    public final short RESUME_AUDIO_APP_RSP				 = (short) 0x0118;

    // Audio Data Messages
    public final short AUDIO_DATA_TX = (short) 0x1002;

    // Template Data Messages ------------------------------
    public final short DISPLAY_TEXT_DATA         = (short) 0x2001;
    public final short DISPLAY_TEXT_DATA_RSP     = (short) 0x2101;
    public final short DISPLAY_IMAGE_DATA        = (short) 0x2002;
    public final short DISPLAY_IMAGE_DATA_RSP    = (short) 0x2102;

    // MSP Control Message Response Codes ------------------------------------------
    public final short OK                                                = (short) 0x8200;
    public final short OK_CONTROL_MESSAGE                                = (short) 0x8201;
    public final short OK_SESSION_ESTABLISH                              = (short) 0x8203; 
    public final short ERR_HAP_SYSTEM                                    = (short) 0x8300;
    public final short ERR_HAP_MIP_SERVICE_UNAVAILABLE                   = (short) 0x8301;
    public final short ERR_HAP_MIP_SERIAL_PROTOCOL_VERSION_INCOMPATIBLE  = (short) 0x8302;
    public final short ERR_CRC                                           = (short) 0x8303; 
    public final short ERR_HUP_SYSTEM_FAILURE                            = (short) 0x8500;
    public final short ERR_HUP_MIP_SERIAL_PROTOCOL_VERSION_INCOMPATIBLE  = (short) 0x8501;
}
