package commands

import Context
import dev.kord.core.Kord
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.attachment
import interfaces.Command
import interfaces.CommandType

class FileCMD(
    override val kord: Kord,
    override val ctx: Context,
    override val name: String = "file",
    override val description: String = "Upload files test",
    override val type: CommandType = CommandType.GUILD,
    override val builder: ChatInputCreateBuilder.() -> Unit = {
        attachment("test-file", "File to upload") {
            required = true
        }
    }
) : Command {
    override suspend fun onCallGuild(interaction: GuildChatInputCommandInteraction) {
        val file = interaction.command.attachments["test-file"]!!
        if (!file.isImage) {
            return
        }

        println("${file.width} x ${file.height}")

        interaction.respondPublic {
            content = file.url
        }

    }
}