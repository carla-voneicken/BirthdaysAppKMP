package de.carlavoneicken.birthdaysapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.business.viewmodels.BirthdaysViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdaysListView() {
    val viewModel: BirthdaysViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Birthdays",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sort_24),
                            contentDescription = "Sort"
                        )
                    }

                    DropdownMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        Modifier.background(
                            color = MaterialTheme.colorScheme.background)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sort by Date") },
                            onClick = {
                                viewModel.setSortMode(BirthdaysViewModel.SortMode.BY_UPCOMING)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_calendar_24),
                                    contentDescription = "Sort"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Sort by Name") },
                            onClick = {
                                viewModel.setSortMode(BirthdaysViewModel.SortMode.BY_NAME)
                                showSortMenu = false
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_abc_24),
                                    contentDescription = "Sort"
                                )
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5),
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = paddingValues.calculateTopPadding() + 8.dp,
                bottom = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(uiState.birthdays) { birthday ->
                BirthdayItemCard(
                    birthday = birthday,
                    onClick = {
                        // Handle click - navigate to detail or edit
                        println("Clicked on ${birthday.name}")
                    }
                )
            }
        }
        Box(Modifier.fillMaxSize()) {
            CakeFab(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(36.dp)
            )
        }
    }
}