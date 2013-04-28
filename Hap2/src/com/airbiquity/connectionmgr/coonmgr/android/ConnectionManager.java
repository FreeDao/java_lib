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
package com.airbiquity.connectionmgr.coonmgr.android;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.airbiquity.connectionmgr.detector.android.DetectorLayer;
import com.airbiquity.hap.A;
import com.airbiquity.mcs.bluetooth.android.BtConManager;
import com.airbiquity.mcs.iostream.BtLayer;

/**
 * Creates and connects those layers:
 * 
 *    top layer: DetectorLayer
 * middle layer: BtConManager
 * bottom layer: BluetoothLayer
 * 
 */
public class ConnectionManager
{
    private static final String TAG = "ConnectionManager";
    private static BtLayer btLayer = null;
    private static BtConManager btConMngr = null;
    private static DetectorLayer detector;
    
    /** Current state of bluetooth connection as defined by BtConManager.STATE_*   */
    private static int lastConnectionState;

    
    /**
     * Create and connect those layers:
     * 
     *    top layer: DetectorLayer
     * middle layer: BtConManager
     * bottom layer: BluetoothLayer
     * 
     */
    public static void connect()
    {
        if( isConnected() )
            return; // Already connected.
            
        if( null == BluetoothAdapter.getDefaultAdapter() )
            return; // We can't setup the connection if Bluetooth hardware is not available.
        
        disconnect();   // Make sure each layer (if any) is closed properly.

        Log.d( TAG, "connect" );
        
        btLayer = new BtLayer();
        btConMngr = new BtConManager( mHandler, btLayer );

        detector = new DetectorLayer();
        detector.setLowerLayer( btLayer );

        btConMngr.start();
    }


    /**
     * Disconnect and close all the layers.
     */
    public static void disconnect()
    {
        if( null != detector )  // Stop DetectorLayer if any.
            detector.stop();
        detector = null;
        
        if( null != btConMngr ) // Stop BT Connection Manager if any.
            btConMngr.stop();
        btConMngr = null;
                
        if( null != btLayer )   // Close BT layer if any.
            btLayer.close();
        btLayer = null;
        
        Log.d( TAG, "disconnect" );
    }

    
    /**
     * Check if Connection Manager is started.
     * @return true if so.
     */
    public static boolean isConnected()
    {
        return null != btConMngr  &&  btConMngr.getState() == BtConManager.STATE_CONNECTED; // TODO:SS: check
    }

    
    /**
     * The Handler that gets information from MCS/MSP back to UI thread.
     */
    private static final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage( Message msg )
        {
            if( BtConManager.MESSAGE_STATE_CHANGE != msg.what )
                return;
            
            final int newConnectionState = msg.arg1;
            final boolean isConnected = BtConManager.STATE_CONNECTED == newConnectionState;
            switch( newConnectionState )
            {
                case BtConManager.STATE_LISTEN:
                case BtConManager.STATE_NONE:
                    if( BtConManager.STATE_CONNECTED == lastConnectionState )
                    {
                        A.toast( "Lost bluetooth connection to HU." );                        
//                        Runnable r = new Runnable()
//                        {
//                            public void run()
//                            {
//                                disconnect();
//                                connect();
//                            }
//                        };
//                        Thread t = new Thread( r );
//                        t.start();
                    }
                    break;
                case BtConManager.STATE_CONNECTED:
                    A.toast( "Connected to " + msg.obj.toString() );
                    break;
            }
            lastConnectionState = newConnectionState;
            A.a().setHuConState( isConnected );
        }
    };
}
