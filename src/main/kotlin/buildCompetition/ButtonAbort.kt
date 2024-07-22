package buildCompetition

import interfaces.Button
import Context
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.rest.builder.component.ButtonBuilder

class ButtonAbort(
    override val kord: Kord,
    override val ctx: Context,
    override val customId: String = "BuildCompetition-abort",
    override var style: ButtonStyle = ButtonStyle.Danger,
    override var builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {
        label = "Abort"
    }
) : Button {
    override suspend fun onPress(interaction: ButtonInteraction) {
        interaction.deferEphemeralResponse()
        interaction.channel.delete("Submission ${interaction.channel.asChannel().data.name} was aborted!")
    }
}