package com.example.task61d;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Answers#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Answers extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Answers() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Answers.
     */
    // TODO: Rename and change types and number of parameters
    public static Answers newInstance(String param1, String param2) {
        Answers fragment = new Answers();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answers, container, false);

        // Step 1: Get the bundle arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            String response1 = bundle.getString("response1");
            String response2 = bundle.getString("response2");
            String response3 = bundle.getString("response3");

            // Step 2: Use these strings (e.g., set to TextViews)
            TextView textView1 = view.findViewById(R.id.response1);
            TextView textView2 = view.findViewById(R.id.response2);
            TextView textView3 = view.findViewById(R.id.response3);

            textView1.setText(response1);
            textView2.setText(response2);
            textView3.setText(response3);
             }

        // Inflate the layout for this fragment
        return view;
    }
}