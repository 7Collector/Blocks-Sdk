package seven.collector.blocks.blocks

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import seven.collector.blocks.NodeType
import seven.collector.blocks.Slot
import seven.collector.blocks.TextPart
import seven.collector.blocks.state.LocalDragTargetInfo
import seven.collector.blocks.state.UINode
import kotlin.math.roundToInt

@Composable
fun NodeRenderer(
    node: UINode,
    animationNodeId: String?,
    resetNodeId: String?,
    moveBelowNodeForAnimation: (showSpace: Boolean) -> Unit,
    addNodeBelow: (UINode) -> Unit,
    resetDraggedNode: (UINode) -> Unit,
) {
    val dragInfo = LocalDragTargetInfo.current
    var startPosition by remember { mutableStateOf(Offset.Zero) }
    var isBeingDragged by remember { mutableStateOf(false) }
    var isHoveredByAnotherNode by remember { mutableStateOf(false) }

    LaunchedEffect(isHoveredByAnotherNode) {
        if (isHoveredByAnotherNode) {
            moveBelowNodeForAnimation(true)
        } else {
            moveBelowNodeForAnimation(false)
        }
    }

    LaunchedEffect(dragInfo.isDragging) {
        if (!dragInfo.isDragging && isHoveredByAnotherNode && dragInfo.draggedNode != null) {
            if (dragInfo.draggedNode!!.node.outputType == null) {
                addNodeBelow(dragInfo.draggedNode!!)
                startPosition = Offset.Zero
            } else {
                resetDraggedNode(dragInfo.draggedNode!!)
            }
        }
    }
    LaunchedEffect(resetNodeId) {
        if (resetNodeId == node.node.id) {

        }
    }


    Column(modifier = Modifier.animateContentSize()) {
        if (animationNodeId == node.node.id) {
            Spacer(
                Modifier
                    .height(10.dp)
                    .width(100.dp)
                    .background(
                        if (isHoveredByAnotherNode && dragInfo.draggedNode!!.node.outputType == null) Color.Gray.copy(
                            alpha = 0.5f
                        ) else Color.White, functionBlockShape
                    )
            )
        }
        Box(
            modifier = Modifier
                .onGloballyPositioned { if (startPosition!= Offset.Zero) startPosition = it.localToWindow(Offset.Zero) }
                .offset {
                    if (isBeingDragged) IntOffset(
                        x = dragInfo.dragOffset.x.roundToInt(),
                        y = dragInfo.dragOffset.y.roundToInt()
                    ) else IntOffset.Zero
                }
                .zIndex(if (isBeingDragged) 1f else 0f)
                .pointerInput(node) {
                    detectDragGesturesAfterLongPress(onDragEnd = {
                        dragInfo.isDragging = false
                        isBeingDragged = false
                        dragInfo.dragOffset = Offset.Zero
                    }, onDragCancel = {
                        dragInfo.isDragging = false
                        isBeingDragged = false
                        dragInfo.dragOffset = Offset.Zero
                    }, onDrag = { pointerInput, offset ->
                        pointerInput.consume()
                        dragInfo.dragOffset += offset
                    }, onDragStart = {
                        dragInfo.isDragging = true
                        isBeingDragged = true
                        dragInfo.position = startPosition
                        dragInfo.draggedNode = node
                    })
                }) {
            LaunchedEffect(resetNodeId) {
                if (resetNodeId == node.node.id) {
                    this@Box.apply { Modifier.offset(x = dragInfo.position.x.dp, y = dragInfo.position.y.dp) }
                }
            }
            when (node.node.type) {
                NodeType.GET_VARIABLE -> GetVariableNodeUI(node)
                NodeType.SET_VARIABLE -> SetVariableNodeUI(node)
                NodeType.FUNCTION -> FunctionNodeUI(node)
                NodeType.SCOPED -> ScopedNodeUI(node)
                NodeType.SCOPED_BRANCHED -> ScopedBranchedNodeUI(node)
            }
            if (!isBeingDragged) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .zIndex(0.5f)
                        .onGloballyPositioned { coordinates ->
                            val bounds = coordinates.boundsInWindow()
                            isHoveredByAnotherNode = dragInfo.isDragging && bounds.contains(
                                dragInfo.position + dragInfo.dragOffset
                            )
                        }
                )
            }

        }
    }
}

@Composable
fun GetVariableNodeUI(node: UINode) {
    Row(
        modifier = Modifier.background(
            color = resolveGetVariableColor(node), shape = getVariableBlockShape
        )
    ) {
        node.node.primaryTemplatePart.forEach {
            when (it) {
                is TextPart -> Text(text = it.text)
                is Slot -> {
                    SlotUI(
                        slot = it,
                        onNodeDropped = { node ->
                            it.valueNode = node.node
                        },
                        resetDraggedNode = {})
                }
            }
        }
    }
}

@Composable
fun SetVariableNodeUI(node: UINode) {
    FunctionNodeUI(node)
}

@Composable
fun FunctionNodeUI(node: UINode) {
    Row(
        modifier = Modifier.background(
            color = resolveBlockColor(node),
            shape = if (node.node.outputType == null) functionBlockShape else getVariableBlockShape
        )
    ) {
        node.node.primaryTemplatePart.forEach {
            when (it) {
                is TextPart -> Text(text = it.text)
                is Slot -> SlotUI(
                    it,
                    onNodeDropped = { node ->
                        it.valueNode = node.node
                    },
                    resetDraggedNode = {})
            }
        }
    }
}

@Composable
fun ScopedNodeUI(node: UINode) {

}

@Composable
fun ScopedBranchedNodeUI(node: UINode) {

}

fun resolveGetVariableColor(node: UINode): Color {
    return Color.Yellow
}

fun resolveBlockColor(node: UINode): Color {
    return Color.Blue
}

val functionBlockShape: Shape = RectangleShape
val getVariableBlockShape = RoundedCornerShape(12.dp)