package seven.collector.blocks.state

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var position by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggedNode: UINode? by mutableStateOf(null)
}

val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }
