package com.example.cognilink.ui.components.auth


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.White

@Composable
fun EmailTextField(email: String,
                   onEmailChange: (String) -> Unit,
                   modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(
            text = "E-MAIL",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = DarkGray
        )
        Surface(modifier = modifier,shape = RoundedCornerShape(24.dp),border = BorderStroke(1.dp, Color(0xFFC6C5D4)))
        {
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically)
            {
                OutlinedTextField(
                    value = email, onValueChange = onEmailChange,
                    modifier = Modifier
                        .weight(1f),
                    textStyle = MaterialTheme.typography.titleLarge,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White,
                    ),
                    placeholder = {
                        Text(
                            text = "seu@email.com",
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        )
                    }
                )
            }
        }
    }

    
}

@Preview
@Composable
private fun EmailTextFieldPreview() {
    CogniLinkTheme {
        EmailTextField(email = "", onEmailChange = {})
    }
}