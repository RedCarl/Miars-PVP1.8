package cn.mcarl.miars.skypvp.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.ConfigValue;
import cc.carm.lib.configuration.core.value.type.ConfiguredList;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;
import cn.mcarl.miars.storage.utils.ItemBuilder;
import cn.mcarl.miars.core.utils.MiarsUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class LuckyConfig extends ConfigurationRoot {

    @HeaderComment({"幸运方块配置"})
    public static final class LUCKY extends ConfigurationRoot {
        @HeaderComment({"普通幸运方块"})
        public static final class NORMAL extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置列表"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();

            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<ItemStack> REWARD = ConfigValue.builder()
                    .asList(ItemStack.class).fromObject()
                    .parseValue(MiarsUtil::itemStackParse)
                    .serializeValue(ItemStack::serialize)
                    .defaults(
                            new ItemBuilder(Material.STONE_SWORD)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .addEnchant(Enchantment.DAMAGE_ALL,5,true)
                                    .toItemStack(),
                            new ItemBuilder(Material.DIAMOND)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .toItemStack(),
                            new ItemBuilder(Material.POTION, 1)
                                    .setData((short) 16421)
                                    .toItemStack()
                    )
                    .build();
        }
        @HeaderComment({"稀有幸运方块"})
        public static final class RARE extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置列表"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();

            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<ItemStack> REWARD = ConfigValue.builder()
                    .asList(ItemStack.class).fromObject()
                    .parseValue(MiarsUtil::itemStackParse)
                    .serializeValue(ItemStack::serialize)
                    .defaults(
                            new ItemBuilder(Material.STONE_SWORD)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .addEnchant(Enchantment.DAMAGE_ALL,5,true)
                                    .toItemStack(),
                            new ItemBuilder(Material.DIAMOND)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .toItemStack()
                    )
                    .build();
        }
        @HeaderComment({"史诗幸运方块"})
        public static final class EPIC extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置列表"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();

            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<ItemStack> REWARD = ConfigValue.builder()
                    .asList(ItemStack.class).fromObject()
                    .parseValue(MiarsUtil::itemStackParse)
                    .serializeValue(ItemStack::serialize)
                    .defaults(
                            new ItemBuilder(Material.STONE_SWORD)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .addEnchant(Enchantment.DAMAGE_ALL,5,true)
                                    .toItemStack(),
                            new ItemBuilder(Material.DIAMOND)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .toItemStack()
                    )
                    .build();
        }
        @HeaderComment({"传奇幸运方块"})
        public static final class LEGENDARY extends ConfigurationRoot {
            @HeaderComment({"多久刷新一次 (秒)"})
            public static final ConfigValue<Long> REFRESH = ConfiguredValue.of(Long.class, 10L);
            @HeaderComment({"位置列表"})
            public static final ConfiguredList<Location> LOCATION = ConfigValue.builder()
                    .asList(Location.class).fromObject()
                    .parseValue(MiarsUtil::locationParse)
                    .serializeValue(Location::serialize)
                    .defaults(
                            new Location(Bukkit.getWorld("world"),0,0,0),
                            new Location(Bukkit.getWorld("world"),1,1,1)
                    )
                    .build();

            @HeaderComment({"奖励内容"})
            public static final ConfiguredList<ItemStack> REWARD = ConfigValue.builder()
                    .asList(ItemStack.class).fromObject()
                    .parseValue(MiarsUtil::itemStackParse)
                    .serializeValue(ItemStack::serialize)
                    .defaults(
                            new ItemBuilder(Material.STONE_SWORD)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .addEnchant(Enchantment.DAMAGE_ALL,5,true)
                                    .toItemStack(),
                            new ItemBuilder(Material.DIAMOND)
                                    .setName("test")
                                    .setLore("probability: 100")
                                    .toItemStack()
                    )
                    .build();
        }
    }
}
