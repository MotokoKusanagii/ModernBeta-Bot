package buildCompetition

import Context
import interfaces.Modal
import dev.kord.common.entity.TextInputStyle
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.updatePublicMessage
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.rest.builder.interaction.ModalBuilder
import dev.kord.rest.builder.message.embed
import java.util.*

class ModalCoordinates(
    override val kord: Kord,
    override val ctx: Context,
    override val title: String = "Coordinates",
    override val customId: String = "BuildCompetition-coords-modal",
    override val builder: ModalBuilder.() -> Unit = {
        actionRow {
            textInput(TextInputStyle.Short, "BuildCompetition-coords-modal-input-X", "X Coordinate") {
                allowedLength = 0..200
                placeholder = "x"
            }
        }
        actionRow {
            textInput(TextInputStyle.Short, "BuildCompetition-coords-modal-input-Y", "Y Coordinate") {
                allowedLength = 0..200
                placeholder = "y"
            }
        }
        actionRow {
            textInput(TextInputStyle.Short, "BuildCompetition-coords-modal-input-Z", "Z Coordinate") {
                allowedLength = 0..200
                placeholder = "z"
            }
        }
    }
) : Modal {
    override suspend fun onSubmit(interaction: ModalSubmitInteraction) {
        val x = interaction.textInputs["BuildCompetition-coords-modal-input-X"]?.value?.toIntOrNull() ?: 0
        val y = interaction.textInputs["BuildCompetition-coords-modal-input-Y"]?.value?.toIntOrNull() ?: 0
        val z = interaction.textInputs["BuildCompetition-coords-modal-input-Z"]?.value?.toIntOrNull() ?: 0
        interaction.updatePublicMessage {
            embed {
                title = interaction.message!!.embeds[0].title
                color = interaction.message!!.embeds[0].color
                description = interaction.message!!.embeds[0].description
            }
            embed {
                title = interaction.message!!.embeds[1].title
                color = interaction.message!!.embeds[1].color
                description = interaction.message!!.embeds[1].description
                field {
                    name = interaction.message!!.embeds[1].fields[0].name
                    value = "X: **%,d** Y: **%,d** Z: **%,d**".format(Locale.GERMANY, x, y, z)
                }
                field {
                    name = interaction.message!!.embeds[1].fields[1].name
                    value = interaction.message!!.embeds[1].fields[1].value
                }
            }
        }
    }
}