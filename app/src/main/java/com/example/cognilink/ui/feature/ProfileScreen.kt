package com.example.cognilink.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.domain.UserStats
import com.example.cognilink.domain.fakeUser
import com.example.cognilink.ui.components.utils.GradientSurface
import com.example.cognilink.ui.components.utils.NavigationHeader
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LavenderBlue
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.OffWhite
import com.example.cognilink.ui.theme.White

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    userName: String = "João Silva",
    userRank: String = "Iniciante",
    userStats: UserStats,

    ) {
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = modifier
            .padding()
            .statusBarsPadding(),
        topBar = {
            NavigationHeader(title = "Perfil")
        },
        containerColor = OffWhite
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = userName,
//                        color = DarkNavyBlue,
//                        fontSize = 36.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_star),
//                            tint = Yellow,
//                            contentDescription = "",
//                            modifier = Modifier.padding(end = 4.dp)
//                        )
//                        Text(
//                            text = userRank,
//                            color = DarkGray,
//                            fontSize = 16.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//                Surface(
//                    color = White,
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(24.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        Row(
//                            Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            Surface(
//                                color = VeryLightRed,
//                                shape = CircleShape
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_fire),
//                                    contentDescription = null,
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                            }
//                            Column() {
//                                Text(
//                                    text = "$dayStreak dias", color = DarkGray,
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Text(
//                                    text = "Ofenciva", color = DarkGray.copy(alpha = 0.7f),
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.Normal
//                                )
//                            }
//                        }
//                        Row(
//                            Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            Surface(
//                                color = MutedBlue,
//                                shape = CircleShape
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_trophy),
//                                    contentDescription = null,
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                            }
//                            Column() {
//                                Text(
//                                    text = "$longestStreak dias", color = DarkGray,
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Text(
//                                    text = "Maior Ofenciva", color = DarkGray.copy(alpha = 0.7f),
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.Normal
//                                )
//                            }
//                        }
//                        Row(
//                            Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(12.dp)
//                        ) {
//                            Surface(
//                                color = OffWhite,
//                                shape = CircleShape
//                            ) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_calendar),
//                                    contentDescription = null,
//                                    modifier = Modifier.padding(8.dp)
//                                )
//                            }
//                            Column() {
//                                Text(
//                                    text = "$lastActiveDate", color = DarkGray,
//                                    fontSize = 18.sp,
//                                    fontWeight = FontWeight.Bold
//                                )
//                                Text(
//                                    text = "Último acesso", color = DarkGray.copy(alpha = 0.7f),
//                                    fontSize = 14.sp,
//                                    fontWeight = FontWeight.Normal
//                                )
//                            }
//                        }
//                    }
//                }
//                Text(
//                    text = "Minhas Estatísticas", color = DarkNavyBlue,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Surface(
//                    color = White,
//                    shape = RoundedCornerShape(24.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.padding(32.dp),
//                        verticalArrangement = Arrangement.spacedBy(32.dp)
//                    ) {
//                        Column(
//                            Modifier.fillMaxWidth(),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {

//                        }
//
//                    }
//                }
                Surface(
                    color = White,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        GradientSurface(shape = CircleShape) {
                            Surface(
                                color = White,
                                shape = CircleShape,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(110.dp) // Ou .aspectRatio(1f) para ser dinâmico
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${userStats.cognitiveEfficiencyIndex}",
                                            fontSize = 48.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkNavyBlue,
                                        )
                                        Text(
                                            text = "ÍNDICE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.W700,
                                            color = DarkGray
                                        )
                                    }

                                }
                            }
                        }
                        Column(
                            Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Eficiência Cognitiva",
                                color = DarkNavyBlue,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Seu cérebro está absorvendo mais\n" +
                                        "conteúdo em menos tempo.",
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                GradientSurface(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_stack),
                                contentDescription = null,
                                tint = White
                            )
                            val annotatedString = buildAnnotatedString {
                                append("$ Flashcards ")
                                withStyle(
                                    style = SpanStyle(
                                        color = LavenderBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append("a Revisar")
                                }
                            }

                            Text(
                                text = annotatedString,
                                color = White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )


                        }
                        Button(
                            onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = White,
                                contentColor = DarkNavyBlue
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Revisar Agora",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
                Surface(color = White) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(color = MutedBlue, shape = RoundedCornerShape(12.dp)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_file),
                                    tint = DarkNavyBlue,
                                    contentDescription = null,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Text(
                                text = "VOLUME DE CARDS",
                                color = DarkGray,
                                fontWeight = FontWeight.W700,
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${userStats.totalFlashcards}",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 30.sp
                            )
                            Text(
                                text = "Total",
                                fontSize = 18.sp,
                                color = DarkGray
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProfileContentPreview() {
    ProfileContent(userStats = fakeUser.stats)
}

