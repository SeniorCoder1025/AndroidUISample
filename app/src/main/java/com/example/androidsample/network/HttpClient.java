package com.example.androidsample.network;

import android.content.Context;

import com.example.androidsample.database.AppDatabase;
import com.example.androidsample.model.Deliver;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HttpClient {

    private static final String BASE_URL = "https://mock-api-mobile.dev.lalamove.coms/";

    private static volatile HttpClient INSTANCE;

    private AppDatabase mDatabase;
    private AsyncHttpClient mClient;

    public static HttpClient getHttpClient(final Context context) {
        if (INSTANCE == null) {
            synchronized (HttpClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpClient(context);
                }
            }
        }
        return INSTANCE;
    }

    public HttpClient(Context context) {
        mDatabase = AppDatabase.getDatabase(context);

        mClient = new AsyncHttpClient();
        /// We initialize a default Keystore
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());// We load the KeyStore
            trustStore.load(null, null);
            // We initialize a new SSLSocketFacrory
            MySSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            // We set that all host names are allowed in the socket factory
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            mClient.setSSLSocketFactory(socketFactory);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public void getDelivers(final int offset, final int limit, final HttpResponseHandler<Deliver> responseHandler) {
        String fullUrl = BASE_URL + "deliveries?offset=" + offset + "&limit=" + limit;
        mClient.get(fullUrl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                responseHandler.onSuccessList(mDatabase.deliverDao().getDelivers(offset, limit));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONArray array = new JSONArray(responseString);
                    ArrayList<Deliver> responseArray = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Deliver deliver = new Deliver();
                        deliver.id = object.optInt("id");
                        deliver.desc = object.optString("description", "");
                        deliver.imageUrl = object.optString("imageUrl", "");
                        JSONObject locationObj = object.getJSONObject("location");
                        deliver.lat = locationObj.optDouble("lat", 0);
                        deliver.lng = locationObj.optDouble("lng", 0);
                        deliver.address = locationObj.optString("address", "");
                        responseArray.add(deliver);
                        mDatabase.deliverDao().insert(deliver);
                    }
                    responseHandler.onSuccessList(responseArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseHandler.onSuccessList(mDatabase.deliverDao().getDelivers(offset, limit));
                }
            }
        });
    }
}
