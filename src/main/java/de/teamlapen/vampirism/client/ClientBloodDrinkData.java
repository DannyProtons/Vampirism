package de.teamlapen.vampirism.client;

/**
 * Client-side storage for blood drink progression data
 */
public class ClientBloodDrinkData {
    private static int zombieCount = 0;
    private static int zombieTier = 0;
    private static int endermanCount = 0;
    private static int endermanTier = 0;
    private static int creeperCount = 0;
    private static int creeperTier = 0;

    public static void setData(int zCount, int zTier, int eCount, int eTier, int cCount, int cTier) {
        zombieCount = zCount;
        zombieTier = zTier;
        endermanCount = eCount;
        endermanTier = eTier;
        creeperCount = cCount;
        creeperTier = cTier;
    }

    public static int getZombieCount() {
        return zombieCount;
    }

    public static int getZombieTier() {
        return zombieTier;
    }

    public static int getEndermanCount() {
        return endermanCount;
    }

    public static int getEndermanTier() {
        return endermanTier;
    }

    public static int getCreeperCount() {
        return creeperCount;
    }

    public static int getCreeperTier() {
        return creeperTier;
    }
}
