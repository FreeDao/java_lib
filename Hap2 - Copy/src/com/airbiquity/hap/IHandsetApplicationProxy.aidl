package com.airbiquity.hap;

import com.airbiquity.hap.IHapCallback;

/**
 * Interface for the Handset Application Proxy (HAP).
 */
interface IHandsetApplicationProxy {

    /**
     * Used to register an application with the Handset Application Proxy (HAP).  A single
     * handset application can call this function multiple times, each time a different
     * connection ID will be returned.  In essence, calling this more than once is viewed
     * as the application re-registering.  Any previous connection ID assigned to the
     * handset application is considered to be released. 
     *
     * @param applicationName Unique handset application name.  Java package naming conventions
     * (e.g. com.company.product) are recommended.
     *
     * @param mipSchemaVersion Version of the MIP schema used by the handset application.  The
     * MIP schema version is a version maintained by the handset application vendor that changes
     * when new MIP messages/features are exposed in the handset application.
     *
     * @param baseActivityName Full name of the base activity of the handset application that
     * HAP can use to start the handset application.
     *
     * @param callback Callback interface used by HAP to pass messages to the handset application.
     *
     * @return Upon success, a unique connection ID that HAP uses to identify the handset
     * application, -1 otherwise.
     */
    int aqHapInit(in String applicationName,
    		in String mipSchemaVersion,
    		in String baseActivityName, 
    		in IHapCallback callback);

	/**
	 * Used to send a message to the head unit.
	 *
	 * @param connectionId Connection ID provided to the handset application when it
	 * initialized with HAP.
	 *
	 * @param sequenceNumber
	 * @param payload
	 * @param contentType
	 */
    boolean aqSendMsg(in int connectionId,
    		in int sequenceNumber,
    		in byte[] payload,
    		in String contentType); 

}
