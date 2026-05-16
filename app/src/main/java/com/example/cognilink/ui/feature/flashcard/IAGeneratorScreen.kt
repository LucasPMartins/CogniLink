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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.R
import com.example.cognilink.data.DifficultyLevel
import com.example.cognilink.ui.components.flashcard.DifficultySelector
import com.example.cognilink.ui.components.flashcard.FlashcardOption
import com.example.cognilink.ui.components.flashcard.QuantitySelector
import com.example.cognilink.ui.components.flashcard.TypeOptionList
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.components.input.FileUploadArea
import com.example.cognilink.ui.components.utils.labels.LabeledText
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.components.utils.labels.SectionLabel
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White
import com.example.cognilink.viewModel.IAGeneratorViewModel

@Composable
fun IAGeneratorScreen(
    modifier: Modifier = Modifier,
    viewModel: IAGeneratorViewModel = viewModel()
) {
    IAGeneratorContent(
        modifier = modifier,
        flashcardTheme = viewModel.flashcardTheme,
        onFlashcardThemeChange = viewModel::onThemeChange,
        quantity = viewModel.quantity,
        onQuantityChange = viewModel::onQuantityChange,
        selectedDifficulty = viewModel.selectedDifficulty,
        onDifficultyChange = viewModel::onDifficultyChange,
        typeOptions = viewModel.typeOptions,
        selectedType = viewModel.selectedType,
        onTypeChange = viewModel::onTypeChange,
        onGenerateClick = viewModel::generateFlashcards,
        isLoading = viewModel.isLoading
    )
}

@Composable
fun IAGeneratorContent(
    modifier: Modifier = Modifier,
    flashcardTheme: String = "",
    onFlashcardThemeChange: (String) -> Unit = {},
    quantity: Int = 1,
    onQuantityChange: (Int) -> Unit = {},
    selectedDifficulty: DifficultyLevel? = null,
    onDifficultyChange: (DifficultyLevel?) -> Unit = {},
    typeOptions: List<FlashcardOption> = emptyList(),
    selectedType: FlashcardOption,
    onTypeChange: (FlashcardOption) -> Unit = {},
    onGenerateClick: () -> Unit = {},
    isLoading: Boolean = false
) {
    val scrollState = rememberScrollState()

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
                LabeledText(
                    label = "OBS: ",
                    text = "Se nulo, a IA criará com base no arquivo anexo. O tema será preenchido após a criação."
                )
            }

            // --- SEÇÃO ANEXO ---
            Column {
                Text(text = "Anexo de Arquivo (Opcional)", color = DarkGray, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(10.dp))

                FileUploadArea(onUploadClick = { /* TODO */ })

                LabeledText(
                    label = "OBS: ",
                    text = "Se nulo, a IA criará com base no tema. Pelo menos o TEMA ou um ARQUIVO deve ser fornecido."
                )
            }

            // --- SEÇÃO SELETOR ---

            TypeOptionList(
                options = typeOptions,
                selectedOption = selectedType,
                onOptionSelected = onTypeChange
            )


            // --- SEÇÃO CONFIGURAÇÕES (Dificuldade e Quantidade) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Dificuldade")
                    DifficultySelector(
                        difficultyLevels = DifficultyLevel.entries,
                        selectedDifficulty = selectedDifficulty,
                        onDifficultySelected = onDifficultyChange,
                        includeAllOption = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    SectionLabel("Quantidade")
                    QuantitySelector(
                        quantity = quantity,
                        onQuantityChange = onQuantityChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Button(
                onClick = onGenerateClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_stars),
                        contentDescription = null,
                        tint = VividCyan,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        if (isLoading) "GERANDO..." else "GERAR FLASHCARDS COM IA",
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
        val viewModel = remember{
            IAGeneratorViewModel()
        }

        IAGeneratorContent(
            flashcardTheme = viewModel.flashcardTheme,
            onFlashcardThemeChange = viewModel::onThemeChange,
            quantity = viewModel.quantity,
            onQuantityChange = viewModel::onQuantityChange,
            selectedDifficulty = viewModel.selectedDifficulty,
            onDifficultyChange = viewModel::onDifficultyChange,
            typeOptions = viewModel.typeOptions,
            selectedType = viewModel.selectedType,
            onTypeChange = viewModel::onTypeChange,
            onGenerateClick = viewModel::generateFlashcards,
            isLoading = viewModel.isLoading
        )
    }
}
