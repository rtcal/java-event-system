package org.rtcal.lib.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {

    /**
     * The order in which handlers should be called, by default the priority is set at 0
     * The lower the priority, the earlier the handler will be called
     * Bytes can hold values from -128 to 127
     *
     * @return - the priority given to this handler
     */
    byte priority() default 0;

    /**
     * Whether the handler ignores the cancelled request for an event
     *
     * @return - whether cancelled events should be ignored
     */
    boolean ignoreCancelled() default false;

}
