package com.example.cognilink.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue

@Composable
fun SignUpComponent(modifier: Modifier = Modifier) {
    Column(modifier = Modifier,verticalArrangement = Arrangement.spacedBy(20.dp))
    {
        NameTextField(name = "", onNameChange = {})
        EmailTextField(email = "", onEmailChange = {})
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "SENHA",
                style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                ),
                color = DarkGray,
            )
            PasswordTextField(password = "", onPasswordChange = {})
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(
                text = "CONFIRMAR SENHA",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = DarkGray,
            )
            PasswordTextField(password = "", onPasswordChange = {})
        }

        TermsAndPrivacyCheckbox()

        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = "CRIAR CONTA",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    painter = painterResource(
                        id = R.drawable.ic_arrow_forward
                    ),
                    contentDescription = "Entrar",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SignUpComponentPreview() {
    CogniLinkTheme {
        SignUpComponent()
    }
}