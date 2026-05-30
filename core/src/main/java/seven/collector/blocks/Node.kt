package seven.collector.blocks

data class Node(
    val id: String,
    val name: String,
    val nodeCategory: NodeCategory,

    val type: NodeType,
    val outputType: Type? = null,

    val primaryTemplatePart: List<TemplatePart>,
    val secondaryTemplatePart: List<TemplatePart>? = null,

    val children: List<Node>? = null,
    val secondaryChildren: List<Node>? = null,

    val executable: (suspend (Context, Map<String, Any?>) -> Any?)? = null
)