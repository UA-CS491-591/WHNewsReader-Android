package com.flesh.washingtonhearld.app.VolleyUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.flesh.washingtonhearld.app.Objects.Login.DtoLoginResponse;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * Created by aaronfleshner on 5/16/14.
 */
public class GsonRequest<T> extends JsonRequest<T> {

    Class<T> mResponseClass;

    public GsonRequest(int method, String url, String requestBody, Class<T> responseClass, Response.Listener<T> listener,
                       Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        mResponseClass = responseClass;
        // Do some generic stuff in here - for example, set your retry policy to
        // longer if you know all your requests are going to take > 2.5 seconds
        // etc etc...
    }



    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString = new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            T response = new GsonBuilder().create().fromJson(jsonString, mResponseClass);
            com.android.volley.Response<T> result = com.android.volley.Response.success(response,
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
            return result;
        } catch (UnsupportedEncodingException e) {
            return com.android.volley.Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return com.android.volley.Response.error(new ParseError(e));
        }
    }

}