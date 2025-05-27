package com.example.task61d;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizFetcher {

    public interface QuizCallback {
        void onSuccess(ArrayList<String> quizItems);
        void onError(String errorMessage);
    }

    private static final String TAG = "QuizFetcher";

    private static final String URL = "http://10.0.2.2:5001/getQuiz?topic=";

    public static void fetch(Context context,String interests ,QuizCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL+interests, null,
                response -> {
                    try {
                        ArrayList<String> quizItems = new ArrayList<>();
                        JSONArray quizArray = response.getJSONArray("quiz");

                        for (int i = 0; i < quizArray.length(); i++) {
                            JSONObject questionObj = quizArray.getJSONObject(i);
                            String question = questionObj.getString("question");
                            JSONArray options = questionObj.getJSONArray("options");
                            String correctAnswer = questionObj.getString("correct_answer");

                            StringBuilder formatted = new StringBuilder();
                            formatted.append("Q").append(i + 1).append(": ").append(question).append("\n");
                            for (int j = 0; j < options.length(); j++) {
                                formatted.append((char) ('A' + j)).append(": ").append(options.getString(j)).append("\n");
                            }
                            formatted.append("Correct Answer: ").append(correctAnswer);

                            quizItems.add(formatted.toString());
                        }

                        callback.onSuccess(quizItems);

                    } catch (Exception e) {
                        Log.e(TAG, "JSON Parsing Error: " + e.getMessage());
                        callback.onError("Error parsing data!");
                    }
                },
                error -> {
                    String errorMsg = "Error";
                    if (error.networkResponse != null) {
                        errorMsg = "HTTP " + error.networkResponse.statusCode + ": " +
                                new String(error.networkResponse.data);
                    } else if (error.getMessage() != null) {
                        errorMsg = error.getMessage();
                    }
                    Log.e(TAG, "Request Error: " + errorMsg);
                    callback.onError("Failed to load quiz: " + errorMsg);
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(request);
    }
}

