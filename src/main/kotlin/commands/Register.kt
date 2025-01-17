package commands

import interfaces.Command
import Context
import deleteDelay
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.ChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed
import interfaces.CommandType

class Register(
    override val kord: Kord,
    override val ctx: Context,
    override val name: String = "register",
    override val description: String = "Register a build for the competition!",
    override val type: CommandType = CommandType.GUILD,
    override val builder: ChatInputCreateBuilder.() -> Unit = {
        string("title", "The title of your submission!") {
            required = true
            maxLength = 50
        }
    }
) : Command {
    @OptIn(KordPreview::class)
    override suspend fun onCallGuild(interaction: GuildChatInputCommandInteraction) {
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


        val abort = ctx.distributor.buttons["BuildCompetition-abort"]!!
        val edit = ctx.distributor.buttons["BuildCompetition-edit-description"]!!
        val coords = ctx.distributor.buttons["BuildCompetition-coords"]!!
        val authors = ctx.distributor.guildUserMenus["BuildCompetition-author-select"]!!

        channel.createMessage{
            embed {
                title = "$submissionTitle submission editor"
                color = dev.kord.common.Color(0x18ebeb)
                description = "**Things you can do here before submitting**\n" +
                              "- Create a description\n" +
                              "- TODO: Do some magic to make files work (sorry future Meiko)\n" +
                              "- TODO: When selecting an author, give them permission to see the channel | Done\n" +
                              "- TDOO: Update the selection menu for everyone after editing | Wont fix (probably)\n" +
                              "- TODO: Unify the edit buttons into one form"
            }
            embed {
                title = submissionTitle
                color = dev.kord.common.Color(0x18ebeb)
                description = "Create your description here"
                field {
                    name = "Coordinates"
                    value = "Not provided!"
                }
                field {
                    name = "Authors"
                    value = interaction.user.mention
                }
            }

            actionRow {
                interactionButton(edit.style, edit.customId, edit.builder)
                interactionButton(coords.style, coords.customId, coords.builder)
                interactionButton(abort.style, abort.customId, abort.builder)
            }
            actionRow {
                userSelect(authors.customId) {
                    allowedValues = 1..20
                    defaultUsers.addFirst(interaction.user.id)
                }
            }
        }

        val confirmation = response.respond {
            content = "Created ${channel.mention} successfully!"
        }
        confirmation.deleteDelay(3000)
    }

}