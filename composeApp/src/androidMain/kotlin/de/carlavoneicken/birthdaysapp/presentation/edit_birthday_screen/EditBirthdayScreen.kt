package de.carlavoneicken.birthdaysapp.presentation.edit_birthday_screen

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayEffect
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.notifications.ReminderSettings
import de.carlavoneicken.birthdaysapp.notifications.scheduleDailyWorker
import de.carlavoneicken.birthdaysapp.utils.BackgroundLight
import de.carlavoneicken.birthdaysapp.utils.TextPrimary
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.compose.koinInject
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

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

    // ------ Notifications and Permission----------------------------------------------------------

    // context for displaying the success message as a toast
    val context = LocalContext.current
    val settings: ReminderSettings = koinInject()

    // create and remember ActivityResultLauncher -> it is launched later
    // “Create a launcher that can perform some system operation and return a result.”
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        // the contract defines what operation the launcher will perform, what input it needs and what
        // type of result it will deliver -> all described in the ActivityResultContract class
        // RequestPermission: shows a system dialog for ONE permission, input: String (permission name), output: Boolean
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            ensureDailyWorkerScheduled(context, settings)
            Toast.makeText(context, "Birthday saved with reminders.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                context,
                "Notification permission denied. Please enable notifications in the system settings to activate reminders.",
                Toast.LENGTH_SHORT
            ).show()
        }
        onDone()
    }

    // LaunchedEffect(k1, k2, ...) runs a coroutine when keys change
    // However, Unit is a Kotlin singleton object -> it never changes
    // Therefore LaunchedEffect(Unit) runs the effect EXACTLY once the first time the Composable enters
    // the composition aka when the screen opens
    LaunchedEffect(Unit) {
        // start collecting the effects flow
        viewModel.effects.collect { effect ->
            when (effect) {
                // when the effect changes to BirthdaySaved check the permission state and if granted set Worker
                is EditBirthdayEffect.BirthdaySaved -> {
                    if (!effect.remindersEnabled) {
                        Toast.makeText(context, "Birthday saved.", Toast.LENGTH_SHORT).show()
                        onDone()
                    } else {
                        // if the device is using Android version lower than 13 (Tiramisu), no permission needed
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                            ensureDailyWorkerScheduled(context, settings)
                            Toast.makeText(context, "Birthday saved with reminders.", Toast.LENGTH_SHORT).show()
                            onDone()
                        } else {
                            // permission string = "android.permission.POST_NOTIFICATIONS"
                            // Manifest.permission -> is a class that contains constants for all runtime permissions
                            val permission = Manifest.permission.POST_NOTIFICATIONS

                            // check current state of the permission
                            // ContextCompat.checkSelfPermission returns PackageManager.PERMISSION_GRANTED if permission already granted,
                            // otherwise .PERMISSION_DENIED
                            // -> result is stored as a boolean
                            val isGranted = ContextCompat.checkSelfPermission(
                                context,
                                permission
                            ) == PackageManager.PERMISSION_GRANTED

                            // if permission is already granted, schedule worker, otherwise ask for permission
                            if (isGranted) {
                                ensureDailyWorkerScheduled(context, settings)
                                Toast.makeText(context, "Birthday saved with reminders.", Toast.LENGTH_SHORT).show()
                                onDone()
                            } else {
                                notificationPermissionLauncher.launch(permission)
                            }
                        }
                    }
                }
            }
        }
    }

    // ------ UI -----------------------------------------------------------------------------------
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

// if now DailyWorker is yet scheduled, schedule one
fun ensureDailyWorkerScheduled(context: Context, settings: ReminderSettings) {
    if (!settings.workerScheduled) {
        scheduleDailyWorker(
            context,
            hour = settings.reminderHour,
            minute = settings.reminderMinute
        )
        settings.workerScheduled = true
    }
}


@Preview
@Composable
fun EditBirthdayScreenPreview() {
    EditBirthdayScreen(birthdayId = 1)
}