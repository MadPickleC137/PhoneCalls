package com.madpickle.calls.addContact

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
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
import com.madpickle.calls.ui.theme.icon
import com.madpickle.calls.ui.theme.simCard
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.ui.theme.text2
import com.madpickle.calls.ui.theme.warn
import com.madpickle.calls.utils.MaskVisualTransformation
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


class EditContactScreen(
    private val draft: ContactDraft? = null,
) : Screen {
    private val iconArrowSize = 50.dp
    private val imageSize = 144.dp
    private val mask = MaskVisualTransformation("+# (###) ###-##-##")

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val model = rememberScreenModel { EditContactModel(context, draft) }
        val pagerState = rememberPagerState(
            pageCount = { model.getImages().size },
            initialPage = 0,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(PaddingItem),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(ContentPadding)
        ) {
            item {
                Box(
                    Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .zIndex(2f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                }
                            },
                            enabled = pagerState.canScrollBackward
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arror_start),
                                modifier = Modifier
                                    .size(iconArrowSize)
                                    .padding(ContentPadding),
                                tint = MaterialTheme.icon,
                                contentDescription = null
                            )
                        }
                        Spacer(Modifier.size(imageSize + 40.dp))
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            enabled = pagerState.canScrollForward
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arror_end),
                                modifier = Modifier
                                    .size(iconArrowSize)
                                    .padding(ContentPadding),
                                tint = MaterialTheme.icon,
                                contentDescription = null
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.align(Alignment.Center),
                        pageSpacing = ContentPadding,
                        snapPosition = SnapPosition.Start
                    ) { index ->
                        val icon = model.getImages()[index]
                        model.selectedImageIndex.intValue = index
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = icon,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(imageSize)
                                    .graphicsLayer {
                                        val pageOffset = (
                                                (pagerState.currentPage - index) + pagerState
                                                    .currentPageOffsetFraction
                                                ).absoluteValue

                                        alpha = lerp(
                                            start = 0.3f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                    }
                                    .background(MaterialTheme.cardItem, CircleShape),
                                alignment = Alignment.Center,
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                            )
                        }
                    }
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