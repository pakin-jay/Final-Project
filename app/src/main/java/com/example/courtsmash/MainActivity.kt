package com.example.courtsmash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.courtsmash.navigation.AppDependencies
import com.example.courtsmash.ui.theme.CourtSmashTheme
import com.example.courtsmash.ui.theme.WarmIvoryBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dependencies = AppDependencies(applicationContext)
        setContent {
            CourtSmashTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = WarmIvoryBackground
                ) {
                    CourtSmashApp(dependencies = dependencies)
                }
            }
        }
    }
}
