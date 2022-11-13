package org.rtcal.lib.event;

public abstract class Event {

    public String getName() {
        return this.getClass().getSimpleName();
    }

}
