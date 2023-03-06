package cn.mcarl.miars.megawalls.classes.stater.hunter;

import cn.mcarl.miars.megawalls.MiarsMegaWalls;
import cn.mcarl.miars.megawalls.classes.Classes;
import cn.mcarl.miars.megawalls.classes.Skill;
import cn.mcarl.miars.megawalls.game.entitiy.GamePlayer;
import cn.mcarl.miars.megawalls.game.manager.GameManager;
import cn.mcarl.miars.megawalls.nms.*;
import cn.mcarl.miars.megawalls.stats.ClassesStats;
import cn.mcarl.miars.megawalls.utils.EntityTypes;
import cn.mcarl.miars.megawalls.utils.StringUtils;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.*;
import java.util.Map.Entry;

public class SecondSkill extends Skill {
   private final Map<String, Class<? extends CustomEntity>> classMap = new HashMap<>();

   public SecondSkill(Classes classes) {
      super("动物伙伴", classes);
      this.classMap.put("皮皮猪", CustomPig.class);
      this.classMap.put("蜘蛛", CustomSpider.class);
      this.classMap.put("爆炸羊", BoomSheep.class);
      this.classMap.put("狼", ShamanWolf.class);
   }

   @Override
   public int maxedLevel() {
      return 3;
   }

   @Override
   public double getAttribute(int level) {
      return switch (level) {
         case 2 -> 0.15D;
         case 3 -> 0.2D;
         default -> 0.1D;
      };
   }

   @Override
   public List<String> getInfo(int level) {
      List<String> lore = new ArrayList<>();
      if (level == 1) {
         lore.add(" &8▪ &7当你被攻击,你将有&a" + StringUtils.percent(this.getAttribute(level)) + "&7的");
         lore.add("   &7几率召唤一只随机宠物。");
         lore.add("   &7它可能是:皮皮猪、蜘蛛");
         lore.add("   &7爆炸羊或狼。");
         lore.add(" ");
         lore.add("&7冷却时间:&a4秒");
         return lore;
      } else {
         lore.add(" &8▪ &7当你被攻击,你将有&8" + StringUtils.percent(this.getAttribute(level - 1)) + " ➜ &a" + StringUtils.percent(this.getAttribute(level)) + "&7的");
         lore.add("   &7几率召唤一只随机宠物。");
         return lore;
      }
   }

   @Override
   public void upgrade(GamePlayer gamePlayer) {

   }

   @Override
   public int getPlayerLevel(GamePlayer gamePlayer) {
      return gamePlayer.getPlayerStats().getClassesStats(this.getClasses()).getSkill2Level();
   }

   @Override
   public boolean use(GamePlayer gamePlayer, ClassesStats classesStats) {
      if (Hunter.skill2Cooldown.getOrDefault(gamePlayer, 0) > 0) {
         return false;
      } else {
         try {
            List<Entry<String, Class<? extends CustomEntity>>> list = new ArrayList(this.classMap.entrySet());
            Entry<String, Class<? extends CustomEntity>> entry = (Entry)list.get((new Random()).nextInt(list.size() - 1));
            CustomEntity customEntity = (CustomEntity)((Class)entry.getValue()).getConstructor(World.class).newInstance(((CraftWorld)Bukkit.getWorld("world")).getHandle());
            customEntity.setGamePlayer(gamePlayer);
            customEntity.getBukkitEntity().setMetadata(MiarsMegaWalls.getMetadataValue(), new FixedMetadataValue(MiarsMegaWalls.getInstance(), GameManager.getInstance().getGamePlayerTeam(gamePlayer)));
            EntityTypes.spawnEntity((Entity)customEntity, gamePlayer.getPlayer().getLocation());
            gamePlayer.sendMessage("&a你的动物伙伴技能召唤了一只" + (String)entry.getKey() + "！");
         } catch (Exception var6) {
         }

         Hunter.skill2Cooldown.put(gamePlayer, 4);
         return true;
      }
   }

   @Override
   public String getSkillTip(GamePlayer gamePlayer) {
      return this.getClasses().getNameColor() + "&l" + this.getName() + " " + ((Integer)Hunter.skill2Cooldown.getOrDefault(gamePlayer, 0) == 0 ? "&8[&a&l✔&8]" : "&c&l" + Hunter.skill2Cooldown.get(gamePlayer) + "秒");
   }
}
