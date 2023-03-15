package com.example.kotlinjetpack.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.ui.theme.primaryColor

class BottomNavigationBarActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                Navigation(navController = navController)
            }
        },
        backgroundColor = primaryColor
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItems.ChatList,
        NavigationItems.ContactList,
        NavigationItems.Settings,
    )
    BottomNavigation(
        backgroundColor = primaryColor,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItems.ChatList.route) {
        composable(NavigationItems.ChatList.route) {
            ChatListScreen()
        }
        composable(NavigationItems.ContactList.route) {
            ContactListScreen()
        }
        composable(NavigationItems.Settings.route) {
            SettingsScreen()
        }
    }
}

sealed class NavigationItems(var route: String, var icon: Int, var title: String) {
    object ChatList : NavigationItems("chat-list", R.drawable.ic_baseline_chat_24, "Chat")
    object ContactList : NavigationItems("contact-list", R.drawable.ic_baseline_perm_contact_calendar_24, "Contact")
    object Settings : NavigationItems("settings", R.drawable.ic_baseline_settings_24, "Settings")
}
