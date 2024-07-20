import dev.kord.core.Kord
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.rest.builder.interaction.ModalBuilder

interface Modal {
    val kord: Kord
    val ctx: Context
    val title: String
    val customId: String
    val builder: ModalBuilder.() -> Unit

    suspend fun onSubmit(interaction: ModalSubmitInteraction)
}