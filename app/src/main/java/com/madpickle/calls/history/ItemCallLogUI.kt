package com.madpickle.calls.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madpickle.calls.R
import com.madpickle.calls.data.CallType
import com.madpickle.calls.data.ItemCallLog
import com.madpickle.calls.ui.theme.text

@Composable
fun ItemCallLogUI(log: ItemCallLog) {

    val iconRes = when(log.type) {
        CallType.INCOMING -> R.drawable.ic_incom
        CallType.OUTGOING -> R.drawable.ic_outcom
        CallType.MISSING -> R.drawable.ic_missed
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Image(
                modifier = Modifier.size(32.dp).align(Alignment.CenterStart),
                painter = painterResource(iconRes),
                contentDescription = ""
            )
            Column(
                Modifier.padding(start = 40.dp)
            ) {
                Text(log.name ?: log.number, color = MaterialTheme.text, fontSize = 16.sp)
                Text(log.getDateFormatted(), lineHeight = 14.sp, color = MaterialTheme.text, fontSize = 10.sp)
            }
            if(log.getDurationToMMSS().isNotEmpty()) {
                Text(modifier = Modifier.align(Alignment.CenterEnd), text = log.getDurationToMMSS(), color = MaterialTheme.text, fontSize = 12.sp)
            }
        }
    }
}



@Composable
@Preview
private fun ItemCallLogUIPreview() {
    ItemCallLogUI(ItemCallLog(
        "",
        "Jon Smith",
        "89091234545",
        CallType.MISSING,
        1672531199000L,
        232,
    ))
}