package com.example.cognilink.viewModel

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cognilink.data.Answer
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerEditor
import com.example.cognilink.ui.components.flashcard.AnswerSelector
import com.example.cognilink.ui.components.flashcard.AnswerVisualState
import com.example.cognilink.ui.components.flashcard.TrueFalseToggle
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.buttons.DeleteButton
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Green

class FlashcardViewModel : ViewModel() {

    var questionText by mutableStateOf("")
        private set

    var cardType by mutableStateOf(FlashcardType.BASIC)
        private set

    var difficulty by mutableStateOf(DifficultyLevel.MEDIUM)
        private set

    var answerOptions by mutableStateOf(listOf<Answer>())
        private set

    var selectedAnswers by mutableStateOf(mapOf<Answer, String>())
        private set

    val isQuestionAnswered by derivedStateOf {
        selectedAnswers.isNotEmpty()
    }

    var isQuestionVerified by mutableStateOf(false)
        private set

    var hints by mutableStateOf(listOf<String>())
        private set

    var isDeleteMode by mutableStateOf(false)
        private set

    fun toggleDeletionMode() {
        isDeleteMode = !isDeleteMode
    }

    fun onQuestionTextChange(newQuestion: String) {
        questionText = newQuestion
    }

    fun verifyQuestion() {
        isQuestionVerified = !isQuestionVerified
    }

    fun onTypeChange(newType: FlashcardType) {
        cardType = newType
        answerOptions = when (newType) {
            FlashcardType.BASIC -> listOf()
            FlashcardType.TRUE_OR_FALSE -> listOf()
            FlashcardType.MULTIPLE_CHOICE -> listOf()
            FlashcardType.OMISSION -> listOf()
            FlashcardType.CHAT_FEYNMAN -> listOf()
        }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel) {
        difficulty = newDifficulty
    }

    fun updateAnswers(newAnswers: List<Answer>) {
        answerOptions = newAnswers
    }

    fun updateHints(newHints: List<String>) {
        hints = newHints
    }

    fun editAnswerControl(): @Composable () -> Unit {
        return{
            when (cardType) {
                FlashcardType.BASIC -> {
                    CustomTextField(
                        inputValue = answerOptions.firstOrNull()?.answer ?: "",
                        onInputValueChange = { newAnswer ->
                            answerOptions = listOf(Answer(newAnswer, true))
                        },
                        placeholder = "Ex: Paris"
                    )
                }
                FlashcardType.TRUE_OR_FALSE -> {
                    AnswerEditor(
                        answerOptions = answerOptions,
                        onAnswerOptionsUpdate = ::updateAnswers,
                        selectionControl = { answer, index ->
                            if (isDeleteMode) {
                                DeleteButton(
                                    onClick = {
                                        val newList = answerOptions.toMutableList()
                                        newList.remove(answer)
                                        updateAnswers(newList)
                                    }
                                )
                            } else
                                TrueFalseToggle(
                                    currentValue = if (answer.isCorrect) "V" else "F",
                                    onToggle = { _ ->
                                        answerOptions = answerOptions.mapIndexed { i, a ->
                                            if (i == index) a.copy(isCorrect = !a.isCorrect)
                                            else
                                                a.copy(isCorrect = a.isCorrect)
                                        }
                                    }
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
                        onAnswerOptionsUpdate = ::updateAnswers,
                        selectionControl = { answer, index ->
                            if (isDeleteMode) {
                                DeleteButton(
                                    onClick = {
                                        val newList = answerOptions.toMutableList()
                                        newList.remove(answer)
                                        updateAnswers(newList)
                                    }
                                )
                            } else {
                                RadioButton(
                                    selected = answer.isCorrect,
                                    onClick = {
                                        answerOptions =
                                            answerOptions.mapIndexed { i, a -> a.copy(isCorrect = i == index) }
                                    },
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
    }

    fun viewAnswerControl(): @Composable () -> Unit {
        return{
            when (cardType) {
                FlashcardType.BASIC -> {

                }
                FlashcardType.TRUE_OR_FALSE -> {

                }
                FlashcardType.MULTIPLE_CHOICE -> {
                    AnswerSelector(
                        answerOptions = answerOptions,
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
                                    onClick = { selectedAnswers = mapOf(answer to "") },
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
        }
    }

    fun saveFlashcard() {
        // Implement save logic using a repository
    }
}