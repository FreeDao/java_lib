/**
 * 
 */
package com.sam.proxy;

/**
 * @author yang_jun2
 *
 */
public class ProxyRole extends Role{
	
	private RealRole realRole = null;
	
	

	@Override
	public void service(String user_id) {
		System.out.println("proxy role servers you.");
		realRole = new RealRole();
		
		realRole.service("magc");
	}

}
