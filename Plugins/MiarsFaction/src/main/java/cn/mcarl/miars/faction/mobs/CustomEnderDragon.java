package cn.mcarl.miars.faction.mobs;

import cc.carm.lib.easyplugin.utils.ColorParser;
import cn.mcarl.miars.faction.conf.PluginConfig;
import cn.mcarl.miars.faction.utils.Schematic;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.data.DataException;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Iterator;

/**
 * @Author: carl0
 * @DATE: 2022/6/27 12:44
 */
public class CustomEnderDragon extends EntityEnderDragon {
    public CustomEnderDragon(World world) {
        super(world);
    }

    @Override
    protected void aZ() {
        if (!this.dead) {
            ++this.by;
            if (this.by >= 180 && this.by <= 200) {
                float f = (this.random.nextFloat() - 0.5F) * 8.0F;
                float f1 = (this.random.nextFloat() - 0.5F) * 4.0F;
                float f2 = (this.random.nextFloat() - 0.5F) * 8.0F;
                this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.locX + (double)f, this.locY + 2.0 + (double)f1, this.locZ + (double)f2, 0.0, 0.0, 0.0, new int[0]);
            }

            boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
            int i;
            int j;
            if (!this.world.isClientSide) {
                if (this.by > 150 && this.by % 5 == 0 && flag) {
                    i = this.expToDrop / 12;

                    while(i > 0) {
                        j = EntityExperienceOrb.getOrbValue(i);
                        i -= j;
                        this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                    }
                }

                if (this.by == 1) {
                    int viewDistance = this.world.getServer().getViewDistance() * 16;
                    Iterator var6 = MinecraftServer.getServer().getPlayerList().players.iterator();

                    label56:
                    while(true) {
                        EntityPlayer player;
                        double deltaX;
                        double deltaZ;
                        double distanceSquared;
                        do {
                            if (!var6.hasNext()) {
                                break label56;
                            }

                            player = (EntityPlayer)var6.next();
                            deltaX = this.locX - player.locX;
                            deltaZ = this.locZ - player.locZ;
                            distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                        } while(this.world.spigotConfig.dragonDeathSoundRadius > 0 && distanceSquared > (double)(this.world.spigotConfig.dragonDeathSoundRadius * this.world.spigotConfig.dragonDeathSoundRadius));

                        if (distanceSquared > (double)(viewDistance * viewDistance)) {
                            double deltaLength = Math.sqrt(distanceSquared);
                            double relativeX = player.locX + deltaX / deltaLength * (double)viewDistance;
                            double relativeZ = player.locZ + deltaZ / deltaLength * (double)viewDistance;
                            player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1018, new BlockPosition((int)relativeX, (int)this.locY, (int)relativeZ), 0, true));
                        } else {
                            player.playerConnection.sendPacket(new PacketPlayOutWorldEvent(1018, new BlockPosition((int)this.locX, (int)this.locY, (int)this.locZ), 0, true));
                        }
                    }
                }
            }

            this.move(0.0, 0.10000000149011612, 0.0);
            this.aI = this.yaw += 20.0F;
            if (this.by == 200 && !this.world.isClientSide) {
                if (flag) {
                    i = this.expToDrop - 10 * this.expToDrop / 12;

                    while(i > 0) {
                        j = EntityExperienceOrb.getOrbValue(i);
                        i -= j;
                        this.world.addEntity(new EntityExperienceOrb(this.world, this.locX, this.locY, this.locZ, j));
                    }
                }
                try {
                    Schematic.getInstance().setSchematic(new Location(Bukkit.getWorld(PluginConfig.PROTECTED_REGION.WORLD_NAME.get()+"_the_end"), 0, 70, 0), "schematics/NetherPvp.schematic");
                    for (Player player:Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ColorParser.parse("&5&l末地! &7末影龙被击杀，末地竞技场已开启..."));
                    }
                } catch (DataException | IOException | MaxChangedBlocksException ex) {
                    throw new RuntimeException(ex);
                }
                this.die();
            }

        }
    }

}
