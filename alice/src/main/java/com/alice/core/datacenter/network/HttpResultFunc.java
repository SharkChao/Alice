package com.alice.core.datacenter.network;


import com.alice.core.model.HttpResult;
import com.alice.core.util.Constants;

import io.reactivex.functions.Function;

/**

 * Created by yuzhijun on 2017/6/27.
 */
public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(HttpResult<T> tHttpResult) throws Exception {
        if (tHttpResult.getCode() != 1){
            throw new ResponseError(Constants.HttpCode.HTTP_SERVER_ERROR, tHttpResult.getMessage());
        }
        return tHttpResult.getData();
    }
}
