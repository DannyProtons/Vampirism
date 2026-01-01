# Vampirism Blood Farm

A companion mod for [Vampirism](https://github.com/TeamLapen/Vampirism) that adds sustainable blood farming through growable crops.

## Features

- **Blood Fruit**: A dark, vampiric fruit that produces blood when processed
- **Blood Crop**: A growable plant (5 growth stages) with purple foliage
- **Wild Spawns**: Find mature crops naturally in Vampire Forest biome
- **Blood Grinder Integration**: Process fruits for 20mb impure blood each
- **NOT Edible**: Must use Blood Grinder (vampires can't eat regular food!)
- **Balanced Yield**: 1-4 fruits per harvest (Fortune increases chance of 4)

## How It Works

1. **Find Wild Crops** in Vampire Forest (easiest method!)
   - OR find in Vampire Hut/Altar chests
2. **Harvest** mature crops for 1-4 Blood Fruits
3. **Craft** Blood Fruit → Blood Fruit (1:1) to get seeds
4. **Plant** on farmland
5. **Wait** ~20-40 minutes for growth
6. **Process** in Blood Grinder → 20mb impure blood per fruit
7. **Blood Sieve** → pure blood for vampires

## Acquisition Methods

### 🌱 Wild Blood Crops (BEST for early game!)
- **Where**: Vampire Forest biome (on grass blocks)
- **State**: Already mature - ready to harvest!
- **Yield**: 1-4 fruits immediately
- **Difficulty**: Very Easy ✅

### 🍖 Crafting Recipe
- **Recipe**: Blood Fruit → Blood Fruit (1:1 shapeless)
- **Use**: Convert harvested fruits into seeds

### 🏚️ Structure Loot
- **Vampire Hut Chests**: 15% chance, 1-3 fruits
- **Vampire Altar Chests**: 25% chance, 2-5 fruits

## Requirements

- Minecraft 1.21
- NeoForge 21.0.143 or higher
- Vampirism 1.10.0 or higher

## Installation

1. Install Vampirism mod and its dependencies
2. Place this mod's JAR file in your mods folder
3. Launch the game

Both mods work together - do NOT remove the base Vampirism mod!

## Yield & Fortune

**Base Harvest**: 1-4 Blood Fruits (random, capped at 4)

**Fortune Enchantment**: Increases chance of getting higher yields
- Fortune I: Better chance of 3-4 fruits
- Fortune II: Even better chance of max yield
- Fortune III: Best chance of 4 fruits

**Max Yield**: 4 fruits = 80mb impure blood → 60mb pure blood (after sieve)

## Growth Details

- **Stages**: 5 (ages 0-4)
- **Growth Time**: ~20-40 minutes with random ticks
- **Bone Meal**: Supported ✅
- **Requires**: Farmland, light level ≥9, water within 4 blocks

## Blood Production

**Per Fruit**: 20mb impure blood
**Average Harvest (2.5 fruits)**: 50mb impure → 37.5mb pure
**Max Harvest (4 fruits)**: 80mb impure → 60mb pure

**Farm Efficiency**: A 9×9 farm yields ~2,025mb impure blood per harvest cycle!

## Why This Mod?

**Problem**: Early vampires struggle with blood management
**Solution**: Sustainable, renewable blood source through farming

**Benefits**:
- No need to hunt animals constantly
- Works in Vampire Forest (your home biome!)
- Scales with Fortune for late-game efficiency
- Integrates seamlessly with existing Blood Grinder

## Building

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`

## Credits

- Built for Vampirism by maxanier and cheaterpaul
- Uses Vampirism's data map system for blood integration
- Purple foliage matches Vampire Forest aesthetic

## License

MIT License
