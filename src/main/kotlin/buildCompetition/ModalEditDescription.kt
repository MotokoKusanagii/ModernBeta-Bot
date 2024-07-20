package buildCompetition

import Context
import Modal
import deleteDelay
import dev.kord.core.Kord
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.builder.message.embed

class ModalEditDescription(
    override val kord: Kord,
    override val ctx: Context,
    override val title: String = "Enter description",
    override val customId: String = "BuildCompetition-description-modal",
    override val builder: ModalBuilder.() -> Unit = {}
) : Modal {
    override suspend fun onSubmit(interaction: ModalSubmitInteraction) {
        interaction.message?.edit {
            embed {
                title = interaction.message!!.embeds[0].title
                color = interaction.message!!.embeds[0].color
                description = interaction.message!!.embeds[0].description
            }
            embed {
                title = interaction.message!!.embeds[1].title
                color = interaction.message!!.embeds[1].color
                description = interaction.textInputs["BuildCompetition-modal-input"]?.value
            }
        }

        val msg = interaction.respondEphemeral {
            content = "Edited description successfully!"
        }
        msg.deleteDelay(3000)
    }
}