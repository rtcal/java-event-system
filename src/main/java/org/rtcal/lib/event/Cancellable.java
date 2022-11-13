package org.rtcal.lib.event;

public interface Cancellable {

    /**
     * Check whether the event is cancelled
     *
     * @return - whether the event is cancelled
     */
    boolean isCancelled();

    /**
     * Set whether the event should be cancelled
     *
     * @param cancel - whether the event should be cancelled
     */
    void setCancelled(boolean cancel);

}
