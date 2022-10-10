package fr.anarchick.anapi.bukkit.commands;

public interface Completable {

    /**
     * Called when this command is completable
     * @param event
     */
    void onTabComplete(TabCompleteEvent.PluginTabCompleteEvent event);

}
