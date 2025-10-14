package de.carlavoneicken.birthdaysapp.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.utils.createPreviewBirthday
import de.carlavoneicken.birthdaysapp.utils.formattedNextBirthday
import de.carlavoneicken.birthdaysapp.utils.toDrawableRes


@Composable
fun BirthdayItemSubview(
    item: Birthday,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item.zodiacSign?.let { sign ->
            Image(
                painter = painterResource(id = sign.toDrawableRes()),
                contentDescription = sign.description,
                Modifier.height(40.dp).padding(end = 10.dp)
            )
        }

        Column(
            modifier = Modifier.wrapContentWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            if (item.nextAge != null) {
                Text(
                    text = "turns ${item.nextAge} on ${formattedNextBirthday(item)}",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Text(
                    text = "on ${formattedNextBirthday(item)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = item.daysFromNow,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(min = 48.dp) // gives some width so multiline alignment works
        )
    }
}


@Preview(showBackground = true)
@Composable
fun BirthdayItemSubviewPreview() {
    MaterialTheme {
        BirthdayItemSubview(
            item = createPreviewBirthday(),
            modifier = Modifier.padding(8.dp))
    }
}
