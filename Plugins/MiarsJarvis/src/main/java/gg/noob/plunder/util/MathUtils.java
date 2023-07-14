//
// Decompiled by Procyon v0.5.36
//

package gg.noob.plunder.util;

import com.google.common.collect.Maps;
import org.bukkit.entity.LivingEntity;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.Optional;
import java.util.Comparator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import java.text.DecimalFormat;
import net.minecraft.server.v1_8_R3.MathHelper;
import org.bukkit.util.Vector;
import java.util.Arrays;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.HashMap;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.entity.Player;
import gg.noob.plunder.data.PlayerData;
import java.util.Collection;
import net.minecraft.server.v1_8_R3.Tuple;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import java.util.Map;

public class MathUtils
{
    public static final Map<Integer, Float> SENSITIVITY_MAP;
    private static double xDiff;
    private static double yDiff;
    private static double zDiff;
    public static final double EXPANDER;
    private static final float[] moveValues;

    public static double[] getOffsetFromLocation(final Location one, final Location two) {
        final double yaw = getRotations(one, two)[0];
        final double pitch = getRotations(one, two)[1];
        final double yawOffset = Math.abs(yaw - PlayerUtils.yawTo180F(one.getYaw()));
        final double pitchOffset = Math.abs(pitch - one.getPitch());
        return new double[] { yawOffset, pitchOffset };
    }

    public static int floor_float(final float value) {
        final int i = (int)value;
        return (value < i) ? (i - 1) : i;
    }

    public static int sensToPercent(final float sensitivity) {
        return floor_float(sensitivity / 0.5f * 100.0f);
    }

    public static float percentToSens(final int percent) {
        return percent * 0.0070422534f;
    }

    public static double getMedian(final List<Double> data) {
        if (data.size() <= 1) {
            return 0.0;
        }
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2.0;
        }
        return data.get(Math.round(data.size() / 2.0f));
    }

    public static double getMedian(final Iterable<? extends Number> iterable) {
        final List<Double> data = new ArrayList<Double>();
        for (final Number number : iterable) {
            data.add(number.doubleValue());
        }
        return getMedian(data);
    }

    public static Tuple<List<Float>, List<Float>> getOutliersFloat(final List<Float> values) {
        if (values.size() < 4) {
            return (Tuple<List<Float>, List<Float>>)new Tuple((Object)new ArrayList(), (Object)new ArrayList());
        }
        final double q1 = getMedian(values.subList(0, values.size() / 2));
        final double q2 = getMedian(values.subList(values.size() / 2, values.size()));
        final double iqr = Math.abs(q1 - q2);
        final double lowThreshold = q1 - 1.5 * iqr;
        final double highThreshold = q2 + 1.5 * iqr;
        final Tuple<List<Float>, List<Float>> tuple = (Tuple<List<Float>, List<Float>>)new Tuple((Object)new ArrayList(), (Object)new ArrayList());
        for (final Float value : values) {
            if (value < lowThreshold) {
                ((List)tuple.a()).add(value);
            }
            else {
                if (value <= highThreshold) {
                    continue;
                }
                ((List)tuple.b()).add(value);
            }
        }
        return tuple;
    }

    public static double getAverage(final Collection<? extends Number> values) {
        return values.stream().mapToDouble(Number::doubleValue).average().orElse(0.0);
    }

    public static Tuple<List<Double>, List<Double>> getOutliers(final Collection<? extends Number> collection) {
        final List<Double> values = new ArrayList<Double>();
        for (final Number number : collection) {
            values.add(number.doubleValue());
        }
        if (values.size() < 4) {
            return (Tuple<List<Double>, List<Double>>)new Tuple((Object)new ArrayList(), (Object)new ArrayList());
        }
        final double q1 = getMedian(values.subList(0, values.size() / 2));
        final double q2 = getMedian(values.subList(values.size() / 2, values.size()));
        final double iqr = Math.abs(q1 - q2);
        final double lowThreshold = q1 - 1.5 * iqr;
        final double highThreshold = q2 + 1.5 * iqr;
        final Tuple<List<Double>, List<Double>> tuple = (Tuple<List<Double>, List<Double>>)new Tuple((Object)new ArrayList(), (Object)new ArrayList());
        for (final Double value : values) {
            if (value < lowThreshold) {
                ((List)tuple.a()).add(value);
            }
            else {
                if (value <= highThreshold) {
                    continue;
                }
                ((List)tuple.b()).add(value);
            }
        }
        return tuple;
    }

    public static float getExpiermentalDeltaX(final PlayerData data) {
        final float deltaPitch = data.deltaYaw;
        final float sens = data.sensitivityX;
        final float f = sens * 0.6f + 0.2f;
        final float calc = f * f * f * 8.0f;
        return deltaPitch / (calc * 0.15f);
    }

    public static float getExpiermentalDeltaY(final PlayerData data) {
        final float deltaPitch = data.deltaPitch;
        final float sens = data.sensitivityY;
        final float f = sens * 0.6f + 0.2f;
        final float calc = f * f * f * 8.0f;
        return deltaPitch / (calc * 0.15f);
    }

    private static float yawToF2(final float yawDelta) {
        return yawDelta / 0.15f;
    }

    private static float pitchToF3(final float pitchDelta) {
        final int b0 = (pitchDelta >= 0.0f) ? 1 : -1;
        return pitchDelta / b0 / 0.15f;
    }

    public static float getSensitivityFromYawGCD(final float gcd) {
        return ((float)Math.cbrt(yawToF2(gcd) / 8.0f) - 0.2f) / 0.6f;
    }

    public static float getSensitivityFromPitchGCD(final float gcd) {
        return ((float)Math.cbrt(pitchToF3(gcd) / 8.0f) - 0.2f) / 0.6f;
    }

    public static boolean isAlmostEqual(final float num1, final float num2) {
        return Math.abs(num1 - num2) < 0.001;
    }

    public static float gcdSmall(final float current, final float previous) {
        if (current < previous) {
            return gcdSmall(Math.abs(previous), Math.abs(current));
        }
        return (Math.abs(previous) <= 0.001f) ? current : gcdSmall(previous, current - (float)Math.floor(current / previous) * previous);
    }

    public static float getDelta(final float one, final float two) {
        return Math.abs(one - two);
    }

    public static float getBaseSpeed(final Player player, final PlayerData data) {
        return 0.2f + ((data.getSprintTicks() > 0) ? 0.06f : 0.0f) + PlayerUtils.getPotionEffectLevel(player, PotionEffectType.SPEED) * 0.03001f + (player.getWalkSpeed() - 0.2f) * 1.6f;
    }

    public static boolean isOpposite(final double num1, final double num2) {
        return (num1 > 0.0 && num2 < 0.0) || (num1 < 0.0 && num2 > 0.0);
    }

    public static double deviation(final Collection<? extends Number> samples) {
        return Math.sqrt(varianceOther(samples));
    }

    public static double varianceOther(final Collection<? extends Number> samples) {
        double sum = 0.0;
        final double size = samples.size() - 1;
        final double mean = mean(samples);
        for (final Number i : samples) {
            sum += Math.pow(i.doubleValue() - mean, 2.0);
        }
        return sum / size;
    }

    public static double mean(final Collection<? extends Number> samples) {
        double sum = 0.0;
        for (final Number val : samples) {
            sum += val.doubleValue();
        }
        return sum / samples.size();
    }

//    public static Number getMode(final Collection<? extends Number> samples) {
//        final Map<Number, Integer> frequencies = new HashMap<Number, Integer>();
//        final Map<Number, Integer> map;
//        final Integer n;
//        samples.forEach(i -> n = map.put(i, map.getOrDefault(i, 0) + 1));
//        Number mode = null;
//        int highest = 0;
//        for (final Map.Entry<Number, Integer> entry : frequencies.entrySet()) {
//            if (entry.getValue() > highest) {
//                mode = entry.getKey();
//                highest = entry.getValue();
//            }
//        }
//        return mode;
//    }

    public static double gcd(final double a, final double b) {
        try {
            if (a < b) {
                return gcd(b, a);
            }
            if (Math.abs(b) < 0.001) {
                return a;
            }
            return gcd(b, a - Math.floor(a / b) * b);
        }
        catch (StackOverflowError e) {
            return 0.0;
        }
    }

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    public static double stDeviation(final Collection<Integer> numbers) {
        final double average = average(numbers);
        double stdDev = 0.0;
        for (final int j : numbers) {
            stdDev += Math.pow(j - average, 2.0);
        }
        stdDev /= numbers.size();
        stdDev = Math.sqrt(stdDev);
        return stdDev;
    }

    public static float getMoveAngle(final Location from, final Location to) {
        final double dx = to.getX() - from.getX();
        final double dz = to.getZ() - from.getZ();
        final float moveAngle = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        return Math.abs(wrapAngleTo180_float(moveAngle - to.getYaw()));
    }

    public static float wrapAngleTo180_float(float value) {
        value %= 360.0f;
        if (value >= 180.0f) {
            value -= 360.0f;
        }
        if (value < -180.0f) {
            value += 360.0f;
        }
        return value;
    }

    public static double average(final Number... array) {
        return average(Arrays.asList(array));
    }

    public static double average(final Iterable<? extends Number> iterable) {
        double n = 0.0;
        int n2 = 0;
        for (final Number number : iterable) {
            n += number.doubleValue();
            ++n2;
        }
        return n / n2;
    }

    public static double variance(final Number n, final Iterable<? extends Number> iterable) {
        return Math.sqrt(varianceSquared(n, iterable));
    }

    public static double lowestAbs(final Number... array) {
        return lowestAbs(Arrays.asList(array));
    }

    public static double lowestAbs(final Iterable<? extends Number> iterable) {
        Double value = null;
        for (final Number number : iterable) {
            if (value != null && Math.abs(number.doubleValue()) >= Math.abs(value)) {
                continue;
            }
            value = number.doubleValue();
        }
        return JavaV.firstNonNull(value, 0.0);
    }

    public static Double getMinimumAngle(final Pair<Long, Location> packetLocation, final Location... array) {
        Double value = null;
        for (final Location playerLocation : array) {
            final double distanceBetweenAngles360 = getDistanceBetweenAngles360(playerLocation.getYaw(), (float)(Math.atan2(packetLocation.getY().getZ() - playerLocation.getZ(), packetLocation.getY().getX() - playerLocation.getX()) * 180.0 / 3.141592653589793) - 90.0f);
            if (value == null || value > distanceBetweenAngles360) {
                value = distanceBetweenAngles360;
            }
        }
        return value;
    }

    public static double getCPS(final Collection<? extends Number> values) {
        return 20.0 / getAverage(values);
    }

    public static double getDistanceBetweenAngles360(final double n, final double n2) {
        final double abs = Math.abs(n % 360.0 - n2 % 360.0);
        return Math.abs(Math.min(360.0 - abs, abs));
    }

    public static double positiveSmaller(final Number n, final Number n2) {
        return (Math.abs(n.doubleValue()) < Math.abs(n2.doubleValue())) ? n.doubleValue() : n2.doubleValue();
    }

    public static double hypot(final double... array) {
        return Math.sqrt(hypotSquared(array));
    }

    public static double hypotSquared(final double... array) {
        double n = 0.0;
        for (final double v : array) {
            n += Math.pow(v, 2.0);
        }
        return n;
    }

    public static double varianceSquared(final Number n, final Iterable<? extends Number> iterable) {
        double n2 = 0.0;
        int n3 = 0;
        for (final Number number : iterable) {
            n2 += Math.pow(number.doubleValue() - n.doubleValue(), 2.0);
            ++n3;
        }
        return (n2 == 0.0) ? 0.0 : (n2 / (n3 - 1));
    }

    public static boolean onGround(final double n) {
        return n % 0.015625 == 0.0;
    }

    public static void distance(final Location one, final Location two) {
        MathUtils.xDiff = Math.abs(one.getX() - two.getX());
        MathUtils.yDiff = Math.abs(one.getY() - two.getY());
        MathUtils.zDiff = Math.abs(one.getZ() - two.getZ());
    }

    public static Vector getDirection(final float yaw, final float pitch) {
        final Vector vector = new Vector();
        final float rotX = (float)Math.toRadians(yaw);
        final float rotY = (float)Math.toRadians(pitch);
        vector.setY(-MathHelper.sin(rotY));
        final double xz = MathHelper.cos(rotY);
        vector.setX(-xz * MathHelper.sin(rotX));
        vector.setZ(xz * MathHelper.cos(rotX));
        return vector;
    }

    public static int msToTicks(final double time) {
        return (int)Math.round(time / 50.0);
    }

    public static double trim(final int degree, final double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format += "#";
        }
        final DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.parseDouble(twoDForm.format(d).replaceAll(",", "."));
    }

    public static int getIntQuotient(final float dividend, final float divisor) {
        final float ans = dividend / divisor;
        final float error = Math.max(dividend, divisor) * 0.001f;
        return (int)(ans + error);
    }

    public static float gcdRational(final float a, final float b) {
        if (a == 0.0f) {
            return b;
        }
        final int quotient = getIntQuotient(b, a);
        float remainder = (b / a - quotient) * a;
        if (Math.abs(remainder) < Math.max(a, b) * 0.001f) {
            remainder = 0.0f;
        }
        return gcdRational(remainder, a);
    }

    public static int getDistinct(final Collection<? extends Number> user) {
        return (int)user.stream().distinct().count();
    }

    public static double deviationSquared(final Iterable<? extends Number> iterable) {
        double n = 0.0;
        int n2 = 0;
        for (final Number anIterable : iterable) {
            n += anIterable.doubleValue();
            ++n2;
        }
        final double n3 = n / n2;
        double n4 = 0.0;
        for (final Number anIterable2 : iterable) {
            n4 += Math.pow(anIterable2.doubleValue() - n3, 2.0);
        }
        return (n4 == 0.0) ? 0.0 : (n4 / (n2 - 1));
    }

    public static double movingFlyingV3(PlayerData data, boolean blockChecking, Location to, Location from, PacketPlayInFlying packetPlayInFlying, boolean lastGround, double deltaXZ, double velocityH, boolean mode) {
        if (data.hasHitSlowdown() && data.getSprintTicks() > 0) {
            velocityH *= 0.6;
        }
        double preD = 0.01;
        double mx = to.getX() - from.getX();
        double mz = to.getZ() - from.getZ();
        float motionYaw = (float)(FastTrig.fast_atan2(mz, mx) * 180.0 / Math.PI) - 90.0f;
        motionYaw -= to.getYaw();
        while (motionYaw > 360.0f) {
            motionYaw -= 360.0f;
        }
        while (motionYaw < 0.0f) {
            motionYaw += 360.0f;
        }
        motionYaw /= 45.0f;
        float moveS = 0.0f;
        float moveF = 0.0f;
        if (Math.abs(Math.abs(mx) + Math.abs(mz)) > preD) {
            int direction = (int)new BigDecimal(motionYaw).setScale(1, RoundingMode.HALF_UP).doubleValue();
            if (direction == 1) {
                moveF = 1.0f;
                moveS = -1.0f;
            } else if (direction == 2) {
                moveS = -1.0f;
            } else if (direction == 3) {
                moveF = -1.0f;
                moveS = -1.0f;
            } else if (direction == 4) {
                moveF = -1.0f;
            } else if (direction == 5) {
                moveF = -1.0f;
                moveS = 1.0f;
            } else if (direction == 6) {
                moveS = 1.0f;
            } else if (direction == 7) {
                moveF = 1.0f;
                moveS = 1.0f;
            } else if (direction == 8) {
                moveF = 1.0f;
            } else if (direction == 0) {
                moveF = 1.0f;
            }
        }
        float moveStrafe = moveS *= 0.98f;
        float moveForward = moveF *= 0.98f;
        float f = moveStrafe * moveStrafe + moveForward * moveForward;
        if (blockChecking && ((CraftHumanEntity)data.getPlayer()).getHandle().g != null) {
            moveStrafe *= 0.2f;
            moveForward *= 0.2f;
        }
        float var3 = data.getFriction() * 0.91f;
        float aiMoveSpeed = data.getPlayer().getWalkSpeed() / 2.0f + (data.getSprintTicks() > 0 ? 0.3000001f : 0.0f);
        switch (PlayerUtils.getPotionEffectLevel(data.getPlayer(), PotionEffectType.SPEED)) {
            case 0: {
                break;
            }
            case 1: {
                aiMoveSpeed = 0.156f;
                break;
            }
            case 2: {
                aiMoveSpeed = 0.18200001f;
                break;
            }
            case 3: {
                aiMoveSpeed = 0.208f;
                break;
            }
            case 4: {
                aiMoveSpeed = 0.23400001f;
                break;
            }
            default: {
                return 1.0;
            }
        }
        float var4 = 0.16277136f / (var3 * var3 * var3);
        float friction = lastGround ? aiMoveSpeed * var4 : 0.026f;
        float f4 = 0.026f;
        float f5 = 0.8f;
        if (data.getLiquidTicks() > 0 && data.getPlayer().getInventory().getBoots() != null && data.getPlayer().getInventory().getBoots().getEnchantments() != null) {
            float f3 = data.getPlayer().getInventory().getBoots().getEnchantmentLevel(Enchantment.DEPTH_STRIDER);
            if (f3 > 3.0f) {
                f3 = 3.0f;
            }
            if (data.getLastGroundLoc() == null) {
                f3 *= 0.5f;
            }
            if (f3 > 0.0f) {
                f5 += (0.54600006f - f5) * f3 / 3.0f;
                f4 += (aiMoveSpeed * 1.0f - f4) * f3 / 3.0f;
            }
            friction = f4;
        }
        if (mode) {
            double velocityHSave = velocityH;
            float fSave = f;
            ArrayList<Tuple> predictions = new ArrayList<Tuple>();
            for (float forward : moveValues) {
                for (float strafe : moveValues) {
                    double value;
                    velocityH = value = MathUtils.moveFlying(strafe, forward, f, friction, to, from, packetPlayInFlying);
                    predictions.add(new Tuple((Object)new Float[]{Float.valueOf(forward), Float.valueOf(strafe)}, (Object)velocityH));
                    velocityH = velocityHSave;
                    f = fSave;
                }
            }
            Optional<Tuple> velocity = predictions.stream().filter(tuple -> {
                double xzDiff = Math.abs((Double)tuple.b() - deltaXZ);
                return xzDiff * xzDiff < 0.01;
            }).min(Comparator.comparing(tuple -> {
                double xzDiff = Math.abs((Double)tuple.b() - deltaXZ);
                return xzDiff * xzDiff;
            }));
            if (!velocity.isPresent()) {
                float s2 = moveStrafe;
                float f2 = moveForward;
                moveStrafe = s2;
                moveForward = f2;
                double value = MathUtils.moveFlying(s2, f2, f, friction, to, from, packetPlayInFlying);
                velocityH -= value;
            } else {
                Tuple tuple2 = velocity.get();
                moveForward = ((Float[])tuple2.a())[0].floatValue();
                moveStrafe = ((Float[])tuple2.a())[1].floatValue();
                velocityH -= ((Double)tuple2.b()).doubleValue();
            }
            return Math.abs(deltaXZ / velocityH);
        }
        return MathUtils.moveFlying(moveStrafe, moveForward, f, friction, to, from, packetPlayInFlying);
    }

    private static double moveFlying(float strafe, float forward, float f, final float friction, final Location to, final Location from, final PacketPlayInFlying packetPlayInFlying) {
        if (f >= 1.0E-4f) {
            f = (float)Math.sqrt(f);
            if (f < 1.0f) {
                f = 1.0f;
            }
            f = friction / f;
            strafe *= f;
            forward *= f;
            final float f2 = MathHelper.sin(from.getYaw() * 3.1415927f / 180.0f);
            final float f3 = MathHelper.cos(from.getYaw() * 3.1415927f / 180.0f);
            final float motionXAdd = strafe * f3 - forward * f2;
            final float motionZAdd = forward * f3 + strafe * f2;
            return Math.hypot(motionXAdd, motionZAdd);
        }
        return 0.0;
    }

    public static float gcdRational(final List<Float> numbers) {
        float result = numbers.get(0);
        for (int i = 1; i < numbers.size(); ++i) {
            result = gcdRational(numbers.get(i), result);
            if (result < 1.0E-7) {
                return 0.0f;
            }
        }
        return result;
    }

    public static double highest(final Number... array) {
        return highest(Arrays.asList(array));
    }

    public static double highest(final Iterable<? extends Number> iterable) {
        Double value = null;
        for (final Number number : iterable) {
            if (value != null && number.doubleValue() <= value) {
                continue;
            }
            value = number.doubleValue();
        }
        return JavaV.firstNonNull(value, 0.0);
    }

    public static double lowest(final Number... array) {
        return lowest(Arrays.asList(array));
    }

    public static double lowest(final Iterable<? extends Number> iterable) {
        Double value = null;
        for (final Number number : iterable) {
            if (value != null && number.doubleValue() >= value) {
                continue;
            }
            value = number.doubleValue();
        }
        return JavaV.firstNonNull(value, 0.0);
    }

    public static boolean isExponentiallySmall(final Number number) {
        return number.doubleValue() < 1.0 && (Double.toString(number.doubleValue()).contains("E") || number.doubleValue() == 0.0);
    }

    public static double getVariance(final Collection<? extends Number> data) {
        int count = 0;
        double sum = 0.0;
        double variance = 0.0;
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        final double average = sum / count;
        for (final Number number2 : data) {
            variance += Math.pow(number2.doubleValue() - average, 2.0);
        }
        return variance;
    }

    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }

    public static double getStandardDeviation(final Collection<? extends Number> data) {
        final double variance = getVariance(data);
        return Math.sqrt(variance);
    }

    public static double getKurtosis(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }
        if (count < 3.0) {
            return 0.0;
        }
        final double efficiencyFirst = count * (count + 1.0) / (count - 1.0) * (count - 2.0) * (count - 3.0);
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / (count - 2.0) * (count - 3.0);
        final double average = sum / count;
        double variance = 0.0;
        double varianceSquared = 0.0;
        for (final Number number2 : data) {
            variance += Math.pow(average - number2.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number2.doubleValue(), 4.0);
        }
        return efficiencyFirst * varianceSquared / Math.pow(variance / sum, 2.0) - efficiencySecond;
    }

    public static double getSkewness(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;
        final List<Double> numbers = Lists.newArrayList();
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
            numbers.add(number.doubleValue());
        }
        Collections.sort(numbers);
        final double mean = sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : ((numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2.0);
        final double variance = getVariance(data);
        return 3.0 * (mean - median) / variance;
    }

    public static Vector getRotation(final Location location, final Location location2) {
        final double d = location2.getX() - location.getX();
        final double d2 = location2.getY() - location.getY();
        final double d3 = location2.getZ() - location.getZ();
        final double d4 = Math.sqrt(d * d + d3 * d3);
        final float f = (float)(Math.atan2(d3, d) * 180.0 / 3.141592653589793) - 90.0f;
        final float f2 = (float)(-Math.atan2(d2, d4) * 180.0 / 3.141592653589793);
        return new Vector(f, f2, 0.0f);
    }

    public static double fix180(double d) {
        if ((d %= 360.0) >= 180.0) {
            d -= 360.0;
        }
        if (d < -180.0) {
            d += 360.0;
        }
        return d;
    }

    public static double getDistance3D(final Location location, final Location location2) {
        final double d = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d2 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        final double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d4 = Math.sqrt(d + d2 + d3);
        final double d5 = Math.abs(d4);
        return d5;
    }

    public static double[] getOffsets2(final Player player, final LivingEntity livingEntity) {
        final Location location = livingEntity.getLocation().add(0.0, livingEntity.getEyeHeight(), 0.0);
        final Location location2 = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        final Vector vector = new Vector(location2.getYaw(), location2.getPitch(), 0.0f);
        final Vector vector2 = getRotation(location2, location);
        final double d = fix180(vector.getX() - vector2.getX());
        final double d2 = fix180(vector.getY() - vector2.getY());
        final double d3 = getHorizontalDistance2(location2, location);
        final double d4 = getDistance3D2(location2, location);
        final double d5 = d * d3 * d4;
        final double d6 = d2 * Math.abs(Math.sqrt(location.getY() - location2.getY())) * d4;
        return new double[] { Math.abs(d5), Math.abs(d6) };
    }

    public static double getHorizontalDistance2(final Location location, final Location location2) {
        final double d = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d2 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d3 = Math.sqrt(d + d2);
        final double d4 = Math.abs(d3);
        return d4;
    }

    public static double getDistance3D2(final Location location, final Location location2) {
        final double d = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d2 = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        final double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d4 = Math.sqrt(d + d2 + d3);
        final double d5 = Math.abs(d4);
        return d5;
    }

    public static double getHorizontalDistance(final Location to, final Location from) {
        final double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        final double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));
        return Math.sqrt(x * x + z * z);
    }

    public static double getVerticalDistance(final Location to, final Location from) {
        final double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));
        return Math.sqrt(y * y);
    }

    public static double getVerticalDistance3(final Location location, final Location location2) {
        final double d = (location2.getY() - location.getY()) * (location2.getY() - location.getY());
        final double d2 = Math.sqrt(d);
        final double d3 = Math.abs(d2);
        return d3;
    }

    public static double getHorizontalDistance3(final Location location, final Location location2) {
        double d = 0.0;
        final double d2 = (location2.getX() - location.getX()) * (location2.getX() - location.getX());
        final double d3 = (location2.getZ() - location.getZ()) * (location2.getZ() - location.getZ());
        final double d4 = Math.sqrt(d2 + d3);
        d = Math.abs(d4);
        return d;
    }

    public static double offset(final Location a, final Location b) {
        return offset(a.toVector(), b.toVector());
    }

    public static double offset(final Vector a, final Vector b) {
        return a.subtract(b).length();
    }

    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }

    public static int floor(final double var0) {
        final int var = (int)var0;
        return (var0 < var) ? (var - 1) : var;
    }

    public static boolean close(final Double[] arrdouble, final int n) {
        final double d = arrdouble[4];
        final double d2 = arrdouble[3];
        final double d3 = arrdouble[2];
        final double d4 = arrdouble[1];
        final double d5 = arrdouble[0];
        final boolean bl2 = ((d >= d2) ? (d - d2) : (d2 - d)) <= n;
        final boolean bl3 = ((d >= d3) ? (d - d3) : (d3 - d)) <= n;
        final boolean bl4 = ((d >= d4) ? (d - d4) : (d4 - d)) <= n;
        final boolean bl6;
        final boolean bl5 = bl6 = (((d >= d5) ? (d - d5) : (d5 - d)) <= n);
        return bl2 && bl3 && bl4 && bl5;
    }

    public static double getFraction(final double value) {
        return value % 1.0;
    }

    public static double[] getOffsetFromEntity(final Player player, final LivingEntity entity) {
        final double yawOffset = Math.abs(PlayerUtils.yawTo180F(player.getEyeLocation().getYaw()) - PlayerUtils.yawTo180F(getRotations(player.getLocation(), entity.getLocation())[0]));
        final double pitchOffset = Math.abs(Math.abs(player.getEyeLocation().getPitch()) - Math.abs(getRotations(player.getLocation(), entity.getLocation())[1]));
        return new double[] { yawOffset, pitchOffset };
    }

    public static float[] getRotations(final Location one, final Location two) {
        final double diffX = two.getX() - one.getX();
        final double diffZ = two.getZ() - one.getZ();
        final double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        final double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[] { yaw, pitch };
    }

    public static Vector getVerticalVector(final Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }

    public static Location getHorizontalLocation(final Location loc) {
        loc.setY(0.0);
        return loc;
    }

    public static Location getVerticalLocation(final Location loc) {
        loc.setX(0.0);
        loc.setZ(0.0);
        return loc;
    }

    public static double getDistanceBetweenAngles(final float angle1, final float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }

    public static double angle(final Vector a, final Vector b) {
        final double dot = Math.min(Math.max(a.dot(b) / (a.length() * b.length()), -1.0), 1.0);
        return Math.acos(dot);
    }

    public static int pingFormula(final long ping) {
        return (int)Math.ceil(ping / 50.0);
    }

    public static float[] getRotationFromPosition(final Location playerLocation, final Location targetLocation) {
        final double xDiff = targetLocation.getX() - playerLocation.getX();
        final double zDiff = targetLocation.getZ() - playerLocation.getZ();
        final double yDiff = targetLocation.getY() - playerLocation.getY() + 0.20000000298023224;
        final double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 45.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }

    public static long averageLong(final List<Long> list) {
        long add = 0L;
        for (final Long listlist : list) {
            add += listlist;
        }
        return add / list.size();
    }

    public static double averageDouble(final List<Double> list) {
        double add = 0.0;
        for (final Double listlist : list) {
            add += listlist;
        }
        return add / list.size();
    }

    public static double getXDiff() {
        return MathUtils.xDiff;
    }

    public static double getYDiff() {
        return MathUtils.yDiff;
    }

    public static double getZDiff() {
        return MathUtils.zDiff;
    }

    static {
        (SENSITIVITY_MAP = Maps.newHashMap()).put(1, 0.0070422534f);
        MathUtils.SENSITIVITY_MAP.put(2, 0.014084507f);
        MathUtils.SENSITIVITY_MAP.put(4, 0.02112676f);
        MathUtils.SENSITIVITY_MAP.put(5, 0.028169014f);
        MathUtils.SENSITIVITY_MAP.put(6, 0.028169017f);
        MathUtils.SENSITIVITY_MAP.put(7, 0.03521127f);
        MathUtils.SENSITIVITY_MAP.put(8, 0.04225352f);
        MathUtils.SENSITIVITY_MAP.put(9, 0.049295776f);
        MathUtils.SENSITIVITY_MAP.put(10, 0.049295772f);
        MathUtils.SENSITIVITY_MAP.put(11, 0.056338027f);
        MathUtils.SENSITIVITY_MAP.put(12, 0.06338028f);
        MathUtils.SENSITIVITY_MAP.put(14, 0.07042254f);
        MathUtils.SENSITIVITY_MAP.put(15, 0.07746479f);
        MathUtils.SENSITIVITY_MAP.put(16, 0.08450704f);
        MathUtils.SENSITIVITY_MAP.put(18, 0.09154929f);
        MathUtils.SENSITIVITY_MAP.put(19, 0.09859155f);
        MathUtils.SENSITIVITY_MAP.put(21, 0.1056338f);
        MathUtils.SENSITIVITY_MAP.put(22, 0.112676054f);
        MathUtils.SENSITIVITY_MAP.put(23, 0.11971831f);
        MathUtils.SENSITIVITY_MAP.put(25, 0.12676056f);
        MathUtils.SENSITIVITY_MAP.put(26, 0.13380282f);
        MathUtils.SENSITIVITY_MAP.put(28, 0.14084508f);
        MathUtils.SENSITIVITY_MAP.put(29, 0.14788732f);
        MathUtils.SENSITIVITY_MAP.put(30, 0.15492958f);
        MathUtils.SENSITIVITY_MAP.put(32, 0.16197184f);
        MathUtils.SENSITIVITY_MAP.put(33, 0.16901408f);
        MathUtils.SENSITIVITY_MAP.put(35, 0.17605634f);
        MathUtils.SENSITIVITY_MAP.put(36, 0.18309858f);
        MathUtils.SENSITIVITY_MAP.put(38, 0.19014084f);
        MathUtils.SENSITIVITY_MAP.put(39, 0.1971831f);
        MathUtils.SENSITIVITY_MAP.put(40, 0.20422535f);
        MathUtils.SENSITIVITY_MAP.put(42, 0.2112676f);
        MathUtils.SENSITIVITY_MAP.put(43, 0.21830986f);
        MathUtils.SENSITIVITY_MAP.put(45, 0.22535211f);
        MathUtils.SENSITIVITY_MAP.put(46, 0.23239437f);
        MathUtils.SENSITIVITY_MAP.put(47, 0.23943663f);
        MathUtils.SENSITIVITY_MAP.put(49, 0.24647887f);
        MathUtils.SENSITIVITY_MAP.put(50, 0.2535211f);
        MathUtils.SENSITIVITY_MAP.put(52, 0.26056337f);
        MathUtils.SENSITIVITY_MAP.put(53, 0.26760563f);
        MathUtils.SENSITIVITY_MAP.put(54, 0.2746479f);
        MathUtils.SENSITIVITY_MAP.put(56, 0.28169015f);
        MathUtils.SENSITIVITY_MAP.put(57, 0.28873238f);
        MathUtils.SENSITIVITY_MAP.put(59, 0.29577464f);
        MathUtils.SENSITIVITY_MAP.put(60, 0.3028169f);
        MathUtils.SENSITIVITY_MAP.put(61, 0.30985916f);
        MathUtils.SENSITIVITY_MAP.put(63, 0.31690142f);
        MathUtils.SENSITIVITY_MAP.put(64, 0.32394367f);
        MathUtils.SENSITIVITY_MAP.put(66, 0.3309859f);
        MathUtils.SENSITIVITY_MAP.put(67, 0.33802816f);
        MathUtils.SENSITIVITY_MAP.put(68, 0.34507042f);
        MathUtils.SENSITIVITY_MAP.put(70, 0.35211268f);
        MathUtils.SENSITIVITY_MAP.put(71, 0.35915494f);
        MathUtils.SENSITIVITY_MAP.put(73, 0.36619717f);
        MathUtils.SENSITIVITY_MAP.put(74, 0.37323943f);
        MathUtils.SENSITIVITY_MAP.put(76, 0.3802817f);
        MathUtils.SENSITIVITY_MAP.put(77, 0.38732395f);
        MathUtils.SENSITIVITY_MAP.put(78, 0.3943662f);
        MathUtils.SENSITIVITY_MAP.put(80, 0.40140846f);
        MathUtils.SENSITIVITY_MAP.put(81, 0.4084507f);
        MathUtils.SENSITIVITY_MAP.put(83, 0.41549295f);
        MathUtils.SENSITIVITY_MAP.put(84, 0.4225352f);
        MathUtils.SENSITIVITY_MAP.put(85, 0.42957747f);
        MathUtils.SENSITIVITY_MAP.put(87, 0.43661973f);
        MathUtils.SENSITIVITY_MAP.put(88, 0.44366196f);
        MathUtils.SENSITIVITY_MAP.put(90, 0.45070422f);
        MathUtils.SENSITIVITY_MAP.put(91, 0.45774648f);
        MathUtils.SENSITIVITY_MAP.put(92, 0.46478873f);
        MathUtils.SENSITIVITY_MAP.put(94, 0.471831f);
        MathUtils.SENSITIVITY_MAP.put(95, 0.47887325f);
        MathUtils.SENSITIVITY_MAP.put(97, 0.48591548f);
        MathUtils.SENSITIVITY_MAP.put(98, 0.49295774f);
        MathUtils.SENSITIVITY_MAP.put(100, 0.5f);
        MathUtils.SENSITIVITY_MAP.put(101, 0.5070422f);
        MathUtils.SENSITIVITY_MAP.put(102, 0.5140845f);
        MathUtils.SENSITIVITY_MAP.put(104, 0.52112675f);
        MathUtils.SENSITIVITY_MAP.put(105, 0.52816904f);
        MathUtils.SENSITIVITY_MAP.put(107, 0.53521127f);
        MathUtils.SENSITIVITY_MAP.put(108, 0.5422535f);
        MathUtils.SENSITIVITY_MAP.put(109, 0.5492958f);
        MathUtils.SENSITIVITY_MAP.put(111, 0.556338f);
        MathUtils.SENSITIVITY_MAP.put(112, 0.5633803f);
        MathUtils.SENSITIVITY_MAP.put(114, 0.57042253f);
        MathUtils.SENSITIVITY_MAP.put(115, 0.57746476f);
        MathUtils.SENSITIVITY_MAP.put(116, 0.58450705f);
        MathUtils.SENSITIVITY_MAP.put(118, 0.5915493f);
        MathUtils.SENSITIVITY_MAP.put(119, 0.59859157f);
        MathUtils.SENSITIVITY_MAP.put(121, 0.6056338f);
        MathUtils.SENSITIVITY_MAP.put(122, 0.6126761f);
        MathUtils.SENSITIVITY_MAP.put(123, 0.6197183f);
        MathUtils.SENSITIVITY_MAP.put(125, 0.62676054f);
        MathUtils.SENSITIVITY_MAP.put(126, 0.63380283f);
        MathUtils.SENSITIVITY_MAP.put(128, 0.64084506f);
        MathUtils.SENSITIVITY_MAP.put(129, 0.64788735f);
        MathUtils.SENSITIVITY_MAP.put(130, 0.6549296f);
        MathUtils.SENSITIVITY_MAP.put(132, 0.6619718f);
        MathUtils.SENSITIVITY_MAP.put(133, 0.6690141f);
        MathUtils.SENSITIVITY_MAP.put(135, 0.6760563f);
        MathUtils.SENSITIVITY_MAP.put(136, 0.6830986f);
        MathUtils.SENSITIVITY_MAP.put(138, 0.69014084f);
        MathUtils.SENSITIVITY_MAP.put(139, 0.6971831f);
        MathUtils.SENSITIVITY_MAP.put(140, 0.70422536f);
        MathUtils.SENSITIVITY_MAP.put(142, 0.7112676f);
        MathUtils.SENSITIVITY_MAP.put(143, 0.7183099f);
        MathUtils.SENSITIVITY_MAP.put(145, 0.7253521f);
        MathUtils.SENSITIVITY_MAP.put(146, 0.73239434f);
        MathUtils.SENSITIVITY_MAP.put(147, 0.7394366f);
        MathUtils.SENSITIVITY_MAP.put(149, 0.74647886f);
        MathUtils.SENSITIVITY_MAP.put(150, 0.75352114f);
        MathUtils.SENSITIVITY_MAP.put(152, 0.7605634f);
        MathUtils.SENSITIVITY_MAP.put(153, 0.76760566f);
        MathUtils.SENSITIVITY_MAP.put(154, 0.7746479f);
        MathUtils.SENSITIVITY_MAP.put(156, 0.7816901f);
        MathUtils.SENSITIVITY_MAP.put(157, 0.7887324f);
        MathUtils.SENSITIVITY_MAP.put(159, 0.79577464f);
        MathUtils.SENSITIVITY_MAP.put(160, 0.8028169f);
        MathUtils.SENSITIVITY_MAP.put(161, 0.80985916f);
        MathUtils.SENSITIVITY_MAP.put(163, 0.8169014f);
        MathUtils.SENSITIVITY_MAP.put(164, 0.8239437f);
        MathUtils.SENSITIVITY_MAP.put(166, 0.8309859f);
        MathUtils.SENSITIVITY_MAP.put(167, 0.8380282f);
        MathUtils.SENSITIVITY_MAP.put(169, 0.8450704f);
        MathUtils.SENSITIVITY_MAP.put(170, 0.85211265f);
        MathUtils.SENSITIVITY_MAP.put(171, 0.85915494f);
        MathUtils.SENSITIVITY_MAP.put(173, 0.86619717f);
        MathUtils.SENSITIVITY_MAP.put(174, 0.87323946f);
        MathUtils.SENSITIVITY_MAP.put(176, 0.8802817f);
        MathUtils.SENSITIVITY_MAP.put(177, 0.8873239f);
        MathUtils.SENSITIVITY_MAP.put(178, 0.8943662f);
        MathUtils.SENSITIVITY_MAP.put(180, 0.90140843f);
        MathUtils.SENSITIVITY_MAP.put(181, 0.9084507f);
        MathUtils.SENSITIVITY_MAP.put(183, 0.91549295f);
        MathUtils.SENSITIVITY_MAP.put(184, 0.92253524f);
        MathUtils.SENSITIVITY_MAP.put(185, 0.92957747f);
        MathUtils.SENSITIVITY_MAP.put(187, 0.9366197f);
        MathUtils.SENSITIVITY_MAP.put(188, 0.943662f);
        MathUtils.SENSITIVITY_MAP.put(190, 0.9507042f);
        MathUtils.SENSITIVITY_MAP.put(191, 0.9577465f);
        MathUtils.SENSITIVITY_MAP.put(192, 0.96478873f);
        MathUtils.SENSITIVITY_MAP.put(194, 0.97183096f);
        MathUtils.SENSITIVITY_MAP.put(195, 0.97887325f);
        MathUtils.SENSITIVITY_MAP.put(197, 0.9859155f);
        MathUtils.SENSITIVITY_MAP.put(198, 0.9929578f);
        MathUtils.SENSITIVITY_MAP.put(200, 1.0f);
        EXPANDER = Math.pow(2.0, 24.0);
        moveValues = new float[] { -0.98f, 0.0f, 0.98f };
    }
}
