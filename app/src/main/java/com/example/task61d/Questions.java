package com.example.task61d;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.*;

public class Questions extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private StringBuilder summaryText = new StringBuilder();

    private String mParam1;
    private String mParam2;

    public Questions() {}

    public static Questions newInstance(String param1, String param2) {
        Questions fragment = new Questions();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        TextView desc=view.findViewById(R.id.description);

        Bundle bundle = getArguments();
        if(bundle!=null){
            desc.setText(bundle.getString("desc"));
        }

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView progressText=view.findViewById(R.id.progresstext);
        LinearLayout question2_preview_frame = view.findViewById(R.id.question2_preview_frame);
        FrameLayout question2_full_frame = view.findViewById(R.id.question2_full_frame);
        LinearLayout question3_preview_frame = view.findViewById(R.id.question3_preview_frame);
        FrameLayout question3_full_frame = view.findViewById(R.id.question3_full_frame);
        FrameLayout question_frame = view.findViewById(R.id.question_frame);
        Button submitButton = view.findViewById(R.id.next_button);

        TextView q1 = view.findViewById(R.id.question);
        RadioGroup rg1 = view.findViewById(R.id.radioGroup1);
        RadioButton a1 = view.findViewById(R.id.option_a), b1 = view.findViewById(R.id.option_b), c1 = view.findViewById(R.id.option_c);

        TextView q2 = view.findViewById(R.id.question2);
        RadioGroup rg2 = view.findViewById(R.id.radioGroup2);
        RadioButton a2 = view.findViewById(R.id.option2_a), b2 = view.findViewById(R.id.option2_b), c2 = view.findViewById(R.id.option2_c);

        TextView q3 = view.findViewById(R.id.question3);
        RadioGroup rg3 = view.findViewById(R.id.radioGroup3);
        RadioButton a3 = view.findViewById(R.id.option3_a), b3 = view.findViewById(R.id.option3_b), c3 = view.findViewById(R.id.option3_c);

        ImageView expand1 = view.findViewById(R.id.expand_button1);
        ImageView expand2 = view.findViewById(R.id.expand_button2);

        DBHelper db = new DBHelper(getContext());
        User user = db.getUserDetails(User.getCurrentUser().getUsername());
        if (user == null) {
            Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
            return view;
        }


        List<String> questions = new ArrayList<>();
        List<List<String>> options = new ArrayList<>();
        List<String> currentOptions = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        progressText.setVisibility(View.VISIBLE);
        question_frame.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        question2_preview_frame.setVisibility(View.GONE);
DBHelper dbHelper=new DBHelper(getContext());
        QuizFetcher.fetch(getContext(), user.getInterests(), new QuizFetcher.QuizCallback() {
            @Override
            public void onSuccess(ArrayList<String> quizItems) {
                ArrayList<String> lines = new ArrayList<>();
                for (String item : quizItems) {
                    lines.addAll(Arrays.asList(item.split("\n")));
                }

                String currentQuestion = "";


                for (String line : lines) {
                    line = line.trim();
                    if (line.startsWith("Q")) {
                        if (!currentQuestion.isEmpty()) {
                            questions.add(currentQuestion);
                            options.add(new ArrayList<>(currentOptions));
                            currentOptions.clear();
                        }
                        currentQuestion = line.substring(line.indexOf(":") + 1).trim();
                    } else if (line.matches("^[A-D]:.*")) {
                        currentOptions.add(line.substring(3).trim());
                    }
                }

                if (!currentQuestion.isEmpty()) {
                    questions.add(currentQuestion);
                    options.add(currentOptions);
                }

                // Populate UI
                if (questions.size() >= 3) {
                    q1.setText("Q1: " + questions.get(0));
                    a1.setText(options.get(0).get(0));
                    b1.setText(options.get(0).get(1));
                    c1.setText(options.get(0).get(2));

                    q2.setText("Q2: " + questions.get(1));
                    a2.setText(options.get(1).get(0));
                    b2.setText(options.get(1).get(1));
                    c2.setText(options.get(1).get(2));

                    q3.setText("Q3: " + questions.get(2));
                    a3.setText(options.get(2).get(0));
                    b3.setText(options.get(2).get(1));
                    c3.setText(options.get(2).get(2));
                    Log.d("TAG", options.get(0).get(0)+" "+options.get(0).get(1)+options.get(0).get(2));
                    Log.d("TAG", options.get(1).get(0)+" "+options.get(1).get(1)+options.get(1).get(2));
                    Log.d("TAG", options.get(2).get(0)+" "+options.get(2).get(1)+options.get(2).get(2));

                }

                progressBar.setVisibility(View.GONE);
                progressText.setVisibility(View.GONE);
                question_frame.setVisibility(View.VISIBLE);
                question2_preview_frame.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("QUIZ", "Fetch failed: " + errorMessage);
                progressBar.setVisibility(View.GONE);
                progressText.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load quiz", Toast.LENGTH_SHORT).show();
            }
        });

        expand1.setOnClickListener(v -> {
            int selectedId1 = rg1.getCheckedRadioButtonId();
            if (selectedId1 != -1) {
                String answer1 = ((RadioButton) view.findViewById(selectedId1)).getText().toString();
                summaryText.append("Q1: ").append(q1.getText().toString()).append(" USER_ANS1: ").append(answer1).append(" CORRECT_ANS1 :"+currentOptions.get(0)).append(" | ");
                disableRadioGroupExcept(rg1, selectedId1);
                question2_preview_frame.setVisibility(View.GONE);
                question2_full_frame.setVisibility(View.VISIBLE);
                question3_preview_frame.setVisibility(View.VISIBLE);
                List<String> options1 = Arrays.asList(options.get(0).get(0),options.get(0).get(1),options.get(0).get(2),answer1,currentOptions.get(0));
                dbHelper.insertQuestionWithOptions(user.getUsername(), questions.get(0), options1);
                Toast.makeText(getContext(), "inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
            }
        });

        expand2.setOnClickListener(v -> {
            int selectedId2 = rg2.getCheckedRadioButtonId();
            if (selectedId2 != -1) {
                String answer2 = ((RadioButton) view.findViewById(selectedId2)).getText().toString();
                summaryText.append("Q2: ").append(q2.getText().toString()).append(" USER_ANS2: ").append(answer2).append(" CORRECT_ANS2 :"+currentOptions.get(1)).append(answer2).append(" | ");
                disableRadioGroupExcept(rg2, selectedId2);
                question3_preview_frame.setVisibility(View.GONE);
                question3_full_frame.setVisibility(View.VISIBLE);
                List<String> options2 = Arrays.asList(options.get(1).get(0), options.get(1).get(1),options.get(1).get(2),answer2,currentOptions.get(1));
                dbHelper.insertQuestionWithOptions(user.getUsername(), questions.get(1), options2);
                Toast.makeText(getContext(), "inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
            }
        });

        submitButton.setOnClickListener(v -> {
            int selectedId3 = rg3.getCheckedRadioButtonId();
            if (selectedId3 != -1) {
                submitButton.setText("Submitting...");
                String answer3 = ((RadioButton) view.findViewById(selectedId3)).getText().toString();
                summaryText.append("Q3: ").append(q3.getText().toString()).append(" USER_ANS3: ").append(answer3).append(" CORRECT_ANS3 :"+currentOptions.get(2));
                disableRadioGroupExcept(rg3, selectedId3);
                List<String> options3 = Arrays.asList(options.get(2).get(0),options.get(2).get(1),options.get(2).get(2),answer3,currentOptions.get(2));
                dbHelper.insertQuestionWithOptions(user.getUsername(), questions.get(2), options3);

                SummaryFetcher.fetch(getContext(), summaryText.toString(), new SummaryFetcher.SummaryCallback() {
                    @Override
                    public void onSuccess(String summary) {
                        String cleanedInput = summary.replace("**", "");
                        String[] parts = cleanedInput.split("\n\n");
                        Answers answers=new Answers();
                        Bundle bundle = new Bundle();
                        bundle.putString("response1", parts[0].trim().split("\n")[1]);
                        bundle.putString("response2", parts[1].trim().split("\n")[1]);
                        bundle.putString("response3",parts[2].trim().split("\n")[1] );
                        answers.setArguments(bundle);
                        loadFragment(answers);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }
    private void loadFragment(Fragment fragment) {

        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.main, fragment)
                .commit();
    }

    private void disableRadioGroupExcept(RadioGroup group, int selectedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child instanceof RadioButton) {
                child.setEnabled(child.getId() == selectedId);
            }
        }
    }
}
