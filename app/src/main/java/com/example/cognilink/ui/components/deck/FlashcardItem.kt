package com.example.cognilink.ui.components.deck

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cognilink.R
import com.example.cognilink.domain.model.FlashcardType
import com.example.cognilink.ui.theme.DarkGray
import com.example.cognilink.ui.theme.DarkNavyBlue
import com.example.cognilink.ui.theme.LightGray
import com.example.cognilink.ui.theme.MutedBlue
import com.example.cognilink.ui.theme.VeryDarkGray
import com.example.cognilink.ui.theme.White

@Composable
fun FlashcardItem(
    modifier: Modifier = Modifier,
    flashcardType: FlashcardType,
    flashcardQuestion: String,
    nextReview: String,
    onSelectCard: () -> Unit,
    selectionControl: @Composable (() -> Unit)? = null,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        onClick = onSelectCard,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 2.dp,
        color = White
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp),
            verticalAlignment = CenterVertically
        ) {
            Surface(
                color = MutedBlue,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    painter = painterResource(id = flashcardType.getIcon()),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(30.dp),
                    tint = DarkNavyBlue
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = flashcardQuestion,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = VeryDarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = nextReview,
                    fontSize = 14.sp,
                    color = DarkGray,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (selectionControl != null) {
                selectionControl()
            }
        }
    }
}