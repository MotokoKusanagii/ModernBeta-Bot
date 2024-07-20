import dev.kord.common.entity.ButtonStyle
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.rest.builder.component.ButtonBuilder

interface Button {
    val kord: Kord
    val ctx: Context
    val customId: String
    var style: ButtonStyle
    var builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit

    suspend fun onPress(interaction: ButtonInteraction)
}