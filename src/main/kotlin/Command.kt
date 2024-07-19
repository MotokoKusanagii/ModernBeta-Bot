import dev.kord.core.Kord
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import kotlinx.coroutines.flow.onEach

interface Command {
    val kord: Kord
    val name: String
    val description: String
    val builder: GlobalChatInputCreateBuilder.() -> Unit

    suspend fun onCallGuild(interaction: GuildChatInputCommandInteraction)

    suspend fun register() {
        kord.createGlobalChatInputCommand(name = name, description = description, builder = builder)
    }

    fun makePair(): Pair<String, Command> {
        return Pair<String, Command>(name, this)
    }
}


suspend fun unregisterAll(kord: Kord) {
    val commands = kord.getGlobalApplicationCommands()
    commands.onEach { command ->
        println("Deleting command: ${command.name} (hopefully)")
        command.delete()
    }
}

