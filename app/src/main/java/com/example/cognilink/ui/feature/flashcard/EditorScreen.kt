package com.example.cognilink.ui.feature.flashcard

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.Answer
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.HintEditor
import com.example.cognilink.ui.components.flashcard.TypeSelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.labels.SectionLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.viewModel.FlashcardViewModel

@Composable
fun EditorScreen(
    viewModel: FlashcardViewModel = viewModel()
) {
    EditorContent(
        questionText = viewModel.questionText,
        onQuestionTextChange = viewModel::onQuestionTextChange,


    )
}

@Composable
fun EditorContent(
    modifier: Modifier = Modifier,
    questionText: String = "",
    onQuestionTextChange: (String) -> Unit = {},
    difficulty: DifficultyLevel = DifficultyLevel.EAZY,
    onDifficultyChange: (DifficultyLevel) -> Unit = {},
    flashcardType: FlashcardType = FlashcardType.BASIC,
    onTypeChange: (FlashcardType) -> Unit = {},
    hintList: List<String> = emptyList(),
    onHintsUpdate: (List<String>) -> Unit = {},
    isEditMode: Boolean = false,
    isRemoveModeActive: Boolean = false,
    onToggleRemoveMode: () -> Unit = {},
    editAnswerControl: @Composable () -> Unit = {},
    onSaveChanges: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    val removeToggle by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "EDITAR FLASHCARD"
                else "CRIAR FLASHCARD"
            )
                 },
        containerColor = OffWhite,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                NeonActionButton(
                    text = if (isEditMode) "SALVAR FLASHCARD" else "CRIAR FLASHCARD",
                    icon = if (isEditMode) R.drawable.ic_check_circle else R.drawable.ic_add,
                    onClickButton = onSaveChanges
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
            CustomTextField(
                inputValue = questionText,
                onInputValueChange = onQuestionTextChange,
                label = "Enunciado do Flashcard",
                placeholder = "Ex: Calcule o valor de x na equação: 2x + 5 = 15",
                keyboardType = KeyboardType.Text
            )

            Column {
                SectionLabel("Dificuldade")
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
                SectionLabel("Tipo de Flashcard")
                TypeSelector(
                    options = FlashcardType.entries,
                    selectedOption = flashcardType,
                    onOptionSelected = onTypeChange
                )
            }

            Column {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    SectionLabel("Resposta")
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
                editAnswerControl()
            }

            Column {
                SectionLabel("Dicas")
                HintEditor(
                    hints = hintList,
                    onHintsUpdate = onHintsUpdate
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EditorContentPreview() {

    CogniLinkTheme {
        val previewViewModel = remember {
            FlashcardViewModel()
                .apply {
                onQuestionTextChange("Qual é a capital da França?")
                onDifficultyChange(DifficultyLevel.MEDIUM)
                onTypeChange(FlashcardType.BASIC)
                updateAnswers(listOf(Answer("Paris", true)))
                updateHints(listOf("Dica 1", "Dica 2"))
            }
        }
        EditorContent(
            questionText = previewViewModel.questionText,
            onQuestionTextChange = previewViewModel::onQuestionTextChange,
            difficulty = previewViewModel.difficulty,
            onDifficultyChange = previewViewModel::onDifficultyChange,
            flashcardType = previewViewModel.cardType,
            onTypeChange = previewViewModel::onTypeChange,
            hintList = previewViewModel.hints,
            onHintsUpdate = previewViewModel::updateHints,
            editAnswerControl = previewViewModel.editAnswerControl(),
            isRemoveModeActive = previewViewModel.isDeleteMode,
            onToggleRemoveMode = previewViewModel::toggleDeletionMode,
            isEditMode = true
        )
    }
}
