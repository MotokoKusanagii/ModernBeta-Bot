package buildCompetition

import Button
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.DiscordPartialEmoji
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ButtonInteraction

class Abort(
    override val kord: Kord,
    override val label: String?,
    override val emoji: DiscordPartialEmoji?,
    override val customId: String,
    override var style: ButtonStyle,
    override var disabled: Boolean
) : Button {
    override suspend fun onPress(interaction: ButtonInteraction) {
        TODO("Not yet implemented")
    }
}