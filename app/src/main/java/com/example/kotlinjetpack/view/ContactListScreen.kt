package com.example.kotlinjetpack.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.example.kotlinjetpack.R
import com.example.kotlinjetpack.function.AddContactDialog
import com.example.kotlinjetpack.function.AppSettings
import com.example.kotlinjetpack.function.ButtonState
import com.example.kotlinjetpack.model.ContactItem
import com.example.kotlinjetpack.ui.theme.*
import com.example.kotlinjetpack.view_model.DeleteContactViewModel
import kotlinx.coroutines.delay
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.util.*

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ContactListScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    DisposableEffect(lifecycleOwner) {
        val observer = object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                if (AppSettings.getContactViewModel.contactList.isEmpty()) {
                    AppSettings.getContactViewModel.getContact()
                }
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                AppSettings.getContactViewModel.clearContact()
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
                    AddFriendButton()
                }
            }

            val textSize = (20 + (30 - 15) * toolbarState.toolbarState.progress).sp

            var title by remember { mutableStateOf("") }
            val titleFinal = "Your friends"
            Text(
                text = title,
                modifier = Modifier
                    .road(Alignment.TopCenter, Alignment.BottomEnd)
                    .padding(20.dp),
                color = Color.White,
                fontSize = textSize,
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
        GridView()
    }
}

@Composable
fun ContactItem(
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
                    fontSize = 28.sp,
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
                    ),
                )
                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        textAlign = TextAlign.Start,
                        color = greyTextColor,
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
fun GridView(
) {
    val density = LocalDensity.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(
            start = 8.dp,
            top = 8.dp,
            end = 8.dp,
            bottom = 8.dp,
        ),
        content = {
            items(AppSettings.getContactViewModel.contactList.size) { i ->
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
                    ContactItem(
                        name = AppSettings.getContactViewModel.contactList[i].name!!,
                        phoneNumber = AppSettings.getContactViewModel.contactList[i].phone!!,
                        contactList = AppSettings.getContactViewModel.contactList,
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
fun AddFriendButton(
    density: Density = LocalDensity.current
) {
    val openContactDialog = rememberSaveable { mutableStateOf(false) }
    if (openContactDialog.value) {
        AddContactDialog(
            openDialog = openContactDialog,
            contactList = AppSettings.getContactViewModel.contactList
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
                fontSize = 20.sp,
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