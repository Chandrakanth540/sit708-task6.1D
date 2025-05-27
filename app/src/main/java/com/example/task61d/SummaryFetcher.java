package com.example.task61d;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SummaryFetcher {

    public interface SummaryCallback {
        void onSuccess(String summary);
        void onError(String errorMessage);
    }

    private static final String TAG = "SummaryFetcher";
    private static final String BASE_URL = "http://10.0.2.2:5001/getQuiz?summary=";

    public static void fetch(Context context, String answers, SummaryCallback callback) {



            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL+answers, null,
                    response -> {
                        try {
                            Log.d(TAG, "fetch: "+response);
                            String summary = response.getString("summary");

                            callback.onSuccess(summary);
                        } catch (Exception e) {
                            Log.e(TAG, "Parsing error: " + e.getMessage());
                            callback.onError("Failed to parse summary response.");
                        }
                    },
                    error -> {
                        String errorMsg = "Network error";
                        if (error.networkResponse != null) {
                            errorMsg = "HTTP " + error.networkResponse.statusCode + ": " +
                                    new String(error.networkResponse.data);
                        } else if (error.getMessage() != null) {
                            errorMsg = error.getMessage();
                        }
                        Log.e(TAG, "Request error: " + errorMsg);
                        callback.onError("Failed to fetch summary: " + errorMsg);
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            queue.add(request);

}}
