package com.codylund.companion;

import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;


public class CheckpointRequest extends Request<String> {

    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImNvZHkiLCJpYXQiOjE1MjU1MzUzOTR9.dHJmuizC6Frk98tcSUq8THMhuCiVx4Lq-GZAWwU7gK4";
    public static final String API_ADD = Settings.IP + "/add";

    private Response.Listener<String> mListener;
    private HttpEntity mHttpEntity;

    public CheckpointRequest(CheckpointItem checkpointItem, Response.ErrorListener errorListener, Response.Listener listener) {
        super(Method.POST, API_ADD, errorListener);
        mListener = listener;

        // Prepare the image file if a path is specified
        File file = (checkpointItem.getImagePath() == null) ? null : new File(checkpointItem.getImagePath());

        // Build the HTTP entity for the request
        mHttpEntity = buildEntity(
                checkpointItem.getTime(),
                checkpointItem.getLat(),
                checkpointItem.getLng(),
                checkpointItem.getText(),
                file);

        // Wait for 30 seconds and don't retry if no response is received by then
        this.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(30),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
    }

    private HttpEntity buildEntity(String time, double lat, double lng, String text, File file) {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.seContentType(ContentType.MULTIPART_FORM_DATA);
        builder.setCharset(StandardCharsets.UTF_8);
        builder.addTextBody("time", time);
        builder.addTextBody("lat", String.valueOf(lat));
        builder.addTextBody("lng", String.valueOf(lng));
        if (text != null)
            builder.addTextBody("text", text);
        if (file != null)
            builder.addBinaryBody("attachment", file);
        return builder.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            mHttpEntity.writeTo(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            VolleyLog.e("" + e);
            return null;
        } catch (OutOfMemoryError e){
            VolleyLog.e("" + e);
            return null;
        }

    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data),
                    getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Authorization", TOKEN);
        return params;
    }
}
