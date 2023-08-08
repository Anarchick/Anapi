package fr.anarchick.anapi.java;

import javax.annotation.Nonnull;
import java.util.Random;

public class PityChance {

    public enum BOUND {
        BOUND_1, BOUND_100
    }

    private static final Random RANDOM = new Random();
    private double chance, limit;
    private final double bound;
    private boolean hasSuccess = false;

    /**
     *
     * @param limit limit the cumulative chance to [0;limit], #hasSuccess() return true if #getChance() >= limit
     * @param bound determine if you use chance [0;1] or [0;100]
     */
    public PityChance(double limit, @Nonnull BOUND bound) {
        this.limit = limit;
        this.bound = (bound == BOUND.BOUND_1) ? 1d : 100d;
    }

    /**
     * The threshold of chance.
     * @return
     */
    public Double getLimit() {
        return limit;
    }

    /**
     * The threshold of chance.
     * Updating this value can mark #hasSuccess as true until using #reset
     * @param limit
     * @return
     */
    public boolean setLimit(double limit) {
        this.limit = limit;
        this.hasSuccess = hasSuccess();
        return hasSuccess;
    }

    /**
     * Always returns true if has success one time or more
     * @return
     */
    public boolean hasSuccess() {
        return hasSuccess || (chance >= limit);
    }

    /**
     * The cumulation of all #chance values
     * @return
     */
    @Nonnull
    public Double getChance() {
        return chance;
    }

    /**
     * Return true if already has success,
     * otherwise return the result of random chance of [0;bound] <= value
     * @param value
     * @return
     */
    public boolean chance(double value) {
        this.chance += value;
        if (hasSuccess) return true;
        this.hasSuccess = RANDOM.nextDouble(0, bound) <= value;
        return hasSuccess;
    }

    /**
     * reset #getChance to 0 and #hasSuccess to false;
     */
    public void reset() {
        this.chance = 0;
        this.hasSuccess = false;
    }

}
