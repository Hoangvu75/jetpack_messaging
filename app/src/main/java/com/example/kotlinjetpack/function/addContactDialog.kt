package com.example.kotlinjetpack.function

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.kotlinjetpack.model.ContactItem
import com.example.kotlinjetpack.ui.theme.primaryColor
import com.example.kotlinjetpack.view_model.AddContactViewModel

@Composable
fun AddContactDialog(
    openDialog: MutableState<Boolean>,
    contactList: SnapshotStateList<ContactItem>
) {
    Dialog(onDismissRequest = {}) {
        AddContactDialogUI(
            openDialog = openDialog,
            contactList = contactList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactDialogUI(
    modifier: Modifier = Modifier,
    openDialog: MutableState<Boolean>,
    contactList: SnapshotStateList<ContactItem>
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var phone by remember { mutableStateOf("") }

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
    ) {
        Column(
            modifier
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Add new contact",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleLarge,
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = {
                        Text(text = "Phone")
                    },
                    leadingIcon = {
                        Icon(Icons.Filled.Phone, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = primaryColor,
                        focusedLabelColor = primaryColor,
                        focusedLeadingIconColor = primaryColor,
                        focusedSupportingTextColor = primaryColor,
                        focusedTrailingIconColor = primaryColor,
                        cursorColor = primaryColor
                    ),
                    shape = RoundedCornerShape(20.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(primaryColor),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        val addContactViewModel = AddContactViewModel()
                        addContactViewModel.addContact(phone)
                        addContactViewModel.resultLiveData.observe(lifecycleOwner) { result ->
                            when (result) {
                                0 -> {
                                    println("Loading")
                                }
                                1 -> {
                                    println("Success, contact: ${addContactViewModel.addContactData!!.contact!!.contactList}")
                                    contactList.clear()
                                    contactList.addAll(addContactViewModel.addContactData!!.contact!!.contactList!!)
                                    Toast.makeText(context, "Add contact successfully", Toast.LENGTH_LONG).show()
                                }
                                2 -> {
                                    println("Error: ${addContactViewModel.errorMessage}")
                                    Toast.makeText(context, addContactViewModel.errorMessage, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        "Confirm",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(
                        "Cancel",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
