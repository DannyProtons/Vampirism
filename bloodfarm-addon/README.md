# Vampirism Blood Farm

A companion mod for [Vampirism](https://github.com/TeamLapen/Vampirism) that adds sustainable blood farming through growable crops.

## Features

- **Blood Fruit**: A dark, vampiric fruit that can be grown on farmland
- **Blood Crop**: A growable plant (8 growth stages) that produces Blood Fruit
- **Blood Grinder Integration**: Blood Fruit works with Vampirism's Blood Grinder to produce impure blood
- **Edible**: Can be eaten raw for food (2 hunger, 0.3 saturation)
- **Blood Value**: Produces 20 impure blood when processed

## How It Works

1. Plant Blood Fruit on farmland (like seeds)
2. Wait for the crop to grow (8 stages, like wheat)
3. Harvest the mature crop to get Blood Fruit
4. Either:
   - Eat the fruit directly for food
   - Place in Blood Grinder to get 20 impure blood
   - Process through Blood Sieve to get pure blood

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.115 or higher
- Vampirism 1.10.0 or higher

## Installation

1. Install Vampirism mod and its dependencies
2. Place this mod's JAR file in your mods folder
3. Launch the game

Both mods work together - do NOT remove the base Vampirism mod!

## Building

```bash
./gradlew build
```

The compiled JAR will be in `build/libs/`

## Textures

**IMPORTANT**: This mod requires custom textures to display correctly. See `TEXTURES_NEEDED.txt` for details on what textures are needed and where to place them.

## Credits

- Built for Vampirism by maxanier and cheaterpaul
- Uses Vampirism's data map system for blood integration

## License

MIT License
