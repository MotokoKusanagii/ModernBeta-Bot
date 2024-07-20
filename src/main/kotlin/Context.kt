class InteractionDistributor() {
    var buttons: MutableMap<String, Button> = mutableMapOf()
        get() = field
    var modals: MutableMap<String, Modal> = mutableMapOf()
        get() = field
    var cmd: MutableMap<String, Command> = mutableMapOf()
        get() = field

    fun addButton(button: Button) {
        println("Register button ${button.customId}")
        buttons[button.customId] = button
    }

    fun removeButton(id: String) {
        println("Remove button $id")
        buttons.remove(id)
    }

    fun addCommand(command: Command) {
        println("Register command ${command.name}")
        cmd[command.name] = command
    }

    fun removeCommand(name: String) {
        println("Remove command $name")
        cmd.remove(name)
    }

    fun addModal(modal: Modal) {
        modals[modal.customId] = modal
    }

    fun removeModal(id: String) {
        modals.remove(id)
    }
}

class Context() {
    var distributor = InteractionDistributor()
}