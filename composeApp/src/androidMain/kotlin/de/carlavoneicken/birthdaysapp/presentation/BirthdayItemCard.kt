package de.carlavoneicken.birthdaysapp.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.utils.formattedNextBirthday
import de.carlavoneicken.birthdaysapp.utils.toDrawableRes

@Composable
fun BirthdayItemCard(
    birthday: Birthday,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val zodiacColor = Color(0xFFB8860B)
    val daysColor = Color(0xCC097872)

    // for animation when pressed -> scales a bit down
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "cardScale"
    )


    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Zodiac circle on the left
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape
                    )
                    .border(
                        width = 3.dp,
                        color = zodiacColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                birthday.zodiacSign?.let { sign ->
                    Image(
                        painter = painterResource(id = sign.toDrawableRes()),
                        contentDescription = sign.description,
                        Modifier.size(50.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            // Name and birthday info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = birthday.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = buildString {
                        if (birthday.nextAge != null) {
                            append("turning ${birthday.nextAge} on ")
                        } else {
                            append("on ")
                        }
                        append(formattedNextBirthday(birthday))
                    },
                    fontSize = 13.sp,
                    color = zodiacColor,
                    fontWeight = FontWeight.Medium
                )
            }

            // Days countdown badge on the right
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = daysColor,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    val parts = birthday.daysFromNow.split(" ")
                    if (parts.size == 2) {
                        // "X days" or "X months"
                        Text(
                            text = parts[0], // The number
                            fontSize = 20.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = parts[1], // "days" or "months"
                            fontSize = 10.sp,
                            lineHeight = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    } else {
                        // "Today" or "Tomorrow"
                        Text(
                            text = birthday.daysFromNow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}