import dev.kord.core.behavior.interaction.response.EphemeralMessageInteractionResponseBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun EphemeralMessageInteractionResponseBehavior.deleteDelay(millis: Long) {
    this.kord.launch {
        delay(millis)
        this@deleteDelay.delete()
    }
}