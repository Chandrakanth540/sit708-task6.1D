package com.example.task61d;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Intrests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Intrests extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Intrests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Intrests.
     */
    // TODO: Rename and change types and number of parameters
    public static Intrests newInstance(String param1, String param2) {
        Intrests fragment = new Intrests();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intrests, container, false);

        DBHelper dbHelper = new DBHelper(getContext());

        FlexboxLayout topicContainer = view.findViewById(R.id.topic_container);
        Button nextButton = view.findViewById(R.id.next_button);






        nextButton.setOnClickListener(v -> {
            List<String> selectedTopics = new ArrayList<>();

            for (int i = 0; i < topicContainer.getChildCount(); i++) {
                View child = topicContainer.getChildAt(i);
                if (child instanceof Chip) {
                    Chip chip = (Chip) child;
                    if (chip.isChecked()) {
                        selectedTopics.add(chip.getText().toString());
                    }
                }
            }

            if (selectedTopics.isEmpty()) {
                Toast.makeText(getContext(), "Please select up to 10 topics", Toast.LENGTH_SHORT).show();
                return;
            }

            String interestString = android.text.TextUtils.join("&", selectedTopics);

            User currentUser = User.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String username = currentUser.getUsername();
            int userId = dbHelper.getUserId(username);
            Bundle bundle = new Bundle();
            if (userId != -1 && dbHelper.updateUserInterests(userId, interestString)) {

                nextButton.setText("Processing...");
                ContentDescriptionFetcher.fetch(getContext(), interestString, new ContentDescriptionFetcher.ContentDescriptionCallback() {
                    @Override
                    public void onSuccess(String description) {
                        bundle.putString("desc",description);
                        bundle.putString("user_interests", interestString);

                        Home homeFragment = new Home();
                        homeFragment.setArguments(bundle);
                        loadFragment(homeFragment);
                        // Handle successful response
                        Log.d("RESULT", "Description: " + description);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle error
                        Log.e("ERROR", errorMessage);
                        bundle.putString("user_interests", interestString);

                        Home homeFragment = new Home();
                        homeFragment.setArguments(bundle);
                        loadFragment(homeFragment);
                    }
                });




            } else {
                Toast.makeText(getContext(), "Failed to update interests", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
    private void loadFragment(Fragment fragment) {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .addToBackStack(null)
                .commit();
    }

}