package com.anjuke.android.commonutils.executor;

public abstract class CommonCallback <T>{
    public abstract void onSuccess(T data);

    public abstract void onFailed(Exception exception);

}
