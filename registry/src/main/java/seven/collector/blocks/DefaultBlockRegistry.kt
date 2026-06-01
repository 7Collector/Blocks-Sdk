package seven.collector.blocks

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DefaultBlockRegistry(val allCategories: List<NodeCategory>, initialNodes: List<Node>): BlockRegistry {

    private val nodes: MutableMap<String, Node> = initialNodes.associateBy { it.id}.toMutableMap()

    init {
        // Load nodes here from storage instead of passing as an arg
    }

    override fun getAllNodes(): List<Node> {
        return nodes.map { it.value }
    }

    override fun getAllCategories(): List<NodeCategory> {
        return allCategories
    }

    override fun getAllNodesByCategory(category: NodeCategory): List<Node> {
        return nodes.filter { it.value.nodeCategory==category }.map { it.value }
    }

    override fun addNode(node: Node) {
        nodes[node.id] = node
        saveNode(node)
    }

    @OptIn(ExperimentalUuidApi::class)
    override fun createInstance(id: String): Node? {
        return nodes[id]?.copy(id = "${id}_${Uuid.random().toString().slice(0..4)}")
    }

    private fun saveNode(node: Node) {
        // TODO
    }
}