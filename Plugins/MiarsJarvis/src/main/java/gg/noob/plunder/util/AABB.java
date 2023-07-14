// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import com.google.common.base.Objects;
import org.bukkit.util.NumberConversions;
import java.util.EnumSet;
import org.bukkit.util.Vector;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import net.minecraft.server.v1_8_R3.IBlockState;
import net.minecraft.server.v1_8_R3.BlockSnow;
import net.minecraft.server.v1_8_R3.BlockCarpet;
import net.minecraft.server.v1_8_R3.IBlockData;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.server.v1_8_R3.BlockPosition;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import net.minecraft.server.v1_8_R3.Material;
import java.util.ArrayList;
import net.minecraft.server.v1_8_R3.Block;
import org.bukkit.Location;
import java.util.List;
import org.bukkit.World;

public class AABB
{
    public Vec3D max;
    public Vec3D min;
    public static final AABB playerCollisionBox;
    
    public AABB(final Vec3D min, final Vec3D max) {
        this.min = min;
        this.max = max;
    }
    
    public List<Pair<Location, Block>> getBlocks(final World world) {
        final List<Pair<Location, Block>> blocks = new ArrayList<Pair<Location, Block>>();
        for (int x = (int)Math.floor(this.min.getX()); x < (int)Math.ceil(this.max.getX()); ++x) {
            for (int y = (int)Math.floor(this.min.getY()); y < (int)Math.ceil(this.max.getY()); ++y) {
                for (int z = (int)Math.floor(this.min.getZ()); z < (int)Math.ceil(this.max.getZ()); ++z) {
                    final Location loc = new Location(world, (double)x, (double)y, (double)z);
                    final Block block = BlockUtils.getBlock(world, loc);
                    if (block != null) {
                        blocks.add(new Pair<Location, Block>(loc, block));
                    }
                }
            }
        }
        return blocks;
    }
    
    public boolean intersect(final Ray ray, final AABB aab) {
        final double invDirX = 1.0 / ray.getDirection().getX();
        final double invDirY = 1.0 / ray.getDirection().getY();
        final double invDirZ = 1.0 / ray.getDirection().getZ();
        double tNear;
        double tFar;
        if (invDirX >= 0.0) {
            tNear = (aab.getMin().getX() - ray.getOrigin().getX()) * invDirX;
            tFar = (aab.getMax().getX() - ray.getOrigin().getX()) * invDirX;
        }
        else {
            tNear = (aab.getMax().getX() - ray.getOrigin().getX()) * invDirX;
            tFar = (aab.getMin().getX() - ray.getOrigin().getX()) * invDirX;
        }
        double tymin;
        double tymax;
        if (invDirY >= 0.0) {
            tymin = (aab.getMin().getY() - ray.getOrigin().getY()) * invDirY;
            tymax = (aab.getMax().getY() - ray.getOrigin().getY()) * invDirY;
        }
        else {
            tymin = (aab.getMax().getY() - ray.getOrigin().getY()) * invDirY;
            tymax = (aab.getMin().getY() - ray.getOrigin().getY()) * invDirY;
        }
        if (tNear > tymax || tymin > tFar) {
            return false;
        }
        double tzmin;
        double tzmax;
        if (invDirZ >= 0.0) {
            tzmin = (aab.getMin().getZ() - ray.getOrigin().getZ()) * invDirZ;
            tzmax = (aab.getMax().getZ() - ray.getOrigin().getZ()) * invDirZ;
        }
        else {
            tzmin = (aab.getMax().getZ() - ray.getOrigin().getZ()) * invDirZ;
            tzmax = (aab.getMin().getZ() - ray.getOrigin().getZ()) * invDirZ;
        }
        if (tNear <= tzmax && tzmin <= tFar) {
            tNear = ((tymin <= tNear && !Double.isNaN(tNear)) ? tNear : tymin);
            tFar = ((tymax >= tFar && !Double.isNaN(tFar)) ? tFar : tymax);
            tNear = ((tzmin > tNear) ? tzmin : tNear);
            tFar = ((tzmax < tFar) ? tzmax : tFar);
            return tNear < tFar && tFar >= 0.0;
        }
        return false;
    }
    
    public List<AABB> getBlockAABBs(final World world, final Material... exemptedMats) {
        final Set<Material> exempt = new HashSet<Material>(Arrays.asList(exemptedMats));
        final List<AABB> aabbs = new ArrayList<AABB>();
        final AABB expanded = this.clone();
        expanded.min.y = expanded.getMin().getY() - 1.0;
        final List<Pair<Location, Block>> blocks = expanded.getBlocks(world);
        for (final Pair<Location, Block> b : blocks) {
            if (exempt.contains(b.getY().getMaterial())) {
                continue;
            }
            final AABB[] collisionBoxes;
            final AABB[] bAABBs = collisionBoxes = this.getCollisionBoxes(b.getY(), b.getX(), new BlockPosition(b.getX().getBlockX(), b.getX().getBlockY(), b.getX().getBlockZ()), b.getY().getBlockData());
            for (final AABB aabb : collisionBoxes) {
                if (this.isColliding(aabb)) {
                    aabbs.add(aabb);
                }
            }
        }
        return aabbs;
    }
    
    private AABB[] getCollisionBoxes(final Block b, final Location loc, final BlockPosition bPos, final IBlockData data) {
        if (b instanceof BlockCarpet) {
            final AABB[] aabbarr = { new AABB(Vec3D.fromLocation(loc), Vec3D.fromLocation(loc).add(new Vec3D(1.0, 0.0625, 1.0))) };
            return aabbarr;
        }
        if (b instanceof BlockSnow && (int)data.get((IBlockState)BlockSnow.LAYERS) == 1) {
            final AABB[] aabbarr = { new AABB(Vec3D.fromLocation(loc), Vec3D.fromLocation(loc).add(new Vec3D(1.0, 0.0, 1.0))) };
            return aabbarr;
        }
        final List<AxisAlignedBB> bbs = new ArrayList<AxisAlignedBB>();
        final AxisAlignedBB cube = AxisAlignedBB.a((double)loc.getBlockX(), (double)loc.getBlockY(), (double)loc.getBlockZ(), (double)(loc.getBlockX() + 1), (double)(loc.getBlockY() + 1), (double)(loc.getBlockZ() + 1));
        b.a((net.minecraft.server.v1_8_R3.World)((CraftWorld)loc.getWorld()).getHandle(), bPos, data, cube, (List)bbs, (Entity)null);
        final AABB[] collisionBoxes = new AABB[bbs.size()];
        for (int i = 0; i < bbs.size(); ++i) {
            final AxisAlignedBB bb = bbs.get(i);
            final AABB collisionBox = new AABB(new Vec3D(bb.a, bb.b, bb.c), new Vec3D(bb.d, bb.e, bb.f));
            collisionBoxes[i] = collisionBox;
        }
        return collisionBoxes;
    }
    
    public boolean isColliding(final AABB other) {
        return this.max.getX() >= other.getMin().getX() && this.min.getX() <= other.getMax().getX() && this.max.getY() >= other.getMin().getY() && this.min.getY() <= other.getMax().getY() && this.max.getZ() >= other.getMin().getZ() && this.min.getZ() <= other.getMax().getZ();
    }
    
    public boolean isIntersected(final AABB other) {
        return this.max.getX() > other.getMin().getX() && this.min.getX() < other.getMax().getX() && this.max.getY() > other.getMin().getY() && this.min.getY() < other.getMax().getY() && this.max.getZ() > other.getMin().getZ() && this.min.getZ() < other.getMax().getZ();
    }
    
    public AABB offset(final double x, final double y, final double z) {
        final Vec3D max = this.max;
        max.x += x;
        final Vec3D max2 = this.max;
        max2.y += y;
        final Vec3D max3 = this.max;
        max3.z += z;
        final Vec3D min = this.min;
        min.x += x;
        final Vec3D min2 = this.min;
        min2.y += y;
        final Vec3D min3 = this.min;
        min3.z += z;
        return this;
    }
    
    public AABB shrink(final double x, final double y, final double z) {
        final Vec3D max = this.max;
        max.x += x;
        final Vec3D max2 = this.max;
        max2.y += y;
        final Vec3D max3 = this.max;
        max3.z += z;
        final Vec3D min = this.min;
        min.x -= x;
        final Vec3D min2 = this.min;
        min2.y -= y;
        final Vec3D min3 = this.min;
        min3.z -= z;
        return this;
    }
    
    public AABB clone() {
        return new AABB(this.min, this.max);
    }
    
    public boolean betweenRays(final Vector pos, final Vector dir1, final Vector dir2) {
        if (dir1.dot(dir2) > 0.999) {
            return this.intersectsRay(new Ray3D(Vec3D.fromVector(pos), Vec3D.fromVector(dir2)), 0.0f, Float.MAX_VALUE) != null;
        }
        final Vector planeNormal = dir2.clone().crossProduct(dir1);
        final Vector[] vertices = this.getVertices();
        boolean hitPlane = false;
        boolean above = false;
        boolean below = false;
        for (final Vector vertex : vertices) {
            vertex.subtract(pos);
            if (!hitPlane) {
                if (vertex.dot(planeNormal) > 0.0) {
                    above = true;
                }
                else {
                    below = true;
                }
                if (above && below) {
                    hitPlane = true;
                }
            }
        }
        if (!hitPlane) {
            return false;
        }
        final Vector extraDirToDirNormal = planeNormal.clone().crossProduct(dir2);
        final Vector dirToExtraDirNormal = dir1.clone().crossProduct(planeNormal);
        boolean betweenVectors = false;
        boolean frontOfExtraDirToDir = false;
        boolean frontOfDirToExtraDir = false;
        for (final Vector vertex2 : vertices) {
            if (!frontOfExtraDirToDir && vertex2.dot(extraDirToDirNormal) >= 0.0) {
                frontOfExtraDirToDir = true;
            }
            if (!frontOfDirToExtraDir && vertex2.dot(dirToExtraDirNormal) >= 0.0) {
                frontOfDirToExtraDir = true;
            }
            if (frontOfExtraDirToDir && frontOfDirToExtraDir) {
                betweenVectors = true;
                break;
            }
        }
        return betweenVectors;
    }
    
    public Vector[] getVertices() {
        return new Vector[] { new Vector(this.min.x, this.min.y, this.min.z), new Vector(this.min.x, this.min.y, this.max.z), new Vector(this.min.x, this.max.y, this.min.z), new Vector(this.min.x, this.max.y, this.max.z), new Vector(this.max.x, this.min.y, this.min.z), new Vector(this.max.x, this.min.y, this.max.z), new Vector(this.max.x, this.max.y, this.min.z), new Vector(this.max.x, this.max.y, this.max.z) };
    }
    
    public void translate(final Vector vector) {
        this.min = Vec3D.fromVector(new Vector(this.min.x, this.min.y, this.min.z).add(vector));
        this.max = Vec3D.fromVector(new Vector(this.max.x, this.max.y, this.max.z).add(vector));
    }
    
    public AABB expand(final double xyz) {
        final Vector compliment = new Vector(xyz, xyz, xyz);
        this.min = Vec3D.fromVector(new Vector(this.min.x, this.min.y, this.min.z).subtract(compliment));
        this.max = Vec3D.fromVector(new Vector(this.max.x, this.max.y, this.max.z).add(compliment));
        return this;
    }
    
    public AABB expand(final double x, final double y, final double z) {
        final Vector compliment = new Vector(x, y, z);
        this.min = Vec3D.fromVector(new Vector(this.min.x, this.min.y, this.min.z).subtract(compliment));
        this.max = Vec3D.fromVector(new Vector(this.max.x, this.max.y, this.max.z).add(compliment));
        return this;
    }
    
    public AABB expandMin(final double x, final double y, final double z) {
        final Vector compliment = new Vector(x, y, z);
        this.min = Vec3D.fromVector(new Vector(this.min.x, this.min.y, this.min.z).add(compliment));
        return this;
    }
    
    public AABB expandMax(final double x, final double y, final double z) {
        final Vector compliment = new Vector(x, y, z);
        this.max = Vec3D.fromVector(new Vector(this.max.x, this.max.y, this.max.z).add(compliment));
        return this;
    }
    
    public AABB(final Location block) {
        this(Vec3D.fromLocation(block), Vec3D.fromLocation(block).add(Vec3D.UNIT_MAX));
    }
    
    public Vec3D intersectsRay(final Ray3D ray, final float minDist, final float maxDist) {
        final Vec3D invDir = new Vec3D(1.0 / ray.dir.x, 1.0 / ray.dir.y, 1.0 / ray.dir.z);
        final boolean signDirX = invDir.x < 0.0;
        final boolean signDirY = invDir.y < 0.0;
        final boolean signDirZ = invDir.z < 0.0;
        Vec3D bbox = signDirX ? this.max : this.min;
        double tmin = (bbox.x - ray.x) * invDir.x;
        bbox = (signDirX ? this.min : this.max);
        double tmax = (bbox.x - ray.x) * invDir.x;
        bbox = (signDirY ? this.max : this.min);
        final double tymin = (bbox.y - ray.y) * invDir.y;
        bbox = (signDirY ? this.min : this.max);
        final double tymax = (bbox.y - ray.y) * invDir.y;
        if (tmin > tymax || tymin > tmax) {
            return null;
        }
        if (tymin > tmin) {
            tmin = tymin;
        }
        if (tymax < tmax) {
            tmax = tymax;
        }
        bbox = (signDirZ ? this.max : this.min);
        final double tzmin = (bbox.z - ray.z) * invDir.z;
        bbox = (signDirZ ? this.min : this.max);
        final double tzmax = (bbox.z - ray.z) * invDir.z;
        if (tmin > tzmax || tzmin > tmax) {
            return null;
        }
        if (tzmin > tmin) {
            tmin = tzmin;
        }
        if (tzmax < tmax) {
            tmax = tzmax;
        }
        if (tmin < maxDist && tmax > minDist) {
            return ray.getPointAtDistance(tmin);
        }
        return null;
    }
    
    public double distance(final Vec3D point) {
        final double dX = Math.max(0.0, Math.max(this.min.x - point.x, point.x - this.max.x));
        final double dY = Math.max(0.0, Math.max(this.min.y - point.y, point.y - this.max.y));
        final double dZ = Math.max(0.0, Math.max(this.min.z - point.z, point.z - this.max.z));
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }
    
    public Set<org.bukkit.Material> getMaterials(final World world) {
        final Set<org.bukkit.Material> mats = EnumSet.noneOf(org.bukkit.Material.class);
        for (int x = (int)Math.floor(this.min.x); x < (int)Math.ceil(this.max.x); ++x) {
            for (int y = (int)Math.floor(this.min.y); y < (int)Math.ceil(this.max.y); ++y) {
                for (int z = (int)Math.floor(this.min.z); z < (int)Math.ceil(this.max.z); ++z) {
                    final org.bukkit.Material type = BlockUtils.getBlockType(world, new Location(world, (double)x, (double)y, (double)z));
                    mats.add(type);
                }
            }
        }
        return mats;
    }
    
    public Vec3D getMax() {
        return this.max;
    }
    
    public Vec3D getMin() {
        return this.min;
    }
    
    static {
        playerCollisionBox = new AABB(new Vec3D(-0.3, 0.0, -0.3), new Vec3D(0.3, 1.8, 0.3));
    }
    
    public static class Ray3D extends Vec3D
    {
        public final Vec3D dir;
        
        public Ray3D(final Vec3D origin, final Vec3D direction) {
            super(origin);
            this.dir = direction.normalize();
        }
        
        public Ray3D(final Location loc) {
            this(Vec3D.fromLocation(loc), Vec3D.fromVector(loc.getDirection()));
        }
        
        public Vec3D getDirection() {
            return this.dir;
        }
        
        public Vec3D getPointAtDistance(final double dist) {
            return this.add(this.dir.scale(dist));
        }
        
        @Override
        public String toString() {
            return "origin: " + super.toString() + " dir: " + this.dir;
        }
    }
    
    public static class Vec3D
    {
        public static final Vec3D UNIT_MAX;
        public double x;
        public double y;
        public double z;
        
        public Vec3D(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public Vec3D(final Vec3D v) {
            this.x = v.x;
            this.y = v.y;
            this.z = v.z;
        }
        
        public static Vec3D fromLocation(final Location loc) {
            return new Vec3D(loc.getX(), loc.getY(), loc.getZ());
        }
        
        public static Vec3D fromVector(final Vector v) {
            return new Vec3D(v.getX(), v.getY(), v.getZ());
        }
        
        public final Vec3D add(final Vec3D v) {
            return new Vec3D(this.x + v.x, this.y + v.y, this.z + v.z);
        }
        
        public Vec3D scale(final double s) {
            return new Vec3D(this.x * s, this.y * s, this.z * s);
        }
        
        public Vec3D normalize() {
            final double mag = Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
            if (mag > 0.0) {
                return this.scale(1.0 / mag);
            }
            return this;
        }
        
        public double distance(final Vec3D v) {
            return Math.sqrt(NumberConversions.square(this.x - v.x) + NumberConversions.square(this.y - v.y) + NumberConversions.square(this.z - v.z));
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Vec3D) {
                final Vec3D v = (Vec3D)obj;
                return this.x == v.x && this.y == v.y && this.z == v.z;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(new Object[] { this.x, this.y, this.z });
        }
        
        @Override
        public String toString() {
            return String.format("{x: %g, y: %g, z: %g}", this.x, this.y, this.z);
        }
        
        public double getX() {
            return this.x;
        }
        
        public double getY() {
            return this.y;
        }
        
        public double getZ() {
            return this.z;
        }
        
        static {
            UNIT_MAX = new Vec3D(1.0, 1.0, 1.0);
        }
    }
}
