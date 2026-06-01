package seven.collector.blocks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import seven.collector.blocks.blocks.NodeRenderer
import seven.collector.blocks.state.toUINode

@Composable
fun BlocksPicker(registry: BlockRegistry) {
    val categories = registry.getAllCategories()
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column() {
            registry.getAllNodesByCategory(selectedCategory).forEach {
                NodeRenderer(it.toUINode(Offset.Zero), null, null, {}, {}, {})
            }
        }
        Column() {
            categories.forEach {
                Box(modifier = Modifier) {
                    Text(it.label)
                }
            }
        }
    }
}