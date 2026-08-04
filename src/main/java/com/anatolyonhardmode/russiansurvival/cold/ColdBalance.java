package com.anatolyonhardmode.russiansurvival.cold;

public final class ColdBalance {
    public static double secondsToCritical(double startingCold, double gainPerSecond, double weatherMultiplier) {
        if (gainPerSecond <= 0 || weatherMultiplier <= 0) return Double.POSITIVE_INFINITY;
        return Math.max(0, 100.0 - startingCold) / (gainPerSecond * weatherMultiplier);
    }
    public static float clamp(float value) { return Math.max(0, Math.min(100, value)); }
    private ColdBalance() {}
}
