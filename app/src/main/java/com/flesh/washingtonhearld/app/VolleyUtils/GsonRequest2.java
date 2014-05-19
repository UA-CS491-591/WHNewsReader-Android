package com.flesh.washingtonhearld.app.VolleyUtils;

/**
 * Created by aaronfleshner on 5/17/14.
 */

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Volley adapter for JSON requests with POST method that will be parsed into Java objects by Gson.
 */
public class GsonRequest2<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private Map<String, String> params;
    /**
     * Make a GET request and return a parsed object from JSON. Assumes
     * {@link Method#GET}.
     *
     * @param url
     *            URL of the request to make
     * @param clazz
     *            Relevant class object, for Gson's reflection
     * @param headers
     *            Map of request headers
     */
    public GsonRequest2(String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    /**
     * Like the other, but allows you to specify which {@link Method} you want.
     *
     * @param method
     * @param url
     * @param clazz
     * @param headers
     * @param listener
     * @param errorListener
     */
    public GsonRequest2(int method, String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}