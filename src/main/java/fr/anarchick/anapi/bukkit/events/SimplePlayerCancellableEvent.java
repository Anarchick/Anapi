package fr.anarchick.anapi.bukkit.events;

import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public abstract class SimplePlayerCancellableEvent extends SimpleCancellableEvent {

    private final Player player;

    public SimplePlayerCancellableEvent(Player player, boolean async) {
        super(async);
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

}
