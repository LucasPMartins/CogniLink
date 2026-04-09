package com.example.cognilink.ui.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.cognilink.ui.components.AuthHeroHeader
import com.example.cognilink.ui.components.LegalFooterComponent
import com.example.cognilink.ui.components.LoginComponent
import com.example.cognilink.ui.components.SignUpComponent
import com.example.cognilink.ui.theme.CogniLinkTheme
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.OffWhite

@Composable
fun AuthScreen() {
    
}

@Composable
fun AuthContent(
    authOption: Boolean = false,
    onAuthOptionChange: (Boolean) -> Unit = {}
) {

    var optionState by remember { mutableStateOf(authOption) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = OffWhite),
    ) {

        AuthHeroHeader()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-30).dp)
                .zIndex(1f),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Color(0xFFF1F1F1),
                shape = RoundedCornerShape(24.dp),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = { optionState = false },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!optionState) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "ENTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (!optionState) DarkNavyBlue else DarkGray
                        )
                    }
                    Button(
                        onClick = { optionState = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (optionState) Color.White else Color.Transparent
                        )
                    ) {
                        Text(
                            "CADASTRAR",
                            fontWeight = FontWeight.Bold,
                            color = if (optionState) DarkNavyBlue else DarkGray
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-15).dp)
        ) {
            if (optionState) {
                SignUpComponent()
            } else
                LoginComponent()
        }

        Spacer(modifier = Modifier.weight(1f)) 
        LegalFooterComponent()

    }

}

@Preview
@Composable
private fun AuthContentPreview() {
    CogniLinkTheme{
        AuthContent(authOption = false)
    }
}