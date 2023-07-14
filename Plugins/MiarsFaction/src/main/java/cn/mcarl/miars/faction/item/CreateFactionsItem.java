//package cn.mcarl.miars.faction.item;
//
//import cn.mcarl.miars.storage.utils.easyitem.AbstractItem;
//import net.minecraft.server.v1_8_R3.NBTTagCompound;
//import org.bukkit.Material;
//import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
//import org.bukkit.entity.Player;
//import org.bukkit.event.player.PlayerInteractEvent;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * @Author: carl0
// * @DATE: 2022/6/27 15:13
// */
//public class CreateFactionsItem extends AbstractItem {
//    public Player player;
//
//    public CreateFactionsItem(Player player) {
//        this.player = player;
//        register();
//    }
//
//
//    @Override
//    public void init() {
//        type = Material.BEACON;
//    }
//
//    public String getId() {
//        return "factions.beacon." + player.getUuid();
//    }
//
//    public String getName(@Nullable VLPlayer player) {
//        return "factions.beacon.name";
//    }
//
//    public List<String> getLore(@Nullable VLPlayer player) {
//        return Collections.singletonList("factions.beacon.lore");
//    }
//
//    @Override
//    public void writeTag(@Nullable VLPlayer player, NBTTagCompound tag) {
//        super.writeTag(player, tag);
////        tag.set("name", new NBTTagString("test"));
////        tag.set("present", new NBTTagString("这是一段简介"));
////        tag.set("icon", new NBTTagString("APPLE"));
//    }
//
//    @Override
//    public void onInteract(PlayerInteractEvent e, VLPlayer player, AtomicBoolean cancelled) {
//        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(player.getItemInHand());
//        NBTTagCompound tag = nmsItem.getTag().getCompound("VioLeaft");
//        if (tag.getString("name").equals("") || tag.getString("present").equals("")) {
//            new CreateFactionsMenu(player).open();
//        }
//    }
//
//    @Override
//    public void onUpdate(Player player, ItemStack itemStack) {
//        super.onUpdate(player, itemStack);
//        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
//        NBTTagCompound tag = nmsItem.getTag().getCompound("VioLeaft");
//
//        ItemMeta itemMeta = itemStack.getItemMeta();
//        itemMeta.setDisplayName(getName(player));
//        itemMeta.getLore().clear();
//        itemMeta.setLore(Collections.singletonList("factions.tool.lore>" + (tag.getString("name").equals("") ? "暂无请设置" : tag.getString("name")) + "," + (tag.getString("present").equals("") ? "暂无请设置" : tag.getString("present"))));
//        itemStack.setItemMeta(itemMeta);
//    }
//
//    @Override
//    public boolean whenLanguageReloadResetItem(VLPlayer player) {
//        return false;
//    }
//}
