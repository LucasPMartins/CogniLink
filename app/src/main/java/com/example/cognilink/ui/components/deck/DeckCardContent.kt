package com.example.cognilink.ui.components.deck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.cognilink.data.FlashCard
import com.example.cognilink.ui.theme.DarkNavyBlue
import kotlin.collections.forEach

@Composable
fun DeckCardContent(
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