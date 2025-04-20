package com.madpickle.calls.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madpickle.calls.data.CallType
import com.madpickle.calls.data.ItemCallLog
import com.madpickle.calls.ui.theme.ButtonElevation
import com.madpickle.calls.ui.theme.CardItemShape
import com.madpickle.calls.ui.theme.PaddingCardItem
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.secondaryText
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.ui.theme.text2

@Composable
fun ItemCallLogUI(log: ItemCallLog, onItemClick: () -> Unit) {

    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = CardItemShape,
        onClick = onItemClick,
        elevation = ButtonElevation,
        contentPadding = PaddingCardItem,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.cardItem
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(log.getIconRes()),
                contentDescription = ""
            )
            Column(
                Modifier.padding(start = 40.dp)
            ) {
                Text(
                    log.name ?: log.number,
                    color = MaterialTheme.text,
                    fontSize = 16.sp,
                    style = Typography.body1
                )
                Text(
                    log.getTimeDateFormatted(),
                    lineHeight = 14.sp,
                    color = MaterialTheme.secondaryText,
                    fontSize = 10.sp
                )
            }
            if (log.getDurationToMMSS().isNotEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = log.getDurationToMMSS(),
                    color = MaterialTheme.text2,
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
@Preview
private fun ItemCallLogUIPreview() {
    ItemCallLogUI(
        ItemCallLog(
            "",
            "Jon Smith",
            "89091234545",
            CallType.MISSING,
            1672531199000L,
            232,
        )
    ) {}
}