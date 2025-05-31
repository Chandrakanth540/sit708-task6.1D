package com.example.task61d;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

public class History extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public History() {
        // Required empty public constructor
    }

    public static History newInstance(String param1, String param2) {
        History fragment = new History();
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

    private SpannableStringBuilder getLabeledAnswer(String mainText, String label, int labelColor, String space) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();

        SpannableString labelSpan = new SpannableString(label);
        labelSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#E5E7E9")), 0, label.length(), 0);
        ssb.append(labelSpan);
        ssb.append(space);
        SpannableString mainTextSpan = new SpannableString(mainText);
        mainTextSpan.setSpan(new ForegroundColorSpan(labelColor), 0, mainText.length(), 0);
        ssb.append(mainTextSpan);

        return ssb;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        DBHelper dbHelper = new DBHelper(getContext());
        User user = dbHelper.getUserDetails(User.getCurrentUser().getUsername());
        String username = user.getUsername();

        // Get references to UI elements
        RadioButton option1 = view.findViewById(R.id.option1);
        RadioButton option2 = view.findViewById(R.id.option2);
        RadioButton option3 = view.findViewById(R.id.option3);

        RadioButton option1_q2 = view.findViewById(R.id.option1_q2);
        RadioButton option2_q2 = view.findViewById(R.id.option2_q2);
        RadioButton option3_q2 = view.findViewById(R.id.option3_q2);

        RadioButton option1_q3 = view.findViewById(R.id.option1_q3);
        RadioButton option2_q3 = view.findViewById(R.id.option2_q3);
        RadioButton option3_q3 = view.findViewById(R.id.option3_q3);

        TextView question1 = view.findViewById(R.id.question1_his);
        TextView question2 = view.findViewById(R.id.question2_his);
        TextView question3 = view.findViewById(R.id.question3_his);

        List<QuestionWithOptions> recentQuestions = dbHelper.getLast3QuestionsWithOptions(username);

        for (QuestionWithOptions q : recentQuestions) {
            Log.d("DB_DEBUG", "Question: " + q);

            List<String> options = q.options;
            for (int i = 0; i < options.size(); i++) {
                Log.d("DB_DEBUG", "Option " + (i + 1) + ": " + options.get(i));
            }
        }

        if (recentQuestions.size() > 0) {
            QuestionWithOptions q1 = recentQuestions.get(0);
            question1.setText(q1.question);
            String userAnswer = q1.options.get(3);
            String correctAnswer = q1.options.get(4);

            injectAnswerColor(option1, q1.options.get(0), userAnswer, correctAnswer);
            injectAnswerColor(option2, q1.options.get(1), userAnswer, correctAnswer);
            injectAnswerColor(option3, q1.options.get(2), userAnswer, correctAnswer);
        }

        if (recentQuestions.size() > 1) {
            QuestionWithOptions q2 = recentQuestions.get(1);
            question2.setText(q2.question);
            String userAnswer = q2.options.get(3);
            String correctAnswer = q2.options.get(4);

            injectAnswerColor(option1_q2, q2.options.get(0), userAnswer, correctAnswer);
            injectAnswerColor(option2_q2, q2.options.get(1), userAnswer, correctAnswer);
            injectAnswerColor(option3_q2, q2.options.get(2), userAnswer, correctAnswer);
        }

        if (recentQuestions.size() > 2) {
            QuestionWithOptions q3 = recentQuestions.get(2);
            question3.setText(q3.question);
            String userAnswer = q3.options.get(3);
            String correctAnswer = q3.options.get(4);

            injectAnswerColor(option1_q3, q3.options.get(0), userAnswer, correctAnswer);
            injectAnswerColor(option2_q3, q3.options.get(1), userAnswer, correctAnswer);
            injectAnswerColor(option3_q3, q3.options.get(2), userAnswer, correctAnswer);
        }

        // Setup toggle functionality
        LinearLayout answerGroupQ1 = view.findViewById(R.id.answerGroup);
        ImageButton toggleBtnQ2 = view.findViewById(R.id.btn_toggle_options_q2);
        LinearLayout answerGroupQ2 = view.findViewById(R.id.answerGroup_q2);
        ImageButton toggleBtnQ3 = view.findViewById(R.id.btn_toggle_options_q3);
        LinearLayout answerGroupQ3 = view.findViewById(R.id.answerGroup_q3);

        String ques2 = question2.getText().toString();
        String ques3 = question3.getText().toString();

        question2.setText("Question 2");
        question3.setText("Question 3");

        question2.setTag("2");
        setupToggle(toggleBtnQ2, answerGroupQ2, question2, ques2);

        question3.setTag("3");
        setupToggle(toggleBtnQ3, answerGroupQ3, question3, ques3);

        return view;
    }

    private void setupToggle(ImageButton toggleBtn, LinearLayout answerGroup, TextView questionView, String originalText) {
        toggleBtn.setOnClickListener(v -> {
            boolean isVisible = answerGroup.getVisibility() == View.VISIBLE;
            answerGroup.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            toggleBtn.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
            questionView.setText(isVisible ? "Question " + questionView.getTag() : originalText);
        });

        answerGroup.setOnClickListener(v -> {
            answerGroup.setVisibility(View.GONE);
            toggleBtn.setVisibility(View.VISIBLE);
            questionView.setText("Question " + questionView.getTag());
        });
    }

    private void injectAnswerColor(RadioButton button, String optionText, String userAnswer, String correctAnswer) {
        if (optionText.equals(correctAnswer)) {
            GreenColorInject(button, optionText);
        } else if (optionText.equals(userAnswer)) {
            RedColorInject(button, optionText);
        } else {
            WhiteColorInject(button, optionText);
        }
    }

    public RadioButton GreenColorInject(RadioButton button, String option) {
        button.setText(getLabeledAnswer(option, "Correct answer", Color.GREEN, "    "));
        button.setChecked(true);
        button.setButtonTintList(ColorStateList.valueOf(Color.GREEN));
        button.setEnabled(false);
        return button;
    }

    public RadioButton RedColorInject(RadioButton button, String option) {
        button.setText(getLabeledAnswer(option, "Your answer", Color.RED, "         "));
        button.setChecked(true);
        button.setButtonTintList(ColorStateList.valueOf(Color.RED));
        button.setEnabled(false);
        return button;
    }

    public RadioButton WhiteColorInject(RadioButton button, String option) {
        button.setText(option); // Optional: No label for unchosen options
        button.setChecked(false);
        button.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
        button.setEnabled(false);
        return button;
    }
}
