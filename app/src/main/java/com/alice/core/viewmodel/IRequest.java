package com.alice.core.viewmodel;

import android.arch.lifecycle.LiveData;

/**
 * Created by yuzhijun on 2018/4/13.
 */

public interface IRequest<T> {
    LiveData<T> getLiveObservableData(String url, String... params);
}
