package com.capstone.temantanam.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PlantDescription : Screen("plantDescription/{plantName}") {
        fun createRoute(plantName: String?): String {
            return "plantDescription/$plantName"
        }

        fun parsePlantName(route: String): String {
            return route.substringAfterLast("/")
        }
    }
    object Camera : Screen("camera")
    object Profile : Screen("profile")
    object UpdateProfile : Screen("updateProfile")
    object History : Screen("history")
    object Login : Screen("login")
    object Register : Screen("register")
}
