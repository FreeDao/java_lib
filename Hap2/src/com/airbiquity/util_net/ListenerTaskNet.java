package com.airbiquity.util_net;



/**
 * Listener for network task completion.
 * For example: Registration, Login, etc.
 */
public interface ListenerTaskNet
{
    /**
     * Called when the network task has been completed.
     * @param result : task result.
     */
    public void onDone( NetResp result );
}
