package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel.ReminderOption
import de.carlavoneicken.birthdaysapp.utils.OrangeAccent

@Composable
fun ReminderCheckboxRow(
    checked: Boolean,
    option: ReminderOption,
    label: String,
    onCheckedChange: (ReminderOption, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { isChecked ->
                onCheckedChange(option, isChecked)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = OrangeAccent,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(label)
    }
}