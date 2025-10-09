package de.carlavoneicken.birthdaysapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdaysViewModel

@Composable
fun BirthdaysListView() {
    val viewModel: BirthdaysViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.birthdays) { birthday ->
            BirthdayItemSubview(birthday)
        }
    }
}