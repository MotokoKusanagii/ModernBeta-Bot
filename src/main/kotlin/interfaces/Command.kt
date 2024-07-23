package interfaces

import Context
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder

enum class CommandType {
    GUILD, GLOBAL
}

interface Command {
    val kord: Kord
    val ctx: Context
    val name: String
    val description: String
    val type: CommandType
    val builder: ChatInputCreateBuilder.() -> Unit

    suspend fun onCallGuild(interaction: GuildChatInputCommandInteraction)

    suspend fun register() {
        //kord.createGlobalChatInputCommand(name = name, description = description, builder = builder)
//        kord.createGlobalApplicationCommands{
//            input(name, description, builder = builder)
//        }
        kord.createGuildApplicationCommands(Snowflake(1263077356818661387)) {
            input(name, description, builder = builder)
        }
    }

    fun makePair(): Pair<String, Command> {
        return Pair<String, Command>(name, this)
    }
}
