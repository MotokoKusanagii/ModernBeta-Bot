package commands

import interfaces.Command
import Context
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder

class Ping(
    override val kord: Kord,
    override val ctx: Context,
    override val name: String = "ping",
    override val description: String = "Returns Pong!",
    override val builder: GlobalChatInputCreateBuilder.() -> Unit = {},
) : Command {
    override suspend fun onCallGuild(interaction: GuildChatInputCommandInteraction) {
        interaction.respondPublic {
            content = "https://imgur.com/PMrQZ8V"
        }
    }
}