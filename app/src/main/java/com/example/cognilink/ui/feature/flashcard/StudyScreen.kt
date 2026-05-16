package com.example.cognilink.ui.feature.flashcard

import android.annotation.SuppressLint
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.Answer
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.Header
import com.example.cognilink.ui.components.flashcard.HintReveal
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White
import com.example.cognilink.viewModel.FlashcardViewModel
import kotlinx.coroutines.delay

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
    flashcardQuestion: String = "Qual organela é conhecida como a 'central elétrica' da célula?",
    answerControl: @Composable () -> Unit = {},
    isQuestionAnswered: Boolean = false,
    isQuestionVerified: Boolean = false,
    flashcardHints: List<String> = listOf(),
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
                    text = if (isQuestionVerified) "PROXÍMO FLASHCARD" else "VERIFICAR RESPOSTA",
                    icon = if (isQuestionVerified) R.drawable.ic_arrow_forward else R.drawable.ic_check,
                    iconRightSide = true,
                    isEnabled = isQuestionAnswered,
                    onClickButton = {
                        if (isQuestionVerified) {
                            onClickToNextFlashcard()
                        } else {
                            onClickToVerifyQuestion()
                        }
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    color = VeryLightGray.copy(alpha = 0.5f)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = CenterVertically,
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
                        verticalAlignment = CenterVertically,
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
                Text(text = flashcardQuestion,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkNavyBlue,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                    )
            }

            answerControl()

            HintReveal(hints = flashcardHints)

        }
    }

}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun FlashcardStudyContentPreview() {
    CogniLinkTheme {
        val previewViewModel = remember {
            FlashcardViewModel()
                .apply {
                    onQuestionTextChange("Qual é a capital da França?")
                    onDifficultyChange(DifficultyLevel.MEDIUM)
                    onTypeChange(FlashcardType.MULTIPLE_CHOICE)
                    updateAnswers(listOf(Answer("Paris", true), Answer("Londres", false), Answer("Roma", false)))
                    updateHints(listOf("Dica 1", "Dica 2"))
                }
        }

        FlashcardStudyContent(
            flashcardQuestion = "Qual é a capital da França?",
            answerControl = previewViewModel.viewAnswerControl(),
            isQuestionAnswered = previewViewModel.isQuestionAnswered,
            isQuestionVerified = previewViewModel.isQuestionVerified,
            flashcardHints = listOf("Dica 1", "Dica 2"),
            onClickToVerifyQuestion = { previewViewModel.verifyQuestion() },
            onClickToNextFlashcard = {},
        )
    }
}