import buildCompetition.*
import commands.Ping
import commands.Register
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.*
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File

@Serializable
data class ConfigJson(
    var token: String,
    val registerCommands: Boolean,
    val unregisterCommands: Boolean,
    val returnAfterRegister: Boolean,
    val returnAfterUnregister: Boolean,
)

suspend fun unregisterAll(kord: Kord) {
    val commands = kord.getGlobalApplicationCommands()
    commands.onEach { command ->
        println("Deleting command: ${command.name} (hopefully)")
        command.delete()
    }
}

fun parseConfig(): ConfigJson {
    val cwd = System.getProperty("user.dir")

    val token = File("$cwd/token").readText()
    val config = File("$cwd/config.json").readText()

    val j = Json.decodeFromString<ConfigJson>(config)
    j.token = token
    return j
}

suspend fun main() {

    val config = parseConfig()
    val kord = Kord(config.token)

    val ctx = Context()
    ctx.distributor.addCommand(Ping(kord, ctx))
    ctx.distributor.addCommand(Register(kord, ctx))

    ctx.distributor.addButton(ButtonAbort(kord, ctx))
    ctx.distributor.addButton(ButtonEditDescription(kord, ctx))
    ctx.distributor.addButton(ButtonCoordinates(kord, ctx))

    ctx.distributor.addModal(ModalEditDescription(kord, ctx))
    ctx.distributor.addModal(ModalCoordinates(kord, ctx))

    ctx.distributor.addGuildUserMenu(UserSelectAuthor(kord, ctx))

    // Delete global commandsSe
    if (config.unregisterCommands) {
        unregisterAll(kord)
        println("Deleted all commands")
        if (config.returnAfterUnregister) {
            return
        }
    }

    // Register commands
    if (config.registerCommands) {
        for (command in ctx.distributor.cmd) {
            command.value.register()
            println("Registered command: ${command.key}")
        }
        if (config.returnAfterRegister) {
            return
        }
    }

    kord.on<ReadyEvent> {
        println("Bot ${self.username} ready")
    }

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.type == InteractionType.ApplicationCommand) {
            ctx.distributor.cmd[interaction.invokedCommandName]?.onCallGuild(interaction)
        }
    }

    kord.on<ButtonInteractionCreateEvent> {
        println("Button ${interaction.componentId} clicked")
        ctx.distributor.buttons[interaction.componentId]?.onPress(interaction)
    }

    kord.on<ModalSubmitInteractionCreateEvent> {
        println("Modal ${interaction.modalId} submitted")
        ctx.distributor.modals[interaction.modalId]?.onSubmit(interaction)
    }

    kord.on<GuildSelectMenuInteractionCreateEvent> {
        println("Select Menu ${interaction.componentId}")
        ctx.distributor.guildUserMenus[interaction.componentId]?.onSelect(interaction)
    }

    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}