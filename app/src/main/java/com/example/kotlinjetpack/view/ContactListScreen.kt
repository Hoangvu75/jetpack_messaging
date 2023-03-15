package com.example.kotlinjetpack.view

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kotlinjetpack.function.AddContactDialog
import com.example.kotlinjetpack.model.ContactItem
import com.example.kotlinjetpack.ui.theme.defaultTextColor
import com.example.kotlinjetpack.ui.theme.greyTextColor
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.DeleteContactViewModel
import com.example.kotlinjetpack.view_model.GetContactViewModel

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ContactListScreen() {
    val lifecycleOwner = LocalLifecycleOwner.current

    val contactList = remember { mutableStateListOf<ContactItem>() }

    if (contactList.isEmpty()) {
        val getContactViewModel = GetContactViewModel()
        getContactViewModel.getContact()
        getContactViewModel.resultLiveData.observe(lifecycleOwner) { result ->
            when (result) {
                0 -> {
                    println("Loading")
                }
                1 -> {
                    println("Success, contact: ${getContactViewModel.getContactData!!.contact!!.contactList}")
                    contactList.clear()
                    contactList.addAll(getContactViewModel.getContactData!!.contact!!.contactList!!)
                }
                2 -> {
                    println("Error: ${getContactViewModel.errorMessage}")
                }
            }
        }
    }

    val openContactDialog = rememberSaveable { mutableStateOf(false) }
    if (openContactDialog.value) {
        AddContactDialog(openDialog = openContactDialog, contactList = contactList)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openContactDialog.value = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Contact",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(0.9f),
                    style = MaterialTheme.typography.displayMedium.copy(
                        textAlign = TextAlign.Start,
                        color = defaultTextColor,
                    ),
                )

                LazyColumn(
                    modifier = Modifier.weight(1f, false)
                ) {
                    items(contactList.size) { index ->
                        ContactItem(
                            name = contactList[index].name!!,
                            phoneNumber = contactList[index].phone!!,
                            contactList = contactList,
                        )
                    }
                }
            }
        }
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
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, primaryColor, RoundedCornerShape(8.dp))
            .background(color = primaryColor.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Start,
                        color = defaultTextColor,
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
                    Icons.Filled.Delete,
                    modifier = Modifier.size(50.dp),
                    contentDescription = null,
                    tint = Color.Red
                )
            }
        }
    }
}


