/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.server.v1_8_R3.AxisAlignedBB
 *  net.minecraft.server.v1_8_R3.Block
 *  net.minecraft.server.v1_8_R3.BlockCarpet
 *  net.minecraft.server.v1_8_R3.BlockPosition
 *  net.minecraft.server.v1_8_R3.BlockSnow
 *  net.minecraft.server.v1_8_R3.EntityHuman
 *  net.minecraft.server.v1_8_R3.EntityPlayer
 *  net.minecraft.server.v1_8_R3.IBlockState
 *  net.minecraft.server.v1_8_R3.World
 *  net.minecraft.server.v1_8_R3.WorldServer
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.craftbukkit.v1_8_R3.CraftWorld
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
 *  org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers
 *  org.bukkit.entity.Player
 *  org.bukkit.util.Vector
 */
package gg.noob.plunder.util;

import gg.noob.plunder.util.AABB;
import gg.noob.plunder.util.BoundingBox;
import gg.noob.plunder.util.MathUtils;
import gg.noob.plunder.util.Pair;
import gg.noob.plunder.util.ReflectionUtils;
import gg.noob.plunder.util.SyncCatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockCarpet;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.BlockSnow;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IBlockState;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockUtils {
    private static Set<Byte> blockSolidPassSet = new HashSet<Byte>();
    private static Set<Byte> blockStairsSet = new HashSet<Byte>();
    private static Set<Byte> blockLiquidsSet = new HashSet<Byte>();
    private static Set<Byte> blockWebsSet = new HashSet<Byte>();
    private static Set<Byte> blockIceSet = new HashSet<Byte>();
    public static HashSet<Byte> blockPassSet = new HashSet();
    public static List<Material> allowed = new ArrayList<Material>();
    public static List<Material> semi = new ArrayList<Material>();
    public static List<Material> blockedPearlTypes = new ArrayList<Material>();
    public static Map<Material, BoundingBox[]> collisionBoundingBoxes = new HashMap<Material, BoundingBox[]>();
    static String[] HalfBlocksArray;
    static String[] Blocks_1_13;

    public BlockUtils() {
        this.setupCollisionBB();
    }

    public static boolean isSolid2(Material type) {
        SyncCatcher.catchOp("use block math");
        switch (type.getId()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 29:
            case 33:
            case 34:
            case 35:
            case 36:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 52:
            case 53:
            case 54:
            case 56:
            case 57:
            case 58:
            case 60:
            case 61:
            case 62:
            case 64:
            case 65:
            case 67:
            case 71:
            case 73:
            case 74:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
            case 116:
            case 117:
            case 118:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 144:
            case 145:
            case 146:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 158:
            case 159:
            case 160:
            case 161:
            case 162:
            case 163:
            case 164:
            case 165:
            case 166:
            case 167:
            case 168:
            case 169:
            case 170:
            case 171:
            case 172:
            case 173:
            case 174:
            case 178:
            case 179:
            case 180:
            case 181:
            case 182:
            case 183:
            case 184:
            case 185:
            case 186:
            case 187:
            case 188:
            case 189:
            case 190:
            case 191:
            case 192:
            case 193:
            case 194:
            case 195:
            case 196:
            case 197:
            case 198:
            case 199:
            case 200:
            case 201:
            case 202:
            case 203:
            case 204:
            case 205:
            case 206:
            case 207:
            case 208:
            case 210:
            case 211:
            case 212:
            case 213:
            case 214:
            case 215:
            case 216:
            case 218:
            case 219:
            case 220:
            case 221:
            case 222:
            case 223:
            case 224:
            case 225:
            case 226:
            case 227:
            case 228:
            case 229:
            case 230:
            case 231:
            case 232:
            case 233:
            case 234:
            case 235:
            case 236:
            case 237:
            case 238:
            case 239:
            case 240:
            case 241:
            case 242:
            case 243:
            case 244:
            case 245:
            case 246:
            case 247:
            case 248:
            case 249:
            case 250:
            case 251:
            case 252:
            case 255:
            case 355:
            case 397: {
                return true;
            }
        }
        return false;
    }

    public static boolean isSolid(byte block) {
        SyncCatcher.catchOp("use block math");
        if (blockPassSet.isEmpty()) {
            blockPassSet.add((byte)0);
            blockPassSet.add((byte)6);
            blockPassSet.add((byte)8);
            blockPassSet.add((byte)9);
            blockPassSet.add((byte)10);
            blockPassSet.add((byte)11);
            blockPassSet.add((byte)27);
            blockPassSet.add((byte)28);
            blockPassSet.add((byte)30);
            blockPassSet.add((byte)31);
            blockPassSet.add((byte)32);
            blockPassSet.add((byte)37);
            blockPassSet.add((byte)38);
            blockPassSet.add((byte)39);
            blockPassSet.add((byte)40);
            blockPassSet.add((byte)50);
            blockPassSet.add((byte)51);
            blockPassSet.add((byte)55);
            blockPassSet.add((byte)59);
            blockPassSet.add((byte)63);
            blockPassSet.add((byte)66);
            blockPassSet.add((byte)68);
            blockPassSet.add((byte)69);
            blockPassSet.add((byte)70);
            blockPassSet.add((byte)72);
            blockPassSet.add((byte)75);
            blockPassSet.add((byte)76);
            blockPassSet.add((byte)77);
            blockPassSet.add((byte)78);
            blockPassSet.add((byte)83);
            blockPassSet.add((byte)90);
            blockPassSet.add((byte)104);
            blockPassSet.add((byte)105);
            blockPassSet.add((byte)115);
            blockPassSet.add((byte)119);
            blockPassSet.add((byte)-124);
            blockPassSet.add((byte)-113);
            blockPassSet.add((byte)-81);
            blockPassSet.add((byte)-85);
        }
        return !blockPassSet.contains(block);
    }

    public static boolean Block_1_13(Material b) {
        SyncCatcher.catchOp("use block math");
        return b.equals((Object)Material.getMaterial((String)"TUBE_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"BRAIN_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"BUBBLE_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"FIRE_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"HORN_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"DEAD_TUBE_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"DEAD_BRAIN_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"DEAD_BUBBLE_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"DEAD_FIRE_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"DEAD_HORN_CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"CORAL_BLOCK")) || b.equals((Object)Material.getMaterial((String)"CAVE_AIR")) || b.equals((Object)Material.getMaterial((String)"VOID_AIR")) || b.equals((Object)Material.getMaterial((String)"BLUE_ICE")) || b.equals((Object)Material.getMaterial((String)"STONE_BUTTON")) || b.equals((Object)Material.getMaterial((String)"OAK_BUTTON")) || b.equals((Object)Material.getMaterial((String)"SPRUCE_BUTTON")) || b.equals((Object)Material.getMaterial((String)"BIRCH_BUTTON")) || b.equals((Object)Material.getMaterial((String)"JUNGLE_BUTTON")) || b.equals((Object)Material.getMaterial((String)"ACACIA_BUTTON")) || b.equals((Object)Material.getMaterial((String)"DARK_OAK_BUTTON")) || b.equals((Object)Material.getMaterial((String)"STONE_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"HEAVY_WEIGHTED_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"LIGHT_WEIGHTED_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"OAK_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"SPRUCE_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"BIRCH_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"JUNGLE_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"ACACIA_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"DARK_OAK_PRESSURE_PLATE")) || b.equals((Object)Material.getMaterial((String)"IRON_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"OAK_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"SPRUCE_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"BIRCH_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"JUNGLE_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"ACACIA_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"DARK_OAK_TRAPDOOR")) || b.equals((Object)Material.getMaterial((String)"PUMPKIN")) || b.equals((Object)Material.getMaterial((String)"CARVED_PUMPKIN")) || b.equals((Object)Material.getMaterial((String)"TUBE_CORAL")) || b.equals((Object)Material.getMaterial((String)"BRAIN_CORAL")) || b.equals((Object)Material.getMaterial((String)"BUBBLE_CORAL")) || b.equals((Object)Material.getMaterial((String)"FIRE_CORAL")) || b.equals((Object)Material.getMaterial((String)"HORN_CORAL")) || b.equals((Object)Material.getMaterial((String)"DEAD_TUBE_CORAL")) || b.equals((Object)Material.getMaterial((String)"DEAD_BRAIN_CORAL")) || b.equals((Object)Material.getMaterial((String)"DEAD_BUBBLE_CORAL")) || b.equals((Object)Material.getMaterial((String)"DEAD_FIRE_CORAL")) || b.equals((Object)Material.getMaterial((String)"DEAD_HORN_CORAL")) || b.equals((Object)Material.getMaterial((String)"TUBE_CORAL_FAN\t")) || b.equals((Object)Material.getMaterial((String)"BUBBLE_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"FIRE_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"HORN_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_TUBE_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_BRAIN_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_BUBBLE_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_FIRE_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_HORN_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"TUBE_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"BRAIN_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"BUBBLE_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"FIRE_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"HORN_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_TUBE_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_BRAIN_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_BUBBLE_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_FIRE_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"DEAD_HORN_CORAL_WALL_FAN")) || b.equals((Object)Material.getMaterial((String)"BRAIN_CORAL_FAN")) || b.equals((Object)Material.getMaterial((String)"DRIED_KELP_BLOCK")) || b.equals((Object)Material.getMaterial((String)"CONDUIT")) || b.equals((Object)Material.getMaterial((String)"SEAGRASS")) || b.equals((Object)Material.getMaterial((String)"TALL_SEAGRASS")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_OAK_LOG")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_SPRUCE_LOG")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_BIRCH_LOG")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_JUNGLE_LOG")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_ACACIA_LOG")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_BRICK_SLAB")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_SLAB")) || b.equals((Object)Material.getMaterial((String)"DARK_PRISMARINE_SLAB")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_BRICK_STAIRS")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_STAIRS")) || b.equals((Object)Material.getMaterial((String)"DARK_PRISMARINE_STAIRS")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_DARK_OAK_LOG")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_BRICK_SLAB")) || b.equals((Object)Material.getMaterial((String)"TURTLE_EGG")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_SLAB")) || b.equals((Object)Material.getMaterial((String)"DARK_PRISMARINE_SLAB")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_BRICK_STAIRS")) || b.equals((Object)Material.getMaterial((String)"PRISMARINE_STAIRS")) || b.equals((Object)Material.getMaterial((String)"DARK_PRISMARINE_STAIRS")) || b.equals((Object)Material.getMaterial((String)"STRIPPED_DARK_OAK_LOG")) || b.getId() == 23723 || b.getId() == 30618 || b.getId() == 15437 || b.getId() == 12119 || b.getId() == 19958 || b.getId() == 28350 || b.getId() == 12979 || b.getId() == 28220 || b.getId() == 5307 || b.getId() == 15103 || b.getId() == 17422 || b.getId() == 13668 || b.getId() == 22449 || b.getId() == 12279 || b.getId() == 6214 || b.getId() == 23281 || b.getId() == 26934 || b.getId() == 25317 || b.getId() == 13993 || b.getId() == 6214 || b.getId() == 22591 || b.getId() == 16970 || b.getId() == 14875 || b.getId() == 20108 || b.getId() == 15932 || b.getId() == 9664 || b.getId() == 11376 || b.getId() == 17586 || b.getId() == 31375 || b.getId() == 17095 || b.getId() == 16927 || b.getId() == 10289 || b.getId() == 32585 || b.getId() == 8626 || b.getId() == 18343 || b.getId() == 10355 || b.getId() == 19170 || b.getId() == 25833 || b.getId() == 23048 || b.getId() == 31316 || b.getId() == 12464 || b.getId() == 29151 || b.getId() == 19511 || b.getId() == 18028 || b.getId() == 9116 || b.getId() == 30583 || b.getId() == 8365 || b.getId() == 5755 || b.getId() == 19929 || b.getId() == 10795 || b.getId() == 11112 || b.getId() == 13610 || b.getId() == 17628 || b.getId() == 26150 || b.getId() == 17322 || b.getId() == 27073 || b.getId() == 11387 || b.getId() == 25282 || b.getId() == 22685 || b.getId() == 20382 || b.getId() == 20100 || b.getId() == 28883 || b.getId() == 5128 || b.getId() == 23718 || b.getId() == 18453 || b.getId() == 23375 || b.getId() == 27550 || b.getId() == 13849 || b.getId() == 12966 || b.getId() == 5148 || b.getId() == 23942 || b.getId() == 27189 || b.getId() == 20523 || b.getId() == 6140 || b.getId() == 8838 || b.getId() == 15476 || b.getId() == 18167 || b.getId() == 26672 || b.getId() == 32101 || b.getId() == 31323 || b.getId() == 7577 || b.getId() == 15445 || b.getId() == 19217 || b.getId() == 26511 || b.getId() == 6492;
    }

    public static boolean isClimbableBlock(Material type) {
        SyncCatcher.catchOp("use block math");
        return type == Material.LADDER || type == Material.VINE;
    }

    public static boolean isIce(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.ICE) || type.equals((Object)Material.PACKED_ICE) || type.equals((Object)Material.getMaterial((String)"FROSTED_ICE"));
    }

    public static boolean isLiquid(Material type) {
        SyncCatcher.catchOp("use block math");
        return type == Material.WATER || type == Material.STATIONARY_WATER || type == Material.LAVA || type == Material.STATIONARY_LAVA;
    }

    public static boolean isLava(Material type) {
        SyncCatcher.catchOp("use block math");
        return type == Material.LAVA || type == Material.STATIONARY_LAVA;
    }

    public static boolean isWater(Material type) {
        SyncCatcher.catchOp("use block math");
        return type == Material.WATER || type == Material.STATIONARY_WATER;
    }

    public static boolean isSlab(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.WOOD_STEP) || type.equals((Object)Material.STEP) || type.equals((Object)Material.getMaterial((String)"PRISMARINE_BRICK_SLAB")) || type.equals((Object)Material.getMaterial((String)"PRISMARINE_SLAB")) || type.equals((Object)Material.getMaterial((String)"DARK_PRISMARINE_SLAB")) || type.getId() == 182 || type.getId() == 44 || type.getId() == 126 || type.getId() == 205;
    }

    public static boolean isAllowed(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.getId() == 165 || type.equals((Object)Material.CAULDRON) || type.equals((Object)Material.BREWING_STAND) || type.equals((Object)Material.HOPPER) || type.equals((Object)Material.CARPET) || BlockUtils.isStair(type) || BlockUtils.isPiston(type);
    }

    public static boolean isStair(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.ACACIA_STAIRS) || type.equals((Object)Material.BIRCH_WOOD_STAIRS) || type.equals((Object)Material.BRICK_STAIRS) || type.equals((Object)Material.COBBLESTONE_STAIRS) || type.equals((Object)Material.DARK_OAK_STAIRS) || type.equals((Object)Material.NETHER_BRICK_STAIRS) || type.equals((Object)Material.JUNGLE_WOOD_STAIRS) || type.equals((Object)Material.QUARTZ_STAIRS) || type.equals((Object)Material.SMOOTH_STAIRS) || type.equals((Object)Material.WOOD_STAIRS) || type.equals((Object)Material.SANDSTONE_STAIRS) || type.equals((Object)Material.SPRUCE_WOOD_STAIRS) || type.equals((Object)Material.getMaterial((String)"PRISMARINE_BRICK_STAIRS")) || type.equals((Object)Material.getMaterial((String)"PRISMARINE_STAIRS")) || type.equals((Object)Material.getMaterial((String)"DARK_PRISMARINE_STAIRS")) || type.getId() == 180 || type.getId() == 203;
    }

    public static boolean isPiston(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.PISTON_MOVING_PIECE) || type.equals((Object)Material.PISTON_EXTENSION) || type.equals((Object)Material.PISTON_BASE) || type.equals((Object)Material.PISTON_STICKY_BASE);
    }

    public static boolean isChest(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.CHEST) || type.equals((Object)Material.ENDER_CHEST) || type.equals((Object)Material.TRAPPED_CHEST);
    }

    public static boolean isShulker(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.getId() == 219 || type.getId() == 220 || type.getId() == 221 || type.getId() == 222 || type.getId() == 223 || type.getId() == 224 || type.getId() == 225 || type.getId() == 226 || type.getId() == 227 || type.getId() == 228 || type.getId() == 229 || type.getId() == 230 || type.getId() == 231 || type.getId() == 232 || type.getId() == 233 || type.getId() == 234 || type.getId() == 250;
    }

    public static boolean isBar(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.IRON_FENCE);
    }

    public static boolean isWeb(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.WEB);
    }

    public static boolean isFence(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.FENCE) || type.getId() == 85 || type.getId() == 139 || type.getId() == 113 || type.getId() == 188 || type.getId() == 189 || type.getId() == 190 || type.getId() == 191 || type.getId() == 192 || type.equals((Object)Material.NETHER_FENCE);
    }

    public static boolean containsBlockType(Material[] arrmaterial, Material type) {
        SyncCatcher.catchOp("use block math");
        for (Material material : arrmaterial) {
            if (material != type) continue;
            return true;
        }
        return false;
    }

    public static boolean isSolid(int n) {
        SyncCatcher.catchOp("use block math");
        return BlockUtils.isSolid((byte)n);
    }

    public static double getBlockHeight(Material type) {
        SyncCatcher.catchOp("use block math");
        if (BlockUtils.isSlab(type) || BlockUtils.isStair(type)) {
            return 0.5;
        }
        if (BlockUtils.isFence(type)) {
            return 0.5;
        }
        if (BlockUtils.isChest(type)) {
            return 0.125;
        }
        return 0.0;
    }

    public static boolean isPressure(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.getId() == 70 || type.getId() == 72 || type.getId() == 147 || type.getId() == 148;
    }

    public static boolean isAir(Material type) {
        SyncCatcher.catchOp("use block math");
        return type.equals((Object)Material.AIR);
    }

    public static ArrayList<org.bukkit.block.Block> getSurroundingXZ(org.bukkit.block.Block block, boolean diagonals) {
        SyncCatcher.catchOp("use block math");
        ArrayList<org.bukkit.block.Block> blocks = new ArrayList<org.bukkit.block.Block>();
        if (diagonals) {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.NORTH_EAST));
            blocks.add(block.getRelative(BlockFace.NORTH_WEST));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
            blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        } else {
            blocks.add(block.getRelative(BlockFace.NORTH));
            blocks.add(block.getRelative(BlockFace.SOUTH));
            blocks.add(block.getRelative(BlockFace.EAST));
            blocks.add(block.getRelative(BlockFace.WEST));
        }
        return blocks;
    }

    public static boolean isNearClimbable(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 1)) {
            if (!BlockUtils.isClimbableBlock(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearPistion(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 1)) {
            if (b != Material.PISTON_BASE && b != Material.PISTON_MOVING_PIECE && b != Material.PISTON_STICKY_BASE && b != Material.PISTON_EXTENSION) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearFence(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 1)) {
            if (!BlockUtils.isFence(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearStair(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 1)) {
            if (!BlockUtils.isStair(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearLiquid(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 1)) {
            if (!BlockUtils.isLiquid(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearLava(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 2)) {
            if (!BlockUtils.isLava(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearWater(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 2)) {
            if (!BlockUtils.isWater(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isNearPistion(Player p) {
        return BlockUtils.isNearPistion(p.getLocation());
    }

    public static boolean isNearClimbableBlock(Player p) {
        return BlockUtils.isNearClimbable(p.getLocation());
    }

    public static boolean isNearFence(Player p) {
        return BlockUtils.isNearFence(p.getLocation());
    }

    public static boolean isNearStair(Player p) {
        return BlockUtils.isNearStair(p.getLocation());
    }

    public static boolean isNearLiquid(Player p) {
        return BlockUtils.isNearLiquid(p.getLocation());
    }

    public static boolean isNearLava(Player p) {
        return BlockUtils.isNearLava(p.getLocation());
    }

    public static boolean isNearWater(Player p) {
        return BlockUtils.isNearWater(p.getLocation());
    }

    public static boolean isHalfBlock(Material type) {
        for (String types : HalfBlocksArray) {
            if (!type.toString().toLowerCase().contains(types)) continue;
            return true;
        }
        return false;
    }

    public static boolean B_1_13(Material type) {
        for (String types : Blocks_1_13) {
            if (!type.toString().toLowerCase().equals(types)) continue;
            return true;
        }
        return false;
    }

    public static boolean isLessThanBlock(Material type) {
        for (String types : HalfBlocksArray) {
            if (!type.toString().toLowerCase().contains("chest") && !type.toString().toLowerCase().contains("anvil")) continue;
            return true;
        }
        return false;
    }

    public static boolean allowedPhase(Material type) {
        return type.equals((Object)Material.SIGN) || type.getId() == 425 || type.getId() == 167 || type.getId() == 177 || type.getId() == 176 || type.getId() == 165 || type.equals((Object)Material.FENCE) || type.equals((Object)Material.ANVIL) || type.equals((Object)Material.TRAP_DOOR) || type.equals((Object)Material.SIGN_POST) || type.equals((Object)Material.WALL_SIGN) || type.equals((Object)Material.SUGAR_CANE_BLOCK) || type.equals((Object)Material.WHEAT) || type.equals((Object)Material.POTATO) || type.equals((Object)Material.CARROT) || type.equals((Object)Material.STEP) || type.equals((Object)Material.AIR) || type.equals((Object)Material.WOOD_STEP) || type.equals((Object)Material.SOUL_SAND) || type.equals((Object)Material.CARPET) || type.equals((Object)Material.STONE_PLATE) || type.equals((Object)Material.WOOD_PLATE) || type.equals((Object)Material.LADDER) || type.equals((Object)Material.CHEST) || type.equals((Object)Material.WATER) || type.equals((Object)Material.STATIONARY_WATER) || type.equals((Object)Material.LAVA) || type.equals((Object)Material.STATIONARY_LAVA) || type.equals((Object)Material.REDSTONE_COMPARATOR) || type.equals((Object)Material.REDSTONE_COMPARATOR_OFF) || type.equals((Object)Material.REDSTONE_COMPARATOR_ON) || type.equals((Object)Material.IRON_PLATE) || type.equals((Object)Material.GOLD_PLATE) || type.equals((Object)Material.DAYLIGHT_DETECTOR) || type.equals((Object)Material.STONE_BUTTON) || type.equals((Object)Material.WOOD_BUTTON) || type.equals((Object)Material.HOPPER) || type.equals((Object)Material.RAILS) || type.equals((Object)Material.ACTIVATOR_RAIL) || type.equals((Object)Material.DETECTOR_RAIL) || type.equals((Object)Material.POWERED_RAIL) || type.equals((Object)Material.TRIPWIRE_HOOK) || type.equals((Object)Material.TRIPWIRE) || type.equals((Object)Material.SNOW_BLOCK) || type.equals((Object)Material.REDSTONE_TORCH_OFF) || type.equals((Object)Material.REDSTONE_TORCH_ON) || type.equals((Object)Material.DIODE_BLOCK_OFF) || type.equals((Object)Material.DIODE_BLOCK_ON) || type.equals((Object)Material.DIODE) || type.equals((Object)Material.SEEDS) || type.equals((Object)Material.MELON_SEEDS) || type.equals((Object)Material.PUMPKIN_SEEDS) || type.equals((Object)Material.DOUBLE_PLANT) || type.equals((Object)Material.LONG_GRASS) || type.equals((Object)Material.WEB) || type.equals((Object)Material.SNOW) || type.equals((Object)Material.FLOWER_POT) || type.equals((Object)Material.BREWING_STAND) || type.equals((Object)Material.CAULDRON) || type.equals((Object)Material.CACTUS) || type.equals((Object)Material.WATER_LILY) || type.equals((Object)Material.RED_ROSE) || type.equals((Object)Material.ENCHANTMENT_TABLE) || type.equals((Object)Material.ENDER_PORTAL_FRAME) || type.equals((Object)Material.PORTAL) || type.equals((Object)Material.ENDER_PORTAL) || type.equals((Object)Material.ENDER_CHEST) || type.equals((Object)Material.NETHER_FENCE) || type.equals((Object)Material.NETHER_WARTS) || type.equals((Object)Material.REDSTONE_WIRE) || type.equals((Object)Material.LEVER) || type.equals((Object)Material.YELLOW_FLOWER) || type.equals((Object)Material.CROPS) || type.equals((Object)Material.WATER) || type.equals((Object)Material.LAVA) || type.equals((Object)Material.SKULL) || type.equals((Object)Material.TRAPPED_CHEST) || type.equals((Object)Material.FIRE) || type.equals((Object)Material.BROWN_MUSHROOM) || type.equals((Object)Material.RED_MUSHROOM) || type.equals((Object)Material.DEAD_BUSH) || type.equals((Object)Material.SAPLING) || type.equals((Object)Material.TORCH) || type.equals((Object)Material.MELON_STEM) || type.equals((Object)Material.PUMPKIN_STEM) || type.equals((Object)Material.COCOA) || type.equals((Object)Material.BED) || type.equals((Object)Material.BED_BLOCK) || type.equals((Object)Material.PISTON_EXTENSION) || type.equals((Object)Material.PISTON_MOVING_PIECE) || type.equals((Object)Material.IRON_FENCE) || type.equals((Object)Material.THIN_GLASS) || type.equals((Object)Material.STAINED_GLASS_PANE) || type.equals((Object)Material.COBBLE_WALL);
    }

    public static boolean isSlime(Material type) {
        return type.getId() == 165;
    }

    public static boolean isGrass(Material type) {
        return type.getId() == 2 || type.getId() == 3;
    }

    public static boolean isSign(Material type) {
        return type.getId() == 63 || type.getId() == 68 || type.getId() == 323;
    }

    public static boolean isLog(Material type) {
        return type.getId() == 17 || type.getId() == 162;
    }

    public static AABB getBlockBoundingBox(org.bukkit.block.Block block) {
        if (collisionBoundingBoxes.containsKey(block.getType())) {
            BoundingBox[] newBox = collisionBoundingBoxes.get(block.getType());
            return new AABB(new AABB.Vec3D((float)(newBox[0].minX != -69.0f ? block.getLocation().getX() : 0.0), (float)(newBox[0].minY != -69.0f ? block.getLocation().getY() : 0.0), (float)(newBox[0].minZ != -69.0f ? block.getLocation().getZ() : 0.0)), new AABB.Vec3D((float)(newBox[0].maxX != -69.0f ? block.getLocation().getX() : 0.0), (float)(newBox[0].maxY != -69.0f ? block.getLocation().getY() : 0.0), (float)(newBox[0].maxZ != -69.0f ? block.getLocation().getZ() : 0.0)));
        }
        BoundingBox box = ReflectionUtils.getBlockBoundingBox(block);
        if (box != null) {
            return new AABB(new AABB.Vec3D(box.getMinX(), box.getMinY(), box.getMinZ()), new AABB.Vec3D(box.getMaxX(), box.getMaxY(), box.getMaxZ()));
        }
        return new AABB(new AABB.Vec3D(0.0, 0.0, 0.0), new AABB.Vec3D(0.0, 0.0, 0.0));
    }

    public static boolean isDoor(Material type) {
        return type.equals((Object)Material.IRON_DOOR) || type.equals((Object)Material.IRON_DOOR_BLOCK) || type.equals((Object)Material.WOOD_DOOR) || type.equals((Object)Material.WOODEN_DOOR) || type.getId() == 193 || type.getId() == 194 || type.getId() == 195 || type.getId() == 196 || type.getId() == 197 || type.getId() == 324 || type.getId() == 428 || type.getId() == 429 || type.getId() == 430 || type.getId() == 431;
    }

    public static boolean isBed(Material type) {
        return type.equals((Object)Material.BED_BLOCK) || type.equals((Object)Material.BED);
    }

    public static boolean isTrapDoor(Material type) {
        return type.equals((Object)Material.TRAP_DOOR) || type.getId() == 167;
    }

    public static boolean isFenceGate(Material type) {
        return type.equals((Object)Material.FENCE_GATE) || type.getId() == 183 || type.getId() == 184 || type.getId() == 185 || type.getId() == 186 || type.getId() == 187;
    }

    public static boolean isEdible(Material material) {
        return material.equals((Object)Material.COOKED_BEEF) || material.equals((Object)Material.COOKED_CHICKEN) || material.equals((Object)Material.COOKED_FISH) || material.equals((Object)Material.getMaterial((String)"COOKED_MUTTON")) || material.equals((Object)Material.getMaterial((String)"COOKED_RABBIT")) || material.equals((Object)Material.ROTTEN_FLESH) || material.equals((Object)Material.CARROT_ITEM) || material.equals((Object)Material.CARROT) || material.equals((Object)Material.GOLDEN_APPLE) || material.equals((Object)Material.GOLDEN_CARROT) || material.equals((Object)Material.GRILLED_PORK) || material.equals((Object)Material.RAW_BEEF) || material.equals((Object)Material.RAW_CHICKEN) || material.equals((Object)Material.RAW_FISH) || material.equals((Object)Material.SPIDER_EYE) || material.equals((Object)Material.getMaterial((String)"BEETROOT_SOUP")) || material.equals((Object)Material.MUSHROOM_SOUP) || material.equals((Object)Material.POTATO) || material.equals((Object)Material.POTATO_ITEM) || material.equals((Object)Material.BAKED_POTATO) || material.equals((Object)Material.POISONOUS_POTATO) || material.equals((Object)Material.PUMPKIN_PIE) || material.equals((Object)Material.APPLE) || material.equals((Object)Material.getMaterial((String)"MUTTON")) || material.equals((Object)Material.getMaterial((String)"RABBIT")) || material.equals((Object)Material.MELON) || material.equals((Object)Material.getMaterial((String)"CHORUS_FRUIT")) || material.equals((Object)Material.COOKIE) || material.equals((Object)Material.POTION);
    }

    private void setupCollisionBB() {
        collisionBoundingBoxes.put(Material.BREWING_STAND, new BoundingBox[]{new BoundingBox(0.4375f, 0.0f, 0.4375f, 0.5625f, 0.875f, 0.5625f), new BoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f)});
        Arrays.stream(Material.values()).filter(material -> material.name().contains("FENCE") && !material.name().contains("GATE")).forEach(material -> collisionBoundingBoxes.put((Material)material, new BoundingBox[]{new BoundingBox(-69.0f, 0.0f, -69.0f, -69.0f, 1.5f, -69.0f), new BoundingBox(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)}));
        collisionBoundingBoxes.put(Material.STATIONARY_LAVA, new BoundingBox[]{new BoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f), new BoundingBox(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)});
    }

    public static ArrayList<Material> getBlocksAroundCenter(Location loc, int radius) {
        SyncCatcher.catchOp("use block math");
        ArrayList<Material> blocks = new ArrayList<Material>();
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; ++x) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; ++y) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; ++z) {
                    Location l = new Location(loc.getWorld(), (double)x, (double)y, (double)z);
                    if (!(l.distance(loc) <= (double)radius)) continue;
                    blocks.add(BlockUtils.getBlockType(l.getWorld(), l));
                }
            }
        }
        return blocks;
    }

    public static ArrayList<Material> getBlocksAroundCenterType(Location loc, int radius) {
        SyncCatcher.catchOp("use block math");
        ArrayList<Material> types = new ArrayList<Material>();
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; ++x) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; ++y) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; ++z) {
                    Location l = new Location(loc.getWorld(), (double)x, (double)y, (double)z);
                    if (!(l.distance(loc) <= (double)radius)) continue;
                    types.add(l.getBlock().getType());
                }
            }
        }
        return types;
    }

    public static boolean checkPhase(Material m) {
        int[] whitelist;
        for (int ids : whitelist = new int[]{355, 196, 194, 197, 195, 193, 64, 96, 187, 184, 186, 107, 185, 183, 192, 189, 139, 191, 85, 101, 190, 113, 188, 160, 102, 163, 157, 0, 145, 49, 77, 135, 108, 67, 164, 136, 114, 156, 180, 128, 143, 109, 134, 53, 126, 44, 416, 8, 425, 138, 26, 397, 372, 135, 117, 108, 39, 81, 92, 71, 171, 141, 118, 144, 54, 139, 67, 127, 59, 115, 330, 164, 151, 178, 32, 28, 93, 94, 175, 122, 116, 130, 119, 120, 51, 140, 147, 154, 148, 136, 65, 10, 69, 31, 105, 114, 372, 33, 34, 36, 29, 90, 142, 27, 104, 156, 66, 40, 330, 38, 180, 149, 150, 75, 76, 55, 128, 6, 295, 323, 63, 109, 78, 88, 134, 176, 11, 9, 44, 70, 182, 83, 50, 146, 132, 131, 106, 177, 68, 8, 111, 30, 72, 53, 126, 37}) {
            if (m.getId() != ids) continue;
            return true;
        }
        return false;
    }

    public static ArrayList<Material> getSurrounding(Location loc, boolean diagonals) {
        SyncCatcher.catchOp("use block math");
        ArrayList<Material> blocks = new ArrayList<Material>();
        if (diagonals) {
            // empty if block
        }
        blocks.add(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.3, 0.0)));
        blocks.add(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, -0.3, 0.0)));
        blocks.add(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.3, 0.0, 0.0)));
        blocks.add(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(-0.3, 0.0, 0.0)));
        blocks.add(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, 0.3)));
        blocks.add(BlockUtils.getBlockType(loc.getWorld(), loc.clone().add(0.0, 0.0, -0.3)));
        return blocks;
    }

    public static boolean isSolidBlockBehindPlayer(Player player) {
        SyncCatcher.catchOp("use block math");
        for (int x = player.getLocation().getBlockX() - 1; x <= player.getLocation().getBlockX() + 1; ++x) {
            for (int y = player.getLocation().getBlockY(); y <= player.getLocation().getBlockY() + 1; ++y) {
                for (int z = player.getLocation().getBlockZ() - 1; z <= player.getLocation().getBlockZ() + 1; ++z) {
                    Location loc = new Location(player.getWorld(), (double)x, (double)y, (double)z);
                    if (!BlockUtils.isSolid(BlockUtils.getBlockType(loc.getWorld(), x, y, z))) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public static Material getBlockBehindPlayer(Location playerLocation) {
        SyncCatcher.catchOp("use block math");
        Location location = playerLocation.clone().add(0.0, 1.0, 0.0);
        if (((CraftWorld)location.getWorld()).getHandle().getChunkIfLoaded(location.getBlockX(), location.getBlockZ()) != null) {
            return null;
        }
        Vector direction = location.getDirection().multiply(new Vector(-1, 0, -1));
        location.add(direction);
        return BlockUtils.getBlockType(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Material getBlockBehindPlayer(Player player) {
        if (player == null) {
            return null;
        }
        return BlockUtils.getBlockBehindPlayer(player.getLocation());
    }

    public static boolean isSolid(Material type) {
        return BlockUtils.isSolid(type.getId());
    }

    public static boolean isNearSlime(Location loc) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(loc, 3)) {
            if (b != Material.SLIME_BLOCK) continue;
            out = true;
        }
        return out;
    }

    public static List<Material> getNearbyBlocks(Location location, int radius) {
        SyncCatcher.catchOp("use block math");
        ArrayList<Material> blocks = new ArrayList<Material>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(BlockUtils.getBlockType(location.getWorld(), x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Pair<org.bukkit.block.Block, Block>> getNearbyBlocksBukkitAndNMS(Location location, int radius) {
        SyncCatcher.catchOp("use block math");
        ArrayList<Pair<org.bukkit.block.Block, Block>> blocks = new ArrayList<Pair<org.bukkit.block.Block, Block>>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; ++x) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; ++y) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; ++z) {
                    blocks.add(new Pair<org.bukkit.block.Block, Block>(BlockUtils.getBlockAsync(new Location(location.getWorld(), (double)x, (double)y, (double)z)), BlockUtils.getBlock(location.getWorld(), x, y, z)));
                }
            }
        }
        return blocks;
    }

    public static boolean isOnBlock(Location location, int n, Material[] arrmaterial) {
        SyncCatcher.catchOp("use block math");
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = MathUtils.getFraction(d) > 0.0 ? Math.abs(MathUtils.getFraction(d)) : 1.0 - Math.abs(MathUtils.getFraction(d));
        double d4 = MathUtils.getFraction(d2) > 0.0 ? Math.abs(MathUtils.getFraction(d2)) : 1.0 - Math.abs(MathUtils.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        org.bukkit.World world = location.getWorld();
        if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
            }
        } else if (d3 > 0.7) {
            if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
            }
        } else if (d4 < 0.3 ? BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 - 1))) : d4 > 0.7 && BlockUtils.containsBlockType(arrmaterial, new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 + 1)))) {
            return true;
        }
        return false;
    }

    public static boolean containsBlockType(Material[] arrmaterial, Location location) {
        SyncCatcher.catchOp("use block math");
        if (((CraftWorld)location.getWorld()).getHandle().getChunkIfLoaded(location.getBlockX(), location.getBlockZ()) != null) {
            return false;
        }
        for (Material material : arrmaterial) {
            if (material != BlockUtils.getBlockType(location.getWorld(), location)) continue;
            return true;
        }
        return false;
    }

    public static boolean isOnBlock(Player player, int n, Material[] arrmaterial) {
        return BlockUtils.isOnBlock(player.getLocation(), n, arrmaterial);
    }

    public static boolean isHoveringOverWater(Location location, int n) {
        SyncCatcher.catchOp("use block math");
        double d = location.getX();
        double d2 = location.getZ();
        double d3 = MathUtils.getFraction(d) > 0.0 ? Math.abs(MathUtils.getFraction(d)) : 1.0 - Math.abs(MathUtils.getFraction(d));
        double d4 = MathUtils.getFraction(d2) > 0.0 ? Math.abs(MathUtils.getFraction(d2)) : 1.0 - Math.abs(MathUtils.getFraction(d2));
        int n2 = location.getBlockX();
        int n3 = location.getBlockY() - n;
        int n4 = location.getBlockZ();
        org.bukkit.World world = location.getWorld();
        if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)n4))) {
            return true;
        }
        if (d3 < 0.3) {
            if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
            }
        } else if (d3 > 0.7) {
            if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)n4))) {
                return true;
            }
            if (d4 < 0.3) {
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 - 1)))) {
                    return true;
                }
            } else if (d4 > 0.7) {
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 - 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
                if (BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)(n2 + 1), (double)n3, (double)(n4 + 1)))) {
                    return true;
                }
            }
        } else if (d4 < 0.3 ? BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 - 1))) : d4 > 0.7 && BlockUtils.isOnLiquid(new Location(location.getWorld(), (double)n2, (double)n3, (double)(n4 + 1)))) {
            return true;
        }
        return false;
    }

    public static boolean isHoveringOverWater(Player player, int n) {
        SyncCatcher.catchOp("use block math");
        return BlockUtils.isHoveringOverWater(player.getLocation(), n);
    }

    public static boolean isOnGround(Location location) {
        return BlockUtils.isOnGround(location, 0) || BlockUtils.isOnGround(location, 1);
    }

    public static boolean isOnGround(Location location, int down) {
        SyncCatcher.catchOp("use block math");
        double posX = location.getX();
        double posZ = location.getZ();
        double fracX = posX % 1.0 > 0.0 ? Math.abs(posX % 1.0) : 1.0 - Math.abs(posX % 1.0);
        double fracZ = posZ % 1.0 > 0.0 ? Math.abs(posZ % 1.0) : 1.0 - Math.abs(posZ % 1.0);
        int blockX = location.getBlockX();
        int blockY = (location.getY() % 1.0 == 0.0 ? location.getBlockY() : location.getBlockY() + 1) - down;
        int blockZ = location.getBlockZ();
        org.bukkit.World world = location.getWorld();
        if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ).getId())) {
            return true;
        }
        if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ).getId())) {
            return true;
        }
        if (fracX < 0.3) {
            if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ).getId())) {
                return true;
            }
            if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ).getId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
            } else if (fracZ > 0.7) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
            }
        } else if (fracX > 0.7) {
            if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ).getId())) {
                return true;
            }
            if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ).getId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
            } else if (fracZ > 0.7) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
            }
        } else if (fracZ < 0.3) {
            if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                return true;
            }
            if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                return true;
            }
        } else if (fracZ > 0.7 && !blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId()) && allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
            return true;
        }
        return false;
    }

    public static boolean isOnGroundBlock(Location location) {
        return BlockUtils.isOnGroundBlock(location, 0) || BlockUtils.isOnGroundBlock(location, 1);
    }

    public static boolean isOnGroundBlock(Location location, int down) {
        SyncCatcher.catchOp("use block math");
        double posX = location.getX();
        double posZ = location.getZ();
        double fracX = posX % 1.0 > 0.0 ? Math.abs(posX % 1.0) : 1.0 - Math.abs(posX % 1.0);
        double fracZ = posZ % 1.0 > 0.0 ? Math.abs(posZ % 1.0) : 1.0 - Math.abs(posZ % 1.0);
        int blockX = location.getBlockX();
        int blockY = location.getBlockY() - down;
        int blockZ = location.getBlockZ();
        org.bukkit.World world = location.getWorld();
        if (((CraftWorld)location.getWorld()).getHandle().getChunkIfLoaded(location.getBlockX(), location.getBlockZ()) != null) {
            return false;
        }
        if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ).getId())) {
            return true;
        }
        if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ).getId())) {
            return true;
        }
        if (fracX < 0.3) {
            if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ).getId())) {
                return true;
            }
            if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ).getId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
            } else if (fracZ > 0.7) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
            }
        } else if (fracX > 0.7) {
            if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ).getId())) {
                return true;
            }
            if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ).getId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
            } else if (fracZ > 0.7) {
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
            }
        } else if (fracZ < 0.3) {
            if (!blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                return true;
            }
            if (allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                return true;
            }
        } else if (fracZ > 0.7 && !blockSolidPassSet.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId()) && allowed.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
            return true;
        }
        return false;
    }

    public static boolean isNearSlab(Location location) {
        SyncCatcher.catchOp("use block math");
        boolean out = false;
        for (Material b : BlockUtils.getNearbyBlocks(location, 1)) {
            if (!BlockUtils.isSlab(b)) continue;
            out = true;
        }
        return out;
    }

    public static boolean isOnStairs(Location location) {
        return BlockUtils.isUnderBlock(location, blockStairsSet, 0) || BlockUtils.isUnderBlock(location, blockStairsSet, 1);
    }

    public static AABB[] getCollisionBoxes(Block b, Location loc) {
        if (b instanceof BlockCarpet) {
            AABB[] aabbarr = new AABB[]{new AABB(AABB.Vec3D.fromVector(loc.toVector()), AABB.Vec3D.fromVector(loc.toVector().add(new Vector(1, 0, 1))))};
            return aabbarr;
        }
        if (b instanceof BlockSnow && (Integer)b.getBlockData().get((IBlockState)BlockSnow.LAYERS) == 1) {
            AABB[] aabbarr = new AABB[]{new AABB(AABB.Vec3D.fromVector(loc.toVector()), AABB.Vec3D.fromVector(loc.toVector().add(new Vector(1, 0, 1))))};
            return aabbarr;
        }
        ArrayList bbs = new ArrayList();
        AxisAlignedBB cube = AxisAlignedBB.a((double)loc.getBlockX(), (double)loc.getBlockY(), (double)loc.getBlockZ(), (double)(loc.getBlockX() + 1), (double)(loc.getBlockY() + 1), (double)(loc.getBlockZ() + 1));
        b.a((World)((CraftWorld)loc.getWorld()).getHandle(), new BlockPosition(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), b.getBlockData(), cube, bbs, null);
        AABB[] collisionBoxes = new AABB[bbs.size()];
        for (int i = 0; i < bbs.size(); ++i) {
            AABB collisionBox;
            AxisAlignedBB bb = (AxisAlignedBB)bbs.get(i);
            collisionBoxes[i] = collisionBox = new AABB(AABB.Vec3D.fromVector(new Vector(bb.a, bb.b, bb.c)), AABB.Vec3D.fromVector(new Vector(bb.d, bb.e, bb.f)));
        }
        return collisionBoxes;
    }

    public static boolean isUnderBlock(Location location) {
        return BlockUtils.isOnGround(location, -2);
    }

    private static boolean isUnderBlock(Location location, Set<Byte> itemIDs, int down) {
        SyncCatcher.catchOp("use block math");
        double posX = location.getX();
        double posZ = location.getZ();
        double fracX = posX % 1.0 > 0.0 ? Math.abs(posX % 1.0) : 1.0 - Math.abs(posX % 1.0);
        double fracZ = posZ % 1.0 > 0.0 ? Math.abs(posZ % 1.0) : 1.0 - Math.abs(posZ % 1.0);
        int blockX = location.getBlockX();
        int blockY = location.getBlockY() - down;
        int blockZ = location.getBlockZ();
        org.bukkit.World world = location.getWorld();
        if (((CraftWorld)location.getWorld()).getHandle().getChunkIfLoaded(location.getBlockX(), location.getBlockZ()) != null) {
            return false;
        }
        if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ).getId())) {
            return true;
        }
        if (fracX < 0.3) {
            if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ).getId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
            } else if (fracZ > 0.7) {
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
            }
        } else if (fracX > 0.7) {
            if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ).getId())) {
                return true;
            }
            if (fracZ < 0.3) {
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ - 1).getId())) {
                    return true;
                }
            } else if (fracZ > 0.7) {
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX - 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
                    return true;
                }
                if (itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX + 1, blockY, blockZ + 1).getId())) {
                    return true;
                }
            }
        } else if (fracZ < 0.3 ? itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ - 1).getId()) : fracZ > 0.7 && itemIDs.contains((byte)BlockUtils.getBlockType(world, blockX, blockY, blockZ + 1).getId())) {
            return true;
        }
        return false;
    }

    public static boolean isOnLiquid(Location location) {
        return BlockUtils.isUnderBlock(location, blockLiquidsSet, 0) || BlockUtils.isUnderBlock(location, blockLiquidsSet, 1);
    }

    public static boolean isOnWeb(Location location) {
        return BlockUtils.isUnderBlock(location, blockWebsSet, 0) || BlockUtils.isUnderBlock(location, blockWebsSet, 1);
    }

    public static boolean isOnIce(Location location) {
        return BlockUtils.isUnderBlock(location, blockIceSet, 0) || BlockUtils.isUnderBlock(location, blockIceSet, 1);
    }

    public static BlockPosition toPosition(Location location) {
        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public static Material getBlockType(org.bukkit.World world, int x, int y, int z) {
        return BlockUtils.getBlockType(world, new BlockPosition(x, y, z));
    }

    public static Material getBlockType(org.bukkit.World world, Location location) {
        return BlockUtils.getBlockType(world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public static Material getBlockType(org.bukkit.World world, BlockPosition blockPosition) {
        SyncCatcher.catchOp("use block math");
        WorldServer worldServer = ((CraftWorld)world).getHandle();
        if (worldServer.isLoaded(blockPosition)) {
            return CraftMagicNumbers.getMaterial((Block)worldServer.getType(blockPosition).getBlock());
        }
        return Material.AIR;
    }

    public static org.bukkit.block.Block getBlockAsync(Location loc) {
        if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4)) {
            return loc.getBlock();
        }
        return null;
    }

    public static Block getBlock(org.bukkit.World world, int x, int y, int z) {
        return BlockUtils.getBlock(world, new BlockPosition(x, y, z));
    }

    public static Block getBlock(org.bukkit.World world, Location location) {
        return BlockUtils.getBlock(world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }

    public static Block getBlock(org.bukkit.World world, BlockPosition blockPosition) {
        SyncCatcher.catchOp("use block math");
        WorldServer worldServer = ((CraftWorld)world).getHandle();
        if (worldServer.isLoaded(blockPosition)) {
            return worldServer.getType(blockPosition).getBlock();
        }
        return null;
    }

    public static float getBlockDamage(Player player, org.bukkit.World world, BlockPosition blockPosition) {
        SyncCatcher.catchOp("use block math");
        BlockPosition blockPosition2 = new BlockPosition(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
        WorldServer worldServer = ((CraftWorld)world).getHandle();
        if (worldServer.isLoaded(blockPosition2)) {
            EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
            Block block = worldServer.getType(blockPosition2).getBlock();
            return block.getDamage((EntityHuman)entityPlayer, (World)worldServer, blockPosition2);
        }
        return 0.0f;
    }

    static {
        blockSolidPassSet.add((byte)0);
        blockSolidPassSet.add((byte)6);
        blockSolidPassSet.add((byte)8);
        blockSolidPassSet.add((byte)9);
        blockSolidPassSet.add((byte)10);
        blockSolidPassSet.add((byte)11);
        blockSolidPassSet.add((byte)27);
        blockSolidPassSet.add((byte)28);
        blockSolidPassSet.add((byte)30);
        blockSolidPassSet.add((byte)31);
        blockSolidPassSet.add((byte)32);
        blockSolidPassSet.add((byte)37);
        blockSolidPassSet.add((byte)38);
        blockSolidPassSet.add((byte)39);
        blockSolidPassSet.add((byte)40);
        blockSolidPassSet.add((byte)50);
        blockSolidPassSet.add((byte)51);
        blockSolidPassSet.add((byte)55);
        blockSolidPassSet.add((byte)59);
        blockSolidPassSet.add((byte)63);
        blockSolidPassSet.add((byte)66);
        blockSolidPassSet.add((byte)68);
        blockSolidPassSet.add((byte)69);
        blockSolidPassSet.add((byte)70);
        blockSolidPassSet.add((byte)72);
        blockSolidPassSet.add((byte)75);
        blockSolidPassSet.add((byte)76);
        blockSolidPassSet.add((byte)77);
        blockSolidPassSet.add((byte)78);
        blockSolidPassSet.add((byte)83);
        blockSolidPassSet.add((byte)90);
        blockSolidPassSet.add((byte)104);
        blockSolidPassSet.add((byte)105);
        blockSolidPassSet.add((byte)115);
        blockSolidPassSet.add((byte)119);
        blockSolidPassSet.add((byte)-124);
        blockSolidPassSet.add((byte)-113);
        blockSolidPassSet.add((byte)-81);
        blockStairsSet.add((byte)53);
        blockStairsSet.add((byte)67);
        blockStairsSet.add((byte)108);
        blockStairsSet.add((byte)109);
        blockStairsSet.add((byte)114);
        blockStairsSet.add((byte)-128);
        blockStairsSet.add((byte)-122);
        blockStairsSet.add((byte)-121);
        blockStairsSet.add((byte)-120);
        blockStairsSet.add((byte)-100);
        blockStairsSet.add((byte)-93);
        blockStairsSet.add((byte)-92);
        blockStairsSet.add((byte)126);
        blockStairsSet.add((byte)-76);
        blockLiquidsSet.add((byte)8);
        blockLiquidsSet.add((byte)9);
        blockLiquidsSet.add((byte)10);
        blockLiquidsSet.add((byte)11);
        blockWebsSet.add((byte)30);
        blockIceSet.add((byte)79);
        blockIceSet.add((byte)-82);
        allowed.add(Material.SIGN);
        allowed.add(Material.FENCE);
        allowed.add(Material.ANVIL);
        allowed.add(Material.TRAP_DOOR);
        allowed.add(Material.SIGN_POST);
        allowed.add(Material.WALL_SIGN);
        allowed.add(Material.SUGAR_CANE_BLOCK);
        allowed.add(Material.WHEAT);
        allowed.add(Material.POTATO);
        allowed.add(Material.CARROT);
        allowed.add(Material.STEP);
        allowed.add(Material.AIR);
        allowed.add(Material.WOOD_STEP);
        allowed.add(Material.SOUL_SAND);
        allowed.add(Material.CARPET);
        allowed.add(Material.STONE_PLATE);
        allowed.add(Material.WOOD_PLATE);
        allowed.add(Material.LADDER);
        allowed.add(Material.CHEST);
        allowed.add(Material.WATER);
        allowed.add(Material.STATIONARY_WATER);
        allowed.add(Material.LAVA);
        allowed.add(Material.STATIONARY_LAVA);
        allowed.add(Material.REDSTONE_COMPARATOR);
        allowed.add(Material.REDSTONE_COMPARATOR_OFF);
        allowed.add(Material.REDSTONE_COMPARATOR_ON);
        allowed.add(Material.IRON_PLATE);
        allowed.add(Material.GOLD_PLATE);
        allowed.add(Material.DAYLIGHT_DETECTOR);
        allowed.add(Material.STONE_BUTTON);
        allowed.add(Material.WOOD_BUTTON);
        allowed.add(Material.HOPPER);
        allowed.add(Material.RAILS);
        allowed.add(Material.ACTIVATOR_RAIL);
        allowed.add(Material.DETECTOR_RAIL);
        allowed.add(Material.POWERED_RAIL);
        allowed.add(Material.TRIPWIRE_HOOK);
        allowed.add(Material.TRIPWIRE);
        allowed.add(Material.SNOW_BLOCK);
        allowed.add(Material.REDSTONE_TORCH_OFF);
        allowed.add(Material.REDSTONE_TORCH_ON);
        allowed.add(Material.DIODE_BLOCK_OFF);
        allowed.add(Material.DIODE_BLOCK_ON);
        allowed.add(Material.DIODE);
        allowed.add(Material.SEEDS);
        allowed.add(Material.MELON_SEEDS);
        allowed.add(Material.PUMPKIN_SEEDS);
        allowed.add(Material.DOUBLE_PLANT);
        allowed.add(Material.LONG_GRASS);
        allowed.add(Material.WEB);
        allowed.add(Material.SNOW);
        allowed.add(Material.FLOWER_POT);
        allowed.add(Material.BREWING_STAND);
        allowed.add(Material.CAULDRON);
        allowed.add(Material.CACTUS);
        allowed.add(Material.WATER_LILY);
        allowed.add(Material.RED_ROSE);
        allowed.add(Material.ENCHANTMENT_TABLE);
        allowed.add(Material.ENDER_PORTAL_FRAME);
        allowed.add(Material.PORTAL);
        allowed.add(Material.ENDER_PORTAL);
        allowed.add(Material.ENDER_CHEST);
        allowed.add(Material.NETHER_FENCE);
        allowed.add(Material.NETHER_WARTS);
        allowed.add(Material.REDSTONE_WIRE);
        allowed.add(Material.LEVER);
        allowed.add(Material.YELLOW_FLOWER);
        allowed.add(Material.CROPS);
        allowed.add(Material.WATER);
        allowed.add(Material.LAVA);
        allowed.add(Material.SKULL);
        allowed.add(Material.TRAPPED_CHEST);
        allowed.add(Material.FIRE);
        allowed.add(Material.BROWN_MUSHROOM);
        allowed.add(Material.RED_MUSHROOM);
        allowed.add(Material.DEAD_BUSH);
        allowed.add(Material.SAPLING);
        allowed.add(Material.TORCH);
        allowed.add(Material.MELON_STEM);
        allowed.add(Material.PUMPKIN_STEM);
        allowed.add(Material.COCOA);
        allowed.add(Material.BED);
        allowed.add(Material.BED_BLOCK);
        allowed.add(Material.PISTON_EXTENSION);
        allowed.add(Material.PISTON_MOVING_PIECE);
        semi.add(Material.IRON_FENCE);
        semi.add(Material.THIN_GLASS);
        semi.add(Material.STAINED_GLASS_PANE);
        semi.add(Material.COBBLE_WALL);
        blockedPearlTypes.add(Material.IRON_FENCE);
        blockedPearlTypes.add(Material.FENCE);
        blockedPearlTypes.add(Material.NETHER_FENCE);
        blockedPearlTypes.add(Material.FENCE_GATE);
        blockedPearlTypes.add(Material.ACACIA_STAIRS);
        blockedPearlTypes.add(Material.BIRCH_WOOD_STAIRS);
        blockedPearlTypes.add(Material.BRICK_STAIRS);
        blockedPearlTypes.add(Material.COBBLESTONE_STAIRS);
        blockedPearlTypes.add(Material.DARK_OAK_STAIRS);
        blockedPearlTypes.add(Material.JUNGLE_WOOD_STAIRS);
        blockedPearlTypes.add(Material.NETHER_BRICK_STAIRS);
        blockedPearlTypes.add(Material.QUARTZ_STAIRS);
        blockedPearlTypes.add(Material.SANDSTONE_STAIRS);
        blockedPearlTypes.add(Material.SMOOTH_STAIRS);
        blockedPearlTypes.add(Material.SPRUCE_WOOD_STAIRS);
        blockedPearlTypes.add(Material.WOOD_STAIRS);
        allowed.add(Material.BANNER);
        allowed.add(Material.IRON_TRAPDOOR);
        allowed.add(Material.WALL_BANNER);
        allowed.add(Material.STANDING_BANNER);
        HalfBlocksArray = new String[]{"pot", "flower", "step", "slab", "snow", "detector", "daylight", "comparator", "repeater", "diode", "water", "lava", "ladder", "vine", "carpet", "sign", "pressure", "plate", "button", "mushroom", "torch", "frame", "armor", "banner", "lever", "hook", "redstone", "rail", "brewing", "rose", "skull", "enchantment", "cake", "bed"};
        Blocks_1_13 = new String[]{"tube_coral_block", "brain_coral_block", "bubble_coral_block", "fire_coral_block", "horn_coral_block", "dead_tube_coral_block", "dead_brain_coral_block", "dead_bubble_coral_block", "dead_fire_coral_block", "dead_horn_coral_block", "coral_block", "cave_air", "void_air", "blue_ice", "stone_button", "oak_button", "spruce_button", "birch_button", "jungle_button", "acacia_button", "dark_oak_button", "stone_pressure_plate", "heavy_weighted_pressure_plate", "light_weighted_pressure_plate", "oak_pressure_plate", "spruce_pressure_plate", "birch_pressure_plate", "jungle_pressure_plate", "acacia_pressure_plate", "dark_oak_pressure_plate", "iron_trapdoor", "oak_trapdoor", "spruce_trapdoor", "birch_trapdoor", "jungle_trapdoor", "acacia_trapdoor", "dark_oak_trapdoor", "pumpkin", "carved_pumpkin", "tube_coral", "brain_coral", "bubble_coral", "fire_coral", "horn_coral", "dead_tube_coral", "dead_brain_coral", "dead_bubble_coral", "dead_fire_coral", "dead_horn_coral", "tube_coral_fan\t", "bubble_coral_fan", "fire_coral_fan", "horn_coral_fan", "dead_tube_coral_fan", "dead_brain_coral_fan", "dead_bubble_coral_fan", "dead_fire_coral_fan", "dead_horn_coral_fan", "tube_coral_wall_fan", "brain_coral_wall_fan", "bubble_coral_wall_fan", "fire_coral_wall_fan", "horn_coral_wall_fan", "dead_tube_coral_wall_fan", "dead_brain_coral_wall_fan", "dead_bubble_coral_wall_fan", "dead_fire_coral_wall_fan", "dead_horn_coral_wall_fan", "brain_coral_fan", "dried_kelp_block", "conduit", "seagrass", "tall_seagrass", "stripped_oak_log", "stripped_spruce_log", "stripped_birch_log", "stripped_jungle_log", "stripped_acacia_log", "prismarine_brick_slab", "prismarine_slab", "dark_prismarine_slab", "prismarine_brick_stairs", "prismarine_stairs", "dark_prismarine_stairs", "stripped_dark_oak_log", "prismarine_brick_slab", "turtle_egg", "prismarine_slab", "dark_prismarine_slab", "prismarine_brick_stairs", "prismarine_stairs", "dark_prismarine_stairs", "stripped_dark_oak_log"};
    }
}

