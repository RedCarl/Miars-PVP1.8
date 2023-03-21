package cn.mcarl.miars.core.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.MiarsCore;
import cn.mcarl.miars.core.conf.PluginConfig;
import cn.mcarl.miars.core.entity.MHolograms;
import cn.mcarl.miars.core.entity.MNPCs;
import cn.mcarl.miars.core.utils.ToolUtils;
import cn.mcarl.miars.storage.entity.serverNpc.ServerNPC;
import cn.mcarl.miars.storage.storage.data.serverNpc.ServerNpcDataStorage;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.line.TextHologramLine;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.MirrorTrait;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/8/3 17:41
 */
public class CitizensManager {
    Map<String,NPC> npcList = new HashMap<>();
    Map<String,ServerNPC> npcMap = new HashMap<>();
    private static final CitizensManager instance = new CitizensManager();

    public static CitizensManager getInstance() {
        return instance;
    }

    public void init() {

        for (ServerNPC s:ServerNpcDataStorage.getInstance().getServerNPC(PluginConfig.SERVER_INFO.NAME.get())){
            npcMap.put(s.getName(),s);
            npcList(s);
        }

        tick();
    }

    public void npcList(ServerNPC npc){

        npcList.put(npc.getName(),
                MNPCs.createNPCString(
                        EntityType.PLAYER,
                        new Location(Bukkit.getWorld(npc.getWorld()), npc.getX(), npc.getY(), npc.getZ(), npc.getYaw(), npc.getPitch()),
                        ColorParser.parse(ToolUtils.initLorePapi(npc.getTitle(), true, "npc")),
                        n -> {
                            n.data().set(PluginConfig.SERVER_INFO.NAME.get(), npc.getName());
                            if (npc.getItem()!=null){
                                n.getOrAddTrait(Equipment.class).set(0, new ItemStack(Material.getMaterial(npc.getItem())));
                            }

                            if (npc.getSkinName().equals("PLAYER")){
                                MirrorTrait trait = n.getOrAddTrait(MirrorTrait.class);
                                boolean enabled = true;
                                trait.setEnabled(enabled);
                            }else if (npc.getSkinName()!=null && npc.getSignature()!=null && npc.getData()!=null){
                                SkinTrait skinTrait = n.getOrAddTrait(SkinTrait.class);
                                skinTrait.setSkinPersistent(
                                        npc.getSkinName(),
                                        npc.getSignature(),
                                        npc.getData()
                                );
                            }
                        })
        );

    }


    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {

                for (String npc:npcList.keySet()) {

                    ServerNPC serverNPC = npcMap.get(npc);
                    List<String> stringList = ColorParser.parse(ToolUtils.initLorePapi(serverNPC.getTitle(),true,"npc"));
                    Hologram hologram = MHolograms.getHologramByName(npcList.get(npc).getName());

                    for (int i = 0; i < MHolograms.getHologramByName(npcList.get(npc).getName()).getLines().size(); i++) {
                        TextHologramLine line = (TextHologramLine) hologram.getLines().get(i);
                        line.setText(stringList.get(i));
                    }

                }
            }
        }.runTaskTimer(MiarsCore.getInstance(),0,200);
    }

    public NPC getNpc(String npcType) {
        for (NPC n : npcList.values()) {
            if (n.data().get(PluginConfig.SERVER_INFO.NAME.get()).equals(npcType)) {
                return n;
            }
        }
        return null;
    }
    public ServerNPC getServerNPC(String name) {
        return npcMap.get(name);
    }

    public Map<String,NPC> getAllNpc() {
        return npcList;
    }

    public void clear(){
        for (NPC n:npcList.values()) {
            Hologram hologram = MHolograms.getHologramByName(n.getName());
            hologram.delete();
            n.destroy();
        }
    }
}