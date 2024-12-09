package com.example.virtualfitting.screens

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class SurveyViewModel : ViewModel() {
    var currentQuestion = mutableIntStateOf(1) // 현재 질문 번호
    val answers = mutableStateMapOf<Int, List<String>>() // 답변 상태 저장

    fun updateAnswers(question: Int, selectedAnswers: List<String>) {
        answers[question] = selectedAnswers
    }

    fun goToNextQuestion() {
        if (currentQuestion.intValue < 4) currentQuestion.intValue++
    }

    fun goToPreviousQuestion() {
        if (currentQuestion.intValue > 1) currentQuestion.intValue--
    }
}
