package com.example.kotlinjetpack.view.bottom_navbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.const.NAME
import com.example.kotlinjetpack.const.PHONE
import com.example.kotlinjetpack.function.AddContactDialog
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.function.AppSettings.isDarkMode
import com.example.kotlinjetpack.function.ButtonState
import com.example.kotlinjetpack.function.over_scroll.overScrollVertical
import com.example.kotlinjetpack.function.over_scroll.parabolaScrollEasing
import com.example.kotlinjetpack.function.shadow
import com.example.kotlinjetpack.model.ContactItem
import com.example.kotlinjetpack.ui.theme.Teal200
import com.example.kotlinjetpack.ui.theme.darkModeColor
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.ui.theme.softPink
import com.example.kotlinjetpack.view.ChatActivity
import com.example.kotlinjetpack.view_model.DeleteContactViewModel
import com.example.kotlinjetpack.view_model.GetChatListViewModel
import com.example.kotlinjetpack.view_model.GetContactViewModel
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale
import java.util.Random

@Suppress("DEPRECATION")
class BottomNavigationBarActivity : ComponentActivity() {
    private val getChatListViewModel: GetChatListViewModel = GetChatListViewModel()
    private val getContactViewModel: GetContactViewModel = GetContactViewModel()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
            backgroundColor = if (isDarkMode) darkModeColor else Color.Transparent
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
            label = "bottomNavBarAnimation",
            targetValue = if (animationState) 1f else 0f,
            animationSpec = tween(
                durationMillis = duration,
                delayMillis = delay,
                easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
            ),
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
                NavigationComposableScreen().ChatListScreen().RenderChatListScreen()
            }
            composable(NavigationItems.ContactList.route) {
                NavigationComposableScreen().ContactListScreen().RenderContactListScreen()
            }
            composable(NavigationItems.Settings.route) {
                NavigationComposableScreen().SettingsScreen().RenderSettingsScreen()
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

    inner class NavigationComposableScreen {

        inner class ChatListScreen {

            @SuppressLint("SimpleDateFormat", "NotConstructor")
            @RequiresApi(Build.VERSION_CODES.N)
            @Composable
            fun RenderChatListScreen(
                lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
                context: Context = LocalContext.current,
            ) {
                AndroidThreeTen.init(context)

                DisposableEffect(lifecycleOwner) {
                    val observer = object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                        fun onResume() {
                            if (getChatListViewModel.chatItemList.isEmpty()) {
                                getChatListViewModel.getChatList()
                            }
                        }

                        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                        fun onPause() {
                            getChatListViewModel.clearChatList()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }


                val topR by animateIntAsState(
                    targetValue = if (isDarkMode) 31 else 123,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topG by animateIntAsState(
                    targetValue = if (isDarkMode) 31 else 123,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topB by animateIntAsState(
                    targetValue = if (isDarkMode) 31 else 255,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topA by animateIntAsState(
                    targetValue = if (isDarkMode) 255 else (255 * 0.3).toInt(),
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )

                val botR by animateIntAsState(
                    targetValue = if (isDarkMode) 31 else 255,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botG by animateIntAsState(
                    targetValue = if (isDarkMode) 31 else 87,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botB by animateIntAsState(
                    targetValue = if (isDarkMode) 31 else 199,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botA by animateIntAsState(
                    targetValue = if (isDarkMode) 255 else (255 * 0.7).toInt(),
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val toolbarState = rememberCollapsingToolbarScaffoldState()
                CollapsingToolbarScaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(topR, topG, topB, topA),
                                    Color(botR, botG, botB, botA),
                                ),
                                start = Offset.Zero,
                                end = Offset.Infinite
                            ),
                        ),
                    state = toolbarState,
                    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                    toolbar = {
                        val textSize = (5 + (6 - 3) * toolbarState.toolbarState.progress)

                        Row(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (isDarkMode) {
                                            listOf(
                                                softPink.copy(1f, 0.2f, 0.1f, 0.2f),
                                                primaryColor.copy(1f, 0.4f, 0f, 0.2f),
                                            )
                                        } else {
                                            listOf(
                                                softPink,
                                                primaryColor
                                            )
                                        },
                                    ),
                                    shape = RoundedCornerShape(
                                        bottomStart = 20.dp,
                                        bottomEnd = 20.dp
                                    )
                                )
                                .fillMaxWidth()
                                .height(175.dp)
                                .pin(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            if (toolbarState.toolbarState.progress == 1f) {
                                RenderDateTimeText()
                            }
                        }

                        var title by remember { mutableStateOf("") }
                        val titleFinal = "Chat with friends"
                        Text(
                            text = title,
                            modifier = Modifier
                                .road(Alignment.TopCenter, Alignment.BottomStart)
                                .padding(20.dp),
                            color = Color.White,
                            fontSize = textSize.em,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ).let { animation ->
                            LaunchedEffect(animation) {
                                delay(500)
                                titleFinal.forEachIndexed { index, _ ->
                                    delay(20)
                                    title = titleFinal.substring(0, index + 1)
                                }
                            }
                        }
                    }
                ) {

                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .overScrollVertical(
                                true,
                                { x1, x2 -> parabolaScrollEasing(x1, x2, 5f) },
                                springStiff = 300f,
                                springDamp = 1f,
                            )
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        Text(
                            text = "Recent contacts",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .padding(all = 20.dp),
                            style = TextStyle(
                                textAlign = TextAlign.Start,
                                color = defaultTextColor(),
                                fontSize = 7.em
                            ),
                        )


                        for (i in 0 until getChatListViewModel.chatItemList.size) {
                            val density = LocalDensity.current
                            var chatListVisible by remember { mutableStateOf(false) }
                            val chatListDuration = 500
                            val chatListDelay = 0 + 100 * i
                            AnimatedVisibility(
                                visible = chatListVisible,
                                enter = slideInVertically(
                                    animationSpec = tween(
                                        durationMillis = chatListDuration,
                                        delayMillis = chatListDelay
                                    ),
                                ) {
                                    with(density) {
                                        -50.dp.roundToPx()
                                    }
                                } + fadeIn(
                                    animationSpec = tween(
                                        durationMillis = chatListDuration,
                                        delayMillis = chatListDelay
                                    ),
                                    initialAlpha = 0f
                                )
                            ) {
                                RenderChatItem(
                                    nameList = listOf(
                                        getChatListViewModel.chatItemList[i].users!![0].name!!,
                                        getChatListViewModel.chatItemList[i].users!![1].name!!
                                    ),
                                    phoneList = listOf(
                                        getChatListViewModel.chatItemList[i].users!![0].phone!!,
                                        getChatListViewModel.chatItemList[i].users!![1].phone!!
                                    ),
                                    lastMessageContent =
                                    if (getChatListViewModel.chatItemList[i].chat!!.isNotEmpty())
                                        getChatListViewModel.chatItemList[i].chat!!.last().content!!
                                    else "",
                                    lastMessageTime =
                                    if (getChatListViewModel.chatItemList[i].chat!!.isNotEmpty())
                                        SimpleDateFormat("dd/MM  hh:mm").format(
                                            getChatListViewModel.chatItemList[i].chat!!.last().time!!
                                        )
                                    else "",
                                    chatId = getChatListViewModel.chatItemList[i]._id!!
                                )
                            }.let { animation ->
                                LaunchedEffect(animation) {
                                    chatListVisible = true
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }

                LaunchedEffect(toolbarState.toolbarState.progress) {
                    Log.d("CollapsingToolbar", "Progress: ${toolbarState.toolbarState.progress}")
                }
            }

            @Composable
            fun RenderChatItem(
                nameList: List<String>,
                phoneList: List<String>,
                lastMessageContent: String,
                lastMessageTime: String,
                chatId: String,
            ) {
                val context = LocalContext.current

                val friendName: String
                val friendPhone: String
                if (phoneList[0] == PHONE) {
                    friendName = nameList[1]
                    friendPhone = phoneList[1]
                } else {
                    friendName = nameList[0]
                    friendPhone = phoneList[0]
                }

                val color = Color(
                    Random().nextInt(256),
                    Random().nextInt(256),
                    Random().nextInt(256),
                ).copy(
                    alpha = 1f
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            val intent = Intent(context, ChatActivity::class.java)
                            intent.putExtra("chatId", chatId)
                            intent.putExtra("phone", friendPhone)
                            context.startActivity(intent)
                        }
                ) {
                    Divider(
                        color = greyTextColor,
                        thickness = 1.dp,
                    )

                    Row(
                        modifier = Modifier.padding(
                            all = 16.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .background(
                                    color = color,
                                    shape = CircleShape
                                )
                                .size(60.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = friendName[0].toString().uppercase(),
                                style = TextStyle(
                                    fontSize = 7.em,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.width(15.dp))

                        Column {
                            Row {
                                Text(
                                    text = friendName,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        textAlign = TextAlign.Start,
                                        color = defaultTextColor(),
                                        fontSize = 4.em
                                    ),
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Text(
                                    text = lastMessageTime,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textAlign = TextAlign.Start,
                                        color = greyTextColor,
                                        fontSize = 4.em
                                    ),
                                )
                            }
                            if (lastMessageContent.isEmpty()) {
                                Text(
                                    text = "You and this person have no conversation yet",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textAlign = TextAlign.Start,
                                        color = greyTextColor,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 4.em
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text(
                                    text = lastMessageContent,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        textAlign = TextAlign.Start,
                                        color = greyTextColor,
                                        fontSize = 4.em
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            @RequiresApi(Build.VERSION_CODES.N)
            @OptIn(ObsoleteCoroutinesApi::class)
            @Composable
            fun RenderDateTimeText() {
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)

                var currentTime by remember { mutableStateOf(LocalTime.now()) }
                LaunchedEffect(true) {
                    val ticker = ticker(delayMillis = 1000)
                    for (event in ticker) {
                        currentTime = LocalTime.now()
                    }
                }

                val density = LocalDensity.current

                Column(
                    modifier = Modifier.padding(
                        all = 20.dp
                    ),
                    horizontalAlignment = Alignment.End
                ) {
                    var dateVisible by remember { mutableStateOf(false) }
                    val dateDuration = 1000
                    val dateDelay = 600

                    AnimatedVisibility(
                        visible = dateVisible,
                        enter = slideInVertically(
                            animationSpec = tween(durationMillis = dateDuration, delayMillis = dateDelay),
                        ) {
                            with(density) { -150.dp.roundToPx() }
                        } + fadeIn(
                            animationSpec = tween(durationMillis = dateDuration, delayMillis = dateDelay),
                            initialAlpha = 0f
                        ),
                    ) {
                        Text(
                            text = formattedDate,
                            color = Color.White,
                            fontSize = 4.em,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            ),
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            dateVisible = true
                        }
                    }

                    var timeVisible by remember { mutableStateOf(false) }
                    val timeDuration = 1000
                    val timeDelay = 900

                    AnimatedVisibility(
                        visible = timeVisible,
                        enter = slideInVertically(
                            animationSpec = tween(durationMillis = timeDuration, delayMillis = timeDelay),
                        ) {
                            with(density) { 150.dp.roundToPx() }
                        } + fadeIn(
                            animationSpec = tween(durationMillis = timeDuration, delayMillis = timeDelay),
                            initialAlpha = 0f
                        )
                    ) {
                        Text(
                            text = currentTime.format(
                                DateTimeFormatter.ofPattern(
                                    "HH:mm:ss"
                                )
                            ),
                            color = Color.White,
                            fontSize = 4.em,
                            style = TextStyle(
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            timeVisible = true
                        }
                    }
                }
            }
        }

        inner class ContactListScreen {
            @SuppressLint("MutableCollectionMutableState", "NotConstructor")
            @Composable
            fun RenderContactListScreen(
                lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
            ) {
                DisposableEffect(lifecycleOwner) {
                    val observer = object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                        fun onResume() {
                            if (getContactViewModel.contactList.isEmpty()) {
                                getContactViewModel.getContact()
                            }
                        }

                        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                        fun onPause() {
                            getContactViewModel.clearContact()
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                val topR by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 123,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topG by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 123,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topB by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 255,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topA by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 255 else (255 * 0.3).toInt(),
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )

                val botR by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 255,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botG by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 87,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botB by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 199,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botA by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 255 else (255 * 0.7).toInt(),
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val toolbarState = rememberCollapsingToolbarScaffoldState()
                CollapsingToolbarScaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(topR, topG, topB, topA),
                                    Color(botR, botG, botB, botA),
                                ),
                                start = Offset.Zero,
                                end = Offset.Infinite
                            ),
                        ),
                    state = toolbarState,
                    scrollStrategy = ScrollStrategy.EnterAlways,
                    toolbar = {
                        Box(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (isDarkMode) {
                                            listOf(
                                                softPink.copy(1f, 0.2f, 0.1f, 0.2f),
                                                primaryColor.copy(1f, 0.4f, 0f, 0.2f),
                                            )
                                        } else {
                                            listOf(
                                                softPink,
                                                primaryColor
                                            )
                                        },
                                    ),
                                    shape = RoundedCornerShape(
                                        bottomStart = 150.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .fillMaxWidth()
                                .height(180.dp)
                                .pin()
                                .clip(
                                    shape = RoundedCornerShape(
                                        bottomStart = 150.dp,
                                        bottomEnd = 0.dp
                                    )
                                ),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            if (toolbarState.toolbarState.progress == 1f) {
                                RenderAddFriendButton()
                            }
                        }

                        val textSize = (5 + (6 - 3) * toolbarState.toolbarState.progress)

                        var title by remember { mutableStateOf("") }
                        val titleFinal = "Your friends"
                        Text(
                            text = title,
                            modifier = Modifier
                                .road(Alignment.TopCenter, Alignment.BottomEnd)
                                .padding(20.dp),
                            color = Color.White,
                            fontSize = textSize.em,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            )
                        ).let { animation ->
                            LaunchedEffect(animation) {
                                delay(500)
                                titleFinal.forEachIndexed { index, _ ->
                                    delay(20)
                                    title = titleFinal.substring(0, index + 1)
                                }
                            }
                        }
                    }
                ) {
                    RenderGridView()
                }
            }

            @Composable
            fun RenderContactItem(
                name: String,
                phoneNumber: String,
                contactList: SnapshotStateList<ContactItem>,
            ) {
                val lifecycleOwner = LocalLifecycleOwner.current
                val context = LocalContext.current

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, primaryColor, RoundedCornerShape(8.dp))
                        .background(color = primaryColor.copy(alpha = 0.1f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color(
                                    Random().nextInt(256),
                                    Random().nextInt(256),
                                    Random().nextInt(256),
                                ).copy(
                                    alpha = 1f
                                ),
                                shape = CircleShape
                            )
                            .size(60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = name[0].toString().uppercase(),
                            style = TextStyle(
                                fontSize = 7.em,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    Row(
                        modifier = Modifier.padding(
                            top = 10.dp
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Text(
                                text = name,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    textAlign = TextAlign.Start,
                                    color = defaultTextColor(),
                                    fontSize = 4.em
                                ),
                            )
                            Text(
                                text = phoneNumber,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textAlign = TextAlign.Start,
                                    color = greyTextColor,
                                    fontSize = 4.em
                                ),
                            )
                        }

                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                val deleteContactViewModel = DeleteContactViewModel()
                                deleteContactViewModel.deleteContact(phoneNumber)
                                deleteContactViewModel.resultLiveData.observe(lifecycleOwner) { result ->
                                    when (result) {
                                        0 -> {
                                            println("Loading")
                                        }

                                        1 -> {
                                            println("Success, contact: ${deleteContactViewModel.deleteContactData!!.contact!!.contactList}")
                                            contactList.clear()
                                            contactList.addAll(deleteContactViewModel.deleteContactData!!.contact!!.contactList!!)
                                            Toast.makeText(
                                                context,
                                                "Delete contact successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                        2 -> {
                                            println("Error: ${deleteContactViewModel.errorMessage}")
                                            Toast.makeText(
                                                context,
                                                deleteContactViewModel.errorMessage,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_person_remove_24),
                                modifier = Modifier.size(35.dp),
                                contentDescription = null,
                                tint = Color.Red
                            )
                        }
                    }
                }
            }

            @Composable
            fun RenderGridView(
            ) {
                val density = LocalDensity.current

                LazyVerticalGrid(
                    modifier = Modifier
                        .overScrollVertical(
                            true,
                            { x1, x2 -> parabolaScrollEasing(x1, x2, 5f) },
                            springStiff = 300f,
                            springDamp = 1f,
                        )
                        .fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 8.dp,
                        top = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp,
                    ),
                    content = {
                        items(getContactViewModel.contactList.size) { i ->
                            var contactListVisible by remember { mutableStateOf(false) }
                            val contactListDuration = 500
                            val contactListDelay = 0 + 100 * i
                            AnimatedVisibility(
                                visible = contactListVisible,
                                enter = slideInVertically(
                                    animationSpec = tween(
                                        durationMillis = contactListDuration,
                                        delayMillis = contactListDelay
                                    ),
                                ) {
                                    with(density) {
                                        -50.dp.roundToPx()
                                    }
                                } + fadeIn(
                                    animationSpec = tween(
                                        durationMillis = contactListDuration,
                                        delayMillis = contactListDelay
                                    ),
                                    initialAlpha = 0f
                                )
                            ) {
                                RenderContactItem(
                                    name = getContactViewModel.contactList[i].name!!,
                                    phoneNumber = getContactViewModel.contactList[i].phone!!,
                                    contactList = getContactViewModel.contactList,
                                )
                            }.let { animation ->
                                LaunchedEffect(animation) {
                                    contactListVisible = true
                                }
                            }
                        }
                    }
                )
            }

            @Composable
            fun RenderAddFriendButton(
                density: Density = LocalDensity.current
            ) {
                val openContactDialog = rememberSaveable { mutableStateOf(false) }
                if (openContactDialog.value) {
                    AddContactDialog(
                        openDialog = openContactDialog,
                        contactList = getContactViewModel.contactList
                    )
                }

                var buttonState by remember { mutableStateOf(ButtonState.Idle) }
                val scale by animateFloatAsState(
                    if (buttonState == ButtonState.Pressed) 0.8f else 1f,
                    tween(durationMillis = 150, easing = FastOutSlowInEasing)
                )

                var addFriendTextVisible by remember { mutableStateOf(false) }
                val addFriendTextDuration = 1000
                val addFriendTextDelay = 500

                var addFriendIconVisible by remember { mutableStateOf(false) }
                val addFriendIconDuration = 1000
                val addFriendIconDelay = 1000

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .scale(scale)
                        .padding(16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            openContactDialog.value = true
                        }
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(10.dp)
                        .pointerInput(buttonState) {
                            awaitPointerEventScope {
                                buttonState = if (buttonState == ButtonState.Pressed) {
                                    waitForUpOrCancellation()
                                    ButtonState.Idle
                                } else {
                                    awaitFirstDown(false)
                                    ButtonState.Pressed
                                }
                            }
                        },
                    horizontalArrangement = Arrangement.End,
                ) {
                    AnimatedVisibility(
                        visible = addFriendTextVisible,
                        enter = slideInHorizontally(
                            animationSpec = tween(
                                durationMillis = addFriendTextDuration, delayMillis =
                                addFriendTextDelay
                            ),
                        ) {
                            with(density) { 150.dp.roundToPx() }
                        } + fadeIn(
                            animationSpec = tween(
                                durationMillis = addFriendTextDuration, delayMillis =
                                addFriendTextDelay
                            ),
                            initialAlpha = 0f
                        ),
                    ) {
                        Text(
                            text = "Add friends",
                            color = Color.White,
                            fontSize = 5.em,
                            style = TextStyle(
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            addFriendTextVisible = true
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    AnimatedVisibility(
                        visible = addFriendIconVisible,
                        enter = slideInHorizontally(
                            animationSpec = tween(
                                durationMillis = addFriendIconDuration,
                                delayMillis = addFriendIconDelay
                            ),
                        ) {
                            with(density) { -150.dp.roundToPx() }
                        } + fadeIn(
                            animationSpec = tween(
                                durationMillis = addFriendIconDuration,
                                delayMillis = addFriendIconDelay
                            ),
                            initialAlpha = 0f
                        ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_baseline_person_add_alt_1_24),
                            modifier = Modifier.size(35.dp),
                            contentDescription = null,
                            tint = Teal200
                        )
                    }.let { animation ->
                        LaunchedEffect(animation) {
                            addFriendIconVisible = true
                        }
                    }
                }
            }
        }

        inner class SettingsScreen {
            @SuppressLint("NotConstructor")
            @Composable
            fun RenderSettingsScreen() {
                val topR by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 123,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topG by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 123,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topB by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 255,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val topA by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 255 else (255 * 0.3).toInt(),
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )

                val botR by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 255,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botG by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 87,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botB by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 31 else 199,
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val botA by animateIntAsState(
                    targetValue = if (AppSettings.isDarkMode) 255 else (255 * 0.7).toInt(),
                    animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                )
                val toolbarState = rememberCollapsingToolbarScaffoldState()
                CollapsingToolbarScaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(topR, topG, topB, topA),
                                    Color(botR, botG, botB, botA),
                                ),
                                start = Offset.Zero,
                                end = Offset.Infinite
                            ),
                        ),
                    state = toolbarState,
                    scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                    toolbar = {
                        Column(
                            modifier = Modifier
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = if (AppSettings.isDarkMode) {
                                            listOf(
                                                softPink.copy(1f, 0.2f, 0.1f, 0.2f),
                                                primaryColor.copy(1f, 0.4f, 0f, 0.2f),
                                            )
                                        } else {
                                            listOf(
                                                softPink,
                                                primaryColor
                                            )
                                        },
                                    ),
                                    shape = RoundedCornerShape(
                                        bottomEnd = 150.dp
                                    )
                                )
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(
                                    shape = RoundedCornerShape(
                                        bottomEnd = 150.dp
                                    )
                                )
                        ) {
                            var title by remember { mutableStateOf("") }
                            val titleFinal = "Settings"

                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                RenderScreenModeButton()
                            }


                            Text(
                                text = title,
                                modifier = Modifier.padding(20.dp),
                                color = Color.White,
                                fontSize = 8.em,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            ).let { animation ->
                                LaunchedEffect(animation) {
                                    delay(500)
                                    titleFinal.forEachIndexed { index, _ ->
                                        delay(20)
                                        title = titleFinal.substring(0, index + 1)
                                    }
                                }
                            }
                        }
                    },
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        var panelState by remember { mutableStateOf(false) }
                        val duration = 1000
                        val delay = 0
                        val scale: Float by animateFloatAsState(
                            targetValue = if (panelState) 1f else 0f,
                            animationSpec = tween(durationMillis = duration, delayMillis = delay),
                        )

                        Row(
                            modifier = Modifier
                                .scale(scale)
                                .padding(20.dp)
                                .fillMaxWidth(1f)
                                .background(
                                    if (AppSettings.isDarkMode) Color(0xBF474747) else Color(0x80FFFFFF),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(20.dp),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(
                                        color = Color(
                                            Random().nextInt(256),
                                            Random().nextInt(256),
                                            Random().nextInt(256),
                                        ).copy(
                                            alpha = 1f
                                        ),
                                        shape = CircleShape
                                    )
                                    .size(60.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = NAME[0].toString().uppercase(),
                                    style = TextStyle(
                                        fontSize = 7.em,
                                        fontWeight = FontWeight.Bold,
                                        color = defaultTextColor()
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            Column() {
                                Text(
                                    text = NAME,
                                    style = TextStyle(
                                        fontSize = 6.em,
                                        fontWeight = FontWeight.Bold,
                                        color = defaultTextColor()
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Text(
                                    text = PHONE,
                                    style = TextStyle(
                                        fontSize = 5.em,
                                        fontStyle = FontStyle.Italic,
                                        color = greyTextColor
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }.let { animation ->
                            LaunchedEffect(animation) {
                                panelState = true
                            }
                        }


                        Column(
                            modifier = Modifier
                                .scale(scale)
                                .padding(20.dp)
                                .fillMaxWidth(1f)
                                .background(
                                    if (AppSettings.isDarkMode) Color(0xBF474747) else Color(0x80FFFFFF),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    )
                            ) {
                                Text(
                                    text = "Change password",
                                    style = TextStyle(
                                        fontSize = 5.em,
                                        color = defaultTextColor()
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    Icons.Filled.Password,
                                    contentDescription = "icon-password",
                                    tint = defaultTextColor()
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .background(
                                        greyTextColor
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    )
                            ) {
                                Text(
                                    text = "Change name",
                                    style = TextStyle(
                                        fontSize = 5.em,
                                        color = defaultTextColor()
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    Icons.Filled.AccountCircle,
                                    contentDescription = "icon-account",
                                    tint = defaultTextColor()
                                )
                            }

                            Divider(
                                modifier = Modifier
                                    .background(
                                        greyTextColor
                                    )
                            )

                            Row(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 20.dp,
                                        vertical = 10.dp
                                    )
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        Process.killProcess(Process.myPid())
                                    }
                            ) {
                                Text(
                                    text = "Close",
                                    style = TextStyle(
                                        fontSize = 5.em,
                                        color = defaultTextColor()
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = "icon-close",
                                    tint = defaultTextColor()
                                )
                            }
                        }
                    }
                }
            }

            @Composable
            fun RenderScreenModeButton() {
                var buttonState by remember { mutableStateOf(ButtonState.Idle) }
                val scale by animateFloatAsState(
                    if (buttonState == ButtonState.Pressed) 0.8f else 1f,
                    tween(durationMillis = 150, easing = FastOutSlowInEasing)
                )

                var visible by remember { mutableStateOf(false) }
                val duration = 1000
                val delay = 300
                val density = LocalDensity.current

                AnimatedVisibility(
                    visible = visible,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = duration, delayMillis = delay),
                    ) {
                        with(density) { -150.dp.roundToPx() }
                    } + fadeIn(
                        animationSpec = tween(durationMillis = duration, delayMillis = delay),
                        initialAlpha = 0f
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .scale(scale)
                            .padding(all = 20.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                AppSettings.isDarkMode = !AppSettings.isDarkMode
                            }
                            .padding(all = 10.dp)
                            .pointerInput(buttonState) {
                                awaitPointerEventScope {
                                    buttonState = if (buttonState == ButtonState.Pressed) {
                                        waitForUpOrCancellation()
                                        ButtonState.Idle
                                    } else {
                                        awaitFirstDown(false)
                                        ButtonState.Pressed
                                    }
                                }
                            },
                    ) {
                        val rotationAngle by animateFloatAsState(
                            targetValue = if (buttonState == ButtonState.Pressed) 720f else 0f,
                            animationSpec = tween(durationMillis = 720, easing = LinearOutSlowInEasing),
                        )

                        val scaleText by animateFloatAsState(
                            targetValue = if (buttonState == ButtonState.Pressed) 0f else 1f,
                            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                        )

                        Icon(
                            painterResource(
                                id = if (AppSettings.isDarkMode) R.drawable.ic_baseline_light_mode_24 else R.drawable.ic_baseline_dark_mode_24
                            ),
                            contentDescription = "mode",
                            tint = Color.White,
                            modifier = Modifier.graphicsLayer {
                                rotationZ = rotationAngle
                            }
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (AppSettings.isDarkMode) "Light" else "Dark",
                            color = Color.White,
                            fontSize = 5.em,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.scale(scaleText)
                        )
                    }
                }.let { animation ->
                    LaunchedEffect(animation) {
                        visible = true
                    }
                }


            }
        }
    }
}

