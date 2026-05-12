package com.example.cognilink.ui.feature.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.flashcard.AnswerOption
import com.example.cognilink.ui.components.flashcard.Header
import com.example.cognilink.ui.components.flashcard.HintReveal
import com.example.cognilink.ui.components.flashcard.AnswerOptions
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.White
import kotlinx.coroutines.delay

fun formatSeconds(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    if(h == 0L) return String.format("%02d:%02d", m, s)
    return String.format("%02d:%02d:%02d", h, m, s)
}

@Composable
fun FlashcardStudyContent(
    modifier: Modifier = Modifier,
    ) {

    var secondsElapsed by remember { mutableLongStateOf(0L) }

    var sequenceHits by remember { mutableIntStateOf(0) }

    // LaunchedEffect inicia quando o componente entra na tela
    // O 'Unit' garante que ele rode apenas uma vez e não reinicie sozinho
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Espera 1 segundo
            secondsElapsed++
        }
    }

    val formattedTime = formatSeconds(secondsElapsed)

    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .statusBarsPadding(),
        topBar = { Header() },
        containerColor = OffWhite,
        bottomBar = {
            Column(modifier = Modifier.padding(24.dp)) {

            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Row(verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    shadowElevation = 2.dp,
                    color = White
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null,
                            tint = DarkNavyBlue,
                        )
                        Text(text = "TEMPO: $formattedTime", fontWeight = FontWeight.SemiBold, color = DarkGray)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(32.dp),
                    shadowElevation = 2.dp,
                    color = White
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cached),
                            contentDescription = null,
                            tint = DarkNavyBlue,
                        )
                        Text(text = "SEQUÊNCIA: $sequenceHits", fontWeight = FontWeight.SemiBold, color = DarkGray)
                    }
                }
            }

            Surface(
                shape = RoundedCornerShape(32.dp),
                shadowElevation = 2.dp,
                color = White
            ) {
                Text(text = "Qual organela é conhecida como a 'central elétrica' da célula?",
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkNavyBlue,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth()
                    )
            }
            var listaTeste by remember { mutableStateOf(listOf(AnswerOption("Resposta 1", true), AnswerOption("Resposta 2", false), AnswerOption("Resposta 3", false))) }

            var selectedAnswer by remember { mutableStateOf(listaTeste[0]) }

            AnswerOptions(
                flashcardType = FlashcardType.MULTIPLE_CHOICE,
                responses = listaTeste,
                selectedAnswer = selectedAnswer,
                onSelectedAnswer = { response -> selectedAnswer = response }
            )

            HintReveal(hints = listOf("Dica 1", "Dica 2", "Dica 3"))

        }
    }

}

@Preview
@Composable
private fun FlashcardStudyContentPreview() {
    CogniLinkTheme {
        FlashcardStudyContent()
    }
}