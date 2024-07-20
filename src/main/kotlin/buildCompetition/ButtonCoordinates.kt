package buildCompetition

import Button
import Context
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.rest.builder.component.ButtonBuilder


class ButtonCoordinates(
    override val kord: Kord,
    override val ctx: Context,
    override val customId: String = "BuildCompetition-coords",
    override var style: ButtonStyle = ButtonStyle.Primary,
    override var builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {
        label = "Provide Coordinates"
    }
) : Button {
    override suspend fun onPress(interaction: ButtonInteraction) {
        val modal = ctx.distributor.modals["BuildCompetition-coords-modal"]!!
        interaction.modal(modal.title, modal.customId, modal.builder)
    }
}