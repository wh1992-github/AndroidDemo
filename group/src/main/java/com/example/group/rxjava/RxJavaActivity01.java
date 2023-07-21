package com.example.group.rxjava;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.group.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava2的简单使用(一)
 */
@SuppressLint({"LogNotTimber", "NonConstantResourceId"})
public class RxJavaActivity01 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RxJavaActivity01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava01);

        findViewById(R.id.btn_create).setOnClickListener(this);
        findViewById(R.id.btn_empty).setOnClickListener(this);
        findViewById(R.id.btn_error).setOnClickListener(this);
        findViewById(R.id.btn_never).setOnClickListener(this);
        findViewById(R.id.btn_just).setOnClickListener(this);
        findViewById(R.id.btn_fromArray).setOnClickListener(this);
        findViewById(R.id.btn_fromIterable).setOnClickListener(this);
        findViewById(R.id.btn_defer).setOnClickListener(this);
        findViewById(R.id.btn_timer).setOnClickListener(this);
        findViewById(R.id.btn_interval).setOnClickListener(this);
        findViewById(R.id.btn_intervalRange).setOnClickListener(this);
        findViewById(R.id.btn_range).setOnClickListener(this);
        findViewById(R.id.btn_count_down).setOnClickListener(this);

        //观察者不对被观察者发送的事件做出响应(但是被观察者还可以继续发送事件)
        //public final Disposable subscribe()

        //观察者对被观察者发送的任何事件都做出响应
        //public final void subscribe(Observer<? super T> observer)

        //表示观察者只对被观察者发送的Next事件做出响应
        //public final Disposable subscribe(Consumer<? super T> onNext)

        //表示观察者只对被观察者发送的Next & Error事件做出响应
        //public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError)

        //表示观察者只对被观察者发送的Next & Error & Complete事件做出响应
        //public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
        //Action onComplete)

        //表示观察者只对被观察者发送的Next & Error & Complete & onSubscribe事件做出响应
        //public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
        //Action onComplete, Consumer<? super Disposable> onSubscribe)
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                create();
                break;
            case R.id.btn_empty:
                empty();
                break;
            case R.id.btn_error:
                error();
                break;
            case R.id.btn_never:
                never();
                break;
            case R.id.btn_just:
                just();
                break;
            case R.id.btn_fromArray:
                fromArray();
                break;
            case R.id.btn_fromIterable:
                fromIterable();
                break;
            case R.id.btn_defer:
                defer();
                break;
            case R.id.btn_timer:
                timer();
                break;
            case R.id.btn_interval:
                interval();
                break;
            case R.id.btn_intervalRange:
                intervalRange();
                break;
            case R.id.btn_range:
                range();
                break;
            case R.id.btn_count_down:
                countDown();
                break;
            default:
                break;
        }
    }

    /**
     * 基本创建
     * Observable.create
     * RxJava创建被观察者对象的最基本操作符
     */
    private void create() {
        //1、创建被观察者Observable
        //当 Observable 被订阅时，OnSubscribe 的 call() 方法会自动被调用，即事件序列就会依照设定依次被触发
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NotNull ObservableEmitter<String> emitter) {
                //通过 ObservableEmitter类对象产生事件并通知观察者
                //即观察者会依次调用对应事件的复写方法从而响应事件
                try {
                    if (!emitter.isDisposed()) {
                        emitter.onNext("RxJava:e.onNext = 第一次");
                        emitter.onNext("RxJava:e.onNext = 第二次");
                        emitter.onNext("RxJava:e.onNext = 第三次");
                        Log.i(TAG, "subscribe() = 执行事件的线程 name = " + Thread.currentThread().getName());
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())//指定subscribe()(发射事件的线程)在IO线程()
                .observeOn(AndroidSchedulers.mainThread());//指定订阅者接收事件的线程在主线程;

        //2、创建观察者Observer  并且定义响应事件
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull String s) {
                Log.i(TAG, "onNext = " + s);
                Log.i(TAG, "onNext() = 回调事件的线程 name = " + Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete = ");
            }
        };

        //3、订阅(观察者观察被观察者)
        observable.subscribe(observer);
    }

    private void empty() {
        //快速创建被观察者对象，仅发送onComplete()事件，直接通知完成。
        Observable.empty().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "empty:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Object o) {
                Log.i(TAG, "empty:onNext  = " + o.toString());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "empty:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "empty:onComplete = ");
            }
        });
    }

    private void error() {
        //快速创建被观察者对象，仅发送onError()事件，直接通知异常。
        Observable.error(new Throwable("只回调error")).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "error:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Object o) {
                Log.i(TAG, "error:onNext  = " + o.toString());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "error:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "error:onComplete = ");
            }
        });
    }

    private void never() {
        //快速创建被观察者对象，不发送任何事件。
        Observable.never().subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "never:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Object o) {
                Log.i(TAG, "never:onNext  = " + o.toString());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "never:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "never:onComplete = ");
            }
        });
    }

    /**
     * 通过just()创建传入任意类型的参数构建Observable被观察者，相当于执行了onNext(1)~onNext(5),通过链式编程订阅观察者
     */
    private void just() {
        Observable.just(1, 2, 3, 4, 5).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "just:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Integer integer) {
                Log.i(TAG, "just:onNext = " + integer);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "just:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "just:onComplete = ");
            }
        });
    }

    private void fromArray() {
        //设置需要传入的数组
        String[] strings = {"商品类", "非商品类"};
        //传入数组，被观察者创建后会将数组转换成Observable并且发送里面所有的数据
        Observable.fromArray(strings).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "fromArray:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull String s) {
                Log.i(TAG, "fromArray:onNext = " + s);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "fromArray:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "fromArray:onComplete = ");
            }
        });
    }

    private void fromIterable() {
        //创建集合
        List<Goods> list = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Goods g = new Goods("名称" + i);
            list.add(g);
        }
        //传入集合，被观察者创建后会将数组转换成Observable并且发送里面所有的数据
        Observable.fromIterable(list).subscribe(new Observer<Goods>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "fromIterable:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Goods goods) {
                Log.i(TAG, "fromIterable:onNext = " + goods.getName());
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "fromIterable:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "fromIterable:onComplete = ");
            }
        });
    }

    //1.初始化i
    Integer i = 100;

    /**
     * 直到有观察者调用时，才动态创建被观察者对象并且发送事件
     */
    private void defer() {
        //2.通过defer()定义被观察者（此时被观察者对象还没创建）
        Observable<Integer> defer = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() {
                return Observable.just(i);
            }
        });

        //3.重新设置i值
        i = 200;

        //4.订阅观察者（此时才会调用defer,创建被观察者对象）
        defer.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "defer:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Integer i) {
                Log.i(TAG, "defer:onNext = " + i);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "defer:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "defer:onComplete = ");
            }
        });
    }

    /**
     * 延迟指定时间后发送一个类型为Long的事件
     */
    private void timer() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        Log.i(TAG, "timer:当前时间  = " + dateFormat.format(System.currentTimeMillis()));
        Observable.timer(3, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "timer:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Long aLong) {
                Log.i(TAG, "timer:onNext = " + aLong + ", 时间 = " + dateFormat.format(System.currentTimeMillis()));
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "timer:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "timer:onComplete = ");
            }
        });
    }

    /**
     * 每间隔多少时间发送一次事件
     */
    private void interval() {
        //initialDelay:表示延迟开始的时间, period:距离下一次发送事件的时间间隔, unit:时间单位
        //interval(long initialDelay, long period, TimeUnit unit)
        //在指定的延迟时间后，每隔多少时间发送一次事件，可以指定调度器
        //interval(long initialDelay, long period, TimeUnit unit, Scheduler scheduler)
        //每间隔多少时间发送一次事件，使用默认的线程
        //Observable<Long> interval(long period, TimeUnit unit)
        //每间隔多少时间发送一次事件，可以指定调度器
        //interval(long period, TimeUnit unit, Scheduler scheduler)

        Observable.interval(3, 1, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "interval:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Long aLong) {
                Log.i(TAG, "interval:onNext  = " + aLong);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "interval:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "interval:onComplete = ");
            }
        });
    }

    private void intervalRange() {
        //start:事件开始的数值大小，count:发送事件的次数，initialDelay:表示延迟开始的时间,
        //period:距离下一次发送事件的时间间隔, unit:时间单位，scheduler:调度器
        //intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit)
        //intervalRange(long start, long count, long initialDelay, long period, TimeUnit unit, Scheduler scheduler)
        Observable.intervalRange(10, 5, 3, 1, TimeUnit.SECONDS).filter(new Predicate<Long>() {
            @Override
            public boolean test(@NotNull Long aLong) {
                return false;
            }
        }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "intervalRange:onSubscribe = 订阅");
                d.dispose();
            }

            @Override
            public void onNext(@NotNull Long aLong) {
                Log.i(TAG, "intervalRange:onNext  = " + aLong);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "intervalRange:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "intervalRange:onComplete = ");
            }
        });
    }

    private void range() {
        //start:事件的开始值大小，count:发送的事件次数
        Observable.range(5, 3).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.i(TAG, "range:onSubscribe = 订阅");
            }

            @Override
            public void onNext(@NotNull Integer integer) {
                Log.i(TAG, "range:onNext  = " + integer);
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.i(TAG, "range:onError = " + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "range:onComplete = ");
            }
        });
    }

    //点击获取验证码,10S倒计时,利用Rxjava进行线程切换
    @SuppressLint("CheckResult")
    private void countDown() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        emitter.onNext(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())// 此方法为上面发出事件设置线程为IO线程
                .observeOn(AndroidSchedulers.mainThread())// 为消耗事件设置线程为UI线程
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "aaa---accept: " + integer);
                    }
                });

        mDisposable = Observable.interval(1, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long l) throws Exception {
                Log.i(TAG, "bbb---accept: " + l);
                countDownManager(l);
            }
        });
    }

    private Disposable mDisposable;

    private void countDownManager(Long count) {
        if (count >= 12) {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }
}