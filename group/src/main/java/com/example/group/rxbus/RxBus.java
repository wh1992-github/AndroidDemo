package com.example.group.rxbus;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author test
 */
public class RxBus {
    private static volatile RxBus sInstance;
    private final Subject<Object, Object> mBus;

    private RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getDefault() {
        if (sInstance == null) {
            synchronized (RxBus.class) {
                sInstance = new RxBus();
            }
        }
        return sInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object object) {
        mBus.onNext(object);
    }

    /**
     * 根据类型接收相应类型事件
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }
}
