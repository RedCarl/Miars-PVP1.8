package cn.mcarl.miars.faction.manager;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FakePlayerManager {
    private static final FakePlayerManager instance = new FakePlayerManager();

    public static FakePlayerManager getInstance() {
        return instance;
    }
    private final Map<UUID, Inventory> invMap = new HashMap<>();
    private final Map<UUID, Integer> expMap = new HashMap<>();
    private final Map<UUID, Zombie> entityMap = new HashMap<>();

    public void setFakePlayer(Player player){
        if (CombatWithManager.getInstance().isInCombatWith(player)){
            Zombie zombie = (Zombie)player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);

            EntityEquipment equipment = zombie.getEquipment();

            equipment.setHelmet(player.getInventory().getHelmet());
            equipment.setChestplate(player.getInventory().getChestplate());
            equipment.setLeggings(player.getInventory().getLeggings());
            equipment.setBoots(player.getInventory().getBoots());
            equipment.setHelmetDropChance(0.0F);
            equipment.setChestplateDropChance(0.0F);
            equipment.setLeggingsDropChance(0.0F);
            equipment.setBootsDropChance(0.0F);

            zombie.setCustomName(player.getUniqueId().toString());
            zombie.setHealth(player.getHealth());

            entityMap.put(player.getUniqueId(),zombie);
            invMap.put(player.getUniqueId(),player.getInventory());
            expMap.put(player.getUniqueId(),player.getLevel());
        }
    }

    public void getFakePlayer(Player player){
        if (entityMap.containsKey(player.getUniqueId())){
            if (entityMap.get(player.getUniqueId()).isDead()){
                player.getInventory().clear();
                player.setLevel(0);
                player.setHealth(0);
            }else {
                player.setHealth(entityMap.get(player.getUniqueId()).getHealth());
                entityMap.get(player.getUniqueId()).remove();

            }
        }
        entityMap.remove(player.getUniqueId());
    }

    public void killFakePlayer(Entity entity){
        try {
            UUID uuid = UUID.fromString(entity.getCustomName());
            Location location = entity.getLocation();
            if (entityMap.containsKey(uuid)){
                Inventory inventory = invMap.get(uuid);
                for (ItemStack itemStack : inventory.getContents()) {
                    if (itemStack != null){
                        location.getWorld().dropItem(location, itemStack);
                    }
                }
                Integer exp = expMap.get(uuid);
                if (exp != 0) {
                    ExperienceOrb orb = (ExperienceOrb)location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
                    orb.setExperience(exp);
                }
            }
        }catch (NullPointerException ignored){}
    }
}
