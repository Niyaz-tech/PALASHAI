package com.palashai.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.palashai.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Curriculum : Screen("curriculum")
    object Lesson : Screen("lesson/{lessonId}") {
        fun createRoute(lessonId: String) = "lesson/$lessonId"
    }
    object VoiceTranslation : Screen("voice_translation")
    object Worksheets : Screen("worksheets")
    object Flashcards : Screen("flashcards")
    object Settings : Screen("settings")
    object WorksheetDetail : Screen("worksheet_detail/{worksheetId}") {
        fun createRoute(worksheetId: String) = "worksheet_detail/$worksheetId"
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(route = Screen.Curriculum.route) {
            CurriculumScreen(navController)
        }
        composable(
            route = Screen.Lesson.route,
            arguments = listOf(
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "0"
            LessonScreen(navController, lessonId)
        }
        composable(route = Screen.VoiceTranslation.route) {
            VoiceTranslationScreen(navController)
        }
        composable(route = Screen.Worksheets.route) {
            WorksheetScreen(navController)
        }
        composable(
            route = Screen.WorksheetDetail.route,
            arguments = listOf(
                navArgument("worksheetId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val worksheetId = backStackEntry.arguments?.getString("worksheetId") ?: ""
            WorksheetDetailScreen(navController, worksheetId)
        }
        composable(route = Screen.Flashcards.route) {
            FlashcardScreen(navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController)
        }
    }
}
