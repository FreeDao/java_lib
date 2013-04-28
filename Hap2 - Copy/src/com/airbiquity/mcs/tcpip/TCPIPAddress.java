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
package com.airbiquity.mcs.tcpip;

import com.airbiquity.mcs.common.IMCSConnectionAddress;
import com.airbiquity.mcs.utils.ByteUtils;

/**
 * Stores information for TCP/IP connection address such as IP address, port number and protocol type. Some of parameters
 * may be unspecified.
 */
public class TCPIPAddress implements IMCSConnectionAddress
{
    public static final int ProtocolTypeUnknown = 0;
    public static final int ProtocolTypeTCP = 1;
    public static final int ProtocolTypeUDP = 2;
    protected byte[] m_address = { 0, 0, 0, 0 };
    protected int m_portNumber;
    protected int m_protocolType = ProtocolTypeUnknown;
    protected boolean m_isAddressSpecified = false;
    protected boolean m_isPortSpecified = false;


    /**
     * Checks if this address is the same as provided address.
     */
    public boolean isSameAs( IMCSConnectionAddress address )
    {
        boolean isSame = false;
        if( address != null && address instanceof TCPIPAddress )
        {
            // Check protocol type
            TCPIPAddress addr = (TCPIPAddress) address;
            if( m_protocolType == addr.m_protocolType )
            {
                // Check Address
                boolean isAddressOK = false;
                if( !m_isAddressSpecified && !addr.m_isAddressSpecified )
                {
                    isAddressOK = true;
                }
                else if( m_isAddressSpecified && addr.m_isAddressSpecified )
                {
                    isAddressOK = true;
                    for( int i = 0; i < 4; i++ )
                    {
                        if( addr.m_address[i] != m_address[i] )
                        {
                            isAddressOK = false;
                            break;
                        }
                    }
                }
                if( isAddressOK )
                {
                    // Check port
                    if( !m_isPortSpecified && !addr.m_isPortSpecified )
                    {
                        isSame = true;
                    }
                    else if( m_isPortSpecified && addr.m_isPortSpecified )
                    {
                        if( m_portNumber == addr.m_portNumber )
                        {
                            isSame = true;
                        }
                    }
                }
            }
        }
        return isSame;
    }


    /**
     * Checks if this address is subset of procided address.
     */
    public boolean isSubsetOf( IMCSConnectionAddress address )
    {
        boolean isSubset = false;
        if( address != null && address instanceof TCPIPAddress )
        {
            // Check protocol type
            TCPIPAddress addr = (TCPIPAddress) address;
            if( m_protocolType == addr.m_protocolType || addr.m_protocolType == ProtocolTypeUnknown )
            {
                // Check Address
                boolean isAddressOK = false;
                if( !addr.m_isAddressSpecified )
                {
                    isAddressOK = true;
                }
                else if( m_isAddressSpecified )
                {
                    isAddressOK = true;
                    for( int i = 0; i < 4; i++ )
                    {
                        if( addr.m_address[i] != m_address[i] )
                        {
                            isAddressOK = false;
                            break;
                        }
                    }
                }
                if( isAddressOK )
                {
                    // Check port
                    if( !addr.m_isPortSpecified )
                    {
                        isSubset = true;
                    }
                    else if( m_isPortSpecified && addr.m_isPortSpecified )
                    {
                        if( m_portNumber == addr.m_portNumber )
                        {
                            isSubset = true;
                        }
                    }
                }
            }
        }
        return isSubset;
    }


    /**
     * 
     * @param address
     * @return
     */
    public boolean canHandle( byte[] address )
    {
        if( null == address || address.length != 4 )
        {
            return false;
        }
        if( m_isAddressSpecified )
        {
            for( int i = 0; i < 4; i++ )
            {
                if( m_address[i] != address[i] )
                {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 
     * @param port
     * @param protocolType
     * @return
     */
    public boolean canHandle( int port, byte protocolType )
    {
        if( m_isPortSpecified && m_portNumber != port )
        {
            return false;
        }
        if( m_protocolType == ProtocolTypeTCP && protocolType != TCPIPPacket.PT_TCP )
        {
            return false;
        }
        if( m_protocolType == ProtocolTypeUDP && protocolType != TCPIPPacket.PT_UDP )
        {
            return false;
        }
        return true;
    }


    /**
     * 
     * @param isSpecified
     */
    public void setIsPortSpecified( boolean isSpecified )
    {
        m_isPortSpecified = isSpecified;
    }


    /**
     * 
     * @param isSpecified
     */
    public void setIsAddressSpecified( boolean isSpecified )
    {
        m_isAddressSpecified = isSpecified;
    }


    /**
     * 
     * @param protocolType
     */
    public void setProtocolType( int protocolType )
    {
        m_protocolType = protocolType;
    }


    /**
     * 
     * @return
     */
    public int protocolType()
    {
        return m_protocolType;
    }


    /**
     * 
     * @return
     */
    public int getPortType()
    {
        return m_protocolType;
    }


    /**
     * 
     * @return
     */
    public int getPortNumber()
    {
        return m_portNumber;
    }


    /**
     * 
     * @param address
     */
    public void copyFrom( TCPIPAddress address )
    {
        this.m_portNumber = address.m_portNumber;
        this.m_protocolType = address.m_protocolType;
        System.arraycopy( address.m_address, 0, this.m_address, 0, 4 );
        this.m_isAddressSpecified = address.m_isAddressSpecified;
        this.m_isPortSpecified = address.m_isPortSpecified;
    }


    /**
     * 
     * @param port
     */
    public void setPort( int port )
    {
        m_portNumber = port;
    }


    /**
     * 
     * @param ipAddress
     */
    public void setIPAddress( byte[] ipAddress )
    {
        for( int i = 0; i < 4; i++ )
        {
            m_address[i] = ipAddress[i];
        }
    }


    /**
     * 
     * @return
     */
    public byte[] getIPAddress()
    {
        return m_address;
    }


    /**
     * 
     * @return
     */
    public boolean isAddressSpecified()
    {
        return m_isAddressSpecified;
    }


    /**
     * 
     * @return
     */
    public boolean isPortSpecified()
    {
        return m_isPortSpecified;
    }


    public String toString()
    {
        String res = ByteUtils.toText( m_address ) + ":" + m_portNumber + " " + m_protocolType;
        return res;
    }


    // @Override
    public boolean equals( Object socketAddr )
    {
        boolean result = this == socketAddr;
        if( !result && socketAddr instanceof TCPIPAddress )
        {
            TCPIPAddress addr = (TCPIPAddress) socketAddr;
            result = (m_isAddressSpecified == addr.m_isAddressSpecified) && (m_protocolType == addr.m_protocolType) && (m_isPortSpecified == addr.m_isPortSpecified)
                    && (m_isPortSpecified ? m_portNumber == addr.m_portNumber : true);
            if( result && m_isAddressSpecified && m_address.length == addr.m_address.length )
            {
                for( int index = 0; result && index < m_address.length; index++ )
                {
                    result = m_address[index] == addr.m_address[index];
                }
            }
        }
        return result;
    }
    // @Override
    /*
     * public int hashCode() { // Start with a non-zero constant. int result = 17;
     * 
     * // Include a hash for each field. result = 31 * result + (m_isAddressSpecified ? 1 : 0); result = 31 * result +
     * (m_isPortSpecified ? 1 : 0); result = 31 * result + m_protocolType; result = 31 * result + (m_isPortSpecified ?
     * m_portNumber : 0); result = 31 * result + (m_isAddressSpecified ? Arrays.hashCode(m_address) : 0); return result; }
     */
}
