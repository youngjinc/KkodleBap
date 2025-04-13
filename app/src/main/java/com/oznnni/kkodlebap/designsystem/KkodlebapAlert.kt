package com.oznnni.kkodlebap.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.oznnni.kkodlebap.designsystem.theme.KkodlebapTheme

@Composable
fun KkodlebapAlert(
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (!isOpen) return
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = KkodlebapTheme.colors.white, shape = RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewKkodlebapAlert() {
    KkodlebapAlert(
        isOpen = true,
        onDismissRequest = {},
    ) {}
}
