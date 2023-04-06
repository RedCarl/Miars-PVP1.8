package io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.cmds;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.fanciful.FancyMessage;
import io.github.Leonardo0013YT.UltraSkyWars.modules.SpecialItems.InjectionSpecialItems;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpecialItemsCMD implements CommandExecutor {

    private final UltraSkyWars plugin;
    private final InjectionSpecialItems isi;

    public SpecialItemsCMD(UltraSkyWars plugin, InjectionSpecialItems isi) {
        this.plugin = plugin;
        this.isi = isi;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.hasPermission("usw.admin")) {
                p.sendMessage(plugin.getLang().get(p, "messages.noPermission"));
                return true;
            }
            if (args.length < 1) {
                sendHelp(sender);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "getitem":
                    if (args.length < 2) {
                        p.sendMessage("§eAvailable Items:");
                        p.sendMessage("§7 - §aCompass");
                        p.sendMessage("§7 - §aInstantTNT");
                        p.sendMessage("§7 - §aSoup");
                        p.sendMessage("§7 - §aEndBuff");
                        p.sendMessage("§7 - §aTNTLaunch");
                        return true;
                    }
                    switch (args[1].toLowerCase()) {
                        case "compass":
                            p.getInventory().addItem(isi.getIm().getCompass());
                            break;
                        case "instanttnt":
                            p.getInventory().addItem(isi.getIm().getInstantTNT());
                            break;
                        case "soup":
                            p.getInventory().addItem(isi.getIm().getSoup());
                            break;
                        case "endbuff":
                            p.getInventory().addItem(isi.getIm().getEndBuff());
                            break;
                        case "tntlaunch":
                            p.getInventory().addItem(isi.getIm().getTNTLaunch());
                            break;
                    }
                    break;
            }
        }
        return false;
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage("§7§m---------------§r   §6§lUltraSkyWars §ev" + plugin.getDescription().getVersion() + "§r   §7§m---------------");
        s.sendMessage("§7       §7");
        new FancyMessage("§b/swi getitem <name> §7- §eGet the item.").setHover(HoverEvent.Action.SHOW_TEXT, "§fClick to execute!").setClick(ClickEvent.Action.SUGGEST_COMMAND, "/swi getitem ").build().send(s);
        s.sendMessage("§7§m----------------------------------------------------");
    }

}