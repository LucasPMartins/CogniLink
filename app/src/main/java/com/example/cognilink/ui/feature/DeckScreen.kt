package com.example.cognilink.ui.feature

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.data.FlashCard
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.NeonActionButton
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.ProgressBar
import com.example.cognilink.ui.components.utils.SimpleGradientButton
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LavenderBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.VeryDarkGray
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.ui.theme.White
import com.example.cognilink.ui.theme.secondaryColor
import com.example.cognilink.ui.theme.tertiaryColor

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
    var showCategoryDialog by remember { mutableStateOf(false) }
    var categoryBeingEdited by remember { mutableStateOf<String?>(null) }
    var categoryText by remember { mutableStateOf("") }

    if (showCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showCategoryDialog = false },
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
                    onInputValueChange = { categoryText = it },
                    placeholder = "Ex: Medicina, História..."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (categoryText.isNotBlank()) {
                            val oldName = categoryBeingEdited
                            if (oldName == null) {
                                viewModel.addCategory(categoryText)
                            } else {
                                viewModel.updateCategory(oldName, categoryText)
                            }
                            showCategoryDialog = false
                        }
                    }
                ) {
                    Text("OK", fontWeight = FontWeight.Bold, color = DarkNavyBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCategoryDialog = false }) {
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
                            categoryBeingEdited = null
                            categoryText = ""
                            showCategoryDialog = true
                        },
                        onCategoryClickEdit = { category ->
                            categoryBeingEdited = category
                            categoryText = category
                            showCategoryDialog = true
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
                    EmptyDeckState()
            }
        }
    }
}

@Composable
private fun EditDeckContent(
    name: String,
    onNameChange: (String) -> Unit,
    categories: List<String>,
    onCategoryClickRemove: (String) -> Unit,
    onCategoryClickAdd: (String) -> Unit,
    onCategoryClickEdit: (String) -> Unit,
    description:String,
    onDescriptionChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)){
        CustomTextField(
            inputValue = name,
            onInputValueChange = onNameChange,
            label = "NOME DO BARALHO",
            labelColor = DarkNavyBlue,
            placeholder = "Nome do baralho"
        )
        CustomTextField(
            inputValue = description,
            onInputValueChange = onDescriptionChange,
            label = "DESCRIÇÃO",
            labelColor = DarkNavyBlue,
            placeholder = "Descrição do baralho"
        )
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "CATEGORIA",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = DarkNavyBlue
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = CenterVertically
            ) {
                categories.forEach { category ->
                    Surface(
                        color = MutedBlue,
                        shape = RoundedCornerShape(9999.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = category.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkNavyBlue,
                                modifier = Modifier.clickable { onCategoryClickEdit(category) }
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Remover",
                                modifier = Modifier
                                    .size(10.dp)
                                    .clickable { onCategoryClickRemove(category) },
                                tint = DarkNavyBlue
                            )
                        }
                    }
                }

                if (categories.size < 3){
                    IconButton(
                    onClick = { onCategoryClickAdd("Nova") },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Adicionar Categoria",
                        tint = DarkNavyBlue
                    )
                }}

            }
        }
    }
}

@Composable
private fun ViewDeckContent(
    categories: List<String>,
    difficulty: DifficultyLevel?,
    name: String,
    description:String,
    mastery: Float,
    totalCards: Int,
    cardToReview: Int,
    ) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp))
    {
        if (categories.isNotEmpty() || difficulty != null)
        {
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if(categories.isNotEmpty())
                    Surface(
                        color = MutedBlue,
                        shape = RoundedCornerShape(9999.dp)
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                            text = categories.joinToString(separator = " / ").uppercase(),
                            color = DarkNavyBlue.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp,
                        )
                    }
                if(difficulty != null)
                    Surface(
                        color = difficulty.tertiaryColor,
                        shape = RoundedCornerShape(9999.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(difficulty.secondaryColor, CircleShape)
                            )
                            Text(
                                text = difficulty.toDisplayName(),
                                color = DarkGray.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                            )
                        }
                    }
            }
        }

        Text(
            text = name,
            color = DarkNavyBlue,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 44.sp,
            lineHeight = 1.em
        )

        if(description.isNotEmpty()){
            Text(
                text = description,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = DarkGray
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF000666), // Cor inicial
                            Color(0xFF1222B0)  // Cor final
                        ),
                        // Opcional: define a direção (padrão é da esquerda-topo para direita-baixo)
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    ),
                    shape = RoundedCornerShape(26.dp)
                ),
            color = Color.Transparent,
            shape = RoundedCornerShape(26.dp),
        )
        {
            Column(
                modifier = Modifier.padding(16.dp),
                ) {
                Text(
                    text = "DOMÍNIO ATUAL",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
                Text(
                    text = "${(mastery * 100).toInt()}%",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                ProgressBar(
                    progressColor = White,
                    progress = mastery,
                    modifier = Modifier.height(8.dp)
                )
            }
            if(mastery == 0f){
                Text(
                    text = "Estude com flashcards nesse baralho para aumentar seu domínio",
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

        }

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp))
        {
            Surface(
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.weight(0.5f),
                color = VeryLightGray.copy(alpha = 0.5f)
            )
            {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_deck),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = "TOTAL DE CARDS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$totalCards",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue
                    )
                }
            }
            Surface(
                shape = RoundedCornerShape(26.dp),
                modifier = Modifier.weight(0.5f),
                color = VeryLightGray.copy(alpha = 0.5f)
            )
            {
                Column(modifier = Modifier.padding(16.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_card_review),
                        contentDescription = null,
                        tint = DarkNavyBlue,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = "PARA REVISAR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkGray.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "$cardToReview",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkNavyBlue
                    )
                }
            }
        }
    }
}

@Composable
private fun DeckCardContent(
    isEditMode: Boolean = false,
    onClickTextButton: () -> Unit,
    deckList: List<FlashCard> = listOf()
    ) {
    Row(Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically
    ) {
        Text(
            text = if (isEditMode) "Conteúdo do Baralho" else "Próximos Tópicos",
            color = DarkNavyBlue,
            fontWeight = FontWeight.SemiBold,
        )
        TextButton(onClick = onClickTextButton, contentPadding = PaddingValues(0.dp)) {
            Text(
                text = if (isEditMode) "Gerenciar Cards" else "Ver todos",
                color = DarkNavyBlue,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        deckList.forEach { card ->
            DeckCardItem(flashCard = card, onSelectCard = {})
        }
    }
}

@Composable
private fun DeckCardItem(
    modifier: Modifier = Modifier,
    flashCard: FlashCard,
    onSelectCard: () -> Unit
    ) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        onClick = onSelectCard,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = White
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 4.dp),
            verticalAlignment = CenterVertically
        ) {
            Surface(
                color = MutedBlue,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = flashCard.cardType.getIcon()),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp),
                    tint = DarkNavyBlue
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = flashCard.question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = VeryDarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Revisão necessária em ${flashCard.nextReview} dias",
                    fontSize = 14.sp,
                    color = DarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(
                onClick = onSelectCard,
                modifier = Modifier.offset(x = 8.dp)
            ){
                Icon(
                    painterResource(id = R.drawable.ic_keyboard_arrow_down),
                    contentDescription = null,
                    tint = LightGray,
                    modifier = Modifier.rotate(-90f)
                )
            }
        }
    }
}


@Composable
private fun EmptyDeckState() {
    Surface(color = VeryLightGray.copy(alpha = 0.5f), shape = RoundedCornerShape(32.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_deck),
                contentDescription = null,
                tint = DarkNavyBlue.copy(alpha = 0.6f),
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
                verticalAlignment = CenterVertically
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