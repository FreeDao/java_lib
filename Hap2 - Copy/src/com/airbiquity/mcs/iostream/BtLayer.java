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
package com.airbiquity.mcs.iostream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.airbiquity.mcs.common.AbstractMcsLayer;


/**
 * An McsLayer that use Bluetooth socket for data transfer.
 */
public class BtLayer extends AbstractMcsLayer
{
    private static final String TAG = "BtLayer";
    private InputStream m_input = null;
    private OutputStream m_output = null;
    private boolean isNotificationSent = false;
    private byte[] buf;
    private boolean isCanceled = false;
    
    /** Number of bytes we have read into 'buf' but didn't write out yet. 
     * We don't read anything from the input stream until this is reset 0 again.*/
    private int m_available = 0;
    
    /** Number of bytes we have read from 'buf' and wrote out. */
    private int m_read = 0;
    
    
    private Thread readThread;    
    private Thread notificationThread;
    private BluetoothSocket btSocket;

    /**
     * Constructor. Creates and starts the threads.
     */
    public BtLayer()
    {
        buf = new byte[16*1024];
    }


    /**
     * Set Bluetooth Socket and open it.
     * @param socket
     */
    synchronized public void openSocket( BluetoothSocket socket )
    {
        try
        {
            btSocket = socket;
            m_input = btSocket.getInputStream();
            m_output = btSocket.getOutputStream();
            notificationThread = new NotificationThread();
            readThread = new ReadThread();
            notificationThread.start();
            readThread.start();            
        }
        catch( IOException e )
        {
            Log.e( TAG, "", e );
        }
    }


    /**
     * Closes internal Bluetooth socket.
     */
    synchronized public void closeSocket()
    {
        try
        {
            btSocket.close();
            close();
            btSocket = null;
        }
        catch( IOException e )
        {
            Log.e( TAG, "", e );
        }
    }
    
    
    /**
     * Checks if the BT socket is open.
     * @return true if so.
     */
    synchronized public boolean isOpen()
    {
        return( btSocket != null );
    }
    
    
    /**
     * Reads data from this layer. (Called from the Upper layer)
     * @param buffer : buffer where to put the data.
     * @param size : size of the buffer.
     * @return size of the data written into the buffer.
     */    
    public int readData( byte[] buffer, int size )
    {
        synchronized( this )
        {
            isNotificationSent = false;
            if( m_input != null )
            {
                if( m_available < 1 )
                    return 0;

                int bytesToRead = m_available - m_read;
                if( bytesToRead > size )
                    bytesToRead = size;

                System.arraycopy( buf, m_read, buffer, 0, bytesToRead );
                m_read += bytesToRead;
                if( m_read >= m_available )
                {
                    m_read = 0;
                    m_available = 0;
                }
                return bytesToRead;
            }
            return 0;
        }
    }


    /**
     * Handles IO exception. Closes connection.
     * 
     * @param ex
     */
    private void onIOException( IOException e )
    {
        Log.i( TAG, "BT Connection Lost "+ e.toString() );
        close();
        try
        {
            if( btSocket != null )
                btSocket.close();
        }
        catch( IOException ex )
        {
            Log.e( TAG, "", ex );
        }
        btSocket = null;
    }


    /**
     * Writes data to output IO stream
     */
    public void writeData( byte[] buffer, int size )
    {
        OutputStream out = null;
        synchronized( this )
        {
            out = m_output;
        }
        try
        {
            if( null != out )
                out.write( buffer, 0, size );
        }
        catch( IOException ex )
        {
            onIOException( ex );
        }
    }


    /**
     * Close the streams, remove listeners, notify upper layer, kill the threads.
     */
    public void close()
    {
        synchronized( this )
        {
            isCanceled = true;
            if( m_input != null )
            {
                try { m_input.close(); }
                catch( IOException e ) {}
                m_input = null;
            }
            if( m_output != null )
            {
                try { m_output.close(); }
                catch( IOException e ) { }
                m_output = null;
            }
        }
        tellConnectionClosed();
        removeAllListeners();
    }


    /**
     * Notification thread which checks in a loop if new data is available and then notifies the upper layers.
     */
    private class NotificationThread extends Thread
    {
        public NotificationThread()
        {
            super("NotificationThread");
        }
        
        public void run()
        {
            while( !isCanceled )
            {
                try
                {
                    Thread.sleep( 20 );
                }
                catch( InterruptedException ex )
                {
                    return;
                }
                boolean sendNotification = false;
                synchronized( this )
                {
                    if( m_input != null && m_available > 0 && !isNotificationSent )
                    {
                        isNotificationSent = true;
                        sendNotification = true;
                    }
                }

                if( sendNotification )
                    tellDataReceived();
            }
        }        
    }
    
    
    /**
     * Read thread.
     */
    private class ReadThread extends Thread
    {
        public ReadThread()
        {
            super("ReadThread");
        }


        public void run()
        {
            while( !isCanceled )
            {
                if( m_input != null && m_available == 0 )
                {
                    try
                    {
                        m_available = m_input.read( buf, 0, buf.length );
                    }
                    catch( IOException e )
                    {
                        onIOException( e );
                    }
                }
                else
                {
                    try
                    {
                        Thread.sleep( 10 );
                    }
                    catch( InterruptedException e )
                    {
                        return;
                    }
                }
            }
        }
    }
}
