package com.madpickle.calls.editContact

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import com.madpickle.calls.R
import com.madpickle.calls.data.ContactDraft
import com.madpickle.calls.ui.theme.ButtonElevation
import com.madpickle.calls.ui.theme.ButtonHeight
import com.madpickle.calls.ui.theme.ButtonShapeSmall
import com.madpickle.calls.ui.theme.ContentPadding
import com.madpickle.calls.ui.theme.FontLargeSize
import com.madpickle.calls.ui.theme.IconSize
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.error
import com.madpickle.calls.ui.theme.simCard
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.ui.theme.text2
import com.madpickle.calls.ui.theme.warn
import com.madpickle.calls.utils.MaskVisualTransformation
import es.dmoral.toasty.Toasty


class EditContactScreen(
    private val draft: ContactDraft? = null,
) : Screen {
    private val iconSize = 50.dp
    private val imageSize = 144.dp
    private val mask = MaskVisualTransformation("+# (###) ###-##-##")

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val model = rememberScreenModel { EditContactModel(context, draft) }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingItem),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(ContentPadding)
        ) {
            items(model.getImages()) { bitmap ->
                OutlinedButton(onClick = {
                    model.setUserImage(bitmap)
                },
                    contentPadding = PaddingValues(),
                    modifier = Modifier.size(iconSize).background(MaterialTheme.cardItem, CircleShape),
                    shape = CircleShape,
                    elevation = ButtonElevation,
                    border =  ButtonDefaults.outlinedBorder.copy(
                        width = if(model.isSelectedBitmap(bitmap)) 1.dp else 0.dp,
                        brush = Brush.sweepGradient(listOf(MaterialTheme.simCard))
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = MaterialTheme.cardItem,
                        contentColor = MaterialTheme.cardItem
                    )
                ) {
                    AsyncImage(
                        model = model.getUserImage(),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(iconSize)
                            .background(MaterialTheme.cardItem),
                        error = painterResource(R.drawable.account),
                        alignment = Alignment.Center,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = model.getUserImage(),
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(imageSize)
                            .background(MaterialTheme.cardItem, CircleShape),
                        error = painterResource(R.drawable.account),
                        alignment = Alignment.Center,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            item {
                TextField(
                    value = model.getUsername(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    onValueChange = {
                        if (it.length < 50) {
                            model.setUsername(it)
                        }
                    },
                    label = {
                        Text(
                            stringResource(R.string.name_placeholder),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.text2,
                            fontSize = 16.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    ),
                    leadingIcon = {
                        Spacer(Modifier.size(IconSize))
                    },
                    singleLine = true,
                    trailingIcon = if (model.isErrorName.value) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Warning,
                                tint = MaterialTheme.warn,
                                modifier = Modifier.size(IconSize),
                                contentDescription = "Required"
                            )
                        }
                    } else {
                        {
                            Spacer(Modifier.size(IconSize))
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = MaterialTheme.cardItem,
                        errorIndicatorColor = MaterialTheme.error,
                        textColor = MaterialTheme.text
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
            if (model.getSavedNumbers().isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.phone_saved_hint),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.text2,
                        fontSize = 14.sp
                    )
                }
                items(model.getSavedNumbers()) { number ->
                    PhoneTextField(
                        text = number.value
                    ) {
                        number.value = it
                    }
                }
            } else {
                item { Spacer(Modifier) }
                item {
                    PhoneTextField(
                        hint = stringResource(R.string.phone_primary_hint),
                        isError = model.isErrorPhone.value,
                        text = model.getPrimaryPhone()
                    ) {
                        model.setPrimaryPhone(it)
                    }
                }
                item {
                    PhoneTextField(
                        hint = stringResource(R.string.phone_secondary_hint),
                        isError = false,
                        text = model.getSecondaryPhone()
                    ) {
                        model.setSecondaryPhone(it)
                    }
                }
            }
            item {
                Button(
                    onClick = model::onSave,
                    shape = ButtonShapeSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = ContentPadding)
                        .height(ButtonHeight),
                    contentPadding = PaddingValues(),
                    elevation = ButtonElevation,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.simCard
                    )
                ) {
                    Text(
                        stringResource(R.string.save),
                        fontSize = FontLargeSize,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
        if (model.isSuccess.value) {
            LocalNavigator.current?.pop()
            LaunchedEffect(Unit) {
                Toasty.success(
                    context,
                    context.getString(R.string.contact_saved), Toast.LENGTH_SHORT, true
                ).show()
            }
        }
    }

    @Composable
    private fun PhoneTextField(
        hint: String,
        isError: Boolean,
        text: String,
        onValueChange: (String) -> Unit
    ) {
        Text(
            text = hint,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = if (isError) FontWeight.Bold else FontWeight.Light,
            color = if (isError) MaterialTheme.error else MaterialTheme.text2,
            fontSize = 14.sp
        )
        PhoneTextField(text, onValueChange)
    }

    @Composable
    private fun PhoneTextField(text: String, onValueChange: (String) -> Unit) {
        TextField(
            value = text,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            onValueChange = {
                if (it.length <= 11) {
                    onValueChange.invoke(it)
                }
            },
            textStyle = TextStyle(
                fontSize = 20.sp
            ),
            singleLine = true,
            visualTransformation = mask,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = MaterialTheme.cardItem,
                textColor = MaterialTheme.text
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
    }
}