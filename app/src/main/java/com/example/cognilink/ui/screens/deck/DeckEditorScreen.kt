package com.example.cognilink.ui.screens.deck

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.model.deck1
import com.example.cognilink.data.model.flashcard1
import com.example.cognilink.ui.components.deck.EditDeckContent
import com.example.cognilink.ui.components.deck.FlashcardItem
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.EmptyContent
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.DeleteButton
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.states.DeckEditorUiState
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.viewmodels.DeckEditorViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun DeckEditorScreen(
    userId: Long,
    deckId: Long?,
    onNavigateBack: () -> Unit,
    onNavigateToCreateFlashcard: (Long) -> Unit,
    onNavigateToEditFlashcard: (Long) -> Unit,
    onNavigateToCreateWithIA: (Long) -> Unit,
    viewModel: DeckEditorViewModel = viewModel()
) {
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId, deckId) {
        viewModel.loadDeckData(userId, deckId)
    }

    DeckEditorContent(
        isEditMode = deckId != null,
        deckName = uiState.deckName,
        deckDescription = uiState.deckDescription,
        deckCategories = uiState.deckCategories,
        deckFlashcards = uiState.deckFlashcards,
        showCategoryDialog = uiState.showCategoryDialog,
        categoryBeingEdited = uiState.categoryBeingEdited,
        categoryText = uiState.categoryText,
        isRemoveMode = uiState.isRemoveMode,
        isAddFlashcardDialogOpen = uiState.isAddFlashcardDialogOpen,
        onToggleRemoveMode = viewModel::toggleRemoveMode,
        onDeckNameChange = viewModel::onDeckNameChange,
        onDeckDescriptionChange = viewModel::onDeckDescriptionChange,
        onCategoryTextChange = viewModel::onCategoryTextChange,
        onAddCategory = { viewModel.openCategoryDialog() },
        onEditCategory = { viewModel.openCategoryDialog(it) },
        onRemoveCategory = viewModel::removeCategory,
        onConfirmCategory = viewModel::handleCategoryConfirmation,
        onRemoveFlashcard = viewModel::removeFlashcard,
        onDismissCategoryDialog = viewModel::closeCategoryDialog,
        onClickAddFlashcardDialog = { viewModel.toggleAddFlashcardDialog() },
        onEditFlashcardClick = onNavigateToEditFlashcard,
        onDismissAddFlashcardDialog = { viewModel.toggleAddFlashcardDialog() },
        onCreateFlashcardManuallyClick = {
            viewModel.toggleAddFlashcardDialog()
            scope.launch {
                delay(100)
                onNavigateToCreateFlashcard(deckId ?: 0L)
            }
        },
        onCreateFlashcardWithIAClick = {
            viewModel.toggleAddFlashcardDialog()
            scope.launch {
                delay(100)
                onNavigateToCreateWithIA(deckId ?: 0L)
            }
        },
        onSaveClick = { viewModel.saveDeck(userId, deckId) },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckEditorContent(
    isEditMode: Boolean,
    deckName: String,
    deckDescription: String,
    deckCategories: List<String>,
    deckFlashcards: List<com.example.cognilink.data.model.Flashcard>,
    showCategoryDialog: Boolean,
    categoryBeingEdited: String?,
    categoryText: String,
    isRemoveMode: Boolean,
    isAddFlashcardDialogOpen: Boolean,
    onDeckNameChange: (String) -> Unit,
    onDeckDescriptionChange: (String) -> Unit,
    onCategoryTextChange: (String) -> Unit,
    onAddCategory: () -> Unit,
    onEditCategory: (String) -> Unit,
    onRemoveCategory: (String) -> Unit,
    onConfirmCategory: () -> Unit,
    onRemoveFlashcard: (Long) -> Unit,
    onDismissCategoryDialog: () -> Unit,
    onToggleRemoveMode: () -> Unit,
    onSaveClick: () -> Unit,
    onEditFlashcardClick: (Long) -> Unit,
    onClickAddFlashcardDialog: () -> Unit,
    onDismissAddFlashcardDialog: () -> Unit,
    onCreateFlashcardManuallyClick: () -> Unit,
    onCreateFlashcardWithIAClick: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = onDismissCategoryDialog,
            title = {
                Text(
                    text = if (categoryBeingEdited == null) "Nova Categoria" else "Editar Categoria",
                    fontWeight = FontWeight.Bold,
                    color = DarkNavyBlue
                )
            },
            text = {
                CustomTextField(
                    inputValue = categoryText,
                    onInputValueChange = onCategoryTextChange,
                    placeholder = "Ex: Medicina, História..."
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirmCategory) {
                    Text("OK", fontWeight = FontWeight.Bold, color = DarkNavyBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissCategoryDialog) {
                    Text("CANCELAR", color = DarkGray)
                }
            },
            containerColor = White,
            shape = RoundedCornerShape(28.dp)
        )
    }

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
                        onClickButton = {
                            onCreateFlashcardManuallyClick()
                        }
                    )
                    SimpleGradientButton(
                        text = "Criar com IA",
                        height = 56.dp,
                        onClickButton = {
                            onCreateFlashcardWithIAClick()
                        }
                    )
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.statusBarsPadding(),
        containerColor = Color.Transparent,
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "Editar Baralho" else "Novo Baralho",
                onBackClick = onNavigateBack
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {
                SimpleGradientButton(
                    text = if (isEditMode) "SALVAR" else "CRIAR",
                    height = 40.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSaveClick
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EditDeckContent(
                    name = deckName,
                    onNameChange = onDeckNameChange,
                    categories = deckCategories,
                    onCategoryClickRemove = onRemoveCategory,
                    onCategoryClickAdd = onAddCategory,
                    onCategoryClickEdit = onEditCategory,
                    description = deckDescription,
                    onDescriptionChange = onDeckDescriptionChange
                )

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onClickAddFlashcardDialog,
                    modifier = Modifier.fillMaxWidth()
                )

                if (deckFlashcards.isNotEmpty()) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = CenterVertically
                    ) {
                        Text(
                            text = "Conteúdo do Baralho",
                            color = DarkGray,
                            fontWeight = FontWeight.SemiBold,
                        )
                        TextButton(
                            onClick = { onToggleRemoveMode() },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = if (isRemoveMode) "VOLTAR PARA SELEÇÃO" else "GERENCIAR",
                                color = DarkNavyBlue,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        deckFlashcards.forEach { card ->
                            FlashcardItem(
                                flashcardType = card.cardType,
                                flashcardQuestion = card.question,
                                nextReview = null,
                                onSelectCard = {
                                    if (!isRemoveMode) {
                                        onEditFlashcardClick(card.id)
                                    }
                                },
                                selectionControl = {
                                    if (isRemoveMode) {
                                        DeleteButton(onClick = { onRemoveFlashcard(card.id) })
                                    } else {
                                        IconButton(
                                            onClick = { onEditFlashcardClick(card.id) },
                                            modifier = Modifier.offset(x = 10.dp).size(32.dp)
                                        ) {
                                            Icon(
                                                painterResource(id = R.drawable.ic_keyboard_arrow_down),
                                                contentDescription = null,
                                                tint = LightGray,
                                                modifier = Modifier.rotate(-90f)
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }
                } else
                    EmptyContent()
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckEditorContentPreview() {
    CogniLinkTheme {
        val uiState = DeckEditorUiState(
            deckName = deck1.name,
            deckDescription = deck1.description,
            deckCategories = deck1.categories,
            deckFlashcards = listOf(flashcard1)
        )

        DeckEditorContent(
            isEditMode = true,
            deckName = uiState.deckName,
            deckDescription = uiState.deckDescription,
            deckCategories = uiState.deckCategories,
            deckFlashcards = uiState.deckFlashcards ,
            showCategoryDialog = false,
            categoryBeingEdited = null,
            categoryText = "",
            isRemoveMode = false,
            isAddFlashcardDialogOpen = false,
            onDeckNameChange = {},
            onDeckDescriptionChange = {},
            onCategoryTextChange = {},
            onAddCategory = {},
            onEditCategory = {},
            onRemoveCategory = {},
            onConfirmCategory = {},
            onRemoveFlashcard = {},
            onDismissCategoryDialog = {},
            onToggleRemoveMode = {},
            onSaveClick = {},
            onClickAddFlashcardDialog = {},
            onEditFlashcardClick = { _ -> },
            onDismissAddFlashcardDialog = {},
            onCreateFlashcardManuallyClick = {},
            onCreateFlashcardWithIAClick = {},
            onNavigateBack = {}
        )

    }
}
