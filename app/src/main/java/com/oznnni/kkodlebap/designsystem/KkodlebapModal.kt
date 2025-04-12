package com.oznnni.kkodlebap.designsystem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oznnni.kkodlebap.ui.theme.KkodlebapTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KkodlebapModal(
    isOpen: Boolean,
    sheetState: SheetState,
    properties: ModalBottomSheetProperties = ModalBottomSheetDefaults.properties,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (!isOpen) return
    ModalBottomSheet(
        modifier = Modifier.padding(top = 92.dp),
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        onDismissRequest = onDismissRequest,
        scrimColor = Color.Black.copy(alpha = 0.6f),
        containerColor = KkodlebapTheme.colors.white,
        properties = properties,
        dragHandle = null,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewKkodlebapModal() {
    KkodlebapModal(
        isOpen = true,
        sheetState = rememberModalBottomSheetState(),
        onDismissRequest = {},
    ) {}
}
