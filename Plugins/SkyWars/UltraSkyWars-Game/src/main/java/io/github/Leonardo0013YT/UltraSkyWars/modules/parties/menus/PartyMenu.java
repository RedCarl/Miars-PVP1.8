package io.github.Leonardo0013YT.UltraSkyWars.modules.parties.menus;

import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.InjectionParty;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories.parties.PartyMemberMenu;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories.parties.PartyPartiesMenu;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.party.Party;
import io.github.Leonardo0013YT.UltraSkyWars.utils.ItemBuilder;
import io.github.Leonardo0013YT.UltraSkyWars.utils.NBTEditor;
import io.github.Leonardo0013YT.UltraSkyWars.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PartyMenu {

    private final UltraSkyWars plugin;
    private final InjectionParty ip;

    public PartyMenu(UltraSkyWars plugin, InjectionParty ip) {
        this.plugin = plugin;
        this.ip = ip;
    }

    public void createMembersMenu(Player p, Party party) {
        PartyMemberMenu pmm = (PartyMemberMenu) plugin.getUim().getMenus("partymember");
        Inventory inv = Bukkit.createInventory(null, pmm.getRows() * 9, ip.getParties().get("menus.members.title"));
        Map<Integer, ItemStack> contents = pmm.getContents();
        pmm.getExtra().forEach(i -> inv.setItem(i, contents.get(i)));
        int i = 0;
        for (Map.Entry<UUID, String> entry : party.getMembers().entrySet()) {
            if (i >= pmm.getGameSlots().size()) {
                break;
            }
            if (i >= party.getMembers().size()) {
                break;
            }
            int slot = pmm.getGameSlots().get(i);
            ItemStack item = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, ip.getParties().get("menus.members.member.nameItem").replace("<name>", entry.getValue()), ip.getParties().get("menus.members.member.loreItem"), entry.getValue());
            inv.setItem(slot, NBTEditor.set(item, entry.getKey().toString() + ":" + entry.getValue(), "PARTY_MEMBER"));
            i++;
        }
        int slot = pmm.getSlot("{PMEMBERCLOSE}");
        int slot2 = pmm.getSlot("{PMEMBERDELETE}");
        if (slot > -1 && slot < 54) {
            inv.setItem(slot, contents.get(slot));
        }
        if (slot2 > -1 && slot2 < 54) {
            inv.setItem(slot2, contents.get(slot2));
        }
        p.openInventory(inv);
    }

    public void createPartiesMenu(Player p) {
        PartyPartiesMenu pmm = (PartyPartiesMenu) plugin.getUim().getMenus("partylist");
        Inventory inv = Bukkit.createInventory(null, pmm.getRows() * 9, ip.getParties().get("menus.parties.title"));
        Map<Integer, ItemStack> contents = pmm.getContents();
        pmm.getExtra().forEach(i -> inv.setItem(i, contents.get(i)));
        int i = 0;
        for (Party party : ip.getPam().getParties().values()) {
            if (i >= pmm.getGameSlots().size()) {
                break;
            }
            if (i >= ip.getPam().getParties().size()) {
                break;
            }
            int slot = pmm.getGameSlots().get(i);
            ItemStack item = ItemBuilder.skull(XMaterial.PLAYER_HEAD, 1, ip.getParties().get("menus.parties.party.nameItem").replace("<leader>", party.getLeaderName()), getLoreParty(party), party.getLeaderName());
            inv.setItem(slot, NBTEditor.set(item, party.getLeader().toString() + ":" + party.isPrivacy(), "PARTY_LEADER"));
            i++;
        }
        int slot = pmm.getSlot("{PARTIESCLOSE}");
        if (slot > -1 && slot < 54) {
            inv.setItem(slot, contents.get(slot));
        }
        p.openInventory(inv);
    }

    private List<String> getLoreParty(Party party) {
        List<String> lore = new ArrayList<>();
        for (String l : ip.getParties().get("menus.parties.party.loreItem").replace("<leader>", party.getLeaderName()).replace("<size>", String.valueOf(party.getMaxCapacity())).split("\\n")) {
            if (l.equals("<members>")) {
                for (String name : party.getMembers().values()) {
                    lore.add(ip.getParties().get("menus.parties.party.member").replace("<name>", name));
                }
            } else if (l.equals("<status>")) {
                if (party.isPrivacy()) {
                    lore.add(ip.getParties().get("menus.parties.party.status.privacy"));
                } else {
                    if (party.getMembers().size() >= party.getMaxCapacity()) {
                        lore.add(ip.getParties().get("menus.parties.party.status.full"));
                    } else {
                        lore.add(ip.getParties().get("menus.parties.party.status.join"));
                    }
                }
            } else {
                lore.add(l);
            }
        }
        return lore;
    }

}