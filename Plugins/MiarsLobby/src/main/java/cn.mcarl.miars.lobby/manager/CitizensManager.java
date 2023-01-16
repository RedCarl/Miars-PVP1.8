package cn.mcarl.miars.lobby.manager;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.core.entity.MHolograms;
import cn.mcarl.miars.core.entity.MNPCs;
import cn.mcarl.miars.core.manager.ServerManager;
import cn.mcarl.miars.lobby.MiarsLobby;
import cn.mcarl.miars.storage.entity.MServerInfo;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: carl0
 * @DATE: 2022/8/3 17:41
 */
public class CitizensManager {
    Map<String,NPC> npcList = new HashMap<>();
    private static final CitizensManager instance = new CitizensManager();

    public static CitizensManager getInstance() {
        return instance;
    }

    public void init(World world) {
        survivalNpc(world);

        tick();
    }

    /**
     * Practice
     * @param world
     */
    public void survivalNpc(World world){
        MServerInfo survival = ServerManager.getInstance().getServerInfo("practice");
        npcList.put("practice",
                MNPCs.createNPC(
                        EntityType.PLAYER,
                        new Location(world, -75.5, 153, -62.5, -180F, 0F),
                        Arrays.asList(
                                ColorParser.parse("&c&lPractice 竞技场"),
                                "",
                                ColorParser.parse(survival!=null ? ("&cOnline &f"+survival.getPlayers().size()) : "&c离线"),
                                ColorParser.parse("&cVersion &f1.7.x - 1.8.x"),
                                "",
                                ColorParser.parse("&fClick Join")
                        ),
                        n -> {
                            n.data().set("miars_lobby", "practice");
                            n.getOrAddTrait(Equipment.class).set(0, new ItemStack(Material.DIAMOND_SWORD));
                            SkinTrait skinTrait = n.getOrAddTrait(SkinTrait.class);
                            skinTrait.setSkinPersistent(
                                    "Kurari_",
                                    "T8+enKG90RUiZ9GBtSFTqfJUBwYV3waNCeXma5BKmurkzczN0JyzQzOiczdRuJz0CsGjyJXIgn3E/FqTDwBPVPN84HA5s7Ms4pQJyglVVVP16gI4a4GmW+982MHvpC505zKX8TtRyml2zyxFc3h5VIwwxpoG8u0E0m1rinffef0LUrPq7sZ1+UrLlX/vofg0KaTnRskjeIcNobSqLn7ymsJAbdg9CqiNsCLmg+G+jqbWYqvbMfgTqAYbuwGjLu8aggWPR8xem7qLMXu98r8SX8tINNzBgYzir44ZUEgX5WBkVcVcrDJm/FVtbx4/e2K7YZu1yg8sbQvGBdF6n7bSmK/VG9x8vjvYrJ3WmVaMyXpFdV5i9VqqJZECC6oqAoimiezaL4dqdj0S5LAw3sVqH26SLeZ5A4UcaiwcMKpxZLzd587n81qzt1niNKRaaBgd8wRryHZA5HAX7Nv88JsbgnMmQn66yZlCE6n3MvQzS9g7OMxVAFqcxUVtIAu0ExDuy1yzxqc5f/JJbFfmO9GRBtMx6Rlh2HWe0BQWu52mDqh64ZstbvLfvteqkY6ymeq97ZE/IaPLsQffeD3QllHmUnfJb7cd4DwZeuMb3+iF0gr5U1zM6VTib1yIzfonCzrmWeRb37xFcg4C3K7/4ii6Kdq99QWRoNkCjTob920OzYc=",
                                    "ewogICJ0aW1lc3RhbXAiIDogMTY2ODY5ODc5ODA5MSwKICAicHJvZmlsZUlkIiA6ICJkNjY5NTQxYjYwZDE0NDNmOWRjZTU0ZmQ3MDNmZjkzNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJLdXJhcmlfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2IzYTczM2U3ZDhmYjg0ZjgxNTZhNmQwNDk0ZTY4NDg2MWIzZTY1Y2RjZmU2YjNlYzk3YWIwMjYwZTM4ZmVkZDUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ=="
                            );
                        })
        );

    }


    public void tick(){
        new BukkitRunnable() {
            @Override
            public void run() {
                // survival
                MServerInfo survival = ServerManager.getInstance().getServerInfo("practice");
                ((TextLine) MHolograms.getHologramByName(npcList.get("practice").getName()).getLine(2)).setText(ColorParser.parse(survival!=null ? ("&cOnline &f"+survival.getPlayers().size()) : "&c离线"));
            }
        }.runTaskTimer(MiarsLobby.getInstance(),0,200);
    }

    public NPC getNpc(String npcType) {
        for (NPC n : npcList.values()) {
            if (n.data().get("miars_lobby").equals(npcType)) {
                return n;
            }
        }
        return null;
    }

    public String getNpcType(NPC npc) {
        return npc.data().get("miars_lobby");
    }

    public Map<String,NPC> getAllNpc() {
        return npcList;
    }

    public void clear(){
        for (NPC n:npcList.values()) {
            n.destroy();
        }
    }
}