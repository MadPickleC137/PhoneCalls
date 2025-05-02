package com.madpickle.calls.ui.theme.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.madpickle.calls.ui.theme.FabShape
import com.madpickle.calls.ui.theme.Typography
import com.madpickle.calls.ui.theme.cardItem
import com.madpickle.calls.ui.theme.error
import com.madpickle.calls.ui.theme.simCard
import com.madpickle.calls.ui.theme.text

@Composable
fun NativeDialog(
    text: String,
    title: String? = null,
    confirmText: String,
    dismissText: String? = null,
    onConfirm: () -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.cardItem,
        shape = FabShape,
        properties = DialogProperties(
            dismissOnBackPress = true,
            usePlatformDefaultWidth = true,
            dismissOnClickOutside = false
        ),
        title = if (title != null) {
            {
                Text(
                    text = title,
                    style = Typography.h6,
                    color = MaterialTheme.text
                )
            }
        } else null,
        text = {
            Text(
                text = text,
                style = Typography.h6,
                fontSize = 17.sp,
                color = MaterialTheme.text
            )
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text(
                    confirmText,
                    style = Typography.body1,
                    color = MaterialTheme.simCard
                )
            }
        },
        dismissButton = if (onDismiss != null) {
            {
                TextButton(
                    onClick = {
                        onDismiss.invoke()
                    }
                ) {
                    dismissText?.let {
                        Text(
                            it,
                            style = Typography.body1,
                            color = MaterialTheme.error
                        )
                    }
                }
            }
        } else null
    )
}


@Composable
@Preview(
    showBackground = true,
    device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420",
    showSystemUi = true
)
private fun NativeDialogPreview() {
    NativeDialog(
        text = "Восстановить контакт не получится, придется создавать новые",
        confirmText = "Cancel",
        dismissText = "Delete",
        onDismiss = {}, onConfirm = {}
    )
}