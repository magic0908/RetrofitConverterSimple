package com.xing.retrofitconverter.api;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * Creation Time: 2018/9/14 11:19.
 * Author: King.
 * Description: 泛型T返回的是具体的数据类型，多数时候只需重写onNext方法即可
 */
public abstract class BaseResourceObserver<T> extends DisposableObserver<T> {

    /*========================= HttpException 异常 code ==========================*/

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onError(Throwable throwable) {
        //打印日志到控制台
        throwable.printStackTrace();
        //如果你某个地方不想使用全局错误处理,则重写 onError(Throwable) 并将 super.onError(e); 删掉
        //如果你不仅想使用全局错误处理,还想加入自己的逻辑,则重写 onError(Throwable) 并在 super.onError(e); 后面加入自己的逻辑
        String msg = requestHandle(throwable);
        Log.i("tag",msg);
    }

    @Override
    public void onComplete() {
    }

    /**
     * 统一处理Throwable
     * @param e e
     * @return msg
     */
    private String requestHandle(Throwable e) {
        String msg;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    msg = "服务器错误";
                    break;
            }
        } else if (e instanceof ApiException) {
            //后台异常
            ApiException apiException = (ApiException) e;
            msg = apiException.getMessage();
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            msg = "解析错误";
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof UnknownHostException) {
            msg = "连接失败，请检查网络";
        }  else if (e instanceof NumberFormatException){
            msg = "数字格式化异常";
        } else {
            msg = "请求失败";
        }
        return msg;
    }

}
