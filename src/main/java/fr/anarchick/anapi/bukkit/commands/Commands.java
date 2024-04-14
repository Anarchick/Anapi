package fr.anarchick.anapi.bukkit.commands;

import fr.anarchick.anapi.MainBukkit;
import fr.anarchick.anapi.bukkit.BukkitUtils;
import fr.anarchick.anapi.bukkit.PaperComponentUtils;
import fr.anarchick.anapi.bukkit.softdepend.CharactersPlaceHolder;
import fr.anarchick.anapi.bukkit.softdepend.PlaceHolderAPIUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Example of how to setup USAGES, REGEX, EXAMPLES and DESCRIPTION :
 *
 * 		USAGES.put("create", "/file <file> create <overwrite>");
 * 		REGEX.put("create", Pattern.compile("\\/file (.*?) (create) (true|false)"));
 * 		EXAMPLES.put("create", "/file 'plugins/log.json' create false");
 * 		DESCRIPTION.put("create", "Create a new file at given path. True/false if you want to overwrite existing file.");
 *
 */
@SuppressWarnings("unused")
public abstract class Commands implements CommandExecutor, Listener {

	final protected static String NO_PERMISISON_FR = BukkitUtils.colored("&cTu n'as pas la permissions de faire celà");
	final protected static String EMPTY = "";

	protected final List<String> ARGUMENTS = new ArrayList<>();
	protected final HashMap<String, String> USAGES = new HashMap<>();
	protected final HashMap<String, Pattern> REGEX = new HashMap<>();
	protected final HashMap<String, String> EXAMPLES = new HashMap<>();
	protected final HashMap<String, String> DESCRIPTION = new HashMap<>();
	
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

	/**
	 * Send a message containing information about how the use of this command.
	 * Required to set the variables USAGES, DESCRIPTION and EXAMPLES.
	 *
	 * Support PlaceHolderAPI and use MiniMessage format
	 * @param sender the receiver
	 * @param id the id of USAGES, DESCRIPTION and EXAMPLES
	 * @return Always true;
	 */
	protected boolean info(CommandSender sender, String id) {
		if (USAGES.containsKey(id)) {
			sendMessage(this.sender, "<blue>Usage: <gray>" + USAGES.get(id));
		}
		if (DESCRIPTION.containsKey(id)) {
			sendMessage(this.sender, "<blue>Description: <gray>" + DESCRIPTION.get(id));
		}
		if (EXAMPLES.containsKey(id)) {
			sendMessage(this.sender, "<blue>Example: <gray>" + EXAMPLES.get(id));
		}
		return true;
	}

	/**
	 * Send all informations about the usage of this command.
	 * @param sender the receiver
	 * @return Always true.
	 */
	protected boolean info(CommandSender sender) {
		for (String id : USAGES.keySet()) {
			info(sender, id);
			sendMessage(sender, "");
		}
		return true;
	}
	
}
