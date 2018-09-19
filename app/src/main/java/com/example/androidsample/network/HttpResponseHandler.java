package com.example.androidsample.network;

import java.util.List;

public abstract class HttpResponseHandler<T> {
    public void onSuccessList(List<T> response) {};
    public void onSuccess(T response) {};
    public void onFailure(int responseCode, Exception e) {};
}
