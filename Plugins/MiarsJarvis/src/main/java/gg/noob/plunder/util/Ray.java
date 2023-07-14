package gg.noob.plunder.util;

import gg.noob.plunder.Plunder;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Ray
        implements Cloneable {
    private Vector origin;
    private Vector direction;

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public List<AABB> boxesOnRay(World world, double distance) {
        int amount = Math.round((float)(distance / 0.5));
        Location[] locs = new Location[Math.max(2, amount)];
        ArrayList<AABB> boxes = new ArrayList<>();
        boolean primaryThread = Bukkit.isPrimaryThread();
        for (int i = 0; i < locs.length; ++i) {
            AABB box;
            Material type;
            Block block;
            double ix = (double)i / 2.0;
            double fx = this.origin.getX() + this.direction.getX() * ix;
            double fy = this.origin.getY() + this.direction.getY() * ix;
            double fz = this.origin.getZ() + this.direction.getZ() * ix;
            Location loc = new Location(world, fx, fy, fz);
            Block block2 = block = primaryThread ? loc.getBlock() : BlockUtils.getBlockAsync(loc);
            if (block == null || BlockUtils.checkPhase(type = block.getType()) || !this.isCollided(box = BlockUtils.getBlockBoundingBox(block))) {
                continue;
            }
            boxes.add(box);
        }
        return boxes;
    }

    public boolean isCollided(AABB other) {
        return other.intersect(this, other);
    }

    public boolean intersect(Ray ray, AABB aab, Tuple<Double, Double> result) {
        double tymax;
        double tymin;
        double tFar;
        double tNear;
        double invDirX = 1.0 / ray.getDirection().getX();
        double invDirY = 1.0 / ray.getDirection().getY();
        double invDirZ = 1.0 / ray.getDirection().getZ();
        if (invDirX >= 0.0) {
            tNear = (aab.getMin().getX() - ray.getOrigin().getX()) * invDirX;
            tFar = (aab.getMax().getX() - ray.getOrigin().getX()) * invDirX;
        } else {
            tNear = (aab.getMax().getX() - ray.getOrigin().getX()) * invDirX;
            tFar = (aab.getMin().getX() - ray.getOrigin().getX()) * invDirX;
        }
        if (invDirY >= 0.0) {
            tymin = (aab.getMin().getY() - ray.getOrigin().getY()) * invDirY;
            tymax = (aab.getMax().getY() - ray.getOrigin().getY()) * invDirY;
        } else {
            tymin = (aab.getMax().getY() - ray.getOrigin().getY()) * invDirY;
            tymax = (aab.getMin().getY() - ray.getOrigin().getY()) * invDirY;
        }
        if (tNear <= tymax && tymin <= tFar) {
            double tzmax;
            double tzmin;
            if (invDirZ >= 0.0) {
                tzmin = (aab.getMin().getZ() - ray.getOrigin().getZ()) * invDirZ;
                tzmax = (aab.getMax().getZ() - ray.getOrigin().getZ()) * invDirZ;
            } else {
                tzmin = (aab.getMax().getZ() - ray.getOrigin().getZ()) * invDirZ;
                tzmax = (aab.getMin().getZ() - ray.getOrigin().getZ()) * invDirZ;
            }
            if (tNear <= tzmax && tzmin <= tFar) {
                tNear = tymin <= tNear && !Double.isNaN(tNear) ? tNear : tymin;
                tFar = tymax >= tFar && !Double.isNaN(tFar) ? tFar : tymax;
                tNear = tzmin > tNear ? tzmin : tNear;
                double d = tFar = tzmax < tFar ? tzmax : tFar;
                if (tNear < tFar && tFar >= 0.0) {
                    if (result != null) {
                        result.one = tNear;
                        result.two = tFar;
                    }
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public Vector collisionPoint(AABB box) {
        Tuple<Double, Double> p = new Tuple<Double, Double>();
        if (box != null && this.intersect(this, box, p)) {
            Vector vector = new Vector(this.getDirection().getX(), this.getDirection().getY(), this.getDirection().getZ());
            vector.normalize();
            vector.multiply(((Double)p.one).doubleValue());
            vector.add(new Vector(this.getOrigin().getX(), this.getOrigin().getY(), this.getOrigin().getZ()));
            return vector;
        }
        return null;
    }

    public Vector getPointAtDistance(double distance) {
        Vector dir = new Vector(this.direction.getX(), this.direction.getY(), this.direction.getZ());
        Vector orig = new Vector(this.origin.getX(), this.origin.getY(), this.origin.getZ());
        return orig.add(dir.multiply(distance));
    }

    public Ray clone() {
        try {
            Ray clone = (Ray)super.clone();
            clone.origin = this.origin.clone();
            clone.direction = this.direction.clone();
            return clone;
        }
        catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void highlight(World world, double blocksAway, double accuracy) {
        Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Plunder.getInstance(), () -> {
            for (double x = 0.0; x < blocksAway; x += accuracy) {
                world.playEffect(this.getPointAtDistance(x).toLocation(world), Effect.COLOURED_DUST, 1);
            }
        }, 0L);
    }

    public Pair<Vector, Vector> closestPointsBetweenLines(Ray other) {
        Vector n1 = this.direction.clone().crossProduct(other.direction.clone().crossProduct(this.direction));
        Vector n2 = other.direction.clone().crossProduct(this.direction.clone().crossProduct(other.direction));
        Vector c1 = this.origin.clone().add(this.direction.clone().multiply(other.origin.clone().subtract(this.origin).dot(n2) / this.direction.dot(n2)));
        Vector c2 = other.origin.clone().add(other.direction.clone().multiply(this.origin.clone().subtract(other.origin).dot(n1) / other.direction.dot(n1)));
        return new Pair<Vector, Vector>(c1, c2);
    }

    @Override
    public String toString() {
        return "origin: " + this.origin + " direction: " + this.direction;
    }

    public Vector getOrigin() {
        return this.origin;
    }

    public Vector getDirection() {
        return this.direction;
    }
}

