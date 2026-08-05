package com.anatolyonhardmode.russiansurvival.cold;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class ColdBalanceTest {
    @Test void constantSnowCreatesAShorterOpening() {
        double seconds = ColdBalance.secondsToCritical(20, 0.62, 1.35);
        assertTrue(seconds >= 90 && seconds <= 105, "snowy opening was " + seconds + " seconds");
    }

    @Test void stormWindowStaysHarshButPlayable() {
        double seconds = ColdBalance.secondsToCritical(20, 0.62, 1.75);
        assertTrue(seconds >= 70 && seconds <= 80, "storm opening was " + seconds + " seconds");
    }

    @Test void coldLevelIsBounded() {
        assertEquals(0, ColdBalance.clamp(-5));
        assertEquals(100, ColdBalance.clamp(111));
        assertEquals(42.5F, ColdBalance.clamp(42.5F));
    }
}
