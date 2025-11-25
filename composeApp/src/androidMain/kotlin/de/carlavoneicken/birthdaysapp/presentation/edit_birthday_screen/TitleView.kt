package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.carlavoneicken.birthdaysapp.utils.OrangeAccent

@Composable
fun TitleView(
    imageId: Int,
    imageDescription: String,
    text: String
) {
    Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(id = imageId),
            contentDescription = imageDescription,
            tint = OrangeAccent
        )
        Text(
            text = text,
            fontSize = 20.sp,
            color = OrangeAccent,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}