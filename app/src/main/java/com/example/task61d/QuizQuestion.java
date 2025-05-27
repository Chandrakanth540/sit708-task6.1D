package com.example.task61d;

import java.util.List;

public class QuizQuestion {
    public String questionText;
    public List<String> options;

    public QuizQuestion(String questionText, List<String> options) {
        this.questionText = questionText;
        this.options = options;
    }
}


