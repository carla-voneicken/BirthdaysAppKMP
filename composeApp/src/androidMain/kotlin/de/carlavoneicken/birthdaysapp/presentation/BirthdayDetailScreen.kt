package de.carlavoneicken.birthdaysapp.presentation

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdayDetailViewModel
import de.carlavoneicken.birthdaysapp.data.models.Birthday
import de.carlavoneicken.birthdaysapp.data.utils.formattedBirthDate
import de.carlavoneicken.birthdaysapp.utils.BackgroundLight
import de.carlavoneicken.birthdaysapp.utils.GoldPrimary
import de.carlavoneicken.birthdaysapp.utils.OrangeAccent
import de.carlavoneicken.birthdaysapp.utils.TextPrimary
import de.carlavoneicken.birthdaysapp.utils.TextSecondary
import de.carlavoneicken.birthdaysapp.utils.toDrawableRes
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

// connects to ViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailScreen(
    birthdayId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    val viewModel: BirthdayDetailViewModel = koinViewModel {
        parametersOf(birthdayId)
    }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Handle side effects (navigation, toasts)
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
            onNavigateBack()
        }
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    BirthdayDetailContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onNavigateToEdit = onNavigateToEdit,
        onDeleteBirthday = { viewModel.deleteBirthday() },
        onRetryLoad = { viewModel.loadBirthday(birthdayId) }
    )
}

// pure UI that can be previewed
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayDetailContent(
    uiState: BirthdayDetailViewModel.UiState,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit,
    onDeleteBirthday: () -> Unit,
    onRetryLoad: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                showDeleteDialog = false
                onDeleteBirthday()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            BirthdayDetailTopBar(
                onNavigateBack = onNavigateBack,
                onDeleteClick = { showDeleteDialog = true }
            )
        },floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToEdit,
                containerColor = GoldPrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
        },
        containerColor = BackgroundLight
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(top = 30.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                uiState.isLoading ->
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = GoldPrimary
                    )

                // Not found state (record deleted, wrong id, etc.)
                uiState.birthday == null -> {
                    NotFoundState(
                        onNavigateBack = onNavigateBack,
                        onRetry = onRetryLoad
                    )
                }

                else -> {
                    // by assigning uiState.birthday to a local val we can safely use the non-null assertion operator
                    // since we already verified it's not null in the when condition above
                    val birthday = uiState.birthday!!
                    BirthdayDetailView(birthday = birthday)
                }
            }
        }
    }
}
// Top Bar with Deletion functionality
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthdayDetailTopBar(
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Birthday",
                    tint = TextPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundLight,
            titleContentColor = TextPrimary
        )
    )
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete birthday") },
        text = {
            Text("Are you sure you want to delete this birthday? This action cannot be undone.")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun NotFoundState(
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Birthday not found",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onNavigateBack) {
                Text("Back")
            }
            OutlinedButton(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}

@Composable
private fun BirthdayDetailView(birthday: Birthday) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Zodiac Sign image
        Box(
            modifier = Modifier
                .size(170.dp)
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = 5.dp,
                    color = GoldPrimary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            birthday.zodiacSign?.let { sign ->
                Image(
                    painter = painterResource(id = sign.toDrawableRes()),
                    contentDescription = sign.description,
                    Modifier.size(
                        150.dp
                    ),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = birthday.name,
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = TextSecondary
        )

        BirthdayInfoCard(birthday = birthday)

        Text(
            text = playfulCountdown(birthday.daysFromNow),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = OrangeAccent,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BirthdayInfoCard(birthday: Birthday) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = GoldPrimary.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        val nextAge = birthday.nextAge

        if (nextAge != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoChip(
                    iconRes = R.drawable.ic_calendar_24,
                    label = "Birth date",
                    value = formattedBirthDate(birthday),
                    modifier = Modifier.weight(2f)
                )
                InfoChip(
                    iconRes = R.drawable.ic_cake_24,
                    label = "Age",
                    value = (nextAge - 1).toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            InfoChip(
                iconRes = R.drawable.ic_calendar_24,
                label = "Birth date",
                value = formattedBirthDate(birthday),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            )
        }
    }
}

@Composable
private fun InfoChip(
    @DrawableRes iconRes: Int,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(label, color = TextPrimary, fontSize = 16.sp)
        }
        Spacer(Modifier.height(8.dp))
        Text(
            value,
            color = TextPrimary,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp
        )
    }
}

private fun playfulCountdown(daysFromNow: String): String {
    // daysFromNow is like "11 days" or "2 months" or "Today"
    val countdown = daysFromNow.lowercase()
    return when (countdown) {
        "Today!" -> "🎉 It’s today! Don’t forget to celebrate."
        "1 day" -> "🎈 Tomorrow! Time to get the cake ready."
        else -> "⏳ Only $daysFromNow to go — better start planning!"
    }
}



@Preview
@Composable
fun BirthdayDetailScreenPreview() {
    BirthdayDetailContent(
        uiState = BirthdayDetailViewModel.UiState(
            birthday = Birthday(
                id = 1,
                name = "Alice Johnson",
                day = 15,
                month = 3,
                year = 1990
            ),
            isLoading = false
        ),
        onNavigateBack = {},
        onNavigateToEdit = {},
        onDeleteBirthday = {},
        onRetryLoad = {}
    )
}