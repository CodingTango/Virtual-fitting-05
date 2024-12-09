package com.example.virtualfitting.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class SurveyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SurveyScreen(
                onSurveyCompleted = { finish() } // 종료 콜백 전달
            )
        }
    }
}


@Composable
fun SurveyScreen(
    viewModel: SurveyViewModel = viewModel(),
    onSurveyCompleted: () -> Unit // 종료 콜백
) {
    val context = LocalContext.current
    val currentQuestion = viewModel.currentQuestion.intValue // intValue 사용
    val answers = viewModel.answers // answers 직접 접근

    // 제출 버튼 활성화 여부를 실시간으로 추적
    val submitButtonEnabled = (1..4).all { question ->
        !answers[question].isNullOrEmpty()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "$currentQuestion / 4",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(20.dp))

        when (currentQuestion) {
            1 -> QuestionScreen(
                question = "선호하는 색상을 선택하세요:",
                options = listOf("검은색", "흰색", "파란색", "갈색"),
                initialSelections = answers[currentQuestion] ?: emptyList(),
                onSelect = { selected -> viewModel.updateAnswers(1, selected) }
            )
            2 -> QuestionScreen(
                question = "선호하는 옷 종류를 선택하세요:",
                options = listOf("가디건", "셔츠", "티셔츠", "니트"),
                initialSelections = answers[currentQuestion] ?: emptyList(),
                onSelect = { selected -> viewModel.updateAnswers(2, selected) }
            )
            3 -> QuestionScreen(
                question = "선호하시는 가격대를 선택하세요:",
                options = listOf("비쌈", "적당함", "저렴함"),
                initialSelections = answers[currentQuestion] ?: emptyList(),
                onSelect = { selected ->
                    // 선택 항목 변환 후 저장
                    val mapped = selected.map {
                        when (it) {
                            "비쌈" -> "high"
                            "적당함" -> "middle"
                            "저렴함" -> "low"
                            else -> it
                        }
                    }
                    viewModel.updateAnswers(3, mapped)
                }
            )
            4 -> QuestionScreen(
                question = "프린팅 유무를 선택하세요:",
                options = listOf("있음", "없음"),
                initialSelections = answers[currentQuestion] ?: emptyList(),
                onSelect = { selected ->
                    // 선택 항목 변환 후 저장
                    val mapped = selected.map {
                        when (it) {
                            "있음" -> "yes"
                            "없음" -> "no"
                            else -> it
                        }
                    }
                    viewModel.updateAnswers(4, mapped)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentQuestion > 1) {
                Button(onClick = { viewModel.goToPreviousQuestion() }) {
                    Text("이전")
                }
            }

            if (currentQuestion < 4) {
                Button(onClick = { viewModel.goToNextQuestion() }) {
                    Text("다음")
                }
            } else {
                // 제출 버튼 활성화/비활성화
                Button(
                    onClick = {
                        submitAnswers(context, answers)
                        onSurveyCompleted()
                    },
                    enabled = submitButtonEnabled // 활성화 여부
                ) {
                    Text("제출")
                }
            }
        }
    }
}



fun submitAnswers(context: Context, answers: Map<Int, List<String>>) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val cloudRunUrl = "https://asia-east2-virtual-fitting-05-438415.cloudfunctions.net/upload-survey-result"
            val jsonData = Gson().toJson(answers)
            val requestBody = jsonData.toRequestBody("application/json".toMediaType())

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url(cloudRunUrl)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Log.d("submitAnswers", "설문조사 결과 업로드 성공")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "성공적으로 업로드되었습니다!", Toast.LENGTH_SHORT).show()
                }
            } else {
                val errorBody = response.body?.string()
                Log.e("submitAnswers", "업로드 실패: ${response.code} - $errorBody")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "업로드 실패: ${response.code} - $errorBody", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Log.e("submitAnswers", "업로드 중 오류 발생", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "업로드 중 오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
