package org.vudroid.core.events;

import java.util.ArrayList;
/**
 * 封装 Event Dispatcher 相关逻辑的类。
 */

public class EventDispatcher {
    private final ArrayList<Object> listeners = new ArrayList<Object>();

    public void dispatch(Event event) {
        for (Object listener : listeners) {
            event.dispatchOn(listener);
        }
    }

    public void addEventListener(Object listener) {
        listeners.add(listener);
    }

    public void removeEventListener(Object listener) {
        listeners.remove(listener);
    }
}
