package org.rtcal.lib.managers;

import org.rtcal.lib.event.Event;
import org.rtcal.lib.event.Listener;
import org.rtcal.lib.exceptions.EventException;

public interface EventExecutor {

    /**
     * The function that should be called
     *
     * @param listener - an event listener
     * @param event    - the event the listener is targeting
     * @throws EventException
     */
    void execute(Listener listener, Event event) throws EventException;

}
