package buildCompetition

import Context
import interfaces.Modal
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.updatePublicMessage
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
        interaction.updatePublicMessage {
            embed {
                title = interaction.message!!.embeds[0].title
                color = interaction.message!!.embeds[0].color
                description = interaction.message!!.embeds[0].description
            }
            embed {
                title = interaction.message!!.embeds[1].title
                color = interaction.message!!.embeds[1].color
                description = interaction.textInputs["BuildCompetition-description-modal-input"]?.value
                field {
                    name = interaction.message!!.embeds[1].fields[0].name
                    value = interaction.message!!.embeds[1].fields[0].value
                }
                field {
                    name = interaction.message!!.embeds[1].fields[1].name
                    value = interaction.message!!.embeds[1].fields[1].value
                }
            }
        }
    }
}