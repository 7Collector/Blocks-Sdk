package seven.collector.blocks

interface BlockRegistry {
    fun getAllNodes(): List<Node>
    fun getAllCategories(): List<NodeCategory>
    fun getAllNodesByCategory(category: NodeCategory): List<Node>
    fun addNode(node: Node)
    fun createInstance(id: String): Node?
}