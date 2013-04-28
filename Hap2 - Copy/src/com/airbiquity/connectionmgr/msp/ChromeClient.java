package com.airbiquity.connectionmgr.msp;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;

public class ChromeClient extends WebChromeClient {

	private final static String TAG = "Javascript";

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

		String sourceId = consoleMessage.sourceId();
		int lineNumber = consoleMessage.lineNumber();
		String message = consoleMessage.message();

		if (sourceId != null) {
			sourceId = sourceId.substring(sourceId.lastIndexOf("/") + 1);
		}

		switch (consoleMessage.messageLevel()) {

		case DEBUG:
			Log.d(TAG, sourceId + " - line: " + lineNumber + " -- " + message);
			break;
		case ERROR:
			Log.e(TAG, sourceId + " - line: " + lineNumber + " -- " + message);
			break;
		case LOG:
			Log.v(TAG, sourceId + " - line: " + lineNumber + " -- " + message);
			break;
		case TIP:
			Log.i(TAG, sourceId + " - line: " + lineNumber + " -- " + message);
			break;
		case WARNING:
			Log.w(TAG, sourceId + " - line: " + lineNumber + " -- " + message);
			break;
		}
		return true;
	}

}
