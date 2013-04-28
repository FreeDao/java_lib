/**
 * 
 */
package com.sam.proxy;

/**
 * @author yang_jun2
 *
 */
public class RealRole extends Role{

	/* (non-Javadoc)
	 * @see com.sam.proxy.Role#service(java.lang.String)
	 */
	@Override
	public void service(String user_id) {
		System.out.println("the real role  servers you.");
	}
}
