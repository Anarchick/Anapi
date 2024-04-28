package fr.anarchick.anapi.bukkit.commands;

import fr.anarchick.anapi.MainBukkit;
import fr.anarchick.anapi.bukkit.PaperComponentUtils;
import fr.anarchick.anapi.bukkit.softdepend.PlaceHolderAPIUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public abstract class Commands implements CommandExecutor, Listener {

	final protected static String NO_PERMISSION_FR = "<red>Tu n'as pas la permission de faire celà";
	final protected static String NO_PERMISSION_EN = "<red>You don't have permission to do that";
	final protected static String EMPTY = "";

	protected final List<String> ARGUMENTS = new ArrayList<>();
	
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

	protected int getArgCount() {
		return (this.args != null ) ? this.args.length : 0;
	}

	/**
	 * Returns full the command executed by the sender.
	 * @return the full command including the slash
	 */
	protected String getOriginalCommand() {
		return String.format("/%s %s", this.label, String.join(" ", this.args));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
	public void onPluginTabComplete(final TabCompleteEvent.PluginTabCompleteEvent event) {
		if (event.getExecutor() == this && this instanceof Completable completable) completable.onTabComplete(event);
	}

	/**
	 * Send a message using MiniMessageFormat and PlaceHolderAPI if available
	 * @param sender the console or player
	 * @param messages the unformatted messages
	 */
	protected void sendMessage(CommandSender sender, String... messages) {
		for (String message : messages) {
			message = PlaceHolderAPIUtils.setPlaceholders(sender, message);
			sender.sendMessage(PaperComponentUtils.getMiniMessageTextComponent(message));
		}
	}
	
}
