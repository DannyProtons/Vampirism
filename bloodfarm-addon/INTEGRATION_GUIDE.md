# Vampirism Integration Guide

This document explains how the Blood Farm mod integrates with Vampirism.

## Blood Grinder Integration

The Blood Farm mod uses Vampirism's existing data map system to make Blood Fruit work with the Blood Grinder.

### Method 1: Data Map (Explicit)
Located in: `src/main/resources/data/bloodfarm/data_maps/item/vampirism_item_blood.json`

```json
{
  "values": {
    "bloodfarm:blood_fruit": {
      "blood": 20
    }
  }
}
```

This explicitly tells Vampirism that Blood Fruit should give 20 impure blood when processed.

### Method 2: Meat Tag (Auto-calculation)
Located in: `src/main/resources/data/minecraft/tags/item/meat.json`

```json
{
  "replace": false,
  "values": [
    "bloodfarm:blood_fruit"
  ]
}
```

Additionally, the item has food properties:
- Nutrition: 2
- Saturation: 0.3

Vampirism's auto-calculation: `nutrition * 10 = 2 * 10 = 20 blood`

Both methods work together for redundancy.

## How Vampirism Processes Items

Based on `BloodGrinderBlockEntity.java`:

1. Item is placed in grinder (manually or via hopper)
2. Grinder checks `VampirismAPI.bloodConversionRegistry().getItemBlood(stack)`
3. Registry checks:
   - First: Data map entry
   - Fallback: If item has `minecraft:meat` tag and isn't cooked, calculate from nutrition
4. If blood value > 0:
   - Creates `FluidStack` of impure blood
   - Fills container below (Blood Container)
   - Plays grinding sound
   - Consumes item

## Blood Processing Chain

```
Blood Fruit → Blood Grinder → Impure Blood (20mb) → Blood Sieve → Pure Blood → Blood Container
```

Same as vanilla meats:
- Beef: 200 blood
- Pork/Mutton: 100 blood
- Blood Fruit: 20 blood (balanced as a renewable crop)

## Why This Works Without Modifying Vampirism

1. **Data Maps**: NeoForge's data map system allows mods to extend other mods' registries
2. **Item Tags**: Minecraft's tag system allows adding items to existing tags
3. **API-Based**: Vampirism uses `VampirismAPI.bloodConversionRegistry()` which checks all data sources

The addon doesn't need to modify Vampirism's code - it just provides data that Vampirism already knows how to read!

## Compatibility

- Works with any version of Vampirism 1.10.0+
- If Vampirism's data map system changes, only the JSON files need updating
- No mixins, no core mods, pure data-driven
