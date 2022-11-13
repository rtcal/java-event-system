package org.rtcal.lib.managers;

import org.rtcal.lib.event.Cancellable;
import org.rtcal.lib.event.Event;
import org.rtcal.lib.event.Listener;
import org.rtcal.lib.exceptions.EventException;

public record RegisteredListener(Listener listener, EventExecutor executor, short priority, boolean ignoreCancelled) implements Comparable<RegisteredListener> {

    /**
     * Execute the listener for a given event
     *
     * @throws EventException
     */
    public void callEvent(Event event) throws EventException {
        if (event instanceof Cancellable) {
            if (((Cancellable) event).isCancelled() && !ignoreCancelled()) return;
        }
        
        executor.execute(listener, event);
    }

    @Override
    public int compareTo(RegisteredListener other) {
        return Short.compare(priority, other.priority);
    }
}
