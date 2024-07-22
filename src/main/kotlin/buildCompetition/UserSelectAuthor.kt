package buildCompetition

import Context
import dev.kord.common.entity.OverwriteType
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Permissions
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.TextChannelBehavior
import dev.kord.core.behavior.interaction.updatePublicMessage
import dev.kord.core.cache.data.PermissionOverwriteData
import dev.kord.core.entity.PermissionOverwrite
import dev.kord.core.entity.interaction.GuildSelectMenuInteraction
import dev.kord.rest.builder.component.UserSelectBuilder
import dev.kord.rest.builder.message.embed
import interfaces.GuildUserSelect

class UserSelectAuthor(
    override val kord: Kord,
    override val ctx: Context,
    override val customId: String = "BuildCompetition-author-select",
    override val builder: UserSelectBuilder.() -> Unit = {}
) : GuildUserSelect {
    override suspend fun onSelect(interaction: GuildSelectMenuInteraction) {
        val channel = TextChannelBehavior(interaction.guildId, interaction.channelId, kord)
        var authors = ""


        interaction.values.forEach { value ->
            authors += "<@$value> "
            channel.addOverwrite(
                PermissionOverwrite(
                    PermissionOverwriteData(
                        Snowflake(value),
                        OverwriteType.Member,
                        Permissions {
                            + Permission.ViewChannel
                        },
                        Permissions {}
                    )
                ),
                "Added author to: ${interaction.channel.asChannel().name}"
            )
        }


        interaction.updatePublicMessage {
            embed {
                title = interaction.message.embeds[0].title
                color = interaction.message.embeds[0].color
                description = interaction.message.embeds[0].description
            }
            embed {
                title = interaction.message.embeds[1].title
                color = interaction.message.embeds[1].color
                description = interaction.message.embeds[1].description
                field {
                    name = interaction.message.embeds[1].fields[0].name
                    value = interaction.message.embeds[1].fields[0].value
                }
                field {
                    name = interaction.message.embeds[1].fields[1].name
                    value = authors
                }
            }
        }
    }
}