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
package com.airbiquity.mcs.utils;

/**
 * Provides utility methods for manipulating bytes, words and integers.
 */
public class ByteUtils
{
    /**
     * Convert byte to unsigned integer.
     * @param b
     * @return
     */
    public static int toUnsignedInteger( byte b )
    {
        int n = b >= 0 ? b : b + 256;
        return n;
    }


    /**
     * Convert byte to 16 bit integer.
     * @param buffer
     * @param pos
     * @return
     */
    public static int getWord( byte[] buffer, int pos )
    {
        int first = getUnsignedSafe( buffer, pos );
        int second = getUnsignedSafe( buffer, pos + 1 );
        int res = second + ((first << 8) & 0x0000FF00);
        return res;
    }


    /**
     * Convert byte from byte buffer to unsigned integer.
     * @param buffer
     * @param pos : index of the byte in the buffer.
     * @return
     */
    public static int getUnsignedSafe( byte[] buffer, int pos )
    {
        if( null == buffer || pos >= buffer.length || pos < 0 )
        {
            return 0;
        }
        int res = toUnsignedInteger( buffer[pos] );
        return res;
    }


    /**
     * Get One's complement?
     * @param n
     * @return
     */
    public static int onesComplement( int n )
    {
        int cur = n;
        while( (cur & 0xFFFF0000) != 0 )
        {
            int first = (cur & 0xFFFF);
            int second = (cur >>> 16);
            cur = first + second;
        }
        return cur;
    }


    /**
     * Get integer?
     * @param buffer
     * @param pos
     * @param count
     * @return
     */
    public static int getInt( byte[] buffer, int pos, int count )
    {
        int ret = 0;
        for( int i = 0; i < 4 && i < count && (i + pos) < buffer.length; i++ )
        {
            ret *= 256;
            ret += toUnsignedInteger( buffer[i + pos] );
        }
        return ret;
    }


    /**
     * Get sum words?
     * @param startSum
     * @param buffer
     * @param pos
     * @param words
     * @return
     */
    public static int sumWords( int startSum, byte[] buffer, int pos, int words )
    {
        for( int i = 0; i < words; i++ )
        {
            int word = getWord( buffer, pos + 2 * i );
            startSum += word;
        }
        return startSum;
    }


    /**
     * To text?
     * @param arr
     * @return
     */
    public static String toText( byte[] arr )
    {
        String ret = "";
        if( arr != null && arr.length > 0 )
        {
            for( int i = 0; i < arr.length; i++ )
            {
                if( i > 0 )
                {
                    ret += ".";
                }
                int b = toUnsignedInteger( arr[i] );
                ret += b;
            }
        }
        return ret;
    }


    /**
     * writeToArray
     * @param number
     * @param arr
     * @param pos
     * @param digitCount
     */
    public static void writeToArray( int number, byte[] arr, int pos, int digitCount )
    {
        for( int i = 0; i < 4 && i < digitCount && arr != null; i++ )
        {
            int offset = (digitCount - 1 - i) * 8;
            byte cur = (byte) (number >>> offset);
            arr[i + pos] = cur;
        }
    }


    /**
     * bitwiseNot
     * @param number
     * @return
     */
    public static int bitwiseNot( int number )
    {
        int ret = ~number;
        return ret & 0x0000FFFF;
    }


    /**
     * wordToArray
     * @param word
     * @param arr
     * @param pos
     */
    public static void wordToArray( int word, byte[] arr, int pos )
    {
        byte b = toByte( (word & 0xFF00) >> 8 );
        arr[pos] = b;
        b = toByte( word & 0x00FF );
        arr[pos + 1] = b;
    }


    /**
     * toByte
     * @param n
     * @return
     */
    private static byte toByte( int n )
    {
        return (byte) ((n > 127) ? (n - 256) : n);
    }
}
