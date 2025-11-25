package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel.ReminderOption
import de.carlavoneicken.birthdaysapp.utils.OrangeAccent

@Composable
fun ReminderSection(
    uiState: EditBirthdayViewModel.ReminderUiState,
    onRemindMeChange: (Boolean) -> Unit,
    onOptionCheckedChange: (ReminderOption, Boolean) -> Unit,
    onCustomDaysChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(end = 30.dp)
        ) {
            TitleView(
                R.drawable.ic_alarm_24,
                "Alarm",
                "Reminders"
            )
            Spacer(Modifier.weight(1f))
            Switch(
                checked = uiState.remindMe,
                onCheckedChange = { isChecked ->
                    onRemindMeChange(isChecked)
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = OrangeAccent,
                    checkedTrackColor = OrangeAccent.copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.Gray,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }

        if (uiState.remindMe) {
            Spacer(Modifier.height(10.dp))

            ReminderCheckboxRow(
                uiState.onTheDay,
                option = ReminderOption.ON_THE_DAY,
                label = "On the day",
                onCheckedChange = onOptionCheckedChange,
            )

            ReminderCheckboxRow(
                checked = uiState.dayBefore,
                option = ReminderOption.DAY_BEFORE,
                label = "The day before",
                onCheckedChange = onOptionCheckedChange
            )

            ReminderCheckboxRow(
                checked = uiState.sevenDaysBefore,
                option = ReminderOption.SEVEN_DAYS_BEFORE,
                label = "7 days before",
                onCheckedChange = onOptionCheckedChange
            )

            ReminderCheckboxRow(
                checked = uiState.customEnabled,
                option = ReminderOption.CUSTOM_ENABLED,
                label = "Custom days before",
                onCheckedChange = onOptionCheckedChange
            )

            if (uiState.customEnabled) {
                Spacer(Modifier.height(8.dp))

                BirthdayTextField(
                    value = uiState.customDays,
                    onValueChange = { text ->
                        if (text.all { it.isDigit() }) {
                            onCustomDaysChange(text)
                        }
                    },
                    field = BirthdayFieldConfig.CUSTOM_REMINDER,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}