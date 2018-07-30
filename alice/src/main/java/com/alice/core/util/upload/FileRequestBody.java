package com.alice.core.util.upload;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class FileRequestBody<T> extends RequestBody {
    private Handler handler = new Handler(Looper.getMainLooper());

    private RequestBody requestBody;

    private RetrofitCallBack<T> callback;

    private BufferedSink bufferedSink;

    public FileRequestBody(RequestBody requestBody, RetrofitCallBack<T> callback) {
        super();
        this.requestBody = requestBody;
        this.callback = callback;

    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * @param sink
     * @return
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            @Override
            public void write(Buffer source, final long byteCount) throws IOException {
                super.write(source, byteCount);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //增加当前写入的字节数
                        callback.setCureent(callback.getCureent()+byteCount);
                        //回调
                        callback.onLoading(callback.getTotal(), callback.getCureent());
                    }
                });
            }
        };
    }
}
