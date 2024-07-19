package commands

import Command
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder

class Ping(override val kord: Kord, override val name: String, override val description: String,
           override val builder: GlobalChatInputCreateBuilder.() -> Unit
) : Command {
    override suspend fun callGuild(interaction: GuildChatInputCommandInteraction) {
        interaction.respondPublic {
            content = "Pong!"
        }
    }
}