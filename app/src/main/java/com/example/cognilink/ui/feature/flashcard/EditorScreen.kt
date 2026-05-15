package com.example.cognilink.ui.feature.flashcard

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.Answer
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerOptionsEditor
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.HintEditor
import com.example.cognilink.ui.components.flashcard.TypeSelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.CustomButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.SectionLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.OffWhite

@Composable
fun EditorScreen(
    viewModel: FlashcardViewModel = viewModel()
) {
    EditorContent(
        flashcardViewModel = viewModel,
    )
}

@Composable
fun EditorContent(
    flashcardViewModel: FlashcardViewModel,
    isEditMode: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

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
                CustomButton(
                    text = if (isEditMode) "SALVAR FLASHCARD" else "CRIAR FLASHCARD",
                    icon = if (isEditMode) R.drawable.ic_check_circle else R.drawable.ic_add,
                    onClickButton = flashcardViewModel::saveFlashcard
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
                inputValue = flashcardViewModel.question,
                onInputValueChange = flashcardViewModel::onQuestionChange,
                label = "Enunciado do Flashcard",
                placeholder = "Ex: Calcule o valor de x na equação: 2x + 5 = 15",
                keyboardType = KeyboardType.Text
            )

            Column {
                SectionLabel("Dificuldade")
                DifficultySelector(
                    difficultyLevels = DifficultyLevel.entries,
                    selectedDifficulty = flashcardViewModel.difficulty,
                    onDifficultySelected = flashcardViewModel::onDifficultyChange,
                    modifier = Modifier.width(150.dp)
                )
            }

            Column {
                SectionLabel("Tipo de Flashcard")
                TypeSelector(
                    options = FlashcardType.entries,
                    selectedOption = flashcardViewModel.cardType,
                    onOptionSelected = flashcardViewModel::onTypeChange
                )
            }

            Column {
                when (flashcardViewModel.cardType) {
                    FlashcardType.BASIC -> {
                        CustomTextField(
                            inputValue = flashcardViewModel.answers.firstOrNull()?.answer ?: "",
                            onInputValueChange = flashcardViewModel::onBasicAnswerChange,
                            label = "Resposta",
                            placeholder = "Digite a resposta correta"
                        )
                    }
                    FlashcardType.CHAT_FEYNMAN -> {
                        SectionLabel("Resposta")
                        AnswerOptionsEditor(
                            flashcardType = flashcardViewModel.cardType,
                            responses = flashcardViewModel.answers,
                            onResponsesUpdate = flashcardViewModel::updateAnswers,
                            onSelectedAnswer = flashcardViewModel::onSelectedAnswer,
                            limit = 1
                        )
                    }
                    FlashcardType.TRUE_OR_FALSE -> {
                        SectionLabel("Respostas")
                        AnswerOptionsEditor(
                            flashcardType = flashcardViewModel.cardType,
                            responses = flashcardViewModel.answers,
                            onResponsesUpdate = flashcardViewModel::updateAnswers,
                            onSelectedAnswer = flashcardViewModel::onSelectedAnswer,
                            limit = 10
                        )
                    }
                    FlashcardType.MULTIPLE_CHOICE -> {
                        SectionLabel("Respostas")
                        AnswerOptionsEditor(
                            flashcardType = flashcardViewModel.cardType,
                            responses = flashcardViewModel.answers,
                            onResponsesUpdate = flashcardViewModel::updateAnswers,
                            onSelectedAnswer = flashcardViewModel::onSelectedAnswer,
                            limit = 5
                        )
                    }
                    FlashcardType.OMISSION -> {
                        SectionLabel("Resposta")
                        AnswerOptionsEditor(
                            flashcardType = flashcardViewModel.cardType,
                            responses = flashcardViewModel.answers,
                            onResponsesUpdate = flashcardViewModel::updateAnswers,
                            onSelectedAnswer = flashcardViewModel::onSelectedAnswer,
                            limit = 1
                        )
                    }
                }
            }

            Column {
                SectionLabel("DICAS")
                HintEditor(
                    hints = flashcardViewModel.hints,
                    onHintsUpdate = flashcardViewModel::updateHints
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
                onQuestionChange("Qual é a capital da França?")
                onDifficultyChange(DifficultyLevel.MEDIUM)
                onTypeChange(FlashcardType.BASIC)
                updateAnswers(listOf(Answer("Paris", true)))
                updateHints(listOf("Dica 1", "Dica 2"))
            }
        }
        EditorContent(
            flashcardViewModel = previewViewModel,
            isEditMode = true
        )
    }
}
