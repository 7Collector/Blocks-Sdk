package seven.collector.blocks.blocks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import seven.collector.blocks.state.Anchor

@Composable
fun AnchorUIBlock(anchor: Anchor) {
    var moveAnimationBlockId by remember { mutableStateOf<String?>(null) }
    var resetNodeId by remember { mutableStateOf<String?>(null) }

    anchor.children.forEachIndexed { index, node ->
        NodeRenderer(
            node = node,
            animationNodeId = moveAnimationBlockId,
            resetNodeId = resetNodeId,
            moveBelowNodeForAnimation = { show ->
                moveAnimationBlockId =
                    if (show && anchor.children.size > index) anchor.children[index + 1].node.id else null
            },
            addNodeBelow = { node -> anchor.children.add(index, node) },
            resetDraggedNode = {},
        )
    }
}