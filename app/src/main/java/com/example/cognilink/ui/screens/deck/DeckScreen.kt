package com.example.cognilink.ui.screens.deck

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.domain.model.DifficultyLevel
import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.model.deck1
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.components.deck.FlashcardItem
import com.example.cognilink.ui.components.utils.EmptyContent
import com.example.cognilink.ui.components.deck.ViewDeckContent
import com.example.cognilink.ui.components.utils.labels.CustomLabel
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.viewmodels.DeckViewModel


import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import com.example.cognilink.ui.theme.LightGray

@Composable
fun DeckScreen(
    deckId: Long,
    userId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onNavigateToCreateFlashcard: (Long) -> Unit,
    onNavigateToCreateWithIA: (Long) -> Unit,
    onNavigateToStudy: (Long) -> Unit,
    onNavigateToFlashcard: (Long) -> Unit,
    viewModel: DeckViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(deckId, userId) {
        viewModel.initialize(deckId, userId)
    }

    val deck = uiState.currentDeck
    DeckContent(
        deckName = deck?.name,
        deckId = deckId,
        deckCategories = deck?.categories,
        deckDescription = deck?.description,
        deckDifficulty = deck?.difficulty,
        deckMastery = deck?.mastery,
        deckTotalCards = deck?.totalCards,
        deckCardsToReview = deck?.cardsToReview,
        deckFlashcards = uiState.flashcards,
        isMenuExpanded = uiState.isMenuExpanded,
        isAddFlashcardDialogOpen = uiState.isAddFlashcardDialogOpen,
        onMenuClick = { viewModel.toggleMenu() },
        onAddFlashcardClick = { viewModel.toggleAddFlashcardDialog() },
        onDismissAddFlashcardDialog = { viewModel.toggleAddFlashcardDialog() },
        onCreateFlashcardManuallyClick = {
            viewModel.toggleAddFlashcardDialog()
            onNavigateToCreateFlashcard(deckId)
        },
        onCreateFlashcardWithIAClick = {
            viewModel.toggleAddFlashcardDialog()
            onNavigateToCreateWithIA(deckId)
        },
        onFlashcardClick = {flashcardId -> onNavigateToFlashcard(flashcardId) },
        onStudyNowClick = {
            onNavigateToStudy(deckId)
        },
        onClickSeeMore = { viewModel.loadAllFlashcards() },
        onBackClick = onNavigateBack,
        onDeleteClick = {
            viewModel.toggleMenu()
            viewModel.deleteDeck(deckId,userId)
            onNavigateBack()
        },
        onEditClick = {
            viewModel.toggleMenu()
            onNavigateToEdit()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckContent(
    modifier: Modifier = Modifier,
    deckName: String?,
    deckCategories: List<String>?,
    deckId: Long = 0,
    deckDifficulty: DifficultyLevel?,
    deckDescription: String?,
    deckMastery: Float?,
    deckTotalCards: Int?,
    deckCardsToReview: Int?,
    deckFlashcards: List<Flashcard>?,
    isMenuExpanded: Boolean = false,
    isAddFlashcardDialogOpen: Boolean = false,
    onAddFlashcardClick: () -> Unit,
    onDismissAddFlashcardDialog: () -> Unit,
    onCreateFlashcardWithIAClick: (Long) -> Unit,
    onCreateFlashcardManuallyClick: (Long)->Unit,
    onFlashcardClick: (Long) -> Unit,
    onStudyNowClick: () -> Unit,
    onClickSeeMore: () -> Unit,
    onBackClick: () -> Unit,
    onMenuClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = deckName ?: "Nome do Baralho",
                onMenuClick = onMenuClick,
                onBackClick = onBackClick,
                menuEnabled = true,
                showMenu = isMenuExpanded,
                onDismissMenu = onMenuClick,
                menuContent = {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            onEditClick()
                        }
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = LightGray)
                    DropdownMenuItem(
                        text = { Text("Excluir") },
                        onClick = {
                            onDeleteClick()
                        }
                    )
                },
                modifier = Modifier.statusBarsPadding()
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = "ESTUDAR AGORA",
                    height = 40.dp,
                    icon = R.drawable.ic_arrow_forward,
                    iconRightSide = true,
                    onClickButton = onStudyNowClick
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ViewDeckContent(
                    categories = deckCategories,
                    difficulty = deckDifficulty,
                    name = deckName,
                    description = deckDescription,
                    mastery = deckMastery,
                    totalCards = deckTotalCards,
                    cardToReview = deckCardsToReview,
                )

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onAddFlashcardClick
                )

                if (isAddFlashcardDialogOpen) {
                    BasicAlertDialog(
                        onDismissRequest = onDismissAddFlashcardDialog,
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
                                    text = "Como deseja criar?",
                                    fontWeight = FontWeight.Bold,
                                    color = DarkNavyBlue,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                SimpleGradientButton(
                                    text = "Criar manualmente",
                                    height = 56.dp,
                                    onClickButton = { onCreateFlashcardManuallyClick(deckId) }
                                )
                                SimpleGradientButton(
                                    text = "Criar com IA",
                                    height = 56.dp,
                                    onClickButton = { onCreateFlashcardWithIAClick(deckId) }
                                )
                            }
                        }
                    }
                }

                if (!deckFlashcards.isNullOrEmpty()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        CustomLabel("Próximos tópicos", textColor = DarkNavyBlue)
                        TextButton(onClick = onClickSeeMore, contentPadding = PaddingValues(0.dp)) {
                            Text(
                                text = "Ver todos",
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        deckFlashcards.forEach { flashcard ->
                            FlashcardItem(
                                flashcardType = flashcard.cardType,
                                flashcardQuestion = flashcard.question,
                                nextReview = "10",
                                onSelectCard = { onFlashcardClick(flashcard.id) }
                            )
                        }
                    }
                }
                else
                    EmptyContent()
            }
        }
    }
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckContentPreview() {
    CogniLinkTheme {
        DeckContent(
            deckName = deck1.name,
            deckCategories = deck1.categories,
            deckDescription = deck1.description,
            deckDifficulty = deck1.difficulty,
            deckMastery = deck1.mastery,
            deckTotalCards = deck1.totalCards,
            deckCardsToReview = deck1.cardsToReview,
            deckFlashcards = emptyList(),
            onMenuClick = {},
            onCreateFlashcardWithIAClick = {},
            onCreateFlashcardManuallyClick = {},
            onFlashcardClick = {},
            onStudyNowClick = {},
            onClickSeeMore = {},
            onBackClick = {},
            onDeleteClick = {},
            onEditClick = {},
            onAddFlashcardClick = {},
            onDismissAddFlashcardDialog = {}
        )
    }
}
