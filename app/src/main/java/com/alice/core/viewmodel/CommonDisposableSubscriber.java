package com.alice.core.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import io.reactivex.subscribers.DisposableSubscriber;

public class CommonDisposableSubscriber<T> extends DisposableSubscriber<T> {

    private MutableLiveData<T> mMutableLiveData;
    private MutableLiveData<Throwable> mErrorLiveData;

    public CommonDisposableSubscriber(MutableLiveData<T> mutableLiveData, MutableLiveData<Throwable> errorLiveData) {
        mMutableLiveData = mutableLiveData;
        mErrorLiveData = errorLiveData;
    }
    @Override
    public void onNext(T o) {
        mMutableLiveData.setValue(o);
    }
    @Override
    public void onError(Throwable t) {
        mErrorLiveData.setValue(t);
    }
    @Override
    public void onComplete() {
    }
}
