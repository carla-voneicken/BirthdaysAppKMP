package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import de.carlavoneicken.birthdaysapp.utils.OrangeAccent

@Composable
fun BirthdayTextField(
    value: String,
    onValueChange: (String) -> Unit,
    field: BirthdayFieldConfig,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(field.label) },
        placeholder = { Text(field.placeholder) },
        supportingText = { Text(field.supportingText ?: "") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = field.keyboardType),
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = OrangeAccent,
            focusedLabelColor = OrangeAccent,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}