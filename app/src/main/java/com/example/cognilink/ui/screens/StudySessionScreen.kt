package com.example.cognilink.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cognilink.R
import com.example.cognilink.data.model.Answer
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.preview.PreviewDataProvider
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerEditor
import com.example.cognilink.ui.components.flashcard.AnswerSelector
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.FlashcardHeader
import com.example.cognilink.ui.components.flashcard.HintEditor
import com.example.cognilink.ui.components.flashcard.HintReveal
import com.example.cognilink.ui.components.flashcard.TrueFalseToggle
import com.example.cognilink.ui.components.flashcard.TypeSelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.DeleteButton
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.states.AnswerVisualState
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Green
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.viewmodels.FlashcardFormViewModel
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
            // If session is finished but dialog isn't forced open,
            // we might want to auto-open it or handle final navigation.
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
            isSessionFinished = uiState.isSessionFinished,
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
    isSessionFinished: Boolean = false,
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
            } else if (isSessionInsightDialogOpen || isSessionFinished) {
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
                                    onSelectAnswer(Answer(newAnswer, false), "")
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
                                            enabled = !isQuestionVerified
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
                                            enabled = !isQuestionVerified,
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

@SuppressLint("ViewModelConstructorInComposable")
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
            isSessionFinished = false,
            elapsedTime = "00:00",
        )
    }
}

@Composable
fun FlashcardEditorScreen(
    viewModel: FlashcardFormViewModel = koinViewModel(),
    flashcardId: String? = null,
    deckId: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(flashcardId, deckId) {
        viewModel.initialize(deckId, flashcardId)
    }

    FlashcardEditorContent(
        questionText = uiState.questionText,
        onQuestionTextChange = viewModel::onQuestionTextChange,
        answerOptions = uiState.answerOptions,
        updateAnswers = viewModel::updateAnswers,
        onRemoveAnswer = viewModel::removeAnswer,
        onToggleTrueFalse = viewModel::toggleTrueFalseAnswer,
        onSelectCorrectAnswer = viewModel::selectCorrectAnswer,
        onBasicAnswerChange = viewModel::onBasicAnswerChange,
        difficulty = uiState.difficulty,
        onDifficultyChange = viewModel::onDifficultyChange,
        flashcardType = uiState.cardType,
        onTypeChange = viewModel::onTypeChange,
        hintList = uiState.hints,
        onHintsUpdate = viewModel::updateHints,
        isRemoveModeActive = uiState.isDeleteMode,
        onToggleRemoveMode = viewModel::toggleDeletionMode,
        isEditMode = flashcardId != null,
        onSaveChanges = { viewModel.saveFlashcard() },
        onBackClick = onNavigateBack
    )
}

@Composable
fun FlashcardEditorContent(
    modifier: Modifier = Modifier,
    questionText: String = "",
    onQuestionTextChange: (String) -> Unit = {},
    answerOptions: List<Answer> = emptyList(),
    onSelectCorrectAnswer: (Int) -> Unit = {},
    onBasicAnswerChange: (String) -> Unit = {},
    updateAnswers: (List<Answer>) -> Unit = {},
    onRemoveAnswer: (Answer) -> Unit = {},
    onToggleTrueFalse: (Int) -> Unit = {},
    isRemoveModeActive: Boolean = false,
    onToggleRemoveMode: () -> Unit = {},
    difficulty: DifficultyLevel = DifficultyLevel.EASY,
    onDifficultyChange: (DifficultyLevel) -> Unit = {},
    flashcardType: FlashcardType = FlashcardType.BASIC,
    onTypeChange: (FlashcardType) -> Unit = {},
    hintList: List<String> = emptyList(),
    onHintsUpdate: (List<String>) -> Unit = {},
    isEditMode: Boolean = false,
    onSaveChanges: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier.systemBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "EDITAR FLASHCARD"
                else "CRIAR FLASHCARD",
                onBackClick = onBackClick,
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if(isEditMode) "SALVAR" else "CRIAR",
                    height = 40.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSaveChanges
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                CustomTextField(
                    inputValue = questionText,
                    onInputValueChange = onQuestionTextChange,
                    label = {
                        CustomLabel(
                            text = "Enunciado do flashcard",
                            textColor = DarkGray
                        )
                    },
                    placeholder = "Ex: Calcule o valor de x na equação: 2x + 5 = 15",
                    keyboardType = KeyboardType.Companion.Text
                )

                Column {
                    CustomLabel("Dificuldade")
                    DifficultySelector(
                        difficultyLevels = DifficultyLevel.entries,
                        selectedDifficulty = difficulty,
                        onDifficultySelected = { newDifficulty ->
                            if (newDifficulty != null) {
                                onDifficultyChange(newDifficulty)
                            }
                        },
                        modifier = Modifier.Companion.width(150.dp)
                    )
                }

                Column {
                    CustomLabel("Tipo de Flashcard")
                    TypeSelector(
                        options = FlashcardType.entries,
                        selectedOption = flashcardType,
                        onOptionSelected = onTypeChange
                    )
                }

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomLabel("Resposta")
                        if (flashcardType == FlashcardType.MULTIPLE_CHOICE || flashcardType == FlashcardType.TRUE_OR_FALSE)
                            Text(
                                text = if (isRemoveModeActive) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = DarkNavyBlue,
                                modifier = Modifier.Companion.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onToggleRemoveMode() }
                            )
                    }
                    when (flashcardType) {
                        FlashcardType.BASIC -> {
                            CustomTextField(
                                inputValue = answerOptions.firstOrNull()?.answer ?: "",
                                onInputValueChange = onBasicAnswerChange,
                                placeholder = "Ex: Paris"
                            )
                        }

                        FlashcardType.TRUE_OR_FALSE -> {
                            AnswerEditor(
                                answerOptions = answerOptions,
                                onAnswerOptionsUpdate = updateAnswers,
                                selectionControl = { answer, index ->
                                    if (isRemoveModeActive) {
                                        DeleteButton(onClick = { onRemoveAnswer(answer) })
                                    } else
                                        TrueFalseToggle(
                                            currentValue = if (answer.isCorrect) "T" else "F",
                                            onToggle = { onToggleTrueFalse(index) }
                                        )
                                },
                                getVisualState = { answer ->
                                    if (answer.isCorrect) AnswerVisualState.Correct
                                    else AnswerVisualState.Incorrect
                                },
                                limit = 10
                            )
                        }

                        FlashcardType.MULTIPLE_CHOICE -> {
                            AnswerEditor(
                                answerOptions = answerOptions,
                                onAnswerOptionsUpdate = updateAnswers,
                                selectionControl = { answer, index ->
                                    if (isRemoveModeActive) {
                                        DeleteButton(onClick = { onRemoveAnswer(answer) })
                                    } else {
                                        RadioButton(
                                            selected = answer.isCorrect,
                                            onClick = { onSelectCorrectAnswer(index) },
                                            colors = RadioButtonDefaults.colors(selectedColor = Green),
                                        )
                                    }
                                },
                                getVisualState = { answer ->
                                    if (answer.isCorrect) AnswerVisualState.Correct
                                    else AnswerVisualState.Incorrect
                                }
                            )
                        }

                        FlashcardType.OMISSION -> {

                            // Implementação para FlashcardType.OMISSION

                        }

                        FlashcardType.CHAT_FEYNMAN -> {

                            // Implementação para FlashcardType.CHAT_FEYNMAN

                        }
                    }
                }

                Column {
                    CustomLabel("Dicas")
                    HintEditor(
                        hints = hintList,
                        onHintsUpdate = onHintsUpdate
                    )
                }
            }

        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FlashcardEditorContentPreview() {
    CogniLinkTheme {
        FlashcardEditorContent(
            questionText = "Question",
            answerOptions = emptyList(),
            difficulty = DifficultyLevel.EASY,
            flashcardType = FlashcardType.BASIC,
            hintList = emptyList(),
            isRemoveModeActive = false,
            isEditMode = true,
        )
    }
}