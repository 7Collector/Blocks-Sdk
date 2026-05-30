package seven.collector.blocks

import android.util.Log

class DefaultLogFactory(private val globalTag: String = "AnkiToysEngine") : LogFactory {

    override suspend fun log(message: String, level: LogLevel, throwable: Throwable?, nodeId: String?) {

        val prefix = if (nodeId != null) "[Node: $nodeId] " else ""
        val finalMessage = "$prefix$message"

        when (level) {
            LogLevel.DEBUG -> Log.d(globalTag, finalMessage, throwable)
            LogLevel.INFO -> Log.i(globalTag, finalMessage, throwable)
            LogLevel.WARN -> Log.w(globalTag, finalMessage, throwable)
            LogLevel.ERROR -> Log.e(globalTag, finalMessage, throwable)
        }
    }
}