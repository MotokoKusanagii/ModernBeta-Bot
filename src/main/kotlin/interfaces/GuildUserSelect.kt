package interfaces

import Context
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GuildSelectMenuInteraction
import dev.kord.rest.builder.component.UserSelectBuilder

interface GuildUserSelect {
    val kord: Kord
    val ctx: Context
    val customId: String
    val builder: UserSelectBuilder.() -> Unit

    suspend fun onSelect(interaction: GuildSelectMenuInteraction)
}