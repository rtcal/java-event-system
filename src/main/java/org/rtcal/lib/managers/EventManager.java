package org.rtcal.lib.managers;

import org.rtcal.lib.event.Event;
import org.rtcal.lib.event.EventHandler;
import org.rtcal.lib.event.Listener;
import org.rtcal.lib.exceptions.EventException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class EventManager {

    private static final Map<Class<? extends Event>, List<RegisteredListener>> handlers = new HashMap<>();

    private final EventLogger logger;

    public EventManager(EventLogger logger) {
        this.logger = logger;
    }

    public EventManager() {
        this((msg) -> System.out.println("[EventLogger] " + msg));
    }

    public void callEvent(Event event) {
        if (!handlers.containsKey(event.getClass())) return;

        for (RegisteredListener listener : handlers.get(event.getClass())) {
            try {
                listener.callEvent(event);
            } catch (EventException e) {
                logger.log(e.getMessage());
            }
        }
    }

    public void registerEventListener(Listener listener) {
        for (Map.Entry<Class<? extends Event>, Set<RegisteredListener>> entry : createRegisteredListeners(listener).entrySet()) {
            handlers.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
            Collections.sort(handlers.get(entry.getKey()));
        }
    }

    private Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener) {
        Map<Class<? extends Event>, Set<RegisteredListener>> localHandlers = new HashMap<>();

        if (listener == null) return localHandlers;

        Method[] methods;

        try {
            methods = listener.getClass().getMethods();
        } catch (NoClassDefFoundError e) {
            return localHandlers;
        }

        for (final Method method : methods) {

            final EventHandler eventHandler = method.getAnnotation(EventHandler.class);

            if (eventHandler == null) continue;
            if (method.isBridge() || method.isSynthetic()) continue;

            final Class<?> checkClass;

            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0])) {
                logger.log("Attempted to register an invalid EventHandler method signature " + method.toGenericString() + " in " + listener.getClass());
                continue;
            }

            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);

            method.setAccessible(true);

            Set<RegisteredListener> eventSet = localHandlers.computeIfAbsent(eventClass, k -> new HashSet<>());

            EventExecutor executor = (l, e) -> {
                try {
                    if (!eventClass.isAssignableFrom(e.getClass())) return;
                    method.invoke(l, e);
                } catch (InvocationTargetException ex) {
                    throw new EventException(ex.getCause());
                } catch (Throwable t) {
                    throw new EventException(t);
                }
            };

            eventSet.add(new RegisteredListener(listener, executor, eventHandler.priority(), eventHandler.ignoreCancelled()));
        }

        return localHandlers;
    }

}
