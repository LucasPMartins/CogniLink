package com.example.cognilink.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cognilink.ui.screens.auth.AuthScreen
import com.example.cognilink.ui.screens.auth.TermsScreen
import com.example.cognilink.ui.screens.deck.DeckEditorScreen
import com.example.cognilink.ui.screens.deck.DeckScreen
import com.example.cognilink.ui.screens.flashcard.CreateFlashcardWithIAScreen
import com.example.cognilink.ui.screens.flashcard.FlashcardEditorScreen
import com.example.cognilink.ui.screens.home.HomeScreen
import com.example.cognilink.ui.screens.home.ProfileScreen
import com.example.cognilink.ui.viewmodels.TermsViewModel

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Terms : Screen("terms")
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: Long) = "profile/$userId"
    }

    object Home : Screen("home/{userId}") {
        fun createRoute(userId: Long) = "home/$userId"
    }

    object Deck : Screen("deck/{deckId}/{userId}") {
        fun createRoute(deckId: Long, userId: Long) = "deck/$deckId/$userId"
    }

    object CreateDeck : Screen("createDeck/{userId}") {
        fun createRoute(userId: Long) = "createDeck/$userId"
    }

    object EditDeck : Screen("editDeck/{deckId}/{userId}") {
        fun createRoute(deckId: Long, userId: Long) = "editDeck/$deckId/$userId"
    }

    object CreateFlashcardWithIA: Screen("createFlashcardWithIA/{deckId}"){
        fun createRoute(deckId: Long) = "createFlashcardWithIA/$deckId"
    }

    object CreateFlashcardManually: Screen("createFlashcardManually/{deckId}"){
        fun createRoute(deckId: Long) = "createFlashcardManually/$deckId"
    }
    object EditFlashcard: Screen("editFlashcard/{deckId}/{flashcardId}"){
        fun createRoute(deckId: Long,flashcardId: Long) = "editFlashcard/$deckId/$flashcardId"
    }

}

@Composable
fun CogniLinkNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Auth.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToTerms = { navController.navigate(Screen.Terms.route) },
                onNavigateToHome = { userId ->
                    navController.navigate(Screen.Home.createRoute(userId))
                }
            )
        }

        composable(Screen.Terms.route) {
            val context = LocalContext.current
            val termsViewModel: TermsViewModel = viewModel(
                factory = TermsViewModel.provideFactory(context)
            )
            TermsScreen(
                onNavigateBack = { navController.popBackStack() },
                termsViewModel = termsViewModel
            )
        }

        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            ProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.Deck.route,
            arguments = listOf(
                androidx.navigation.navArgument("deckId") {
                    type = androidx.navigation.NavType.LongType
                },
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            DeckScreen(
                deckId = deckId, userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = {
                    navController.navigate(
                        Screen.EditDeck.createRoute(
                            deckId,
                            userId
                        )
                    )
                },
                onNavigateToStudy = {
                    //TODO
                    navController.popBackStack()
                },
                onNavigateToCreateFlashcard = {
                    navController.navigate(Screen.CreateFlashcardManually.createRoute(deckId))
                },
                onNavigateToCreateWithIA = {
                    navController.navigate(Screen.CreateFlashcardWithIA.createRoute(deckId))
                },
                onNavigateToFlashcard = { flashcardId ->
                    navController.navigate(Screen.EditFlashcard.createRoute(deckId, flashcardId))
                }
            )
        }

        composable(
            route = Screen.CreateDeck.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            DeckEditorScreen(
                userId = userId, deckId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditDeck.route,
            arguments = listOf(
                androidx.navigation.navArgument("deckId") {
                    type = androidx.navigation.NavType.LongType
                },
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            DeckEditorScreen(
                userId = userId,
                deckId = deckId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.CreateFlashcardWithIA.route,
            arguments = listOf(
                androidx.navigation.navArgument("deckId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
            CreateFlashcardWithIAScreen(
                deckId = deckId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.CreateFlashcardManually.route,
            arguments = listOf(
                androidx.navigation.navArgument("deckId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
            FlashcardEditorScreen(
                deckId = deckId,
                flashcardId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.EditFlashcard.route,
            arguments = listOf(
                androidx.navigation.navArgument("deckId") {
                    type = androidx.navigation.NavType.LongType
                },
                androidx.navigation.navArgument("flashcardId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deckId = backStackEntry.arguments?.getLong("deckId") ?: -1L
            val flashcardId = backStackEntry.arguments?.getLong("flashcardId") ?: -1L
            FlashcardEditorScreen(
                deckId = deckId,
                flashcardId = flashcardId,
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable(
            route = Screen.Home.route,
            arguments = listOf(
                androidx.navigation.navArgument("userId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            HomeScreen(
                userId = userId,
                onNavigateToCreateDeck = {
                    navController.navigate(Screen.CreateDeck.createRoute(userId))
                },
                onNavigateToDeck = { deckId ->
                    navController.navigate(Screen.Deck.createRoute(deckId, userId))
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.createRoute(userId))
                }
            )
        }
    }
}
