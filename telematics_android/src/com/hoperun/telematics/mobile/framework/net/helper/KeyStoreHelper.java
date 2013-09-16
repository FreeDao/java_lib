package com.hoperun.telematics.mobile.framework.net.helper;

import java.io.InputStream;
import java.security.KeyStore;

public class KeyStoreHelper {

	private static KeyStoreHelper instance;
	private static KeyStore trustStore;

	private KeyStoreHelper() {

	}

	public static KeyStoreHelper getInstance() {
		if (instance == null) {
			instance = new KeyStoreHelper();
		}
		return instance;
	}

	public KeyStore getKeyStore() throws Exception {
		if (trustStore == null) {
			trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream instream = (InputStream) ResourcesHelper.getInstance().get(
					ResourcesHelper.CACHED_KEY_CLIENT_BKS);
			try {
				trustStore.load(instream, ResourcesHelper.getInstance().get(ResourcesHelper.CACHED_KEY_CLIENT_BKS_PWD)
						.toString().toCharArray());
			} finally {
				instream.close();
			}
		}
		return trustStore;
	}
}
