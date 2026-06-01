package seven.collector.blocks

import android.util.Log

enum class LogLevel {
    DEBUG, INFO, WARN, ERROR
}

interface LogFactory {
    fun log(message: String, level: LogLevel, throwable: Throwable? = null, nodeId: String? = null)
}

// Extension Helper Functions
fun LogFactory.d(message: String, nodeId: String? = null) = log(message, LogLevel.DEBUG, null, nodeId)
fun LogFactory.i(message: String, nodeId: String? = null) = log(message, LogLevel.INFO, null, nodeId)
fun LogFactory.w(message: String, nodeId: String? = null) = log(message, LogLevel.WARN, null, nodeId)
fun LogFactory.e(message: String, throwable: Throwable? = null, nodeId: String? = null) = log(message, LogLevel.ERROR, throwable, nodeId)