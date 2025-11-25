package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.utils.GoldPrimary

@Composable
fun EditBirthdayButtons(
    onDone: () -> Unit,
    onSaveClick: () -> Unit,
    uiState: EditBirthdayViewModel.UiState

) {
    // Action buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Cancel button
        OutlinedButton(
            onClick = { onDone() },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Cancel",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Save button
        Button(
            onClick = {
                onSaveClick()
            },
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (uiState.isNew) "Save" else "Update",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}