package com.example.cognilink.ui.components.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.Gray

@Composable
fun LabeledText(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    labelColor: Color = DarkNavyBlue,
    textColor: Color = DarkGray,
    textSize: TextUnit = 12.sp
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = labelColor, fontWeight = FontWeight.Bold)) {
            append(label)
        }
        append(text)
    }
    Text(
        text = annotatedString,
        color = textColor,
        fontSize = textSize,
        modifier = modifier.fillMaxWidth()
    )
}