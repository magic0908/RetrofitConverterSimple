package com.xing.retrofitconverter.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creation Time: 2019/2/13 13:34.
 * Author: King.
 * Description: 与服务端约定的基本数据格式类型
 * 这里采用 玩Android 开放API 链接 http://www.wanandroid.com/blog/show/2
 */
public class BaseResponse<T> implements Parcelable {

    private int errorCode;
    private String error;
    private T data;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 判断请求是否成功
     * @return bool
     */
    public boolean isSuccess(){
        return getErrorCode() == 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorCode);
        dest.writeString(this.error);
    }

    public BaseResponse() {
    }

    protected BaseResponse(Parcel in) {
        this.errorCode = in.readInt();
        this.error = in.readString();
    }

    public static final Creator<BaseResponse> CREATOR = new Creator<BaseResponse>() {
        @Override
        public BaseResponse createFromParcel(Parcel source) {
            return new BaseResponse(source);
        }

        @Override
        public BaseResponse[] newArray(int size) {
            return new BaseResponse[size];
        }
    };
}
