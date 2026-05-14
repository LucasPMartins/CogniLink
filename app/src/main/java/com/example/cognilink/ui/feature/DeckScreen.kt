package com.example.cognilink.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.ui.components.utils.NeonFAB
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.feature.DeckViewModel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LavenderBlue
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.theme.neonGlow

@Composable
fun DeckScreen(
    viewModel: DeckViewModel = viewModel()
) {
    DeckContent(
        isEditMode = viewModel.isEditMode,
        deckName = viewModel.deckName,
        onDeckNameChange = viewModel::onDeckNameChange,
        deckCategory = viewModel.deckCategory,
        onDeckCategoryChange = viewModel::onDeckCategoryChange,
        deckDescription = viewModel.deckDescription,
        onDeckDescriptionChange = viewModel::onDeckDescriptionChange,
        deckDifficulty = viewModel.deckDifficulty,
        deckMastery = viewModel.deckMastery,
        toggleEditMode = viewModel::toggleEditMode,
        onSaveClick = { /* TODO: Implementar salvamento */ },
        onAddFlashcardClick = { /* TODO: Navegar para criação de card */ }
    )
}

@Composable
fun DeckContent(
    isEditMode: Boolean,
    deckName: String,
    onDeckNameChange: (String) -> Unit,
    deckCategory: String,
    onDeckCategoryChange: (String) -> Unit,
    deckDescription: String,
    onDeckDescriptionChange: (String) -> Unit,
    deckDifficulty: DifficultyLevel?,
    deckMastery: Float?,
    toggleEditMode: () -> Unit,
    onSaveClick: () -> Unit,
    onAddFlashcardClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        floatingActionButton = {
            NeonFAB(
                neonColor = VividCyan,
                backgroundColor = DarkNavyBlue,
                buttonDescription = "Salvar baralho",
                iconColor = VividCyan,
                icon = R.drawable.ic_check,
                onClick = onSaveClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(padding),
        ) {
            NavigationHeader(
                title =
                    if (deckName.isNotEmpty())
                        if (isEditMode)
                            "Editar Baralho"
                        else deckName
                    else "Novo Baralho",
                onMenuClick = toggleEditMode,
                menuEnabled = if (deckName.isNotEmpty()) true else false
            )

            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {

                if(isEditMode){

                }


                Button(
                    onClick = onAddFlashcardClick,
                    modifier = Modifier.height(80.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = VividCyan,
                            modifier = Modifier.neonGlow(VividCyan, 24.dp, 5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null,
                                tint = DarkNavyBlue,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Text(
                            text = "ADICIONAR FLASHCARD",
                            color = White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 20.dp)
                        )
                    }
                }

                EmptyDeckState()
            }
        }
    }
}

@Composable
private fun EmptyDeckState() {
    Surface(color = White, shape = RoundedCornerShape(32.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_deck),
                contentDescription = null,
                tint = LavenderBlue,
                modifier = Modifier.size(60.dp),
            )
            Text(
                text = "Seu baralho está vazio!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Comece a construir seu conhecimento adicionando seu primeiro cartão de estudo acima.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = DarkGray,
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { i ->
                    Icon(
                        painter = painterResource(id = R.drawable.ic_overlay),
                        contentDescription = null,
                        tint = LavenderBlue.copy(alpha = 1f / (i + 1)),
                        modifier = Modifier
                            .size(8.dp)
                            .padding(horizontal = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DeckContentPreview() {
    CogniLinkTheme {
        DeckContent(
            isEditMode = false,
            deckName = "Matemática",
            onDeckNameChange = {},
            deckCategory = "Ciências",
            onDeckCategoryChange = {},
            deckDescription = "Baralho de matemática básica",
            onDeckDescriptionChange = {},
            deckDifficulty = DifficultyLevel.EAZY,
            deckMastery = 0.85f,
            toggleEditMode = {},
            onSaveClick = {},
            onAddFlashcardClick = {}
        )
    }
}