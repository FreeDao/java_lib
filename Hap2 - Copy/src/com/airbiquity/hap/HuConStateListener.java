package com.airbiquity.hap;

/**
 * Listener that gets notified when BT connection to HU changes (connected/disconnected).
 */
public interface HuConStateListener
{
    /**
     * Called when BT connection state to HU changes (connected/disconnected).
     * @param isConnected : true if the Agent is connected to HU, false otherwise.
     */
    public void conStateChanged( boolean isConnected );
}
