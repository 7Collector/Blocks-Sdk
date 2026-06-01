package seven.collector.blocks.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import seven.collector.blocks.Slot
import seven.collector.blocks.state.LocalDragTargetInfo
import seven.collector.blocks.state.UINode

@Composable
fun SlotUI(slot: Slot, onNodeDropped: (UINode) -> Unit, resetDraggedNode: (UINode) -> Unit) {
    val dragInfo = LocalDragTargetInfo.current
    var isHovered by remember { mutableStateOf(false) }

    LaunchedEffect(dragInfo.isDragging) {
        if (!dragInfo.isDragging && isHovered && dragInfo.draggedNode != null) {
            if (dragInfo.draggedNode!!.node.outputType == slot.acceptedType) {
                onNodeDropped(dragInfo.draggedNode!!)
            } else {
                resetDraggedNode(dragInfo.draggedNode!!)
            }
        }
    }

    Box(
        modifier = Modifier.background(
            if (isHovered && dragInfo.draggedNode!!.node.outputType == slot.acceptedType) Color.Gray.copy(
                alpha = 0.5f
            ) else Color.White, getVariableBlockShape
        ).onGloballyPositioned{coordinates->
            val bounds = coordinates.boundsInWindow()
            isHovered = dragInfo.isDragging && bounds.contains(
                dragInfo.position + dragInfo.dragOffset
            )
        }, contentAlignment = Alignment.Center
    ) {
        Text(text = slot.acceptedType.name)
    }
}