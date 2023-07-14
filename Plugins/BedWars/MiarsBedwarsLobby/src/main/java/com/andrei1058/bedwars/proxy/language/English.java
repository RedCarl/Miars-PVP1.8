package com.andrei1058.bedwars.proxy.language;

import com.andrei1058.bedwars.proxy.BedWarsProxy;
import com.andrei1058.bedwars.proxy.api.Messages;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Arrays;

public class English extends Language {
    public English() {
        super(BedWarsProxy.getPlugin(), "en");

        YamlConfiguration yml = getYml();
        yml.options().copyDefaults(true);
        yml.addDefault(Messages.PREFIX, "");
        yml.addDefault("name", "English");
        yml.addDefault(Messages.FORMATTING_STATS_DATE_FORMAT, "yyyy/MM/dd HH:mm");
        yml.addDefault(Messages.ARENA_STATUS_OFFLINE_NAME, "&8Offline");
        yml.addDefault(Messages.ARENA_STATUS_PLAYING_NAME, "&cPlaying");
        yml.addDefault(Messages.ARENA_STATUS_RESTARTING_NAME, "&4Restarting");
        yml.addDefault(Messages.ARENA_STATUS_WAITING_NAME, "&2Waiting");
        yml.addDefault(Messages.ARENA_STATUS_STARTING_NAME, "&6Starting");
        yml.addDefault(Messages.ARENA_GUI_INV_NAME, "&8Click to join");
        yml.addDefault(Messages.ARENA_GUI_ARENA_CONTENT_NAME, "&a&l{name}");
        yml.addDefault(Messages.ARENA_GUI_ARENA_CONTENT_LORE, Arrays.asList("", "&7Status: {status}", "&7Players: &f{on}&7/&f{max}", "&7Type: &a{group}", "", "&aLeft-Click to join.", "&eRight-Click to spectate."));

        yml.addDefault(Messages.ARENA_JOIN_DENIED_SELECTOR, "{prefix}&cSorry but you can't join this arena at this moment. Use Right-Click to spectate!");
        yml.addDefault(Messages.ARENA_SPECTATE_DENIED_SELECTOR, "{prefix}&cSorry but you can't spectate this arena at this moment. Use Left-Click to join!");

        yml.addDefault(Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS, "{prefix}&cCommand not found or you don't have permission!");
        yml.addDefault(Messages.COMMAND_GUI_DISPLAY, "&8- &f/bw gui &o(group) &eopens the arena selector.");
        yml.addDefault(Messages.COMMAND_GUI_HOVER, "&fChoose an arena\n&fand play.");
        yml.addDefault(Messages.COMMAND_LANGUAGE_DISPLAY, "&8- &f/bw language &o<iso> &echange language.");
        yml.addDefault(Messages.COMMAND_LANGUAGE_HOVER, "&fChange your language.");
        yml.addDefault(Messages.COMMAND_REJOIN_DISPLAY, "&8- &f/bw rejoin &ereconnect to a game.");
        yml.addDefault(Messages.COMMAND_REJOIN_HOVER, "&fReconnect to the game from where you got disconnected.");
        yml.addDefault(Messages.COMMAND_TP_DISPLAY, "&8- &f/bw tp <name> &eteleport to a player as spectator.");
        yml.addDefault(Messages.COMMAND_TP_HOVER, "&fSpectate on a player. Used for moderators to check hackers.");

        yml.addDefault(Messages.COMMAND_LANG_LIST_HEADER, "{prefix} &2Available languages:");
        yml.addDefault(Messages.COMMAND_LANG_LIST_FORMAT, "&a▪  &7{iso} - &f{name}");
        yml.addDefault(Messages.COMMAND_LANG_USAGE, "{prefix}&7Usage: /lang &f&o<iso>");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_NOT_EXIST, "{prefix}&cThis language doesn't exist!");
        yml.addDefault(Messages.COMMAND_LANG_SELECTED_SUCCESSFULLY, "{prefix}&aLanguage changed!");
        yml.addDefault(Messages.COMMAND_JOIN_USAGE, "&a▪ &7Usage: /bw join &o<arena/group>");
        yml.addDefault(Messages.COMMAND_JOIN_GROUP_OR_ARENA_NOT_FOUND, "{prefix}&cThere isn't any arena or arena group called: {name}");
        yml.addDefault(Messages.COMMAND_JOIN_DENIED_IS_FULL, "{prefix}&cThis arena is full!\n&aPlease consider donating for more features. &7&o(click)");
        yml.addDefault(Messages.COMMAND_JOIN_NO_EMPTY_FOUND, "{prefix}&cThere isn't any arena available right now ;(");
        yml.addDefault(Messages.COMMAND_JOIN_DENIED_PARTY_TOO_BIG, "{prefix}&cYour party is too big for joining this arena as a team :(");
        yml.addDefault(Messages.COMMAND_JOIN_DENIED_NOT_PARTY_LEADER, "{prefix}&cOnly the leader can choose the arena.");
        yml.addDefault(Messages.COMMAND_JOIN_SPECTATOR_DENIED_MSG, "&cSpectators are not allowed in this arena!");
        yml.addDefault(Messages.REJOIN_NO_ARENA, "{prefix}&cThere is no arena to rejoin!");
        yml.addDefault(Messages.REJOIN_DENIED, "{prefix}&cYou can't rejoin the arena anymore. Game ended or bed destroyed.");
        yml.addDefault(Messages.REJOIN_ALLOWED, "{prefix}&eJoining arena &a{arena}&e!");
        yml.addDefault(Messages.COMMAND_NOT_FOUND_OR_INSUFF_PERMS, "{prefix}&cCommand not found or you don't have permission!");
        yml.addDefault(Messages.COMMAND_PARTY_HELP, Arrays.asList("&6▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬",
                "&aParty Commands:", "&e/party invite <player> &7- &bInvites the player to your party",
                "&e/party leave &7- &bLeaves the current party",
                "&e/party remove <player> &7- &bRemove the player from the party",
                "&e/party accept <player> &7- &bAccept a party invite", "&e/party disband &7- &bDisbands the party"));
        yml.addDefault(Messages.COMMAND_PARTY_INVITE_USAGE, "{prefix}&eUsage: &7/party invite <player>");
        yml.addDefault(Messages.COMMAND_PARTY_INVITE_DENIED_PLAYER_OFFLINE, "{prefix}&7{player} &eis not online!");
        yml.addDefault(Messages.COMMAND_PARTY_INVITE_SENT, "{prefix}&eInvite sent to &7{player}&6.");
        yml.addDefault(Messages.COMMAND_PARTY_INVITE_SENT_TARGET_RECEIVE_MSG, "{prefix}&b{player} &ehas invited you to a party! &o&7(Click to accept)");
        yml.addDefault(Messages.COMMAND_PARTY_INVITE_DENIED_CANNOT_INVITE_YOURSELF, "{prefix}&cYou cannot invite yourself!");
        yml.addDefault(Messages.COMMAND_PARTY_INVITE_DENIED_PLAYER_OFFLINE, "{prefix}&7{player} &eis offline!");
        yml.addDefault(Messages.COMMAND_PARTY_ACCEPT_DENIED_NO_INVITE, "{prefix}&cThere's no party requests to accept");
        yml.addDefault(Messages.COMMAND_PARTY_ACCEPT_DENIED_ALREADY_IN_PARTY, "{prefix}&eYou're already in a party!");
        yml.addDefault(Messages.COMMAND_PARTY_INSUFFICIENT_PERMISSIONS, "{prefix}&cOnly the party owner can do this!");
        yml.addDefault(Messages.COMMAND_PARTY_ACCEPT_USAGE, "{prefix}&eUsage: &7/party accept <player>");
        yml.addDefault(Messages.COMMAND_PARTY_ACCEPT_SUCCESS, "{prefix}&7{player} &ehas joined the party!");
        yml.addDefault(Messages.COMMAND_PARTY_GENERAL_DENIED_NOT_IN_PARTY, "{prefix}&cYou're not in a party!");
        yml.addDefault(Messages.COMMAND_PARTY_LEAVE_DENIED_IS_OWNER_NEEDS_DISBAND, "{prefix}&cYou can't leave your own party!\n&eTry using: &b/party disband");
        yml.addDefault(Messages.COMMAND_PARTY_LEAVE_SUCCESS, "{prefix}&7{player} &ehas leaved the party!");
        yml.addDefault(Messages.COMMAND_PARTY_DISBAND_SUCCESS, "{prefix}&eParty disbanded!");
        yml.addDefault(Messages.COMMAND_PARTY_REMOVE_USAGE, "{prefix}&7Usage: &e/party remove <player>");
        yml.addDefault(Messages.COMMAND_PARTY_REMOVE_SUCCESS, "{prefix}&7{player} &ewas removed from the party,");
        yml.addDefault(Messages.COMMAND_PARTY_REMOVE_DENIED_TARGET_NOT_PARTY_MEMBER, "{prefix}&7{player} &eis not in your party!");
        yml.addDefault(Messages.COMMAND_TP_USAGE, "{prefix}&7Usage: &e/bw tp <name>");
        yml.addDefault(Messages.COMMAND_TP_NOT_FOUND, "{prefix}&7{player}&e was not found in bed-wars arenas.");
        yml.addDefault(Messages.COMMAND_TP_FAIL2, "{prefix}&7{player}&e was found on &7{server}&e but you can't join at this time.");

        yml.addDefault(Messages.SIGN_DYNAMIC_WAITING, Arrays.asList("&4&l[BedWars-{id}]", "&9{group}", "&1{map}", "&5{current}/{max}"));
        yml.addDefault(Messages.SIGN_DYNAMIC_STARTING, Arrays.asList("&4&l[BedWars-{id}]", "&9{group}", "&1{map}", "&5{current}/{max}"));
        yml.addDefault(Messages.SIGN_DYNAMIC_SEARCHING, Arrays.asList("&4▆▆▆▆▆▆", "&1&lBOOTING", "", "&4▆▆▆▆▆▆"));
        yml.addDefault(Messages.SIGN_DYNAMIC_NO_GAMES, Arrays.asList("", "&8&lWaiting for", "&7&lopen lobby", ""));

        yml.addDefault(Messages.SIGN_STATIC_WAITING, Arrays.asList("&4&l[BedWars-{id}]", "&9{group}", "&1{map}", "&5{current}/{max}"));
        yml.addDefault(Messages.SIGN_STATIC_STARTING, Arrays.asList("&4&l[BedWars-{id}]", "&9{group}", "&1{map}", "&5{current}/{max}"));
        yml.addDefault(Messages.SIGN_STATIC_PLAYING, Arrays.asList("&4&l[BedWars-{id}]", "&9{group}", "&1{map}", "{status}"));
        yml.addDefault(Messages.SIGN_STATIC_SEARCHING, Arrays.asList("&4▆▆▆▆▆▆", "&1&lBOOTING", "", "&4▆▆▆▆▆▆"));
        yml.addDefault(Messages.SIGN_STATIC_NO_GAMES, Arrays.asList("", "&8&lWaiting for", "&7&lopen lobby", ""));
        save();
        setPrefix(getMsg(Messages.PREFIX));
    }
}
