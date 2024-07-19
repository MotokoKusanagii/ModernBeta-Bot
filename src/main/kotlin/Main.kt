import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.edit
import dev.kord.core.behavior.interaction.modal
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.behavior.interaction.updateEphemeralMessage
import dev.kord.core.entity.interaction.ButtonInteraction
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.core.entity.interaction.ModalSubmitInteraction
import dev.kord.core.event.interaction.ButtonInteractionCreateEvent
import dev.kord.core.event.interaction.GuildChatInputCommandInteractionCreateEvent
import dev.kord.core.event.interaction.ModalSubmitInteractionCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed


suspend fun cmdRegister(interaction: GuildChatInputCommandInteraction) {
    val response = interaction.deferEphemeralResponse()

    val parent = 1263077357619777638
    val guild = interaction.guild.asGuild()
    val everyone = guild.getEveryoneRole()
    val submission_title = interaction.command.strings["title"]!!


    val channel = guild.createTextChannel(name = "${submission_title}-submission", builder = {
        parentId = Snowflake(parent)
        reason = "User: ${interaction.user.username} created submission channel"
        permissionOverwrites = mutableSetOf(
            Overwrite(
                id = everyone.id,
                type = OverwriteType.Role,
                allow = Permissions{},
                deny = Permissions{
                    +Permission.ViewChannel
                }
            ),
            Overwrite(
                id = interaction.user.id,
                type = OverwriteType.Member,
                allow = Permissions{
                    +Permission.ViewChannel
                    +Permission.SendMessages
                    +Permission.ReadMessageHistory
                },
                deny = Permissions{}
            )
        )
    })

    channel.createMessage {
        embed {
            title = "$submission_title submission editor"
            color = dev.kord.common.Color(0x18ebeb)
            description = "**Things you can do here before submitting**\n" +
                                "- Create a description"
        }
    }

    channel.createMessage {
        embed {
            title = submission_title
            color = dev.kord.common.Color(0x18ebeb)
            description = "..."
        }

        actionRow {
            interactionButton(ButtonStyle.Primary, "register-description") {
                label = "Add Description"
            }
            interactionButton(ButtonStyle.Danger, "register-abort") {
                label = "Abort"
            }
        }
    }

    response.respond {
        content = "Created ${channel.mention} successfully!"
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
                value = interaction.message.embeds.first().description
            }
        }
    }
}

suspend fun modalRegister(interaction: ModalSubmitInteraction) {
    interaction.message?.edit {
        embed {
            title = interaction.message!!.embeds.first().title
            color = dev.kord.common.Color(0x18ebeb)
            description = interaction.textInputs["description"]?.value
        }
    }

    interaction.respondEphemeral {
        content = "Description added!"
    }
}

suspend fun main() {

    val kord = Kord("token")

    kord.on<GuildChatInputCommandInteractionCreateEvent> {
        if (interaction.type == InteractionType.ApplicationCommand) {
            when (interaction.invokedCommandName) {
                "register" -> cmdRegister(interaction)
            }
        }
    }

    kord.on<ButtonInteractionCreateEvent> {
        when (interaction.componentId) {
            "register-abort" -> buttonRegisterAbort(interaction)
            "register-description" -> buttonRegisterDescription(interaction)
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