package com.alice.core.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;

import static com.alice.core.util.Constants.PATH_URL;


/**
 * Created by yuzhijun on 2018/4/12.
 */

public class BaseViewModel<T> extends AndroidViewModel implements IRequest<T>{
    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");
    protected Application mApplication;
    //生命周期观察的数据
    private MutableLiveData<T> liveObservableData = new MutableLiveData<>();
    public MutableLiveData<Throwable> errorObservableData = new MutableLiveData<>();
    public final CompositeDisposable mDisposable = new CompositeDisposable();

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    /**
     * @return
     */
    public LiveData<T> getELiveObservableData(String url, String... params) {
            IRequest request = (IRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{IRequest.class}, (proxy, method, args) -> {
                Object object = null;
                try {
                    String url1 = redirectParseMethodUrl(args);
                    if (null != url1){
                        object = getLiveObservableData(url1);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return object;
            });

            request.getLiveObservableData(url,params);
        return liveObservableData;
    }

    public LiveData<Throwable> getErrorObservableData(){
        return errorObservableData;
    }


    @SuppressLint("CheckResult")
    public LiveData<T> getLiveObservableData(String url, String...params) {
//        DataRepository.getDynamicData(url,getTClass())
//                .subscribeWith(new DisposableSubscriber<T>() {
//                    @Override
//                    public void onNext(T value) {
//                        if(null != value){
//                            liveObservableData.setValue(value);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        errorObservableData.setValue( t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        return liveObservableData;
    }

    private String redirectParseMethodUrl(Object[] args){
        if (null != args && args.length > 0){
            String url = (String) args[0];
            boolean isPath = false;
            if (null != url){
                isPath = url.contains(PATH_URL);
                if (isPath){
                   return parseMethodUrlPath(args);
                }else{
                   return parseMethodUrlKey(args);
                }
            }
        }
        return "";
    }

    private String parseMethodUrlKey(Object[] args){
        String baseUrl = "";
        if (null != args && args.length > 0){
            String url = (String) args[0];
            int index = url.indexOf("{");
            if (index > 0){
                baseUrl = url.substring(0,index-1);
                Matcher matcher = pattern.matcher(url);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("?");
                for (int i = 0;matcher.find();i++){
                    String key = matcher.group();
                    String realKey = key.substring(key.indexOf("{")+1,key.lastIndexOf("}"));
                    Object[] value = (Object[]) args[1];
                    if (value != null){
                        stringBuilder.append(realKey+"="+value[i]+ "&");
                    }
                }

                baseUrl += stringBuilder.toString();
            }
        }

        return baseUrl;
    }

    private String parseMethodUrlPath(Object[] args) {
        String url = "";
        if (null != args && args.length > 0){
            url = (String) args[0];
            url = url.substring(0,url.lastIndexOf("/") - 1);
            Matcher matcher = pattern.matcher(url);
            for (int i = 0;matcher.find();i++){
                Object[] value = (Object[]) args[1];
                String key = matcher.group();
                if (null != value){
                    url = url.replace(key,(String)value[i]);
                }
            }
        }

        return url;
    }

    private Type getTClass(){
        Type tClass = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
