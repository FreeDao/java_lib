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
*              @author
*              Rayn Xu
*
*****************************************************************************/

package com.airbiquity.application.model;

import java.util.List;
/**
 * This class is the app delta profile.
 * This is the response from HAP to Backend when do handset profile exchange.
 * @author Rayn Xu
 */
public class AppDeltaProfile {
	
	private List<AppInfo> appList2Add;
	private List<AppInfo> appList2Delete;
	
	/**
	 * Return application Add list .
	 * @return  appList2Add   application list to add.
	 */
	public List<AppInfo> getAppList2Add() {
		return appList2Add;
	}
	/**
	 * set the application add list.
	 * @param appList2Add  application list to add
	 */
	public void setAppList2Add(List<AppInfo> appList2Add) {
		this.appList2Add = appList2Add;
	}
	/**
	 * return application delete list
	 * @return appList2Delete  application list to delete 
	 */
			
	public List<AppInfo> getAppList2Delete() {
		return appList2Delete;
	}
	/**
	 * set the application delete list
	 * @param appList2Delete application list to delete
	 */
	public void setAppList2Delete(List<AppInfo> appList2Delete) {
		this.appList2Delete = appList2Delete;
	}
}
