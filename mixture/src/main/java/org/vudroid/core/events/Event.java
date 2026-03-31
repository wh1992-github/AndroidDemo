package org.vudroid.core.events;
/**
 * 用于约束 Event 相关能力的接口。
 */

public interface Event<T> {
    void dispatchOn(Object listener);
}
