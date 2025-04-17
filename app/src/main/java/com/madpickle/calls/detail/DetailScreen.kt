package com.madpickle.calls.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import coil3.compose.AsyncImage
import com.madpickle.calls.ui.theme.HeightItem
import com.madpickle.calls.ui.theme.IconCornersShape
import com.madpickle.calls.ui.theme.PaddingItem
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.icon
import com.madpickle.calls.ui.theme.text

class DetailScreen(private val number: String): Screen {

    @Composable
    override fun Content() {
        val context = LocalContext.current
        val model = rememberScreenModel { DetailModel(context.contentResolver) }
        val detail = model.getDetail(number)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(PaddingItem),
            verticalArrangement = Arrangement.spacedBy(PaddingItem, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                AsyncImage(
                    model = detail?.contact?.imageUri,
                    modifier = Modifier.background(MaterialTheme.cardItem, CircleShape).size(
                        160.dp
                    ).clip(CircleShape),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.icon),
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(Icons.Rounded.AccountCircle),
                )
            }
            item {
                Text(detail?.contact?.name.orEmpty(), color = MaterialTheme.text, fontSize = 24.sp, style = Typography.body1)
            }
            item {
                Text(detail?.contact?.number.orEmpty(), color = MaterialTheme.text, fontSize = 24.sp, style = Typography.body1)
            }
        }
    }
}