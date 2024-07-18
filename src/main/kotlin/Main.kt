import dev.kord.common.entity.InteractionType
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.ChatInputCommandInteraction
import dev.kord.core.event.interaction.ChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent


suspend fun cmdRegister(interaction: ChatInputCommandInteraction) {


    interaction.respondEphemeral {
        content = "Submission channel created successfully!"
    }
}
suspend fun main() {

    val kord = Kord("token");

    kord.on<MessageCreateEvent> {
        // ignore other bots
        if (message.author?.isBot == true) return@on;

        message.channel.createMessage(message.content);
    }

    kord.on<ChatInputCommandInteractionCreateEvent> {
        if (interaction.type == InteractionType.ApplicationCommand) {
            when (interaction.invokedCommandName) {
                "register" -> cmdRegister(interaction)
            }
        }
    }


    kord.login {
        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent
    };
}