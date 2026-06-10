package com.example.cognilink.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cognilink.R
import com.example.cognilink.data.model.Answer
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerEditor
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.HintEditor
import com.example.cognilink.ui.components.flashcard.TrueFalseToggle
import com.example.cognilink.ui.components.flashcard.TypeSelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.dialogs.BasicCustomAlertDialog
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.DeleteButton
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.states.AnswerVisualState
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Green
import com.example.cognilink.ui.viewmodels.FlashcardFormViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlashcardEditorScreen(
    viewModel: FlashcardFormViewModel = koinViewModel(),
    flashcardId: String? = null,
    deckId: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(flashcardId, deckId) {
        viewModel.initialize(deckId, flashcardId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    BackHandler {
        if (uiState.wasEdited) {
            viewModel.toggleChangeDialog()
        } else {
            scope.launch {
                delay(150)
                onNavigateBack()
            }
        }
    }

    FlashcardEditorContent(
        questionText = uiState.questionText,
        questionTextError = uiState.questionTextError,
        onQuestionTextChange = viewModel::onQuestionTextChange,
        answerOptions = uiState.answerOptions,
        answersError = uiState.answersError,
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
        isEditMode = uiState.isEditMode,
        onSaveChanges = {
            viewModel.saveFlashcard()
        },
        onDeleteClick = {
            viewModel.toggleDeleteDialog()
        },
        isMenuExpanded = uiState.isMenuExpanded,
        onMenuClick = viewModel::toggleMenu,
        onDismissDeleteDialog = { viewModel.toggleDeleteDialog() },
        showDeleteDialog = uiState.showDeleteDialog,
        onConfirmDelete = {
            viewModel.deleteFlashcard()
        },
        onBackClick = {
            if (uiState.wasEdited) {
                viewModel.toggleChangeDialog()
            } else {
                scope.launch {
                    delay(150)
                    onNavigateBack()
                }
            }
        },
        showChangeDialog = uiState.showChangeDialog,
        onDismissChangeDialog = { viewModel.toggleChangeDialog() },
        onConfirmDiscard = {
            viewModel.discardFlashcard()
            viewModel.toggleChangeDialog()
            scope.launch {
                delay(150)
                onNavigateBack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardEditorContent(
    questionText: String = "",
    questionTextError: String? = null,
    onQuestionTextChange: (String) -> Unit = {},
    answerOptions: List<Answer> = emptyList(),
    answersError: String? = null,
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
    onDeleteClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    showDeleteDialog: Boolean = false,
    isMenuExpanded: Boolean = false,
    onDismissDeleteDialog: () -> Unit = {},
    onConfirmDelete: () -> Unit = {},
    showChangeDialog: Boolean = false,
    onDismissChangeDialog: () -> Unit = {},
    onConfirmDiscard: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    if (showDeleteDialog) {
        BasicCustomAlertDialog(
            dialogTitle = "Tem certeza disso?",
            dialogText = "Essa ação não poderá ser desfeita!",
            confirmationButtonText = "Sim",
            dismissButtonText = "Cancelar",
            onConfirmation = onConfirmDelete,
            onDismissRequest = onDismissDeleteDialog,
        )
    }
    if (showChangeDialog) {
        BasicCustomAlertDialog(
            onDismissRequest = onDismissChangeDialog,
            onConfirmation = onConfirmDiscard,
            dialogTitle = "Alterações não salvas",
            dialogText = "Você possui alterações não salvas. Deseja realmente sair e descartá-las?",
            confirmationButtonText = "Sair e descartar",
            dismissButtonText = "Continuar editando",
        )
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "EDITAR FLASHCARD"
                else "CRIAR FLASHCARD",
                onBackClick = onBackClick,
                onMenuClick = onMenuClick,
                menuEnabled = isEditMode,
                showMenu = isMenuExpanded,
                menuContent = {
                    if (isEditMode) {
                        DropdownMenuItem(
                            text = { Text("Excluir") },
                            onClick = {
                                onDeleteClick()
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if (isEditMode) "SALVAR" else "CRIAR",
                    height = 40.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSaveChanges
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

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
                    keyboardType = KeyboardType.Text,
                    errorMessage = questionTextError
                )

                Column {
                    CustomLabel(text = "Dificuldade")
                    DifficultySelector(
                        difficultyLevels = DifficultyLevel.entries,
                        selectedDifficulty = difficulty,
                        onDifficultySelected = { newDifficulty ->
                            if (newDifficulty != null) {
                                onDifficultyChange(newDifficulty)
                            }
                        },
                        modifier = Modifier.width(150.dp)
                    )
                }

                Column {
                    CustomLabel(text = "Tipo de Flashcard")
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
                        CustomLabel(text = "Resposta")
                        if (flashcardType == FlashcardType.MULTIPLE_CHOICE || flashcardType == FlashcardType.TRUE_OR_FALSE)
                            Text(
                                text = if (isRemoveModeActive) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = DarkNavyBlue,
                                modifier = Modifier.clickable(
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
                                placeholder = "Ex: Paris",
                                errorMessage = answersError
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
                    if (flashcardType != FlashcardType.BASIC && answersError != null) {
                        Text(
                            text = answersError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Column {
                    CustomLabel(text = "Dicas")
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