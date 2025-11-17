package de.carlavoneicken.birthdaysapp.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.business.viewmodels.EditBirthdayViewModel
import de.carlavoneicken.birthdaysapp.utils.BackgroundLight
import de.carlavoneicken.birthdaysapp.utils.GoldPrimary
import de.carlavoneicken.birthdaysapp.utils.OrangeAccent
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

    // context for displaying the success message as a toast
    val context = LocalContext.current
    val viewModel: EditBirthdayViewModel = koinViewModel {
        parametersOf(birthdayId)
    }
    val uiState by viewModel.uiState.collectAsState()

    // LaunchedEffect runs a coroutine whenever the key(s) passed to it change
    // here: "run this block whenever uiState.successMessage changes"
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            // Show a Toast
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            // Navigate back or do any other side-effect
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person_24),
                    contentDescription = "Person",
                    tint = OrangeAccent
                )
                Text(
                    text = "Name",
                    fontSize = 20.sp,
                    color = OrangeAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
            // Name field
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Name") },
                placeholder = { Text("Enter name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = OrangeAccent,
                    focusedLabelColor = OrangeAccent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_24),
                    contentDescription = "Calendar",
                    tint = OrangeAccent
                )
                Text(
                    text = "Birth Date",
                    fontSize = 20.sp,
                    color = OrangeAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            // Date fields row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day field
                OutlinedTextField(
                    value = uiState.day,
                    onValueChange = { viewModel.updateDay(it) },
                    label = { Text("Day") },
                    placeholder = { Text("DD") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        focusedLabelColor = OrangeAccent,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                // Month field
                OutlinedTextField(
                    value = uiState.month,
                    onValueChange = { viewModel.updateMonth(it) },
                    label = { Text("Month") },
                    placeholder = { Text("MM") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        focusedLabelColor = OrangeAccent,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )

                // Year field (optional)
                OutlinedTextField(
                    value = uiState.year,
                    onValueChange = {
                        if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                            viewModel.updateYear(it)
                        }
                    },
                    label = { Text("Year") },
                    placeholder = { Text("YYYY") },
                    supportingText = { Text("Optional") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1.2f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangeAccent,
                        focusedLabelColor = OrangeAccent,
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    )
                )
            }


            Spacer(modifier = Modifier.weight(1f))

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
                        viewModel.saveBirthday()
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
    }
}

@Preview
@Composable
fun EditBirthdayScreenPreview() {
    EditBirthdayScreen(birthdayId = 1)
}