package com.madpickle.calls.ui.theme.widgets

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madpickle.calls.ui.theme.FabShape
import com.madpickle.calls.ui.theme.fab
import com.madpickle.calls.ui.theme.text

@Composable
fun BoxScope.Fab(text: String, icon: Painter, onClick: ()-> Unit) {
    FloatingActionButton(
        shape = FabShape,
        modifier = Modifier
            .wrapContentSize()
            .defaultMinSize(minWidth = 120.dp)
            .align(Alignment.BottomEnd)
            .padding(12.dp),
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 8.dp
        ),
        onClick = onClick,
        contentColor = Color.Transparent,
        backgroundColor = MaterialTheme.fab
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Icon(
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.text,
                painter = icon,
                contentDescription = ""
            )
            Text(text, fontSize = 12.sp, color = MaterialTheme.text)
        }
    }
}