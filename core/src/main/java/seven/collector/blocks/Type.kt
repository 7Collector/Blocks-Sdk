package seven.collector.blocks

sealed interface Type {
    val name: String
}

// Primitive Types
object Primitives {
    data object Boolean : Type { override val name = "boolean" }
    data object String : Type { override val name = "string" }
    data object Int : Type { override val name = "int" }
    data object Float : Type { override val name = "float" }
    data object Long : Type { override val name = "long" }
    data object Any : Type { override val name = "any" }
}

// List Container Types
data class ListType(val innerType: Type) : Type {
    override val name = "list<${innerType.name}>"
}

// Map Types
data class MapType(val keyType: Type, val valueType: Type) : Type {
    override val name = "map<${keyType.name}, ${valueType.name}>"
}

// Nullable Types
data class NullableType(val innerType: Type) : Type {
    override val name = "${innerType.name}?"
}

// Custom Types (Like Anki Decks or Notes)
data class CustomType(override val name: String) : Type