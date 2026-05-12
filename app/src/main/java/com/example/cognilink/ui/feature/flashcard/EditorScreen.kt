package com.example.cognilink.ui.feature.flashcard

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerOptionsEditor
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.HintEditor
import com.example.cognilink.ui.components.flashcard.TypeSelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.SectionLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White
import kotlin.collections.emptyList

@Composable
fun EditorContent(modifier: Modifier = Modifier,
                  title: String = "CRIAR NOVO FLASHCARD",
                  flashcardQuestion : String = "",
                  onFlashcardQuestionChange : (String) -> Unit = {},
                  selectedOption : FlashcardType = FlashcardType.BASIC,
                  onOptionSelected : (FlashcardType) -> Unit = {},
                  selectedDifficulty : DifficultyLevel = DifficultyLevel.EAZY,
                  onDifficultySelected : (DifficultyLevel) -> Unit = {},
                  flashcardAnswer : String = "",
                  onFlashcardAnswerChange : (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { NavigationHeader(title = title) },
        containerColor = OffWhite,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 12.dp)) {
                        Icon(painter = painterResource(id = R.drawable.ic_check_circle),
                            contentDescription = null,
                            tint = VividCyan,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text("SALVAR FLASHCARD",
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }
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
                inputValue = flashcardQuestion,
                onInputValueChange = onFlashcardQuestionChange,
                label = "Enunciado do Flashcard",
                placeholder = "Ex: Calcule o valor de x na equação: 2x + 5 = 15",
                keyboardType = KeyboardType.Text
            )

            Column {
                SectionLabel("Dificuldade")
                DifficultySelector(
                    difficultyLevels = listOf(DifficultyLevel.EAZY, DifficultyLevel.MEDIUM, DifficultyLevel.HARD),
                    selectedDifficulty = selectedDifficulty,
                    onDifficultySelected = onDifficultySelected,
                    modifier = Modifier.width(150.dp)
                )
            }

            Column {
                SectionLabel("Tipo de Flashcard")
                TypeSelector(
                    options = listOf(FlashcardType.BASIC, FlashcardType.MULTIPLE_CHOICE, FlashcardType.OMISSION, FlashcardType.TRUE_OR_FALSE, FlashcardType.CHAT_FEYNMAN),
                    selectedOption = selectedOption,
                    onOptionSelected = {}
                )
            }

            var listaTeste by remember { mutableStateOf(listOf("Resposta 1")) }

            Column {
                when (selectedOption) {
                    FlashcardType.BASIC -> {
                        SectionLabel("Resposta")
                        AnswerOptionsEditor(
                            flashcardType = selectedOption,
                            responses = listaTeste,
                            onResponsesUpdate = { listaTeste = it },
                            onSelectedAnswer = {},
                            limit = 1
                        )
                    }
                    FlashcardType.CHAT_FEYNMAN -> {
                        SectionLabel("Resposta")
                        AnswerOptionsEditor(
                            flashcardType = selectedOption,
                            responses = listaTeste,
                            onResponsesUpdate = { listaTeste = it },
                            onSelectedAnswer = {},
                            limit = 1
                        )
                    }
                    FlashcardType.TRUE_OR_FALSE ->{
                        SectionLabel("Resposta")
                        AnswerOptionsEditor(
                            flashcardType = selectedOption,
                            responses = listaTeste,
                            onResponsesUpdate = { listaTeste = it },
                            onSelectedAnswer = {},
                            limit = 10
                        )
                    }
                    FlashcardType.MULTIPLE_CHOICE -> {
                        SectionLabel("Respostas")
                        AnswerOptionsEditor(
                            flashcardType = selectedOption,
                            responses = listaTeste,
                            onResponsesUpdate = { listaTeste = it },
                            onSelectedAnswer = {},
                            limit = 5
                        )
                    }
                    FlashcardType.OMISSION -> {
                        SectionLabel("Resposta")
                        AnswerOptionsEditor(
                            flashcardType = selectedOption,
                            responses = listaTeste,
                            onResponsesUpdate = { listaTeste = it },
                            onSelectedAnswer = {},
                            limit = 1 // TODO: obter quantos serão necessários pelo enunciado
                        )
                    }
                }
            }

            var listaDicas by remember { mutableStateOf(emptyList<String>()) }

            Column {
                SectionLabel("DICAS")
//                HintEditor(
//                    hints = listaDicas,
//                    onHintsUpdate = { listaDicas = it }
//                )
            }

        }
    }
}

@Preview
@Composable
private fun EditorContentPreview() {
    CogniLinkTheme {
        EditorContent(selectedOption = FlashcardType.CHAT_FEYNMAN)
    }
}