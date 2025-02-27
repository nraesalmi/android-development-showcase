package com.example.examplehw2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examplehw2.ui.theme.ExampleHW2Theme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MyAppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onNavigateToProfile = { navController.navigate("profile_screen") },
                messages = SampleData.conversationSample
            )
        }
        composable("profile_screen") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack("main", false) }
            )
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExampleHW2Theme {
                val navController = rememberNavController()
                MyAppNavHost(navController = navController)
            }
        }
    }
}
