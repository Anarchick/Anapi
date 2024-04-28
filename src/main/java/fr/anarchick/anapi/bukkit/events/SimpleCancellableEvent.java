package fr.anarchick.anapi.bukkit.events;

import org.bukkit.event.Cancellable;

@SuppressWarnings("unused")
public abstract class SimpleCancellableEvent extends SimpleEvent implements Cancellable {

    public SimpleCancellableEvent(boolean async) {
        super(async);
    }

    private boolean isCancelled;
    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

}
