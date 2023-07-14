// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.util.Vector;

public class BoundingBox
{
    public float minX;
    public float minY;
    public float minZ;
    public float maxX;
    public float maxY;
    public float maxZ;
    
    public BoundingBox(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }
    
    public BoundingBox(final Vector min, final Vector max) {
        this.minX = (float)Math.min(min.getX(), max.getX());
        this.minY = (float)Math.min(min.getY(), max.getY());
        this.minZ = (float)Math.min(min.getZ(), max.getZ());
        this.maxX = (float)Math.max(min.getX(), max.getX());
        this.maxY = (float)Math.max(min.getY(), max.getY());
        this.maxZ = (float)Math.max(min.getZ(), max.getZ());
    }
    
    public BoundingBox(final Vector data) {
        this.minX = (float)data.getX() - 0.4f;
        this.minY = (float)data.getY();
        this.minZ = (float)data.getZ() - 0.4f;
        this.maxX = (float)data.getX() + 0.4f;
        this.maxY = (float)data.getY() + 1.9f;
        this.maxZ = (float)data.getZ() + 0.4f;
    }
    
    public BoundingBox add(final float x, final float y, final float z) {
        final float newMinX = this.minX + x;
        final float newMaxX = this.maxX + x;
        final float newMinY = this.minY + y;
        final float newMaxY = this.maxY + y;
        final float newMinZ = this.minZ + z;
        final float newMaxZ = this.maxZ + z;
        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    public BoundingBox add(final Vector vector) {
        final float x = (float)vector.getX();
        final float y = (float)vector.getY();
        final float z = (float)vector.getZ();
        final float newMinX = this.minX + x;
        final float newMaxX = this.maxX + x;
        final float newMinY = this.minY + y;
        final float newMaxY = this.maxY + y;
        final float newMinZ = this.minZ + z;
        final float newMaxZ = this.maxZ + z;
        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    public BoundingBox grow(final float x, final float y, final float z) {
        final float newMinX = this.minX - x;
        final float newMaxX = this.maxX + x;
        final float newMinY = this.minY - y;
        final float newMaxY = this.maxY + y;
        final float newMinZ = this.minZ - z;
        final float newMaxZ = this.maxZ + z;
        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }
    
    public BoundingBox shrink(final float x, final float y, final float z) {
        final float newMinX = this.minX + x;
        final float newMaxX = this.maxX - x;
        final float newMinY = this.minY + y;
        final float newMaxY = this.maxY - y;
        final float newMinZ = this.minZ + z;
        final float newMaxZ = this.maxZ - z;
        return new BoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
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
    
    public BoundingBox add(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return new BoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }
    
    public BoundingBox subtract(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ) {
        return new BoundingBox(this.minX - minX, this.minY - minY, this.minZ - minZ, this.maxX - maxX, this.maxY - maxY, this.maxZ - maxZ);
    }
    
    public boolean intersectsWithBox(final Vector vector) {
        return vector.getX() > this.minX && vector.getX() < this.maxX && vector.getY() > this.minY && vector.getY() < this.maxY && vector.getZ() > this.minZ && vector.getZ() < this.maxZ;
    }
    
    public Vector getMinimum() {
        return new Vector(this.minX, this.minY, this.minZ);
    }
    
    public Vector getMaximum() {
        return new Vector(this.maxX, this.maxY, this.maxZ);
    }
    
    public boolean intersectsWithBox(final Object other) {
        if (other instanceof BoundingBox) {
            final BoundingBox otherBox = (BoundingBox)other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        }
        final BoundingBox otherBox = ReflectionUtils.toBoundingBox(other);
        return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
    }
    
    public Object toAxisAlignedBB() {
        return ReflectionUtils.newBoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }
    
    @Override
    public String toString() {
        return "[" + this.minX + ", " + this.minY + ", " + this.minZ + ", " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
    
    public float getMinX() {
        return this.minX;
    }
    
    public float getMinY() {
        return this.minY;
    }
    
    public float getMinZ() {
        return this.minZ;
    }
    
    public float getMaxX() {
        return this.maxX;
    }
    
    public float getMaxY() {
        return this.maxY;
    }
    
    public float getMaxZ() {
        return this.maxZ;
    }
}
