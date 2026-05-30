package seven.collector.blocks

interface Context {
    fun getValue(id: String): Any?
    fun setValue(id: String, value: Any?)
    fun createChildScope(): Context
    fun contains(id: String): Boolean
}

class DefaultContext(
    private val parent: Context? = null,
    initialValues: Map<String, Any?> = emptyMap()
) : Context {

    private val variablesStore: MutableMap<String, Any?> = initialValues.toMutableMap()

    override fun getValue(id: String): Any? {
        if (variablesStore.containsKey(id)) {
            return variablesStore[id]
        }
        if (parent != null) {
            return parent.getValue(id)
        }
        throw NoKeyException("Variable with id '$id' not found in current or parent contexts.")
    }

    override fun setValue(id: String, value: Any?) {
        if (!variablesStore.containsKey(id) && parent != null && parent.contains(id)) {
            parent.setValue(id, value)
        } else {
            variablesStore[id] = value
        }
    }

    override fun contains(id: String): Boolean {
        return variablesStore.containsKey(id) || (parent?.contains(id) == true)
    }

    override fun createChildScope(): Context {
        return DefaultContext(parent = this)
    }
}

class TypeMismatchException(message: String? = null) : Exception(message)
class NonNullValueExceptedException(message: String? = null) : Exception(message)
class NoKeyException(message: String? = null) : Exception(message)