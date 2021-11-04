package com.example.util;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicWork {
    private static final String TAG = "AtomicWork";
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    private static AtomicInteger atomicInteger = new AtomicInteger(10);

    public static void atomicWork() {
        setAtomicBoolean();
        setAtomicInteger();
    }

    private static void setAtomicBoolean() {
        Log.i(TAG, "setAtomicBoolean: atomicBoolean = " + atomicBoolean.get());
        if (atomicBoolean.compareAndSet(false, true)) {
            Log.i(TAG, "setAtomicBoolean: atomicBoolean = " + atomicBoolean.get());
        }

        Log.i(TAG, "setAtomicBoolean: atomicBoolean = " + atomicBoolean.get());
        boolean getAndSet = atomicBoolean.getAndSet(false);
        Log.i(TAG, "setAtomicBoolean: getAndSet = " + getAndSet);
        Log.i(TAG, "setAtomicBoolean: atomicBoolean = " + atomicBoolean.get());
    }

    private static void setAtomicInteger() {
        Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
        if (atomicInteger.compareAndSet(10, 100)) {
            Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
        }

        Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
        int getAndAdd = atomicInteger.getAndAdd(10);
        int addAndGet = atomicInteger.addAndGet(10);
        Log.i(TAG, "setAtomicInteger: getAndAdd = " + getAndAdd + ", addAndGet = " + addAndGet);

        Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
        int getAndIncrement = atomicInteger.getAndIncrement();
        int incrementAndGet = atomicInteger.incrementAndGet();
        Log.i(TAG, "setAtomicInteger: getAndIncrement = " + getAndIncrement + ", incrementAndGet = " + incrementAndGet);

        Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
        int getAndDecrement = atomicInteger.getAndDecrement();
        int decrementAndGet = atomicInteger.decrementAndGet();
        Log.i(TAG, "setAtomicInteger: getAndDecrement = " + getAndDecrement + ", decrementAndGet = " + decrementAndGet);

        Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
        int getAndSet = atomicInteger.getAndSet(125);
        Log.i(TAG, "setAtomicInteger: getAndSet = " + getAndSet);
        Log.i(TAG, "setAtomicInteger: atomicInteger = " + atomicInteger.get());
    }
}