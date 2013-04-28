package com.airbiquity.application.manager;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * Handles the notification and retrieval of asynchronous events.
 */
public class AsynchronousEventHandler {

	// collection of <event ID, asynchronous event> that are waiting
	// for either head unit notification or retrieval
	private Map<Integer, AsynchronousEvent> eventMap = new ConcurrentHashMap<Integer, AsynchronousEvent>();

	private OutputStream eventNotificationSocket = null;
	private boolean hasProcessed = true;

	// timer used to remove events if the head unit cannot be
	// notified of, or retrieved by the head unit
	private Timer timer = new Timer();
	private final static int EVENT_TIMEOUT = 60000; // msec

	private final AtomicInteger currentEventId = new AtomicInteger(0);

	private final static String COMMAND_STR = "command";
	private final static String COMMAND_TYPE_STR = "event";
    private final static String COMMAND_TYPE_STR_EVENTS = "events";
	private final static String EVENT_ID_STR = "eventId";
	private final static String APPLICATION_NAME_STR = "applicationName";
	private final static String CONTENT_TYPE_APPLICTION_JSON = "application/json";

	/**
	 * Adds an event to the handler.
	 * @param event Event to be added
	 * @return true if the event was successfully added, otherwise false
	 */
	public boolean addEvent(ApplicationMessage message) {
		boolean retVal = true;

		AsynchronousEvent event = new AsynchronousEvent(message);
		Integer eventId = Integer.valueOf(currentEventId.incrementAndGet());
		
		// try to send HUP notification of the event


//		if ( sendNotification(eventId, event) ) {
//			event.setNotified(true);
//		}

		// add the event to the map
		retVal = eventMap.put(eventId, event) != null;

        processNotifications();
		// set a timer to timeout the event if the head unit doesn't retrieve it
		timer.schedule(new RemoveStaleAsynchronousEvent(eventId), EVENT_TIMEOUT);

		return retVal;
		}

	/**
	 * Retrieves the event with the specified ID, if it exists.
	 * @param eventId ID of event to remove.
	 * @return 
	 */
	public ApplicationMessage removeEvent(Integer eventId) {
		AsynchronousEvent event = eventMap.remove(eventId);
		return (null != event) ? event.getApplicationMessage() : null;
	}
	
	/**
	 * Hacky way to get the path to send event notifications.
	 * @param socket
	 */
	public synchronized void setNotificationSocket(OutputStream socket) {
		this.eventNotificationSocket = socket;
		Log.d("HAP", "AsynchronousEventHandler(): socket=" + eventNotificationSocket);
		if (this.eventNotificationSocket != null) {
		    hasProcessed = false;
		} else {
		    hasProcessed = true;
		}
        processNotifications();
	}
	

	/**
	 * Hacky way to get the path to send event notifications.
	 * @param socket
	 */
//	public synchronized void setNotificationSocket(HttpLayer socket) {
//		this.eventNotificationSocket = socket;
//		Log.d("HAP", "AsynchronousEventHandler(): socket=" + eventNotificationSocket);
//		if (this.eventNotificationSocket != null) {
//		    hasProcessed = false;
//		} else {
//		    hasProcessed = true;
//		}
//        processNotifications();
//	}
	
	private void processNotifications() {
        // search for the first event that hasn't been notified yet, if one exists
        ArrayList<Integer> eventIds = new ArrayList<Integer>();
        String appName = "";
        Log.i("HAP","---------------processNotifications-----------------------------");
        Log.i("HAP","event map size = " + eventMap.entrySet().size());
        for (Map.Entry<Integer, AsynchronousEvent> entry : eventMap.entrySet()) {
            Log.d("HAP", "AsynchronousEventHandler():  entry.getKey()=" + entry.getKey());
            
            if (!entry.getValue().isNotified()) {
                // try to send the notification and change the notification state 
                // of the event if successful

                if(eventIds.isEmpty()) {
                    eventIds.add(entry.getKey());
                    appName = entry.getValue().getApplicationMessage().getApplication();
                } else {
                    if (appName.equalsIgnoreCase(entry.getValue().getApplicationMessage().getApplication())) {
                        eventIds.add(entry.getKey());
                    }
                }
            }
        }
        if (eventIds.size() == 1) {
            Integer eventId = eventIds.get(0);
            AsynchronousEvent event = eventMap.get(eventId);
            event.setNotified(sendNotification(eventId, event));
        } else if (eventIds.size() > 1) {
            if (sendNotifications(eventIds, appName)) {
                for (Integer eventId: eventIds) {
                    AsynchronousEvent event = eventMap.get(eventId);
                    event.setNotified(true);
                }
            }
        }
    
	}
	private boolean sendNotifications(ArrayList<Integer> eventIds,String appName){
        
        boolean retVal = false;

        if ( null != eventNotificationSocket && !hasProcessed) {

            try {
                JSONObject eventMessage = new JSONObject();
                eventMessage.put(COMMAND_STR, COMMAND_TYPE_STR_EVENTS);
                JSONArray events = new JSONArray();
                for (Integer eventId: eventIds) {
                    events.put(eventId);
                }
                eventMessage.put(EVENT_ID_STR, events);
                eventMessage.put(APPLICATION_NAME_STR, appName);
                Log.d("HAP", "AsynchronousEventHandler(): eventMessage=" + eventMessage.toString() +
                        ", eventNotificationSocket=" + eventNotificationSocket);
                
//                eventNotificationSocket.writeData(eventMessage.toString().getBytes(), CONTENT_TYPE_APPLICTION_JSON);
                
                byte[] message = eventMessage.toString().getBytes();
                String header = "HTTP/1.1 200 OK\r\nContent-Type: " 
                	+ CONTENT_TYPE_APPLICTION_JSON + "\r\nContent-Length: " 
                	+ Integer.toString(message.length) + "\r\n\r\n";
                
                byte[] response = new byte[header.length() + message.length];
                System.arraycopy(header.getBytes(), 0, response, 0, header.length());
                System.arraycopy(message, 0, response, header.length(), message.length);
                
                eventNotificationSocket.write(response);
                eventNotificationSocket.flush();
				eventNotificationSocket.close();
                retVal = true;
            } catch (Exception e) {
                Log.e("HAP", "Failure trying to send event to HUP: ", e);
            }
            hasProcessed = true;
        }
        return retVal;
    }
	
	
	/**
	 * Sends notification to the head unit of a new asynchronous event.
	 * @param eventId
	 * @param event
	 * @return true if the notification was successfully sent, otherwise false.
	 */
	private boolean sendNotification(Integer eventId, AsynchronousEvent event) {
		
		if( null == eventNotificationSocket || hasProcessed) 
		    return false;		

		try {
			JSONObject eventMessage = new JSONObject();
			eventMessage.put(COMMAND_STR, COMMAND_TYPE_STR);
			eventMessage.put(EVENT_ID_STR, eventId);
			eventMessage.put(APPLICATION_NAME_STR, event.getApplicationMessage().getApplication());
			Log.d("HAP", "AsynchronousEventHandler(): eventMessage=" + eventMessage.toString() + ", Socket=" + eventNotificationSocket);
//          eventNotificationSocket.writeData(eventMessage.toString().getBytes(), CONTENT_TYPE_APPLICTION_JSON);
			
			byte[] message = eventMessage.toString().getBytes();
			String header = "HTTP/1.1 200 OK\r\nContent-Type: "
							+ CONTENT_TYPE_APPLICTION_JSON + "\r\nContent-Length: "
							+ Integer.toString(message.length) + "\r\n\r\n";

			byte[] response = new byte[header.length() + message.length];
			System.arraycopy(header.getBytes(), 0, response, 0, header.length());
			System.arraycopy(message, 0, response, header.length(), message.length);
			
			eventNotificationSocket.write(response);
			eventNotificationSocket.flush();
			eventNotificationSocket.close();
			
	        hasProcessed = true;
			return true;
		} 
		catch (Exception e) {
			Log.e("HAP", "Failure trying to send event to HUP: ", e);
			return false;
		}		
	}

	/**
	 * Task to remove a stale asynchronous event upon timer expiration.
	 */	
	private class RemoveStaleAsynchronousEvent extends TimerTask {

		private final Integer eventId;

		/**
		 *
		 * @param eventId ID of event to remove.
		 */
		public RemoveStaleAsynchronousEvent(Integer eventId) {
			this.eventId = eventId;
		}

		@Override
		public void run() {
			Log.e("HAP", "RemoveStaleAsynchronousEvent.eventId" + eventId);
			eventMap.remove(eventId);
		}
	}

}
