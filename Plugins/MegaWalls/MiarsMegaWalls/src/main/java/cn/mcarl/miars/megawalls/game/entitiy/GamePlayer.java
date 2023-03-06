package cn.mcarl.miars.megawalls.game.entitiy;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.ClassesManager;
import cn.mcarl.miars.megawalls.game.entitiy.enums.TeamType;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.game.manager.GamePlayerManager;
import cn.mcarl.miars.megawalls.stats.PlayerStats;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayer {
    private UUID uuid;
    private String name;
    private String displayName;
    private TeamType teamType;
    private Inventory enderChest;
    private PlayerStats playerStats;
    private boolean isRandom = false;
    private boolean isSpectator = false;
    private boolean protect = false;
    private int kills = 0;
    private int finalKills = 0;
    private int assists = 0;
    private int FinalAssists = 0;
    private int protectChallenge = 0;
    private final List<Block> protectedBlock = new ArrayList<>();
    private int energy = 0;
    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
        this.name = this.getPlayer().getName();
        this.displayName = ChatColor.stripColor(this.getPlayer().getDisplayName());
        this.playerStats = new PlayerStats(this);
    }

    public void sendMessage(String message){
        getPlayer().sendMessage(ColorParser.parse(message));
    }
    public void sendMessageAll(String message){
        Bukkit.broadcastMessage(ColorParser.parse(message));
    }
    public void tp(Location location){
        getPlayer().teleport(location);
    }
    public void clearInv(){
        getPlayer().getInventory().clear();
    }

    public void sendTitle(String s,String s1){
        getPlayer().sendTitle(ColorParser.parse(s),ColorParser.parse(s1));
    }

    public boolean isOnline(){
        return getPlayer().isOnline();
    }

    public void givePotionEffect(PotionEffect potionEffect){
        getPlayer().addPotionEffect(potionEffect);
    }
    public Player getPlayer() {
        return Bukkit.getPlayer(this.getUuid());
    }
    public void createEnderChest() {
        if (this.enderChest == null) {
            int size = this.getPlayerStats().getClassesStats(ClassesManager.getClassesByName(this.getPlayerStats().getClasses())).getEnderChest() * 9;
            this.enderChest = Bukkit.createInventory(this.getPlayer(), size, "末影箱");
        }
    }

    public static List<GamePlayer> getOnlinePlayers(){
        List<GamePlayer> list = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            list.add(GamePlayerManager.getInstance().getGamePlayer(player));
        });
        return list;
    }

    public static GamePlayer get(UUID uuid){
        return GamePlayerManager.getInstance().getGamePlayer(Bukkit.getPlayer(uuid));
    }
    public void sendActionBar(String message) {
        if (this.isOnline()) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            PlayerConnection connection = ((CraftPlayer)this.getPlayer()).getHandle().playerConnection;
            IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat ppoc = new PacketPlayOutChat(icbc, (byte)2);
            connection.sendPacket(ppoc);
        }

    }
    public void playSound(Sound sound, float volume, float pitch) {
        if (this.isOnline()) {
            this.getPlayer().playSound(this.getPlayer().getLocation(), sound, volume, pitch);
        }

    }
    public String getDisplayName(GamePlayer gamePlayer) {
        if (GameManager.getInstance().getGamePlayerTeam(this) != null && !this.isSpectator) {
            return gamePlayer.getTeamType().getColor() + gamePlayer.displayName;
        } else {
            return "&9" + gamePlayer.displayName;
        }
    }
}
