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
package com.hoperun.telematics.mobile.framework.resource.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hoperun.telematics.mobile.framework.resource.LocalResourceManager;
import com.hoperun.telematics.mobile.framework.resource.ResourceException;

/**
 * 
 * @author hu_wg
 * 
 */
public class ResourceHelper {

	public Bitmap getImage(String resId) throws ResourceException {
		return BitmapFactory.decodeFile(LocalResourceManager.getInstance().getPath(resId));
	}
}
