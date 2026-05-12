package com.example.cognilink.ui.feature.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.input.FileUploadArea
import com.example.cognilink.ui.components.flashcard.QuantitySelector
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.utils.LabeledText
import com.example.cognilink.ui.components.utils.SectionLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White


@Composable
fun IAGeneratorScreen(modifier: Modifier = Modifier) {

}

@Composable
fun IAGeneratorContent(
    modifier: Modifier = Modifier,
    flashcardTheme: String = "",
    onFlashcardThemeChange: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()
    var quantity by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { NavigationHeader(title = "CRIAR NOVO FLASHCARD") },
        containerColor = OffWhite
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // --- SEÇÃO TEMA ---
            Column {
                CustomTextField(
                    inputValue = flashcardTheme,
                    onInputValueChange = onFlashcardThemeChange,
                    label = "Tema do Flashcard",
                    placeholder = "Ex: Mitocôndrias e Ciclo de Krebs",
                    keyboardType = KeyboardType.Text
                )
                LabeledText(label = "OBS: ",text = "Se nulo, a IA criará com base no arquivo anexo. O tema será preenchido após a criação.")
            }

            // --- SEÇÃO ANEXO ---
            Column {
                Text(text = "Anexo de Arquivo (Opcional)", color = DarkGray, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                FileUploadArea(onUploadClick = { /* TODO */ })

                LabeledText(label = "OBS: ",text = "Se nulo, a IA criará com base no tema. Pelo menos o TEMA ou um ARQUIVO deve ser fornecido.")
            }

            // --- SEÇÃO SELETOR ---
            //TypeOptionList(selectedOption = null, onOptionSelected = { })

            // --- SEÇÃO CONFIGURAÇÕES (Dificuldade e Quantidade) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Dificuldade")
                    DifficultySelector(
                        difficultyLevels = listOf(DifficultyLevel.EAZY, DifficultyLevel.MEDIUM, DifficultyLevel.HARD),
                        selectedDifficulty = DifficultyLevel.EAZY,
                        onDifficultySelected = { }
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Quantidade")
                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChange = { quantity = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_stars),
                        contentDescription = null,
                        tint = VividCyan,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text("GERAR FLASHCARDS COM IA",
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }

            }
        }
    }
}


@Preview
@Composable
private fun IAGeneratorContentPreview() {
    CogniLinkTheme {
        IAGeneratorContent()
    }
}