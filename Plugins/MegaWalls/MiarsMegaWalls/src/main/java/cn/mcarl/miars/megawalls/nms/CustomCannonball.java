package cn.mcarl.miars.megawalls.nms;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

public class CustomCannonball extends EntityArmorStand {
   public double dirX;
   public double dirY;
   public double dirZ;
   private int e = -1;
   private int f = -1;
   private int g = -1;
   private Block h;
   private boolean i;
   private int ar;
   private int as;
   private Player shooter;

   public CustomCannonball(World world, double d0, double d1, double d2) {
      super(world);
      this.setPosition(d0, d1, d2);
      ArmorStand as = (ArmorStand)this.getBukkitEntity();
      as.setGravity(true);
      as.setVisible(false);
      as.setHelmet(createSkull());
      this.setDirection(d0, d1, d2);
   }

   private static ItemStack createSkull() {
      ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
      SkullMeta meta = (SkullMeta)item.getItemMeta();
      GameProfile gp = new GameProfile(UUID.randomUUID(), (String)null);
      gp.getProperties().put("textures", new Property("textures", "eyJ0aW1lc3RhbXAiOjE1NDk0Mzc4NDUyNzYsInByb2ZpbGVJZCI6ImRhNzQ2NWVkMjljYjRkZTA5MzRkOTIwMTc0NDkxMzU1IiwicHJvZmlsZU5hbWUiOiJLaWRTbGljZXIiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VjMzk1Mzc0OTc5MGQ1M2NlODkwZmVlN2UzMDY5NzQwNTIwZmJiMjQyNjJmY2VlNWZjNjAwNGNmODcyMDVhZDkifX19"));

      try {
         Field field = meta.getClass().getDeclaredField("profile");
         field.setAccessible(true);
         field.set(meta, gp);
      } catch (Exception var4) {
      }

      item.setItemMeta(meta);
      return item;
   }

   public Player getShooter() {
      return this.shooter;
   }

   public void setShooter(Player shooter) {
      this.shooter = shooter;
   }

   public void setDirection(double d0, double d1, double d2) {
      d0 += this.random.nextGaussian() * 0.4D;
      d1 += this.random.nextGaussian() * 0.4D;
      d2 += this.random.nextGaussian() * 0.4D;
      double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
      this.dirX = d0 / d3 * 0.1D;
      this.dirY = d1 / d3 * 0.1D;
      this.dirZ = d2 / d3 * 0.1D;
   }

   @Override
   public boolean damageEntity(DamageSource damagesource, float f) {
      return true;
   }

   @Override
   public void t_() {
      if (this.world.isClientSide || (this.shooter == null || !this.shooter.isDead()) && this.world.isLoaded(new BlockPosition(this))) {
         super.t();
         if (this.i) {
            if (this.world.getType(new BlockPosition(this.e, this.f, this.g)).getBlock() == this.h) {
               ++this.ar;
               if (this.ar == 600) {
                  this.die();
               }

               return;
            }

            this.i = false;
            this.motX *= (double)(this.random.nextFloat() * 0.2F);
            this.motY *= (double)(this.random.nextFloat() * 0.2F);
            this.motZ *= (double)(this.random.nextFloat() * 0.2F);
            this.ar = 0;
            this.as = 0;
         } else {
            ++this.as;
         }

         Vec3D vec3d = new Vec3D(this.locX, this.locY, this.locZ);
         Vec3D vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         MovingObjectPosition movingobjectposition = this.world.rayTrace(vec3d, vec3d1);
         vec3d = new Vec3D(this.locX, this.locY, this.locZ);
         vec3d1 = new Vec3D(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
         if (movingobjectposition != null) {
            vec3d1 = new Vec3D(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
         }

         Entity entity = null;
         List<Entity> list = this.world.getEntities(this, this.getBoundingBox().a(this.motX, this.motY, this.motZ).grow(1.0D, 1.0D, 1.0D));
         double d0 = 0.0D;

         for(int i = 0; i < list.size(); ++i) {
            Entity entity1 = (Entity)list.get(i);
            if (entity1.ad() && (!entity1.k(((CraftPlayer)this.shooter).getHandle()) || this.as >= 25)) {
               float f = 0.3F;
               AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double)f, (double)f, (double)f);
               MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);
               if (movingobjectposition1 != null) {
                  double d1 = vec3d.distanceSquared(movingobjectposition1.pos);
                  if (d1 < d0 || d0 == 0.0D) {
                     entity = entity1;
                     d0 = d1;
                  }
               }
            }
         }

         if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
         }

         if (movingobjectposition != null) {
            this.a(movingobjectposition);
         }

         this.locX += this.motX;
         this.locY += this.motY;
         this.locZ += this.motZ;
         float f1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
         this.yaw = (float)(MathHelper.b(this.motZ, this.motX) * 180.0D / 3.141592741012573D) + 90.0F;

         for(this.pitch = (float)(MathHelper.b((double)f1, this.motY) * 180.0D / 3.141592741012573D) - 90.0F; this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
         }

         while(this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
         }

         while(this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
         }

         while(this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
         }

         this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
         this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
         float f2 = 0.95F;
         if (this.V()) {
            for(int j = 0; j < 4; ++j) {
               float f3 = 0.25F;
               this.world.addParticle(EnumParticle.WATER_BUBBLE, this.locX - this.motX * (double)f3, this.locY - this.motY * (double)f3, this.locZ - this.motZ * (double)f3, this.motX, this.motY, this.motZ, new int[0]);
            }

            f2 = 0.8F;
         }

         this.motX += this.dirX;
         this.motY += this.dirY;
         this.motZ += this.dirZ;
         this.motX *= (double)f2;
         this.motY *= (double)f2;
         this.motZ *= (double)f2;
         this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
         this.setPosition(this.locX, this.locY, this.locZ);
      } else {
         this.die();
      }

   }

   @Override
   public float a(Explosion explosion, World world, BlockPosition blockposition, IBlockData iblockdata) {
      float f = super.a(explosion, world, blockposition, iblockdata);
      Block block = iblockdata.getBlock();
      if (EntityWither.a(block)) {
         f = Math.min(0.8F, f);
      }

      return f;
   }

   protected void a(MovingObjectPosition movingobjectposition) {
      if (!this.world.isClientSide) {
         if (movingobjectposition.entity != null) {
            boolean didDamage;
            if (this.shooter != null) {
               didDamage = movingobjectposition.entity.damageEntity(DamageSource.projectile(this, ((CraftPlayer)this.shooter).getHandle()), 8.0F);
            } else {
               didDamage = movingobjectposition.entity.damageEntity(DamageSource.MAGIC, 5.0F);
            }

            if (didDamage && movingobjectposition.entity instanceof EntityLiving) {
               byte b0 = 0;
               if (this.world.getDifficulty() == EnumDifficulty.NORMAL) {
                  b0 = 10;
               } else if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                  b0 = 40;
               }

               if (b0 > 0) {
                  ((EntityLiving)movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 20 * b0, 1));
               }
            }
         }

         ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 1.0F, false);
         this.world.getServer().getPluginManager().callEvent(event);
         if (!event.isCancelled()) {
            this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
         }

         this.die();
      }

   }

   @Override
   public void b(NBTTagCompound nbttagcompound) {
      nbttagcompound.setShort("xTile", (short)this.e);
      nbttagcompound.setShort("yTile", (short)this.f);
      nbttagcompound.setShort("zTile", (short)this.g);
      MinecraftKey minecraftkey = (MinecraftKey)Block.REGISTRY.c(this.h);
      nbttagcompound.setString("inTile", minecraftkey == null ? "" : minecraftkey.toString());
      nbttagcompound.setByte("inGround", (byte)(this.i ? 1 : 0));
      nbttagcompound.set("power", this.a((double[])(new double[]{this.dirX, this.dirY, this.dirZ})));
      nbttagcompound.set("direction", this.a((double[])(new double[]{this.motX, this.motY, this.motZ})));
   }

   @Override
   public void a(NBTTagCompound nbttagcompound) {
      this.e = nbttagcompound.getShort("xTile");
      this.f = nbttagcompound.getShort("yTile");
      this.g = nbttagcompound.getShort("zTile");
      if (nbttagcompound.hasKeyOfType("inTile", 8)) {
         this.h = Block.getByName(nbttagcompound.getString("inTile"));
      } else {
         this.h = Block.getById(nbttagcompound.getByte("inTile") & 255);
      }

      this.i = nbttagcompound.getByte("inGround") == 1;
      NBTTagList nbttaglist;
      if (nbttagcompound.hasKeyOfType("power", 9)) {
         nbttaglist = nbttagcompound.getList("power", 6);
         this.dirX = nbttaglist.d(0);
         this.dirY = nbttaglist.d(1);
         this.dirZ = nbttaglist.d(2);
      } else if (nbttagcompound.hasKeyOfType("direction", 9)) {
         nbttaglist = nbttagcompound.getList("direction", 6);
         this.motX = nbttaglist.d(0);
         this.motY = nbttaglist.d(1);
         this.motZ = nbttaglist.d(2);
      } else {
         this.die();
      }

   }
}
