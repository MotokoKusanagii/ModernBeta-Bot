import interfaces.Button
import interfaces.Command
import interfaces.Modal
import interfaces.GuildUserSelect

class InteractionDistributor() {
    var buttons: MutableMap<String, Button> = mutableMapOf()
        get() = field

    fun addButton(button: Button) {
        println("Register button ${button.customId}")
        buttons[button.customId] = button
    }

    fun removeButton(id: String) {
        println("Remove button $id")
        buttons.remove(id)
    }

    var cmd: MutableMap<String, Command> = mutableMapOf()
        get() = field

    fun addCommand(command: Command) {
        println("Register command ${command.name}")
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

class Context() {
    var distributor = InteractionDistributor()
}