package com.airbiquity.application.manager;



/**
 * 
 */
public class AsynchronousEvent {

	private final ApplicationMessage message;
	private boolean isNotified = false;

	/**
	 * 
	 */
	public AsynchronousEvent(ApplicationMessage message) {
		this.message = message;
	}

	/**
	 * 
	 * @param notified
	 */
	public void setNotified(boolean isNotified) {
		this.isNotified = isNotified;
		}

	/**
	 * 
	 * @param notificationSent
	 */
	public boolean isNotified() {
		return isNotified;
	}

	/**
	 * 
	 * @return
	 */
	public ApplicationMessage getApplicationMessage() {
		return message;
	}

}
