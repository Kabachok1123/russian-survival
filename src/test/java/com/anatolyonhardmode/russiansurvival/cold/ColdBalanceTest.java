package com.anatolyonhardmode.russiansurvival.cold;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class ColdBalanceTest {
    @Test void calmSurfaceGivesThreeMinuteOpening() {
        double seconds = ColdBalance.secondsToCritical(20, 0.44, 1.0);
        assertTrue(seconds >= 150 && seconds <= 210, "calm opening was " + seconds + " seconds");
    }

    @Test void stormWindowStaysHarshButPlayable() {
        double seconds = ColdBalance.secondsToCritical(20, 0.44, 1.75);
        assertTrue(seconds >= 90 && seconds <= 130, "storm opening was " + seconds + " seconds");
    }

    @Test void coldLevelIsBounded() {
        assertEquals(0, ColdBalance.clamp(-5));
        assertEquals(100, ColdBalance.clamp(111));
        assertEquals(42.5F, ColdBalance.clamp(42.5F));
    }
}
