package seven.collector.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import seven.collector.blocks.blocks.AnchorUIBlock
import seven.collector.blocks.state.CanvasState


@Composable
fun BlocksCanvas(state: CanvasState) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White).padding(12.dp)) {
        state.anchors.forEach {
            AnchorUIBlock(it)
        }
    }
}