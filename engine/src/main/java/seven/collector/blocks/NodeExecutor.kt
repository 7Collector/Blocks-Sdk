package seven.collector.blocks

interface NodeExecutor {
    suspend fun execute(nodes: List<Node>, context: Context, logFactory: LogFactory? = null)
}