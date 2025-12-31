# Blood Crop Growth Information

## Growth Timing

Blood Crops use Minecraft's standard crop growth mechanics with **random ticks**.

### How Random Ticks Work

- Minecraft runs 3 random ticks per chunk section (16×16×16) per game tick
- Each random tick has a chance to advance crop growth by 1 stage
- This creates natural variation in growth time

### Average Growth Time

**Without Modifiers:**
- Estimated time to full maturity: **20-40 minutes** of active gameplay
- Per stage: approximately **4-8 minutes**
- Total stages: 5 (ages 0-4)

**Factors that affect growth speed:**

1. **Light Level**: Crops need light level ≥ 9 to grow
2. **Hydrated Farmland**: Must be planted on hydrated farmland
3. **Neighboring Blocks**: Clear area above crop (no blocks blocking)
4. **Tick Speed**: Game rule `randomTickSpeed` (default: 3)
   - Increase for faster testing: `/gamerule randomTickSpeed 50`

### Comparison to Vanilla Crops

| Crop | Stages | Average Growth Time |
|------|--------|---------------------|
| Wheat | 8 | 25-35 minutes |
| Beetroot | 4 | 20-30 minutes |
| **Blood Crop** | **5** | **20-40 minutes** |
| Carrots/Potatoes | 8 | 25-35 minutes |

Blood Crop grows at a similar rate to beetroot but yields more items (2-4 vs beetroot's 1).

### Optimizing Growth Speed

**Best Setup:**
```
1. Plant on hydrated farmland (water within 4 blocks)
2. Ensure light level ≥ 9 (torches, glowstone, etc.)
3. Keep area above crop clear
4. Optional: Use bone meal for instant growth (if enabled)
```

**For Testing:**
```
/gamerule randomTickSpeed 100
```
This makes crops grow MUCH faster for development/testing.

### Bone Meal Support

By default, Blood Crop **can** be bone mealed (inherits from `CropBlock`).

- Each bone meal use has a chance to advance 1 stage
- Full growth from seed requires approximately 5-8 bone meal

To disable bone meal, we'd need to override the `isValidBonemealTarget()` method.

## Yield Information

### Base Harvest (No Fortune)
- **2-4 Blood Fruits** per mature crop (randomized)
- Total blood value: 40-80mb impure blood

### With Fortune Enchantment
- Fortune I: +0-2 extra fruits
- Fortune II: +0-4 extra fruits
- Fortune III: +0-6 extra fruits

**Maximum possible yield:** 10 Blood Fruits (4 base + 6 Fortune III) = 200mb impure blood!

### Blood Value Breakdown
- 1 Blood Fruit = 20mb impure blood
- Average harvest (3 fruits) = 60mb impure blood
- After blood sieve (75% conversion): 45mb pure blood
- Pure blood feeds a vampire for ~2 hunger points

## Growth Rate Formula

The exact probability of growth per random tick:

```
growth_chance = (floor((25.0 / grow_speed_modifier) + 1))

Where grow_speed_modifier depends on:
- Neighboring crops (penalty for too many neighbors)
- Moisture level of farmland
- Biome (some biomes have modifiers)
```

For most cases with proper setup:
- ~1/3 chance per random tick to advance stage
- Average ~40 random ticks per stage
- At 3 random ticks per chunk per game tick
- At 20 game ticks per second
- = ~20-40 minutes to full maturity (varies by chunk activity)

## Performance Notes

Large blood crop farms (100+ plants):
- Minimal performance impact
- Random ticks are distributed across chunks
- Same performance as vanilla wheat farms

Recommended farm size: 9×9 = 81 crops per water source block
