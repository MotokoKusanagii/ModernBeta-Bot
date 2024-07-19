package commands

import Command
import deleteDelay
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed

class Register(override val kord: Kord, override val name: String, override val description: String,
               override val builder: GlobalChatInputCreateBuilder.() -> Unit) :  Command {
    override suspend fun callGuild(interaction: GuildChatInputCommandInteraction) {
        val response = interaction.deferEphemeralResponse()

        val parent = 1263077357619777638 // This has to be later retrieved from the settings table
        val guild = interaction.guild.asGuild()
        val everyone = guild.getEveryoneRole()
        val submissionTitle = interaction.command.strings["title"]!!

        val channel = guild.createTextChannel("${submissionTitle}-submission") {
            parentId = Snowflake(parent)
            reason = "User: ${interaction.user.username} created submission"
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
        }

        channel.createMessage{
            embed {
                title = "$submissionTitle submission editor"
                color = dev.kord.common.Color(0x18ebeb)
                description = "**Things you can do here before submitting**\n" +
                              "- Create a description"
            }
            embed {
                title = submissionTitle
                color = dev.kord.common.Color(0x18ebeb)
                description = "Create your description here"
            }

            actionRow {
                interactionButton(ButtonStyle.Primary, "button/register/edit description") {
                    label = "Edit Description"
                }
                interactionButton(ButtonStyle.Danger, "button/register/abort") {
                    label = "Abort"
                }
            }

            val message = response.respond {
                content = "Created ${channel.mention} successfully!"
            }

            message.deleteDelay(3000)
        }
    }
}