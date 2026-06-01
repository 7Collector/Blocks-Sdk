package seven.collector.blocks

sealed interface TemplatePart

data class TextPart(val text: String): TemplatePart

data class Slot(
    val id: String,
    val label: String,
    val acceptedType: Type,
    var valueNode: Node? = null,
    val inlineAllowed: Boolean? = acceptedType in listOf(
        Primitives.Int,
        Primitives.Float,
        Primitives.String,
        Primitives.Boolean
    ),
    val inlineValue: Any? = null
): TemplatePart