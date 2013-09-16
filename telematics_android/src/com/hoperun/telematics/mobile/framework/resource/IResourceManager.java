/*****************************************************************************
 *
 *                      HOPERUN PROPRIETARY INFORMATION
 *
 *          The information contained herein is proprietary to HopeRun
 *           and shall not be reproduced or disclosed in whole or in part
 *                    or used for any design or manufacture
 *              without direct written authorization from HopeRun.
 *
 *            Copyright (c) 2012 by HopeRun.  All rights reserved.
 *
 *****************************************************************************/
package com.hoperun.telematics.mobile.framework.resource;

import java.io.File;
import java.io.InputStream;

/**
 * 
 * @author hu_wg
 * 
 */
public interface IResourceManager {

	void set(String resId, InputStream is) throws ResourceException;

	String getPath(String resId);

	File getCacheRoot();

	InputStream get(String resId) throws ResourceException;

	boolean exists(String resId);

	void delete(String resId);

}
