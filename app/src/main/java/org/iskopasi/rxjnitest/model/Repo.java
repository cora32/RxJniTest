package org.iskopasi.rxjnitest.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cora32 on 08.01.2018.
 */

public class Repo implements IRepo {
    private static final String TAG = "RxRepo";
    private static IRepo instance;

    public static IRepo getInstance() {
        if (instance == null)
            instance = new Repo();

        return instance;
    }

    private String heavyCalculation(String v) {
        Log.e(TAG, " Computing on " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return v + " OMG STRING";
    }

    private String heavyCalculation2(String v) {
        Log.e(TAG, " Computing 2 on " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return v + " OMG STRING 2";
    }

    @Override
    public Observable<String> getObservableString() {
        return Observable.just("Repo item " + Thread.currentThread().getName())
                .observeOn(Schedulers.computation()) //Heavy compute on new thread
                .map(this::heavyCalculation)
                .map(this::heavyCalculation2)
                .observeOn(AndroidSchedulers.mainThread()); //OnComplete on Main thread
    }

    @Override
    public Observable<String> getObservableList() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }

        return Observable.fromIterable(list)
                .observeOn(Schedulers.computation()) //Heavy compute on new thread
                .filter(str -> {
                    Log.e(TAG, str + " -- filter on " + Thread.currentThread().getName());
                    return str.contains("3");
                })
                .observeOn(AndroidSchedulers.mainThread()); //OnComplete on Main thread
    }

    @Override
    public Observable<String> getObservableListWithError() {
        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }

        return Observable.just(list)
                .observeOn(Schedulers.computation()) //Heavy compute on new thread
                .filter(s -> true)
                .map(this::throwError)
                .observeOn(AndroidSchedulers.mainThread()); //OnComplete on Main thread
    }

    @Override
    public Observable<String> getFromJni() {
        return Observable.just(stringFromJNI())
                .observeOn(Schedulers.computation())
                .map(this::heavyCalculation)
                .map(s -> s.replace("C++", "Repo!"))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> getJniCachedData() {
        List<String> list = generateData();
        jniCacheData(list.toArray());

        return Observable.just(stringFromJNICache(55))
                .observeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private List<String> generateData() {
        List<String> list = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            list.add(String.valueOf(i) + " item!");
        }
        return list;
    }

    private String throwError(String s) {
        Log.e(TAG, "Throwing ex " + s);
        throw new RuntimeException();
    }

    private String throwError(List<String> s) {
        Log.e(TAG, "Throwing ex " + s);
        throw new RuntimeException();
    }

    public native String stringFromJNI();

    public native String stringFromJNICache(int index);

    public native void jniCacheData(Object[] data);
}
