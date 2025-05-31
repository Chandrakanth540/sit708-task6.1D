package com.example.task61d;

public class Question {
    String questionText;
    String[] options;
    int correctOptionIndex;  // index in options array
    int userAnswerIndex;     // user-selected index, -1 if none

    public Question(String questionText, String[] options, int correctOptionIndex, int userAnswerIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.userAnswerIndex = userAnswerIndex;
    }
}
