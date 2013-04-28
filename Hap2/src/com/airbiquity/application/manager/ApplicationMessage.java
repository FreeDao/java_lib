package com.airbiquity.application.manager;

/**
 * A message that contain: appName, sequenceNumber, contentType, payload.
 */
public class ApplicationMessage {

    public final String appName;
    public final int sequenceNumber;
    public final byte[] payload;
    public final String contentType;

    /**
     * Constructs a message using the given arguments.
     * @param application_name
     * @param sequenceNumber
     * @param payload
     * @param contentType
     */
	public ApplicationMessage(String application_name, int sequenceNumber, byte[] payload, String contentType) {
		this.appName = application_name;
		this.sequenceNumber = sequenceNumber;
		this.payload = payload;
		this.contentType = contentType;
	}

	public String getApplication() { return appName; }
	public int getSequenceNumber() { return sequenceNumber; }
	public byte[] getPayload() { return payload; }
	public String getContentType() { return contentType; }

	/**
	 * Determine is this is a valid application message.
	 * @return true if valid, otherwise false
	 */
	public boolean isValid() {
		return (null != appName) &&
			(sequenceNumber >= 0) &&
			(null != contentType) &&
			(null != payload) && (payload.length > 0);
	}

	public String toString() {
		String out = "HttpResponse: application=" + appName +
			", sequenceNumber=" + sequenceNumber +
			", contentType=" + contentType;

		if ( contentType.equals("application/json") || contentType.equals("plain/text") ) {
			out = out.concat(new String(payload));
		}
		return out;
	}
	
	/**
	 * Creates an invalid application message
	 * @return invalid application message
	 */
	static public ApplicationMessage createInvalidApplicationMessage() {
		return new ApplicationMessage(null, 0, null, null);
	}
}
