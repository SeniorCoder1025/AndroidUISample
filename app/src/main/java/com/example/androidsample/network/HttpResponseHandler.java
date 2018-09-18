package com.example.androidsample.network;

import java.util.List;

public interface HttpResponseHandler<T> {
    void onSuccessList(List<T> response) ;
    void onSuccess(T response);
    void onFailure(int responseCode, Exception e);
}
