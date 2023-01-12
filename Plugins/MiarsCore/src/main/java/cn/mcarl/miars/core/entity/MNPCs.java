package cn.mcarl.miars.core.entity;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface MNPCs {
    NPCRegistry REGISTRY = CitizensAPI.createNamedNPCRegistry("mcarl", new MemoryNPCDataStore());
    String PLAYER_SKIN = "mcarl:player_skin";

    static NPC createNPC(EntityType entityType, Location location, List<Object> texts) {
        return createNPC(entityType,location, texts, npc -> {
        });
    }

    static NPC createNPC(EntityType entityType,Location location,List<Object> texts, Consumer<NPC> consumer) {
        //temp name
        String name = "§8[NPC] " + UUID.randomUUID().toString().split("-")[0];
        NPC npc = REGISTRY.createNPC(entityType, name);
        consumer.accept(npc);
        // real name
        npc.setName("§8[NPC] " + npc.getUniqueId().toString().split("-")[0]);
        npc.data().set(NPC.Metadata.NAMEPLATE_VISIBLE, false);
        npc.spawn(location);

        // holograms
        if (texts==null){
            texts = new ArrayList<>();
        }
        if (!texts.isEmpty()) {
            location.setY(location.getBlockY()+2+(texts.size()*0.25));
            MHolograms.createHologram(npc.getName(), location,texts);
        }

        return npc;
    }
}
