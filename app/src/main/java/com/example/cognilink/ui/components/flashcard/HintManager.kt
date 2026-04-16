package com.example.cognilink.ui.components.flashcard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.data.FlashcardType
import com.example.cognilink.ui.components.utils.LabeledText
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.VividCyan
import com.example.cognilink.ui.theme.White


@Composable
fun HintManager(
    modifier: Modifier = Modifier,
    hints: List<String> = emptyList(),
    onHintsUpdate: (List<String>) -> Unit,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        hints.forEachIndexed { index, hint ->
            val index = index + 1
            HintItem(
                label = "DICA $index",
                hint = hint,
                onHintChange = { newValue ->
                    val newList = hints.toMutableList()
                    newList[index - 1] = newValue
                    onHintsUpdate(newList)
                },
                onClickToRemove = {
                    val newList = hints.toMutableList()
                    newList.removeAt(index - 1)
                    onHintsUpdate(newList)
                }
            )
        }

        if (hints.size < 3) {
            OutlinedButton(
                onClick = {
                    val newList = hints.toMutableList()
                    newList.add("")
                    onHintsUpdate(newList)
                },
                modifier = Modifier.padding(top = 8.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = White,
                    containerColor = DarkNavyBlue
                )
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = VividCyan
                )
                Spacer(Modifier.width(8.dp))
                Text("Adicionar dica (${hints.size + 1}/3)")
            }
        }
    }
}

@Preview
@Composable
private fun HintManagerPreview() {
    //var listaTeste by remember { mutableStateOf(emptyList<String>()) }
    var listaTeste by remember { mutableStateOf(listOf("Teste 1", "Teste 2")) }
    CogniLinkTheme {
        HintManager(
            hints = listaTeste, onHintsUpdate = { listaTeste = it }
        )
    }
}