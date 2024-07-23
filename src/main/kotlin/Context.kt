import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import interfaces.*
import kotlinx.coroutines.flow.onEach

class InteractionDistributor() {
    var buttons: MutableMap<String, Button> = mutableMapOf()
        get() = field

    fun addButton(button: Button) {
        println("Adding button ${button.customId}")
        buttons[button.customId] = button
    }

    fun removeButton(id: String) {
        println("Remove button $id")
        buttons.remove(id)
    }

    var cmd: MutableMap<String, Command> = mutableMapOf()
        get() = field

    fun addCommand(command: Command) {
        println("Adding command ${command.name}")
        cmd[command.name] = command
    }

    fun removeCommand(name: String) {
        println("Remove command $name")
        cmd.remove(name)
    }

    var modals: MutableMap<String, Modal> = mutableMapOf()
        get() = field

    fun addModal(modal: Modal) {
        modals[modal.customId] = modal
    }

    fun removeModal(id: String) {
        modals.remove(id)
    }

    var guildUserMenus: MutableMap<String, GuildUserSelect> = mutableMapOf()
        get() = field

    fun addGuildUserMenu(menu: GuildUserSelect) {
        guildUserMenus[menu.customId] = menu
    }

    fun removeGuildUserMenu(id: String) {
        guildUserMenus.remove(id)
    }

}

class Context(val kord: Kord, val applicationId: Snowflake) {
    val testGuildId = Snowflake(1263077356818661387)
    var distributor = InteractionDistributor()

    suspend fun registerCommands() {
        kord.createGlobalApplicationCommands {
            distributor.cmd.forEach { (key, value) ->
                if (value.type == CommandType.GLOBAL) {
                    println("Registering global command $key")
                    input(value.name, value.description, value.builder)
                }
            }
        }

        kord.createGuildApplicationCommands (testGuildId) {
            distributor.cmd.forEach { (key, value) ->
                if (value.type == CommandType.GUILD) {
                    println("Registering guild command $key")
                    input(value.name, value.description, value.builder)
                }
            }
        }
    }
    fun deleteGlobalCommands() {
        val commands = kord.getGlobalApplicationCommands()
        commands.onEach { command ->
            println("Deleting global command ${command.name}")
            command.delete()
        }
    }
    fun deleteGuildCommands() {
        val commands = kord.getGuildApplicationCommands(testGuildId)
        commands.onEach { command ->
            println("Deleting guild command ${command.name}")
            command.delete()
        }
    }
}