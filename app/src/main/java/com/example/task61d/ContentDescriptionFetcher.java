package com.example.task61d;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ContentDescriptionFetcher {

    public interface ContentDescriptionCallback {
        void onSuccess(String description);
        void onError(String errorMessage);
    }

    private static final String TAG = "ContentDescFetcher";
    private static final String BASE_URL = "http://10.0.2.2:5001/getQuiz?desc=";

    public static void fetch(Context context, String content, ContentDescriptionCallback callback) {
        try {
            String encodedContent = URLEncoder.encode(content, "UTF-8");

            RequestQueue queue = Volley.newRequestQueue(context);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL + encodedContent, null,
                    response -> {
                        try {
                            Log.d(TAG, "fetch: " + response);
                            String description = response.getString("Description");
                            Log.d(TAG, "fetch: "+description);
                            callback.onSuccess(description);
                        } catch (Exception e) {
                            Log.e(TAG, "Parsing error: " + e.getMessage());
                            callback.onError("Failed to parse content description response.");
                        }
                    },
                    error -> {
                        String errorMsg = "Network error";
                        if (error.networkResponse != null) {
                            try {
                                errorMsg = "HTTP " + error.networkResponse.statusCode + ": " +
                                        new String(error.networkResponse.data);
                            } catch (Exception e) {
                                errorMsg = "HTTP " + error.networkResponse.statusCode + ": [data parse error]";
                            }
                        } else if (error.getMessage() != null) {
                            errorMsg = error.getMessage();
                        }
                        Log.e(TAG, "Request error: " + errorMsg);
                        callback.onError("Failed to fetch content description: " + errorMsg);
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            queue.add(request);

        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Encoding error: " + e.getMessage());
            callback.onError("Encoding error occurred.");
        }
    }
}
