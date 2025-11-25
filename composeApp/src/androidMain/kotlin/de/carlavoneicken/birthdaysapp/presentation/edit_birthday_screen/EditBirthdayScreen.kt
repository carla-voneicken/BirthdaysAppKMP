package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.utils.BackgroundLight
import de.carlavoneicken.birthdaysapp.utils.TextPrimary
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBirthdayScreen(
    birthdayId: Long? = null, // null for new birthday, id for editing
    onDone: () -> Unit = {}
) {

    val viewModel: EditBirthdayViewModel = koinViewModel {
        parametersOf(birthdayId)
    }
    val uiState by viewModel.uiState.collectAsState()

    // context for displaying the success message as a toast
    val context = LocalContext.current
    // LaunchedEffect runs a coroutine whenever the key(s) passed to it change
    // here: "run this block whenever uiState.successMessage changes"
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            // Show a Toast
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            // Navigate back
            onDone()
            // Reset state so it doesn’t re-trigger
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isNew) "New Birthday" else "Edit Birthday",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onDone() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BackgroundLight,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.errorMessage?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFc62828),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Name field
            TitleView(
                R.drawable.ic_person_24,
                "Person",
                "Name"
            )

            BirthdayTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                field = BirthdayFieldConfig.NAME,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Birthdate
            TitleView(
                R.drawable.ic_calendar_24,
                "Calendar",
                "Birthdate"
            )

            // Date fields row
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day field
                BirthdayTextField(
                    value = uiState.day,
                    onValueChange = { viewModel.updateDay(it) },
                    field = BirthdayFieldConfig.DAY,
                    modifier = Modifier.weight(1f)
                )

                // Month field
                BirthdayTextField(
                    value = uiState.month,
                    onValueChange = { viewModel.updateMonth(it) },
                    field = BirthdayFieldConfig.MONTH,
                    modifier = Modifier.weight(1f)
                )

                // Year field (optional)
                BirthdayTextField(
                    value = uiState.year,
                    onValueChange = { viewModel.updateYear(it) },
                    field = BirthdayFieldConfig.YEAR,
                    modifier = Modifier.weight(1f)
                )
            }

            // Reminders
            ReminderSection(
                uiState = uiState.reminders,
                onRemindMeChange = { remindMe ->
                    viewModel.setRemindMe(remindMe)
                },
                onOptionCheckedChange = { option, checked ->
                    viewModel.toggleReminderOption(option, checked)
                },
                onCustomDaysChange = { days ->
                    viewModel.setCustomDays(days)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Save and Cancel buttons
            EditBirthdayButtons(
                onDone = { onDone() },
                onSaveClick = { viewModel.saveBirthday() },
                uiState = uiState
            )
        }
    }
}

@Preview
@Composable
fun EditBirthdayScreenPreview() {
    EditBirthdayScreen(birthdayId = 1)
}