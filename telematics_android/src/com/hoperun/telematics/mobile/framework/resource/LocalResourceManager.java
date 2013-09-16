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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author hu_wg
 * 
 */
public class LocalResourceManager implements IResourceManager {
	private static LocalResourceManager instance;
	private static String DEFAULT_CACHE_LOCATION = "telematics_cache";
	private Context context;
	private String cacheRootPath;
	private File cacheRoot;

	private LocalResourceManager() {
	}

	public static LocalResourceManager getInstance() {
		if (instance == null) {
			instance = new LocalResourceManager();
		}
		return instance;
	}

	public void init(Context context) {
		this.context = context;
		initCacheLocation();
	}

	protected void initCacheLocation() {
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheRootPath = String.format("%s/%s", Environment.getExternalStorageDirectory().toString(),
					DEFAULT_CACHE_LOCATION);
		} else {
			cacheRootPath = String.format("%s/%s", context.getFilesDir().getAbsolutePath(), DEFAULT_CACHE_LOCATION);
		}
		cacheRoot = new File(cacheRootPath);
		if (!cacheRoot.exists()) {
			cacheRoot.mkdirs();
		}
		Log.i(this.getClass().getName(), cacheRootPath);

	}

	@Override
	public void set(String resId, InputStream is) throws ResourceException {
		if (exists(resId)) {
			delete(resId);
		}
		File newFile = new File(getPath(resId));
		OutputStream fos = null;
		try {
			newFile.createNewFile();
			fos = new FileOutputStream(newFile);
			byte[] b = new byte[128];
			int off = 0, len = 128;
			while ((off = is.read(b, off, len)) != -1) {
				fos.write(b);
			}
			fos.close();
			is.close();
		} catch (IOException e) {
			Log.e(this.getClass().getName(), "create cache file error!", e);
			throw new ResourceException(e);
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public String getPath(String resId) {
		return cacheRootPath + "/" + resId;
	}

	@Override
	public File getCacheRoot() {
		return cacheRoot;
	}

	@Override
	public boolean exists(String resId) {
		return new File(getPath(resId)).exists();
	}

	@Override
	public InputStream get(String resId) throws ResourceException {
		String path = getPath(resId);
		try {
			return new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getName(), "get resource inputstream error!", e);
			throw new ResourceException(e);
		}
	}


	@Override
	public void delete(String resId) {
		new File(getPath(resId)).delete();
	}

}
