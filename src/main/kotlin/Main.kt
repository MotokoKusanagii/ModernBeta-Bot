import commands.Ping
import commands.Register
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.EphemeralMessageInteractionResponseBehavior
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.embed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

fun EphemeralMessageInteractionResponseBehavior.deleteDelay(millis: Long) {
    this.kord.launch {
        delay(millis)
        this@deleteDelay.delete()
    }
}

suspend fun buttonRegisterAbort(interaction: ButtonInteraction) {
    interaction.deferEphemeralResponse()
    interaction.channel.delete("Submission ${interaction.channel.asChannel().data.name} was aborted!")
}

suspend fun buttonRegisterDescription(interaction: ButtonInteraction) {
    interaction.modal("Enter description", "register-modal") {
        actionRow {
            textInput(TextInputStyle.Paragraph, "description", "Description") {
                allowedLength = 3..3000
                placeholder = "Enter description about your build here"
                value = interaction.message.embeds[1].description
            }
        }
    }
}

suspend fun modalRegister(interaction: ModalSubmitInteraction) {
    interaction.message?.edit {
        embed {
            title = interaction.message!!.embeds[0].title
            color = interaction.message!!.embeds[0].color
            description = interaction.message!!.embeds[0].description
        }
        embed {
            title = interaction.message!!.embeds[1].title
            color = interaction.message!!.embeds[1].color
            description = interaction.textInputs["description"]?.value
        }
    }

    val msg = interaction.respondEphemeral {
        content = "Edited description successfully!"
    }

    msg.deleteDelay(3000)
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

    val commands: Map<String, Command> = mapOf(
        Ping(kord, "ping", "Returns Pong!"){}.makePair(),
        Register(kord, "register", "Register a build for the competition!") {
            string("title", "The title of your submission!") {
                required = true
                maxLength = 50
            }
        }.makePair()
    )

    // Delete global commands
    if (config.unregisterCommands) {
        unregisterAll(kord)
        println("Deleted all commands")
        if (config.returnAfterUnregister) {
            return
        }
    }

    // Register commands
    if (config.registerCommands) {
        for (command in commands) {
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
            commands[interaction.invokedCommandName]?.callGuild(interaction)
        }
    }

    kord.on<ButtonInteractionCreateEvent> {
        when (interaction.componentId) {
            "button/register/abort" -> buttonRegisterAbort(interaction)
            "button/register/edit description" -> buttonRegisterDescription(interaction)
        }
    }

    kord.on<ModalSubmitInteractionCreateEvent> {
        when (interaction.modalId) {
            "register-modal" -> modalRegister(interaction)
        }
    }


    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    }
}