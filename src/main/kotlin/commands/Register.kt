package commands

import Command
import Context
import deleteDelay
import dev.kord.common.entity.*
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.createTextChannel
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.entity.interaction.GuildChatInputCommandInteraction
import dev.kord.rest.builder.interaction.GlobalChatInputCreateBuilder
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed

class Register(
    override val kord: Kord,
    override val ctx: Context,
    override val name: String = "register",
    override val description: String = "Register a build for the competition!",
    override val builder: GlobalChatInputCreateBuilder.() -> Unit = {
        string("title", "The title of your submission!") {
            required = true
            maxLength = 50
        }
    }
) :  Command {
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

        val message = channel.createMessage{
            embed {
                title = "$submissionTitle submission editor"
                color = dev.kord.common.Color(0x18ebeb)
                description = "**Things you can do here before submitting**\n" +
                              "- Create a description\n" +
                              "- TODO: Do some magic to make files work (sorry future Meiko)"
            }
            embed {
                title = submissionTitle
                color = dev.kord.common.Color(0x18ebeb)
                description = "Create your description here"
                field {
                    name = "Coordinates"
                    value = "Not provided!"
                }
            }

            actionRow {
                interactionButton(edit.style, edit.customId, edit.builder)
                interactionButton(coords.style, coords.customId, coords.builder)
                interactionButton(abort.style, abort.customId, abort.builder)
            }
        }

        val confirmation = response.respond {
            content = "Created ${channel.mention} successfully!"
        }
        confirmation.deleteDelay(3000)
    }
}