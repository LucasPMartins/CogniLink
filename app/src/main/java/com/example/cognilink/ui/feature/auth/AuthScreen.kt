package com.example.cognilink.ui.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cognilink.ui.components.auth.Footer
import com.example.cognilink.ui.components.auth.Header
import com.example.cognilink.ui.components.auth.SignInContent
import com.example.cognilink.ui.components.auth.SignUpContent
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.VeryLightGray
import com.example.cognilink.viewModel.AuthViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = viewModel()) {
    AuthContent(viewModel = viewModel)
}

@Composable
fun AuthContent(
    viewModel: AuthViewModel
) {

    // Cria o estado da rolagem
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OffWhite),
    ) {
        Header()

        // Seletor (Entrar / Cadastrar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-30).dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = VeryLightGray,
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { viewModel.onModeChange(false) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!viewModel.isSignUpMode) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "ENTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (!viewModel.isSignUpMode) DarkNavyBlue else DarkGray
                        )
                    }
                    Button(
                        onClick = { viewModel.onModeChange(true) },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.isSignUpMode) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "CADASTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (viewModel.isSignUpMode) DarkNavyBlue else DarkGray
                        )
                    }
                }
            }
        }

        //Column interna que ocupará o espaço restante e terá scroll
        Column(
            modifier = Modifier
                .weight(1f) // Faz esta coluna ocupar todo o espaço entre Header e Footer
                .verticalScroll(scrollState) // Habilita a rolagem vertical
        ) {

            // Layouts de Login/Cadastro
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            ) {
                if (viewModel.isSignUpMode) {
                    SignUpContent(
                        name = viewModel.signUpName,
                        onNameChange = viewModel::onSignUpNameChange,
                        email = viewModel.signUpEmail,
                        onEmailChange = viewModel::onSignUpEmailChange,
                        password = viewModel.signUpPassword,
                        onPasswordChange = viewModel::onSignUpPasswordChange,
                        confirmPassword = viewModel.signUpConfirmPassword,
                        onConfirmPasswordChange = viewModel::onSignUpConfirmPasswordChange,
                        isTermsAccepted = viewModel.isTermsAccepted,
                        onTermsAcceptedChange = viewModel::onTermsAcceptedChange,
                        onSignUpClick = viewModel::onSignUpClick
                    )
                } else {
                    SignInContent(
                        email = viewModel.signInEmail,
                        onEmailChange = viewModel::onSignInEmailChange,
                        password = viewModel.signInPassword,
                        onPasswordChange = viewModel::onSignInPasswordChange,
                        onSignInClick = viewModel::onSignInClick,
                        onSignUpClick = { viewModel.onModeChange(true) }
                    )
                }
            }
        }

        //Footer fica fora da área de scroll para estar sempre visível
        Footer()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthScreenPreview() {
    CogniLinkTheme {
        val viewModel = remember { AuthViewModel() }
        AuthContent(viewModel = viewModel)
    }
}
