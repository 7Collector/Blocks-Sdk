package seven.collector.blocks.state

data class Anchor(
    val id: String,
    val label: String,
    val positionX: Float,
    val positionY: Float,
    val children: MutableList<UINode>,
    val allowMovement: Boolean
)
