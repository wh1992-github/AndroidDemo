package com.example.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private static final String TAG = "ThreadManager";
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int THREADS_CORE_COUNT = CPU_COUNT + 1;
    private static int sCoreThreadsCount = THREADS_CORE_COUNT;

    private static volatile ThreadManager sInstance;
    private ExecutorService mSingleThreadPool;
    private ExecutorService mCachedThreadPool;
    private ExecutorService mFixedThreadPool;
    private ScheduledExecutorService mScheduledThreadPool;
    private ScheduledExecutorService mScheduledSingleThreadPool;
    private ThreadPoolExecutor mThreadPoolExecutor;

    public static ThreadManager getInstance() {
        if (null == sInstance) {
            sInstance = new ThreadManager();
        }
        return sInstance;
    }

    private ThreadManager() {
        mSingleThreadPool = Executors.newSingleThreadExecutor();
        mCachedThreadPool = Executors.newCachedThreadPool();
        mFixedThreadPool = Executors.newFixedThreadPool(sCoreThreadsCount);
        mScheduledThreadPool = Executors.newScheduledThreadPool(sCoreThreadsCount);
        mScheduledSingleThreadPool = Executors.newSingleThreadScheduledExecutor();
        //建议使用这个:
        //corePoolSize 线程池的核心线程数,默认情况下,核心线程会在线程池中一直存活,即使它们处于闲置状态.
        //但如果将 ThreadPoolExecutor 的 allowCoreThreadTimeOut 属性设置为 true ,那么闲置的核心线程在等待新任务到来时会有超时策略,这个时间间隔是由 keepAliveTime 所指定,当等待时间超过 keepAliveTime 所指定的时长后,核心线程就会被终止.
        //maximumPoolSize 线程池所能容纳的最大线程数,当活动线程数达到这个数值后,后续的新任务将会被阻塞.
        //keepAliveTime 非核心线程闲置时的超时时长,超过这个时长,非核心线程就会被回收.当 ThreadPoolExecutor 的 allowCoreThreadTimeOut 属性设置为 true 时,keepAliveTime 同样会作用于核心线程.
        //unit 用于指定 keepAliveTime 参数的时间单位,这是一个枚举,常用的有 TimeUnit.MILLISECONDS 和 TimeUnit.SECONDS.
        //workQueue 线程池中的任务队列,通过线程池的 execute 方法提交的 Runnable 对象会存储在这个参数中.
        mThreadPoolExecutor = new ThreadPoolExecutor(sCoreThreadsCount, 30, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>(128));
    }

    public void executeSingle(Runnable command) {
        mSingleThreadPool.execute(command);
    }

    public void executeCached(Runnable command) {
        mCachedThreadPool.execute(command);
    }

    public void executeFixed(Runnable command) {
        mFixedThreadPool.execute(command);
    }

    public void executeScheduled(Runnable command, long delay, TimeUnit unit) {
        mScheduledThreadPool.schedule(command, delay, unit);
    }

    public void executeScheduled(Runnable command, long delay, long reRun, TimeUnit unit) {
        mScheduledThreadPool.scheduleAtFixedRate(command, delay, reRun, unit);
    }

    public void executeScheduledSingle(Runnable command, long delay, TimeUnit unit) {
        mScheduledSingleThreadPool.schedule(command, delay, unit);
    }

    public void executeScheduledSingle(Runnable command, long delay, long reRun, TimeUnit unit) {
        mScheduledSingleThreadPool.scheduleAtFixedRate(command, delay, reRun, unit);
    }

    public void executeThreadPoolExecutor(Runnable command) {
        mThreadPoolExecutor.execute(command);
    }
}
