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
        practiceNpc(world);
        storyNpc(world);
        tick();
    }

    /**
     * Practice
     * @param world
     */
    public void practiceNpc(World world){
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

    /**
     * Story
     * @param world
     */
    public void storyNpc(World world){
        npcList.put("story",
                MNPCs.createNPC(
                        EntityType.PLAYER,
                        new Location(world, -86.5, 152, -71.5, -90F, 0F),
                        Arrays.asList(
                                ColorParser.parse("&e&lMiarsStory 充值商店"),
                                "",
                                ColorParser.parse("&fClick Join")
                        ),
                        n -> {
                            n.data().set("miars_lobby", "story");
                            n.getOrAddTrait(Equipment.class).set(0, new ItemStack(Material.GOLD_INGOT));
                            SkinTrait skinTrait = n.getOrAddTrait(SkinTrait.class);
                            skinTrait.setSkinPersistent(
                                    "Sentida",
                                    "ACsdVA+P+lI0ghihr5oLa6v2sgoBs4iMkrYsqf+qKDePumYrLZSX7FDzBvzKGJYL/x8770UkqoKfOCxfcsHuZOkDV+7TwpDDKdbLzIFM6CycrOrC1w6PCrpxKAZCc4kwgasctalZO8CFFA4QJUGrlOjWPhJvFioKI7Wd1X11QjPREJuPJIg2ZWkZFmsyVxVbLHJpS1aEkE3+Fr4EIKhfc/bW69q5GRfyJ7aEU1QYFqY1E563aC1+pWy0drkJt+ITGQnhdPDH2VS1E3zyM4hbuutH/j0gEI5MKVLNJ6zrVWeDkxvjoROLn9Eb1Ya1yKJjO6yDWW9pzazmQcaPUFkCyBEoIZdJIYMSVo/YvS1X24m6AvfB6qnjYZpBKzc/pjcinnFbreKgJTObMdLs0//ASUHOdaLyRyt8XFW0+mLCBrxjs2B2mea35g5ldi+PNHqYfyATQrqXVBqUBnR+fbuF6or6AwQy+cS5zb+EBGnf2aMMJY6V1f9mNtTj3pG82a4/qtC4J1Ca3ji1dgr1Ch2jHuPjiGZ7U9kqnZIEVM/rP5YRXme6xCkmzZdi7bXbh0zswmL5jzTIQuM3yczP8/UOvXnDNrkbSgz2Z87Ms7M2Bn0/hy7mp92cAG9zhzphUjj5Ye71dmX8XQfmwBC8oEYx561EB1XEy0D+xox6sOXeI0g=",
                                    "ewogICJ0aW1lc3RhbXAiIDogMTY3NDAwODUxMjczOSwKICAicHJvZmlsZUlkIiA6ICIxOTQyY2I3ZGRmZDk0NGQ0YTkxZTQxN2VlNGIzMDhjNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJTZW50aWRhIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVjMTg4Zjc2OTVkYmY5Y2QzZTU2MDk2MzVkZjVmNmUyOTNlYTliNGE4NmNiODc1OGEwNTE2NjdlNDNmNmMwZSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9"
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