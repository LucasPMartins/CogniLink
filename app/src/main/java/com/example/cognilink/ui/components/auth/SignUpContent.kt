package com.example.cognilink.ui.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cognilink.R
import com.example.cognilink.ui.components.input.PasswordTextField
import com.example.cognilink.ui.components.input.CustomTextField
import com.example.cognilink.ui.theme.CogniLinkTheme

@Composable
fun SignUpContent(
    modifier: Modifier = Modifier,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    isTermsAccepted: Boolean = false,
    onTermsAcceptedChange: (Boolean) -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        CustomTextField(
            label = "NOME",
            placeholder = "Seu nome",
            inputValue = name,
            onInputValueChange = onNameChange
        )

        CustomTextField(
            label = "E-MAIL",
            placeholder = "seu@email.com",
            inputValue = email,
            onInputValueChange = onEmailChange
        )

        PasswordTextField(
            label = "CRIAR SENHA",
            password = password,
            onPasswordChange = onPasswordChange
        )

        PasswordTextField(
            label = "CONFIRMAR SENHA",
            password = confirmPassword,
            onPasswordChange = onConfirmPasswordChange
        )

        TermsCheckbox(
            checkedState = isTermsAccepted,
            onCheckedChange = onTermsAcceptedChange
        )

        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isTermsAccepted
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CRIAR CONTA",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 10.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Criar Conta",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpContentPreview() {
    CogniLinkTheme {
        var name by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }
        var isTermsAccepted by remember { mutableStateOf(false) }

        SignUpContent(
            name = name,
            onNameChange = { name = it },
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            confirmPassword = confirmPassword,
            onConfirmPasswordChange = { confirmPassword = it },
            isTermsAccepted = isTermsAccepted,
            onTermsAcceptedChange = { isTermsAccepted = it }
        )
    }
}
