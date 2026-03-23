package com.smartbit.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.smartbit.mobile.ui.navigation.SmartBitApp
import com.smartbit.mobile.ui.theme.PathwayTheme
import com.smartbit.mobile.ui.theme.SmartBitTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var selectedTheme by rememberSaveable { mutableStateOf(PathwayTheme.WELLNESS) }

            // Apply a global surface with the dark navy background color to eliminate white flashes between theme/screen transitions
            Surface(color = Color(0xFF0A0E21)) {
                Crossfade(
                    targetState = selectedTheme,
                    animationSpec = tween(durationMillis = 600),
                    label = "themeTransition"
                ) { theme ->
                    SmartBitTheme(pathwayTheme = theme) {
                        SmartBitApp(
                            currentTheme = theme,
                            onThemeSelected = { selectedTheme = it }
                        )
                    }
                }
            }
        }
    }
}
