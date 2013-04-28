package com.airbiquity.util_net;

import java.util.List;



/**
 * Listener for network tasks completion.
 */
public interface ListenerTaskNets
{
    /**
     * Called when the network task has been completed.
     * @param result : list of task results.
     */
    public void onDone( List<NetResp> results );
}
