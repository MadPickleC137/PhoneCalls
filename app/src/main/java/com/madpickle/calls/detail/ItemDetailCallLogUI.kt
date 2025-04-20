package com.madpickle.calls.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.madpickle.calls.ui.theme.HeightItem
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.secondaryText
import com.madpickle.calls.ui.theme.text
import com.madpickle.calls.ui.theme.text2

@Composable
fun ItemDetailCallLogUI(log: ItemCallLog) {
    Box(
        modifier = Modifier.fillMaxWidth().height(HeightItem).padding(8.dp)
    ) {
        Image(
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterStart),
            painter = painterResource(log.getIconRes()),
            contentDescription = ""
        )
        Column(
            Modifier.padding(start = 40.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                log.number,
                color = MaterialTheme.text,
                fontSize = 16.sp,
                style = Typography.body1
            )
            Text(
                log.getTimeFormatted(),
                lineHeight = 14.sp,
                color = MaterialTheme.secondaryText,
                fontSize = 12.sp
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

@Composable
@Preview
private fun ItemDetailCallLogUIPreview() {
    ItemDetailCallLogUI(
        ItemCallLog(
            "",
            "ivan",
            "894238493849",
            CallType.INCOMING,
            89384938444,
            1234
        )
    )
}