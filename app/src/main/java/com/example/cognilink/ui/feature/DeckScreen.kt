package com.example.cognilink.ui.feature

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashCard
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.buttons.NeonActionButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.components.deck.DeckCardContent
import com.example.cognilink.ui.components.deck.EditDeckContent
import com.example.cognilink.ui.components.deck.EmptyDeckContent
import com.example.cognilink.ui.components.deck.ViewDeckContent
import com.example.cognilink.viewModel.DeckViewModel


@Composable
fun DeckScreen(
    viewModel: DeckViewModel = viewModel()
) {
    DeckContent(
        viewModel = viewModel,
        toggleEditMode = { viewModel.toggleEditMode() },
        onSaveClick = { /* TODO: Implementar salvamento */ },
        onAddFlashcardClick = { /* TODO: Navegar para criação de card */ }
    )
}

@Composable
fun DeckContent(
    viewModel: DeckViewModel,
    toggleEditMode: () -> Unit,
    onSaveClick: () -> Unit,
    onAddFlashcardClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    if (viewModel.showCategoryDialog) {
        AlertDialog(
            onDismissRequest = viewModel::closeCategoryDialog,
            title = {
                Text(
                    text = if (viewModel.categoryBeingEdited == null) "Nova Categoria" else "Editar Categoria",
                    fontWeight = FontWeight.Bold,
                    color = DarkNavyBlue
                )
            },
            text = {
                CustomTextField(
                    inputValue = viewModel.categoryText,
                    onInputValueChange = viewModel::onCategoryTextChange,
                    placeholder = "Ex: Medicina, História..."
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::handleCategoryConfirmation) {
                    Text("OK", fontWeight = FontWeight.Bold, color = DarkNavyBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::closeCategoryDialog) {
                    Text("CANCELAR", color = DarkGray)
                }
            },
            containerColor = White,
            shape = RoundedCornerShape(28.dp)
        )
    }

    Scaffold(
        bottomBar = {
            if(viewModel.deckFlashcards.isNotEmpty() && !viewModel.isEditMode){
                Column(modifier = Modifier.padding(24.dp)) {
                    SimpleGradientButton(
                        text = "ESTUDAR AGORA",
                        height = 40.dp,
                        icon = R.drawable.ic_arrow_forward,
                        iconRightSide = true,
                        onClickButton = {}
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
            NavigationHeader(
                title =
                    if (viewModel.deckName.isNotEmpty())
                        if (viewModel.isEditMode)
                            "Editar Baralho"
                        else viewModel.deckName
                    else "Novo Baralho",
                onMenuClick = toggleEditMode,
                menuEnabled = viewModel.deckName.isNotEmpty()
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                if(viewModel.isEditMode){
                    EditDeckContent(
                        name = viewModel.deckName,
                        onNameChange = viewModel::onDeckNameChange,
                        categories = viewModel.deckCategories,
                        onCategoryClickRemove = viewModel::removeCategory,
                        onCategoryClickAdd = {
                            viewModel.openCategoryDialog()
                        },
                        onCategoryClickEdit = { category ->
                            viewModel.openCategoryDialog(category)
                        },
                        description = viewModel.deckDescription,
                        onDescriptionChange = viewModel::onDeckDescriptionChange
                    )
                }else{
                    ViewDeckContent(
                        categories = viewModel.deckCategories,
                        difficulty = viewModel.deckDifficulty,
                        name = viewModel.deckName,
                        description = viewModel.deckDescription,
                        mastery = viewModel.deckMastery ?: 0f,
                        totalCards = viewModel.deckTotalCards,
                        cardToReview = viewModel.deckCardsToReview,
                    )
                }

                NeonActionButton(
                    text = "ADICIONAR FLASHCARD",
                    icon = R.drawable.ic_add,
                    onClickButton = onAddFlashcardClick
                )

                if(viewModel.deckTotalCards > 0)
                    DeckCardContent(onClickTextButton = {}, deckList = viewModel.deckFlashcards, isEditMode = viewModel.isEditMode)
                else
                    EmptyDeckContent()
            }
        }
    }
}



@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckContentPreview() {
    val previewViewModel = DeckViewModel().apply {
        onDeckNameChange("Neurociência Avançada")
        addCategory("Medicina")
        addCategory("Biologia")
        onDeckDescriptionChange("Domine as complexidades das redes neurais, sistemas sensoriais e\n" +
                "patologias corticais com este guia curado para especialistas.")
        deckFlashcards.addAll(listOf(
            FlashCard("O que é um neurônio?", FlashcardType.BASIC, DifficultyLevel.EAZY),
            FlashCard("Explique o potencial de ação.", FlashcardType.BASIC, DifficultyLevel.HARD),
            FlashCard("Função do cerebelo.", FlashcardType.BASIC, DifficultyLevel.MEDIUM)
        ))
        onDeckMasteryChange(0.65f)
        onDeckCardsToReviewChange(12)
        //toggleEditMode()
    }

    CogniLinkTheme {
        DeckContent(
            viewModel = previewViewModel,
            toggleEditMode = { previewViewModel.toggleEditMode() },
            onSaveClick = {},
            onAddFlashcardClick = {}
        )
    }
}