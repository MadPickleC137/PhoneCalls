package com.madpickle.calls.contacts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.madpickle.calls.R
import com.madpickle.calls.data.ItemContact
import com.madpickle.calls.ui.theme.ButtonElevation
import com.madpickle.calls.ui.theme.CardItemShape
import com.madpickle.calls.ui.theme.HeightItem
import com.madpickle.calls.ui.theme.IconCornersShape
import com.madpickle.calls.ui.theme.PaddingCardItem
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.icon
import com.madpickle.calls.ui.theme.secondaryText
import com.madpickle.calls.ui.theme.text

@Composable
fun ItemContactUI(data: ItemContact, onClick: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = CardItemShape,
        onClick = onClick,
        elevation = ButtonElevation,
        contentPadding = PaddingCardItem,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.cardItem
        )
    ) {
        if (data.imageUri == null) {
            Icon(
                modifier = Modifier.size(HeightItem),
                imageVector = Icons.Rounded.AccountCircle,
                tint = MaterialTheme.icon,
                contentDescription = ""
            )
        } else {
            AsyncImage(
                model = data.imageUri,
                modifier = Modifier
                    .background(MaterialTheme.icon, IconCornersShape)
                    .size(HeightItem)
                    .clip(CircleShape),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                error = rememberVectorPainter(Icons.Rounded.AccountCircle),
            )
        }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(data.name, color = MaterialTheme.text, fontSize = 16.sp, style = Typography.body1)
            if(data.numbers.isNotEmpty()) {
                Text(
                    pluralStringResource(
                        R.plurals.phone_counters,
                        data.numbers.count(),
                        data.numbers.count(),
                        data.numbers.first()
                    ),
                    color = MaterialTheme.secondaryText,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview
private fun ItemContactUIPreview() {
    ItemContactUI(
        ItemContact(
            id = 124,
            "Ivan Ivanocv",
            listOf("0981279387129", "1231234823974", "1231234823974"),
            null,
        )
    ) {}
}