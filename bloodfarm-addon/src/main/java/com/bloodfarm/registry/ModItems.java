package com.bloodfarm.registry;

import com.bloodfarm.BloodFarmMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(BloodFarmMod.MODID);

    // Blood Fruit - the harvestable item from the crop
    // NOT edible as food - vampires can't eat regular food!
    // Only works when processed in Blood Grinder to produce 20mb impure blood
    // Blood value defined in data map: data/bloodfarm/data_maps/item/vampirism_item_blood.json
    public static final DeferredItem<Item> BLOOD_FRUIT = ITEMS.register("blood_fruit",
            () -> new ItemNameBlockItem(ModBlocks.BLOOD_CROP.get(),
                    new Item.Properties()));
}
