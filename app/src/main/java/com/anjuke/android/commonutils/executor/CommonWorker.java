package com.anjuke.android.commonutils.executor;


public abstract class CommonWorker<T> implements Runnable {
    public CommonCallback<T> callback;

    public CommonWorker(CommonCallback<T> callback) {
        this.callback = callback;
    }
    public abstract T doInBackground();
    @Override
    public void run() {
        try {
            final T data = doInBackground();
            HandlerUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null)
                        callback.onSuccess(data);
                }
            });
        } catch (final Exception e) {
            HandlerUtil.post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null)
                        callback.onFailed(e);
                }
            });
        }
    }


}
