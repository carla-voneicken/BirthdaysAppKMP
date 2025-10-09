package de.carlavoneicken.birthdaysapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import de.carlavoneicken.birthdaysapp.presentation.BirthdaysListView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    // Optionally apply your theme
    //BirthdayTheme {
    BirthdaysListView()
    //}
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}