package de.carlavoneicken.birthdaysapp.presentation.birthdays_list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.carlavoneicken.birthdaysapp.R
import de.carlavoneicken.birthdaysapp.utils.GoldPrimary

@Composable
fun CakeFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = GoldPrimary,   // pick your brand color
    contentColor: Color = Color.White
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(74.dp),
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = FloatingActionButtonDefaults.elevation(6.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(R.drawable.bg_cake),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(55.dp)
            )
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(35.dp),
                tint = GoldPrimary
            )
        }
    }
}
