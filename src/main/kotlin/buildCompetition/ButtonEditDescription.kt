package buildCompetition

import Button
import Context
import dev.kord.common.entity.ButtonStyle
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.rest.builder.component.ButtonBuilder

class ButtonEditDescription(
    override val kord: Kord,
    override val ctx: Context,
    override val customId: String = "BuildCompetition-edit-description",
    override var style: ButtonStyle = ButtonStyle.Primary,
    override var builder: ButtonBuilder.InteractionButtonBuilder.() -> Unit = {
        label = "Edit Description"
    }
) : Button {
    override suspend fun onPress(interaction: ButtonInteraction) {
        val modal = ctx.distributor.modals["BuildCompetition-description-modal"]!!
        // It does not use Modal::builder because I have to replace value
        interaction.modal(modal.title, modal.customId) {
            actionRow {
                textInput(TextInputStyle.Paragraph, "BuildCompetition-modal-input", "Description") {
                    allowedLength = 3..3000
                    placeholder = "Enter description of your build here!"
                    value = interaction.message.embeds[1].description
                }
            }
        }
    }
}