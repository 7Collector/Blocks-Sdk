package seven.collector.blocks.state

data class Anchors(
    val id: String,
    val label: String,
    val positionX: Float,
    val positionY: Float,
    val children: List<UINode>,
    val allowMovement: Boolean
)
