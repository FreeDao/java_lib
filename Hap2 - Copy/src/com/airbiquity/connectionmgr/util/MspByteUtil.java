package com.airbiquity.connectionmgr.util;

/**
 * Collection of static methods that convert primitive types to byte arrays and vice versa.
 */
public final class MspByteUtil
{
    public final static byte[] getBytes( short s )
    {
        byte[] buf = new byte[2];
        for( int i = buf.length - 1; i >= 0; i-- )
        {
            buf[i] = (byte) (s & 0x00ff);
            s >>= 8;
        }
        return buf;
    }


    public final static byte[] getBytes( int s )
    {
        byte[] buf = new byte[4];
        for( int i = buf.length - 1; i >= 0; i-- )
        {
            buf[i] = (byte) (s & 0x000000ff);
            s >>= 8;
        }
        return buf;
    }


    public final static byte[] getBytes( long s )
    {
        byte[] buf = new byte[8];
        for( int i = buf.length - 1; i >= 0; i-- )
        {
            buf[i] = (byte) (s & 0x00000000000000ff);
            s >>= 8;
        }
        return buf;
    }


    /**
     * Get short value from big-endian bytes.
     * @param buf
     * @return
     */
    public final static short getShort( byte[] buf )
    {
        if( buf == null )
            throw new IllegalArgumentException( "byte array is null!" );

        if( buf.length > 2 )
            throw new IllegalArgumentException( "byte array size > 2 !" );

        short r = 0;
        for( int i = 0; i < buf.length; i++ )
        {
            r <<= 8;
            r |= (buf[i] & 0x00ff);
        }
        return r;
    }


    public final static int getInt( byte[] buf )
    {
        if( buf == null )
        {
            throw new IllegalArgumentException( "byte array is null!" );
        }
        if( buf.length > 4 )
        {
            throw new IllegalArgumentException( "byte array size > 4 !" );
        }
        int r = 0;
        for( int i = 0; i < buf.length; i++ )
        {
            r <<= 8;
            r |= (buf[i] & 0x000000ff);
        }
        return r;
    }

    
    /**
     * Get integer value of bytes at given position in given buffer. (big-endian)
     * @param buf      : buffer that contain the bytes.
     * @param idxStart : index of the 1st byte to use.
     * @return integer value that is represented by the bytes in the buffer.
     */
    public final static int getInt( byte[] buf, int idxStart )
    {
        int r = 0;  // Result.
        for( int i = 0; i < 4; i++ )
        {
            r <<= 8;
            r |= (buf[idxStart+i] & 0xff);
        }
        return r;
    }

    
    /**
     * Get integer value of bytes at given position in given buffer. (big-endian)
     * @param buf      : buffer that contain the bytes.
     * @param idxStart : index of the 1st byte to use.
     * @return integer value that is represented by the bytes in the buffer.
     */
    public final static short getShort( byte[] buf, int idxStart )
    {
        short r = 0;  // Result.
        for( int i = 0; i < 2; i++ )
        {
            r <<= 8;
            r |= (buf[idxStart+i] & 0xff);
        }
        return r;
    }


    public final static long getLong( byte[] buf )
    {
        if( buf == null )
        {
            throw new IllegalArgumentException( "byte array is null!" );
        }
        if( buf.length > 8 )
        {
            throw new IllegalArgumentException( "byte array size > 8 !" );
        }
        long r = 0;
        for( int i = 0; i < buf.length; i++ )
        {
            r <<= 8;
            r |= (buf[i] & 0x00000000000000ff);
        }
        return r;
    }
    // end test
}
