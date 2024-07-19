import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.rest.builder.component.ButtonBuilder

interface Button {
    val kord: Kord
    val label: String?
    val emoji: DiscordPartialEmoji?
    val customId: String
    var style: ButtonStyle
    var disabled: Boolean

    suspend fun onPress(interaction: ButtonInteraction)

    suspend fun get(): ButtonBuilder.InteractionButtonBuilder.() -> Unit {
        return {
            label = this@Button.label
            emoji = this@Button.emoji
            customId = this@Button.customId
            style = this@Button.style
            disabled = this@Button.disabled
        }
    }

}