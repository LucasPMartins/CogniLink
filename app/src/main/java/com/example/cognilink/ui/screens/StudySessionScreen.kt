package com.example.cognilink.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cognilink.R
import com.example.cognilink.data.model.Answer
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.preview.PreviewDataProvider
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerSelector
import com.example.cognilink.ui.components.flashcard.FlashcardHeader
import com.example.cognilink.ui.components.flashcard.HintReveal
import com.example.cognilink.ui.components.flashcard.TrueFalseToggle
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.states.AnswerVisualState
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.viewmodels.StudySessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StudySessionScreen(
    studyMode: String,
    contextId: String,
    onNavigateBack: () -> Unit,
    viewModel: StudySessionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(studyMode, contextId) {
        viewModel.initializeSession(studyMode, contextId)
    }

    LaunchedEffect(uiState.isSessionFinished) {
        if (uiState.isSessionFinished && !uiState.isSessionInsightDialogOpen) {
            viewModel.toggleSessionInsightDialog()
        }
    }

    uiState.currentFlashcard?.let { flashcard ->
        StudySessionContent(
            flashcard = flashcard,
            currentFlashcardIndex = uiState.currentFlashcardIndex,
            totalFlashcards = uiState.sessionFlashcards.size,
            sessionTitle = uiState.sessionTitle,
            selectedAnswers = uiState.selectedAnswers,
            onSelectAnswer = viewModel::onSelectAnswer,
            isQuestionAnswered = uiState.isQuestionAnswered,
            isQuestionVerified = uiState.isQuestionVerified,
            isCloseDialogOpen = uiState.isCloseDialogOpen,
            isSessionInsightDialogOpen = uiState.isSessionInsightDialogOpen,
            isLastFlashcard = uiState.isLastFlashcard,
            elapsedTime = viewModel.formatSeconds(uiState.secondsElapsed),
            sequenceHits = uiState.sequenceHits,
            onDismissSessionInsight = {
                viewModel.toggleSessionInsightDialog()
                scope.launch {
                    delay(100)
                    onNavigateBack()
                }
            },
            onCloseClick = viewModel::toggleCloseDialog,
            onAcceptCloseDialog = {
                viewModel.toggleCloseDialog()
                scope.launch {
                    delay(100)
                    onNavigateBack()
                }
            },
            onDismissCloseDialog = viewModel::toggleCloseDialog,
            onClickToVerifyQuestion = viewModel::verifyQuestion,
            onClickToNextFlashcard = viewModel::nextFlashcard,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySessionContent(
    modifier: Modifier = Modifier,
    flashcard: Flashcard,
    currentFlashcardIndex: Int,
    totalFlashcards: Int,
    sessionTitle: String = "",
    selectedAnswers: Map<Answer, String> = mapOf(),
    onSelectAnswer: (Answer, String) -> Unit = { _, _ -> },
    isQuestionAnswered: Boolean,
    isQuestionVerified: Boolean,
    isCloseDialogOpen: Boolean = false,
    isSessionInsightDialogOpen: Boolean = false,
    isLastFlashcard: Boolean = false,
    elapsedTime: String,
    sequenceHits: Int = 0,
    onDismissSessionInsight: () -> Unit = {},
    onCloseClick: () -> Unit = {},
    onAcceptCloseDialog: () -> Unit = {},
    onDismissCloseDialog: () -> Unit = {},
    onClickToVerifyQuestion: () -> Unit = {},
    onClickToNextFlashcard: () -> Unit = {}
) {

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = {
            FlashcardHeader(
                title = sessionTitle, onCloseClick = onCloseClick,
                actualCard = currentFlashcardIndex + 1,
                totalCards = totalFlashcards
            )
        },
        containerColor = OffWhite,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = when {
                        isQuestionVerified && isLastFlashcard -> "FINALIZAR SESSÃO"
                        isQuestionVerified -> "PROXÍMO FLASHCARD"
                        else -> "VERIFICAR RESPOSTA"
                    },
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
        ) {
            if (isCloseDialogOpen) {
                BasicAlertDialog(
                    onDismissRequest = onDismissCloseDialog,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(28.dp),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Tem certeza disso?",
                                fontWeight = FontWeight.Bold,
                                color = DarkNavyBlue,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                text = "O progresso não será salvo!",
                                color = DarkGray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            SimpleGradientButton(
                                text = "Sim",
                                height = 56.dp,
                                onClickButton = onAcceptCloseDialog
                            )
                            SimpleGradientButton(
                                text = "Cancelar",
                                height = 56.dp,
                                onClickButton = onDismissCloseDialog
                            )
                        }
                    }
                }
            } else if (isSessionInsightDialogOpen) {
                BasicAlertDialog(
                    onDismissRequest = onDismissSessionInsight,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(28.dp),
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Sessão Finalizada!",
                                fontWeight = FontWeight.Bold,
                                color = DarkNavyBlue,
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Você completou todos os flashcards desta sessão.",
                                color = DarkGray,
                                textAlign = TextAlign.Center
                            )
                            SimpleGradientButton(
                                text = "Voltar ao Início",
                                onClickButton = onDismissSessionInsight
                            )
                        }
                    }
                }
            }
            Row(
                verticalAlignment = CenterVertically,
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
                            text = "TEMPO: $elapsedTime",
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

            AnimatedContent(
                targetState = flashcard,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                }
            ) { targetFlashcard ->
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Surface(
                        shape = RoundedCornerShape(32.dp),
                        shadowElevation = 2.dp,
                        color = White
                    ) {
                        Text(
                            text = targetFlashcard.question,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkNavyBlue,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(32.dp)
                                .fillMaxWidth()
                        )
                    }

                    when (targetFlashcard.cardType) {
                        FlashcardType.BASIC -> {
                            CustomTextField(
                                inputValue = selectedAnswers.values.firstOrNull() ?: "",
                                onInputValueChange = { newAnswer ->
                                    onSelectAnswer(Answer("", false), newAnswer)
                                },
                                placeholder = "Sua resposta",
                                minLines = 3
                            )
                        }

                        FlashcardType.TRUE_OR_FALSE -> {
                            AnswerSelector(
                                answerOptions = targetFlashcard.answerOptions,
                                getVisualState = { answer ->
                                    val userChoice = selectedAnswers[answer] // "T" ou "F" ou null
                                    val isAnswerCorrectType = answer.isCorrect

                                    if (isQuestionVerified) {
                                        when {
                                            (userChoice == "T" && isAnswerCorrectType) || (userChoice == "F" && !isAnswerCorrectType) -> {
                                                AnswerVisualState.Correct
                                            }

                                            userChoice != null -> AnswerVisualState.Incorrect
                                            else -> AnswerVisualState.Default
                                        }
                                    } else {
                                        if (userChoice != null) AnswerVisualState.Selected else AnswerVisualState.Default
                                    }
                                },
                                selectionControl = { answer, _ ->
                                    if (!isQuestionVerified) {
                                        TrueFalseToggle(
                                            currentValue = selectedAnswers[answer],
                                            onToggle = { choice ->
                                                onSelectAnswer(answer, choice)
                                            },
                                            enabled = true
                                        )
                                    }
                                },
                            )

                        }

                        FlashcardType.MULTIPLE_CHOICE -> {
                            AnswerSelector(
                                answerOptions = targetFlashcard.answerOptions,
                                getVisualState = { answer ->
                                    val isSelected = selectedAnswers.contains(answer)
                                    if (isQuestionVerified) {
                                        if (answer.isCorrect) AnswerVisualState.Correct
                                        else if (isSelected) AnswerVisualState.Incorrect
                                        else AnswerVisualState.Default
                                    } else {
                                        if (isSelected) AnswerVisualState.Selected else AnswerVisualState.Default
                                    }
                                },
                                selectionControl = { answer, _ ->
                                    if (!isQuestionVerified) {
                                        RadioButton(
                                            selected = (selectedAnswers.contains(answer)),
                                            onClick = { onSelectAnswer(answer, "") },
                                            enabled = true,
                                            colors = RadioButtonDefaults.colors(selectedColor = DarkNavyBlue),
                                        )
                                    }
                                }
                            )

                        }

                        FlashcardType.OMISSION -> {

                        }

                        FlashcardType.CHAT_FEYNMAN -> {

                        }
                    }

                    HintReveal(hints = targetFlashcard.hints)
                }
            }


        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StudySessionContentPreview() {
    CogniLinkTheme {
        StudySessionContent(
            flashcard = PreviewDataProvider.flashcard,
            currentFlashcardIndex = 0,
            totalFlashcards = 0,
            selectedAnswers = emptyMap(),
            isQuestionAnswered = false,
            isQuestionVerified = false,
            isCloseDialogOpen = false,
            elapsedTime = "00:00",
        )
    }
}

