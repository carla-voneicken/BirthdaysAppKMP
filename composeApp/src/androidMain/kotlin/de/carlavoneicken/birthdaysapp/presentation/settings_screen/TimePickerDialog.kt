package de.carlavoneicken.birthdaysapp.presentation.settings_screen

import android.text.format.DateFormat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    currentTime: String,
    onDismiss: () -> Unit,
    onConfirm: (Pair<Int, Int>) -> Unit
) {
    val context = LocalContext.current
    val is24Hours = DateFormat.is24HourFormat(context)

    // parse current time to initialize the picker
    val (hour, minute) = parseTime(currentTime)

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = is24Hours
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select reminder time") },
        text = {
            TimePicker(state = timePickerState)
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(timePickerState.hour to timePickerState.minute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private fun parseTime(timeString: String): Pair<Int, Int> {

    val parts = timeString.split(":", " ")
    val hour = parts[0].toInt()
    val minute: Int = parts[1].toInt()
    val twentyfourHourFormat = parts.size == 2

    return if (twentyfourHourFormat) {
        hour to minute
    } else if (parts[2] == "AM") {
        if (hour == 12) {
            0 to minute
        } else {
            hour to minute
        }
    } else if (parts[2] == "PM"){
        if (hour == 12) {
            hour to minute
        } else {
            (hour + 12) to minute
        }
    } else {
        throw IllegalArgumentException("Invalid time format: $timeString")
    }
}

private fun formatTime(hour: Int, minute: Int, is24Hour: Boolean): String {
    return if (is24Hour) {
        String.format("%02d:%02d", hour, minute)
    } else {
        val period = if (hour < 12) "AM" else "PM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        String.format("%d:%02d %s", displayHour, minute, period)
    }
}