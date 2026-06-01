package seven.collector.blocks.state

import androidx.compose.ui.geometry.Offset
import seven.collector.blocks.Node


data class UINode(
    val node: Node,
    val isAttached: Boolean = false,
    val position: Offset
)

fun UINode.toNode(): Node {
    return this.node
}

fun Node.toUINode(offset: Offset): UINode {
    return UINode(
        node = this,
        isAttached = false,
        position = offset
    )
}