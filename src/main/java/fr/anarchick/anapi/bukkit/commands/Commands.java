package fr.anarchick.anapi.bukkit.commands;

import fr.anarchick.anapi.MainBukkit;
import fr.anarchick.anapi.bukkit.BukkitUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public abstract class Commands implements CommandExecutor, Listener {

	final protected static String NO_PERMISISON_FR = BukkitUtils.colored("&cTu n'as pas la permissions de faire celà");
	final protected static String EMPTY = "";
	
	private String[] args;
	protected CommandSender sender;
	protected Command command;
	protected String label;

	public Commands() {
		MainBukkit.registerEvents(this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		this.args = args;
		this.sender = sender;
		this.command = command;
		this.label = label;
		return onCommand();
	}

	protected abstract boolean onCommand();

	@Nonnull
	protected String getArg(int i) {
		return (this.args != null && this.args.length > i) ? this.args[i] : EMPTY;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPluginTabComplete(final TabCompleteEvent.PluginTabCompleteEvent event) {
		if (event.getExecutor() == this && this instanceof Completable completable) completable.onTabComplete(event);
	}
	
}
