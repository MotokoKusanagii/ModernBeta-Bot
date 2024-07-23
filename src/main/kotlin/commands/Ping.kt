package commands

import interfaces.Command
import Context
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import interfaces.CommandType

class Ping(
    override val kord: Kord,
    override val ctx: Context,
    override val name: String = "ping",
    override val description: String = "Returns Pong!",
    override val type: CommandType = CommandType.GUILD,
    override val builder: ChatInputCreateBuilder.() -> Unit = {},
) : Command {
    override suspend fun onCallGuild(interaction: GuildChatInputCommandInteraction) {
        interaction.respondPublic {
            content = "Pong!"
        }
    }
}