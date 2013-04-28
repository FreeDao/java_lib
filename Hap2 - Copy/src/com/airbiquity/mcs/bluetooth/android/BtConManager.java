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
package com.airbiquity.mcs.bluetooth.android;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.airbiquity.mcs.common.McsLayerListener;
import com.airbiquity.mcs.iostream.BtLayer;

/**
 * This class does all the work for setting up and managing Bluetooth connection to HU. 
 * It has a thread that listens for incoming connections, a thread for connecting with a device, 
 * and a thread for performing data transmissions when connected.
 */
public class BtConManager implements McsLayerListener
{
    private static final String TAG = "BtConManager";
    
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0; // we're doing nothing
    public static final int STATE_LISTEN = 1; // now listening for incoming connections
    //public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3; // now connected to a remote device
    
    // Messages
    public static final int MESSAGE_STATE_CHANGE = 1;
    
    // Key names received from the BluetoothEchoTestService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    /** Name for the SDP record when creating server socket. */
    private static final String SDP_NAME = "MIP";
    
    /** Unique UUID for this application */
    private static final UUID MY_LISTEN_UUID = UUID.fromString( "3d92d640-ad7f-11e0-b780-0002a5d5c51b" );
    
    /** Android's default BluetoothAdapter */
    private final BluetoothAdapter btAdapter;

    /** Handler to send messages back to the UI thread. */
    private final Handler mHandler;
    
    private AcceptThread mAcceptThread;
    private int mState;
    private BtLayer btLayer;
    private String devName; // Connected BT device name.
    

    /**
     * Constructor.
     * @param handler : A Handler to send messages back to the UI thread.
     * @param layer : the lower layer (Bluetooth).
     */
    public BtConManager( Handler handler, BtLayer layer )
    {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        mHandler = handler;
        btLayer = layer;
        btLayer.addListener( this );
    }


    /**
     * Set the current state of the BtConManager connection.
     * 
     * @param state An integer defining the current connection state
     */
    private synchronized void setState( int state )
    {
        Log.d( TAG, "setState() " + mState + " -> " + state );
        mState = state;
        
        // Notify UI thread of the state change.
        Message msg = mHandler.obtainMessage( MESSAGE_STATE_CHANGE, state, -1, devName );
        msg.sendToTarget();        
    }


    /**
     * Return the current connection state.
     */
    public synchronized int getState()
    {
        return mState;
    }


    /**
     * Start the AcceptThread to listen for incoming connection.
     */
    public synchronized void start()
    {
        if( STATE_LISTEN == mState )
        {
            Log.d( TAG, "Already listening, do nothing." );
            return;
        }
        
        Log.d( TAG, "start" );
        
        // Cancel any thread currently running a connection
        if( btLayer.isOpen() )
            btLayer.closeSocket();

        // Start the thread to listen on a BluetoothServerSocket
        if( mAcceptThread == null )
        {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }
        setState( STATE_LISTEN );
    }


    /**
     * Set BT socket on BT layer, which starts the threads.
     * @param socket : BluetoothSocket on which the connection was made.
     */
    private synchronized void setSocket( BluetoothSocket socket )
    {
        if( btLayer.isOpen() )
            btLayer.closeSocket();

        btLayer.openSocket( socket );   // Set BT socket on BT layer, which starts the threads.
        
        BluetoothDevice device = socket.getRemoteDevice();
        devName = device.getName();
        
        setState( STATE_CONNECTED );
    }


    /**
     * Stop all threads
     */
    public synchronized void stop()
    {
        Log.d( TAG, "stop 2 mAcceptThread="+mAcceptThread );
        btLayer.removeListener( this );
        if( btLayer.isOpen() )
            btLayer.closeSocket();
        
        if( mAcceptThread != null )
        {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
        setState( STATE_NONE );
    }

    /**
     * This thread runs while listening for incoming connections. It behaves like a server-side client. 
     * It runs until a connection is accepted (or until cancelled).
     */
    private class AcceptThread extends Thread
    {
        // The local server socket
        private volatile BluetoothServerSocket btSocket;

        @Override public String toString() { return "AcceptThread btSocket="+btSocket; };

        public AcceptThread()
        {
            super( "AcceptThread" );
        }


        public void run()
        {
            try
            {
                btSocket = btAdapter.listenUsingRfcommWithServiceRecord( SDP_NAME, MY_LISTEN_UUID );
                Log.i( TAG, "AcceptThread Listening as " + SDP_NAME + " for " + MY_LISTEN_UUID.toString() );

                // This is a blocking call and will only return on a successful connection or an exception
                BluetoothSocket socket = btSocket.accept();
                
                synchronized( BtConManager.this )
                {
                    if( STATE_LISTEN == mState )
                        setSocket( socket );    // Start the connected thread.
                    else
                        socket.close();         // Either not ready or already connected.
                }
            }
            catch( IOException e )
            {
                btSocket = null;
                Log.e( TAG, "", e );
            }
            Log.i( TAG, "AcceptThread END" );
        }


        public void cancel()
        {
            Log.d( TAG, "cancel Thread=" + this );
            try
            {
                if( btSocket != null )
                    btSocket.close();
            }
            catch( IOException e )
            {
                Log.e( TAG, "close() of server failed", e );
            }
        }
    }


    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    public void onConnectionClosed()
    {
        setState( STATE_NONE );
    }


    public void onDataReceived()
    {
        // Do nothing
    }
}
