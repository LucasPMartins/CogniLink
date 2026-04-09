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
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray

@Composable
fun SignUpLayout(
    email: String = "",
    onEmailChange: (String) -> Unit = {},
    password: String = "",
    onPasswordChange: (String) -> Unit = {},
    confirmPassword: String = "",
    onConfirmPasswordChange: (String) -> Unit = {},
    name: String = "",
    onNameChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }


    Column(modifier = Modifier,verticalArrangement = Arrangement.spacedBy(20.dp))
    {
        NameTextField(name = name, onNameChange = { name = it })
        EmailTextField(email = email, onEmailChange = { email = it })

        PasswordTextField(label = "CRIAR SENHA",password = password, onPasswordChange = { password = it })

        PasswordTextField(label = "CONFIRMAR SENHA",password = confirmPassword, onPasswordChange = { confirmPassword = it })

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
private fun SignUpLayoutPreview() {
    CogniLinkTheme {
        SignUpLayout()
    }
}