package com.example.kotlinjetpack.view.bottom_navbar

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.function.AppSettings.isDarkMode
import com.example.kotlinjetpack.function.shadow
import com.example.kotlinjetpack.ui.theme.darkModeColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import com.example.kotlinjetpack.view.ChatListScreen
import com.example.kotlinjetpack.view.ContactListScreen
import com.example.kotlinjetpack.view.SettingsScreen
import com.example.kotlinjetpack.view_model.GetChatListViewModel

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
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = {
            Navigation(navController = navController)
        },
        backgroundColor = if (AppSettings.isDarkMode) darkModeColor else Color.Transparent
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItems.ChatList,
        NavigationItems.ContactList,
        NavigationItems.Settings,
    )

    var animationState by remember { mutableStateOf(false) }
    val duration = 2000
    val delay = 500
    val animationValue: Float by animateFloatAsState(
        targetValue = if (animationState) 1f else 0f,
        animationSpec = tween(
            durationMillis = duration,
            delayMillis = delay,
            easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
        )
    )

    MyBottomNavigation(
        modifier = Modifier
            .scale(animationValue)
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp,
            )
            .shadow(
                color = Color.Black.copy(alpha = 0.5f),
                borderRadius = 20.dp,
                offsetY = 20.dp,
                spread = 5.dp,
                blurRadius = 10.dp
            )
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (isDarkMode) {
                        listOf(
                            primaryColor.copy(1f, 0.4f, 0f, 0.2f),
                            softPink.copy(1f, 0.2f, 0.1f, 0.2f)
                        )
                    } else {
                        listOf(
                            primaryColor,
                            softPink
                        )
                    },
                ),
                shape = RoundedCornerShape(
                    corner = CornerSize(20.dp)
                )
            )
            .clip(
                shape = RoundedCornerShape(
                    corner = CornerSize(20.dp)
                )
            )

    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            MyCustomBottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = {
                    val labelDuration = 1500
                    val density = LocalDensity.current

                    AnimatedVisibility(
                        visible = (currentRoute == item.route),
                        enter = slideInVertically(
                            animationSpec = tween(durationMillis = labelDuration),
                            initialOffsetY = {
                                with(density) { 500.dp.roundToPx() }
                            }
                        ) + expandVertically(
                            animationSpec = tween(durationMillis = labelDuration),
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            animationSpec = tween(durationMillis = labelDuration),
                            initialAlpha = 0f
                        ),
                        exit = slideOutVertically(
                            animationSpec = tween(durationMillis = labelDuration),
                            targetOffsetY = {
                                with(density) { 500.dp.roundToPx() }
                            }
                        ) + shrinkVertically(
                            animationSpec = tween(durationMillis = labelDuration),
                        ) + fadeOut(
                            animationSpec = tween(durationMillis = labelDuration),
                        ),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = item.title,
                            color = Color(0xFFFFFFFF),
                        )
                    }

                },
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
    }.let { animation ->
        LaunchedEffect(animation) {
            animationState = true
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
    object ChatList : NavigationItems(
        "chat-list",
        R.drawable.ic_baseline_chat_24,
        "Chat"
    )

    object ContactList :
        NavigationItems(
            "contact-list",
            R.drawable.ic_baseline_perm_contact_calendar_24,
            "Contact"
        )

    object Settings : NavigationItems(
        "settings",
        R.drawable.ic_baseline_settings_24,
        "Settings"
    )
}
