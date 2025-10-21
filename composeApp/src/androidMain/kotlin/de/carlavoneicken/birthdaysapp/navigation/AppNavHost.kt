package de.carlavoneicken.birthdaysapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.carlavoneicken.birthdaysapp.presentation.BirthdaysListScreen
import de.carlavoneicken.birthdaysapp.presentation.EditBirthdayScreen

@Composable
fun AppNavHost() {
    // creates and remembers a NavController which tracks the current screen (route) and the navigation stack (back stack)
    val navController = rememberNavController()

    // NavHost observes NavControllers state -> starts by displaying the startDestination
    NavHost(navController = navController, startDestination = "list") {

        // defining which screen belongs to which route string
        // when the route is "list" -> show BirthdaysListScreen
        composable("list") {
            BirthdaysListScreen(
                // when clicking add button, navigate to the edit route for a new item
                onAdd = { navController.navigate("edit") },
                // when clicking a birthday item, navigate to edit route for an existing item (providing the id)
                onEdit = { id -> navController.navigate("edit/$id") }
            )
        }

        composable(
            route ="edit"
        ) {
            EditBirthdayScreen(
                birthdayId = null,
                onDone = { navController.popBackStack() }
            )
        }

        //
        composable(
            route ="edit/{id}",
            arguments = listOf(
                // declares which arguments this destination supports, their types (and possibly defaults)
                navArgument("id") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            // read id from arguments
            val id = backStackEntry.arguments!!.getLong("id")
            EditBirthdayScreen(
                birthdayId = id,
                // popBackStack -> navigate back to previous screen
                onDone = { navController.popBackStack() }
            )
        }


        // Version that combines editing existing entry and creating a new entry
//        composable(
//            // Routes.EDIT_ROUTE = "edit?id={id}" -> base path "edit" plus an optional "id" query parameter
//            route = Routes.EDIT_ROUTE,
//            // declares which arguments this destination supports, their types and defaults
//            arguments = listOf(
//                // defines the "id" argument -> a Long with defaultValue -1
//                navArgument("id") {
//                    type = NavType.LongType
//                    defaultValue = -1L   // -1L -> creating new
//                }
//            )
//        ) { backStackEntry ->
//            // read id from the arguments bundle
//            val id = backStackEntry.arguments?.getLong("id") ?: -1L
//            EditBirthdayScreen(
//                birthdayId = id.takeIf { it >= 0L }, // pass real id if existing, else pass null
//                // popBackStack -> navigate back to previous screen
//                onDone = { navController.popBackStack() }
//            )
//        }
    }
}
