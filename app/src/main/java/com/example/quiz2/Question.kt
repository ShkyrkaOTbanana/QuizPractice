package com.example.quiz2

data class Question(
    val question: String,
    var answers: ArrayList<String>,
    var rightAnswerIndex: Int
)
