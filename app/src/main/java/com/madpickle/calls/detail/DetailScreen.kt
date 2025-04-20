package com.madpickle.calls.detail

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import com.madpickle.calls.R
import com.madpickle.calls.ui.theme.ButtonElevation
import com.madpickle.calls.ui.theme.CardItemShape
import com.madpickle.calls.ui.theme.ContentPadding
import com.madpickle.calls.ui.theme.HeightItem
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.divider
import com.madpickle.calls.ui.theme.error
import com.madpickle.calls.ui.theme.icon
import com.madpickle.calls.ui.theme.secondaryText
import com.madpickle.calls.ui.theme.text

class DetailScreen(private val name: String) : Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val navigator = LocalNavigator.current
        val model = rememberScreenModel { DetailModel(context.contentResolver) }
        val detail = model.getDetail(name)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = PaddingItem),
            verticalArrangement = Arrangement.spacedBy(PaddingItem, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = detail.imageUri,
                        modifier = Modifier
                            .background(MaterialTheme.cardItem, CircleShape)
                            .align(Alignment.Center)
                            .size(100.dp)
                            .clip(CircleShape),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(MaterialTheme.icon),
                        contentScale = ContentScale.Crop,
                        error = rememberVectorPainter(Icons.Rounded.AccountCircle),
                    )
                    Button(
                        onClick = {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.WRITE_CONTACTS
                                ) == PackageManager.PERMISSION_GRANTED
                            ) {
                                model.deleteContact(detail.name)
                                navigator?.pop()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.cardItem
                        ),
                        elevation = ButtonElevation,
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(end = 12.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            stringResource(R.string.delete),
                            color = MaterialTheme.error
                        )
                    }
                }
            }
            item {
                Text(
                    detail.name,
                    color = MaterialTheme.text,
                    fontSize = 24.sp,
                    style = Typography.body1
                )
            }
            items(detail.numbers) {
                ItemDetailContactUI(context, it)
            }
            if(detail.sectionsLogs.isNotEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .height(HeightItem)
                            .background(MaterialTheme.cardItem)
                    ) {
                        Text(
                            stringResource(R.string.last_calls).uppercase(),
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(PaddingItem),
                            color = MaterialTheme.text,
                            fontSize = 14.sp,
                            style = Typography.body1
                        )
                    }
                }
                detail.sectionsLogs.forEach { section ->
                    item {
                        Text(
                            text = section.key.uppercase(),
                            style = MaterialTheme.typography.body1,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(top = 16.dp, start = ContentPadding)
                                .fillMaxWidth(),
                            color = MaterialTheme.secondaryText
                        )
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(PaddingItem)
                                .background(
                                    MaterialTheme.cardItem,
                                    CardItemShape
                                )
                                .padding(horizontal = ContentPadding)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                section.value.forEachIndexed { index, itemCallLog ->
                                    ItemDetailCallLogUI(itemCallLog)
                                    if(index != section.value.lastIndex) {
                                        Divider(
                                            Modifier
                                                .fillMaxWidth()
                                                .background(MaterialTheme.divider))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}