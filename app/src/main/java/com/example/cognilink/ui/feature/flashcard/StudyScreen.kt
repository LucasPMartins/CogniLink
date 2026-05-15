package com.example.cognilink.ui.feature.flashcard

import android.annotation.SuppressLint
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.Answer
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.Header
import com.example.cognilink.ui.components.flashcard.HintReveal
import com.example.cognilink.ui.components.flashcard.AnswerOptions
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White
import kotlinx.coroutines.delay
import kotlin.collections.plus

@SuppressLint("DefaultLocale")
fun formatSeconds(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    if(h == 0L) return String.format("%02d:%02d", m, s)
    return String.format("%02d:%02d:%02d", h, m, s)
}

@Composable
fun FlashcardStudyContent(
    modifier: Modifier = Modifier,
    flascardQuestion: String = "Qual organela é conhecida como a 'central elétrica' da célula?",
    flashcardAnswersOptions: List<Answer> = listOf(),
    inputTextAnswer: String,
    onInputTextAnswerChange: (String) -> Unit,
    selectedAnswer: Answer? = null,
    onSelectAnswer: (Answer?) -> Unit = {},
    selectedAnswers: List<Answer> = listOf(),
    onToggleAnswer: (Answer) -> Unit = {},
    flashcardHints: List<String> = listOf(),
    flashcardType: FlashcardType,
    isQuestionVerified: Boolean = false,
    isQuestionAnswered: Boolean = false,
    onClickToVerifyQuestion: () -> Unit = {},
    onClickToNextFlashcard: () -> Unit = {}
    ) {

    var secondsElapsed by remember { mutableLongStateOf(0L) }

    var sequenceHits by remember { mutableIntStateOf(0) }

    // LaunchedEffect inicia quando o componente entra na tela
    // O 'Unit' garante que ele rode apenas uma vez e não reinicie sozinho
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Espera 1 segundo
            secondsElapsed++
        }
    }

    val formattedTime = formatSeconds(secondsElapsed)

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { Header() },
        containerColor = OffWhite,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if (isQuestionVerified) "PROXÍMO FlASHCARD" else "VERIFICAR RESPOSTA",
                    icon = if (isQuestionVerified) R.drawable.ic_arrow_forward else R.drawable.ic_check,
                    iconRightSide = true,
                    isEnabled = isQuestionAnswered,
                    onClickButton = {
                        if (isQuestionVerified) onClickToNextFlashcard() else onClickToVerifyQuestion()
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Row(verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = VeryLightGray.copy(alpha = 0.5f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = DarkNavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "TEMPO: $formattedTime",
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGray,
                            fontSize = 12.sp
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = VeryLightGray.copy(alpha = 0.5f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cached),
                            contentDescription = null,
                            tint = DarkNavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "SEQUÊNCIA: $sequenceHits",
                            fontWeight = FontWeight.SemiBold,
                            color = DarkGray,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(32.dp),
                shadowElevation = 2.dp,
                color = White
            ) {
                Text(text = flascardQuestion,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkNavyBlue,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                    )
            }

            when (flashcardType){
                FlashcardType.BASIC -> {
                    CustomTextField(
                        inputValue = inputTextAnswer,
                        onInputValueChange = onInputTextAnswerChange,
                        placeholder = "Sua resposta",
                        minLines = 3
                    )
                }
                FlashcardType.MULTIPLE_CHOICE -> {
                    AnswerOptions(
                        flashcardType = flashcardType,
                        answerOptions = flashcardAnswersOptions,
                        selectedAnswer = selectedAnswer,
                        onSelectedAnswer = onSelectAnswer
                    )
                }
                FlashcardType.TRUE_OR_FALSE -> {
                    AnswerOptions(
                        flashcardType = flashcardType,
                        answerOptions = flashcardAnswersOptions,
                        selectedAnswers = selectedAnswers,
                        onToggleAnswer = onToggleAnswer
                    )
                }
                    FlashcardType.OMISSION -> {
                    TODO()
                }

                FlashcardType.CHAT_FEYNMAN -> {
                    TODO()
                }
            }

            HintReveal(hints = flashcardHints)

        }
    }

}

@Preview
@Composable
private fun FlashcardStudyContentPreview() {
    CogniLinkTheme {
        var listaTeste by remember { mutableStateOf(listOf(Answer("Resposta 1", true), Answer("Resposta 2", false), Answer("Resposta 3", false))) }

        var selectedAnswer by remember { mutableStateOf<Answer?>(null) }
        var selectedAnswers by remember { mutableStateOf(listOf<Answer>()) }

        var inputTextAnswer by remember { mutableStateOf("") }

        FlashcardStudyContent(
            flashcardAnswersOptions = listaTeste,
            inputTextAnswer = inputTextAnswer,
            onInputTextAnswerChange = {inputTextAnswer = it},
            selectedAnswer = selectedAnswer,
            onSelectAnswer = { selectedAnswer = it },
            flashcardHints = listOf("Dica 1", "Dica 2", "Dica 3"),
            selectedAnswers = selectedAnswers,
            flashcardType = FlashcardType.BASIC,
            onToggleAnswer = { answer ->
                selectedAnswers = if (selectedAnswers.contains(answer)) {
                    selectedAnswers - answer
                } else {
                    selectedAnswers + answer
                }
            }
        )
    }
}