package seven.collector.blocks

import seven.collector.blocks.NodeType.*

sealed class Branch {
    object PRIMARY : Branch()
    object SECONDARY : Branch()
    data class ERROR(val message: String) : Branch()
}

class DefaultNodeExecutor : NodeExecutor {

    override suspend fun execute(nodes: List<Node>, context: Context, logFactory: LogFactory?) {
        for (node in nodes) {
            executeNode(node, context, logFactory)
        }
    }

    private suspend fun executeNode(node: Node, context: Context, logFactory: LogFactory?): Any? {
        return when (node.type) {
            GET_VARIABLE -> {
                context.getValue(node.id)
            }

            SET_VARIABLE -> {
                val slots = node.primaryTemplatePart.filterIsInstance<Slot>()
                if (slots.size != 2) {
                    logFactory?.w("SET_VARIABLE node has invalid slot count.", node.id)
                    return null
                }

                val firstSlotVariable = slots.first().valueNode ?: return null
                val secondSlot = slots.last()

                context.setValue(firstSlotVariable.id, evaluateSlot(secondSlot, context, logFactory))
                null
            }

            FUNCTION -> {
                logFactory?.d("Executing function: ${node.name}", node.id)
                if (node.executable == null) {
                    logFactory?.w("Function node has no executable defined.", node.id)
                    return null
                }

                val resolvedArgs = mutableMapOf<String, Any?>()
                node.primaryTemplatePart.filterIsInstance<Slot>().forEach {
                    resolvedArgs[it.id] = evaluateSlot(it, context, logFactory)
                }

                try {
                    val output = node.executable!!.invoke(context, resolvedArgs)
                    logFactory?.d("Output: $output", node.id)
                    output
                } catch (e: Exception) {
                    logFactory?.e("Execution crashed: ${e.message}", e, node.id)
                    null
                }
            }

            SCOPED -> {
                node.children?.let {
                    this.execute(it, context.createChildScope(), logFactory)
                }
                null
            }

            SCOPED_BRANCHED -> {
                when (val branch = resolveBranch(node, context, logFactory)) {
                    Branch.PRIMARY -> node.children?.let {
                        this.execute(it, context.createChildScope(), logFactory)
                    }

                    Branch.SECONDARY -> node.secondaryChildren?.let {
                        this.execute(it, context.createChildScope(), logFactory)
                    }

                    is Branch.ERROR -> { logFactory?.e(branch.message, nodeId = node.id)}
                }
                null
            }
        }
    }

    private suspend fun resolveBranch(node: Node, context: Context, logFactory: LogFactory?): Branch {
        if (node.secondaryTemplatePart == null) return Branch.PRIMARY
        val conditionSlot = node.primaryTemplatePart.last() as? Slot ?: return Branch.PRIMARY

        if (conditionSlot.valueNode == null) return Branch.ERROR("Condition Slot is Empty")
        if (conditionSlot.acceptedType != Primitives.Boolean) return Branch.ERROR("Condition Slot should only accept BOOLEAN values")

        val result = evaluateSlot(conditionSlot, context, logFactory)

        return when (result) {
            true -> Branch.PRIMARY
            false -> Branch.SECONDARY
            else -> Branch.ERROR("Condition Slot did not evaluate to a Boolean")
        }
    }

    private suspend fun evaluateSlot(slot: Slot, context: Context, logFactory: LogFactory?): Any? {
        if (slot.inlineAllowed == true && slot.inlineValue != null) {
            return slot.inlineValue
        }
        if (slot.valueNode != null) {
            return executeNode(slot.valueNode!!, context, logFactory)
        }
        return null
    }
}

val sampleAddNode = Node(
    id = "add_int",
    name = "Add Int",
    nodeCategory = NodeCategory(id = "math", label = "Math"),
    type = FUNCTION,
    outputType = Primitives.Int,
    primaryTemplatePart = listOf(
        Slot(
            id = "int1_slot",
            label = "First Integer",
            acceptedType = Primitives.Int,
            inlineAllowed = true,
        ), TextPart(text = "+"), Slot(
            id = "int2_slot",
            label = "First Integer",
            acceptedType = Primitives.Int,
            inlineAllowed = true,
        )
    ),
    secondaryTemplatePart = null,
    executable = { context, args ->
        val i1 = args["int1_slot"] as Int
        val i2 = args["int2_slot"] as Int
        i1 + i2
    },
)