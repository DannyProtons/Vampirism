# Blood Farm Mod - Complete Summary

## What Was Created

A **standalone companion mod** for Vampirism that adds sustainable blood farming. This is NOT a patch to the base mod - it's a separate mod that works alongside Vampirism.

### Location
`/home/user/vampirism-bloodfarm/`

## Features

### Blood Fruit Item
- Edible food item (2 hunger, 0.3 saturation)
- Can be planted on farmland like seeds
- Produces **20 impure blood** when processed in Blood Grinder
- Tagged with `minecraft:meat` for automatic Vampirism integration

### Blood Crop Block
- Growable crop with 8 growth stages (like wheat)
- Grows on farmland
- Harvests to Blood Fruit when mature
- Uses vanilla crop mechanics

## How Integration Works

### No Code Modification Required!

The mod uses **data-driven integration**:

1. **Data Maps** (`data/bloodfarm/data_maps/item/vampirism_item_blood.json`)
   - Explicitly tells Vampirism: Blood Fruit = 20 blood
   
2. **Item Tags** (`data/minecraft/tags/item/meat.json`)
   - Tags Blood Fruit as meat
   - Vampirism auto-calculates: nutrition × 10 = 2 × 10 = 20 blood

3. **Loot Tables** (`data/bloodfarm/loot_table/blocks/blood_crop.json`)
   - Defines crop drops
   - Fortune enchantment support

## Installation for Users

1. Install Vampirism mod (1.10.0+)
2. Install this mod
3. Both mods work together - **do NOT remove Vampirism!**

## Project Structure

```
vampirism-bloodfarm/
├── src/main/
│   ├── java/com/bloodfarm/
│   │   ├── BloodFarmMod.java          # Main mod class
│   │   ├── blocks/
│   │   │   └── BloodCropBlock.java    # Crop implementation
│   │   └── registry/
│   │       ├── ModBlocks.java         # Block registration
│   │       └── ModItems.java          # Item registration
│   └── resources/
│       ├── META-INF/
│       │   └── neoforge.mods.toml     # Mod metadata + Vampirism dependency
│       ├── assets/bloodfarm/
│       │   ├── blockstates/           # Crop growth stages
│       │   ├── models/                # Item & block models
│       │   ├── textures/              # ⚠️ EMPTY - needs textures!
│       │   └── lang/en_us.json        # English translations
│       └── data/
│           ├── bloodfarm/
│           │   ├── data_maps/         # Blood value integration
│           │   └── loot_table/        # Crop drops
│           └── minecraft/tags/        # Meat tag for auto-integration
├── build.gradle                        # NeoForge build config
├── gradle.properties                   # Mod metadata & versions
├── settings.gradle                     # Gradle settings
├── README.md                           # User documentation
├── INTEGRATION_GUIDE.md                # Technical integration details
├── TEXTURES_NEEDED.txt                 # Texture requirements
├── BUILD_INSTRUCTIONS.md               # Build guide
└── LICENSE                             # MIT License
```

## Technical Specs

- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.115+
- **Vampirism**: 1.10.0+
- **Language**: Java 21
- **Build System**: Gradle with NeoGradle/ModDevGradle

## Next Steps

### Before Building:

1. **Add Gradle Wrapper** (required):
   ```bash
   cd /home/user/vampirism-bloodfarm
   gradle wrapper --gradle-version 8.10.2
   ```

2. **Create Textures** (optional but recommended):
   - See `TEXTURES_NEEDED.txt` for requirements
   - 9 textures needed (1 fruit + 8 crop stages)
   - 16x16 pixels, dark vampiric theme

### Building:

```bash
cd /home/user/vampirism-bloodfarm
./gradlew build
```

Output: `build/libs/bloodfarm-1.0.0.jar`

### Testing:

```bash
./gradlew runClient
```

## Why This Approach Works

### Advantages:
✅ No mixins or core mods  
✅ No Vampirism code modification  
✅ Pure data-driven integration  
✅ Compatible with future Vampirism updates  
✅ Can be distributed separately  
✅ No conflicts with base mod  

### How Vampirism Reads Our Data:
1. NeoForge loads all data maps at startup
2. Vampirism's `BloodConversionRegistry` checks:
   - Data maps first (our explicit value)
   - Then tag-based auto-calculation (our fallback)
3. When Blood Fruit enters grinder → registry returns 20 blood
4. Grinder creates 20mb impure blood fluid
5. Blood sieve processes it normally

## Customization

Users can modify blood value by editing:
`data/bloodfarm/data_maps/item/vampirism_item_blood.json`

Change `"blood": 20` to any value (in millibuckets).

## Credits

Built for Vampirism by TeamLapen (maxanier & cheaterpaul)

---

**Status**: ✅ Complete - Ready for testing (needs textures)
