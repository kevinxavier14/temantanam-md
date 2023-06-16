package com.capstone.temantanam

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.capstone.temantanam.ui.collection.PlantCollectionIdViewModel
import com.capstone.temantanam.ui.navigation.NavigationItem
import com.capstone.temantanam.ui.navigation.Screen
import com.capstone.temantanam.ui.screen.camera.CameraScreen
import com.capstone.temantanam.ui.screen.description.PlantDescriptionScreen
import com.capstone.temantanam.ui.screen.history.HistoryScreen
import com.capstone.temantanam.ui.screen.home.HomeScreen
import com.capstone.temantanam.ui.screen.login.LoginScreen
import com.capstone.temantanam.ui.screen.profile.ProfileScreen
import com.capstone.temantanam.ui.screen.profile.UpdateProfileScreen
import com.capstone.temantanam.ui.screen.register.RegisterScreen
import com.capstone.temantanam.ui.session.UserSessionViewModel
import com.capstone.temantanam.ui.theme.TemanTanamTheme

@Composable
fun TemanTanamApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    userSessionViewModel: UserSessionViewModel = viewModel(),
    plantCollectionIdViewModel: PlantCollectionIdViewModel = viewModel()
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentDestination != Screen.Login.route
                && currentDestination != Screen.Register.route
                && currentDestination != Screen.PlantDescription.route
            ) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel,
                    plantCollectionIdViewModel = plantCollectionIdViewModel
                )
            }
            composable(Screen.PlantDescription.route) { backStackEntry ->
                val plantName = remember {
                    Screen.PlantDescription.parsePlantName(backStackEntry.arguments?.getString("plantName") ?: "")
                }
                PlantDescriptionScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel,
                    plantName = plantName,
                    plantCollectionIdViewModel = plantCollectionIdViewModel
                )
            }
            composable(Screen.Camera.route) {
                CameraScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel
                ) { result ->
                    navController.navigate(Screen.PlantDescription.createRoute(result)) {
                        popUpTo(Screen.Camera.route) {
                            inclusive = true
                        }
                    }
                }
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel
                )
            }
            composable(Screen.UpdateProfile.route) {
                UpdateProfileScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    navController = navController,
                    userSessionViewModel = userSessionViewModel
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val navigationItems = listOf(
        NavigationItem(
            title = "Home",
            icon = Icons.Default.Home,
            screen = Screen.Home
        ),
        NavigationItem(
            title = "Scan",
            icon = Icons.Default.Flip,
            screen = Screen.Camera
        ),
        NavigationItem(
          title = "History",
          icon = Icons.Default.History,
          screen = Screen.History
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Default.AccountCircle,
            screen = Screen.Profile
        ),
    )

    BottomNavigation(
        modifier = modifier.clip(CurvedShape(15.dp)),
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Row {
            navigationItems.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(item.title, fontSize = 12.sp) },
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CurvedShape(curveRadius: Dp): Shape {
    return RoundedCornerShape(topStart = curveRadius, topEnd = curveRadius)
}

@Preview(showBackground = true)
@Composable
fun TemanTanamAppPreview() {
    TemanTanamTheme {
        TemanTanamApp()
    }
}