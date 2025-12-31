package com.bloodfarm.registry;

import com.bloodfarm.BloodFarmMod;
import com.bloodfarm.blocks.BloodCropBlock;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(BloodFarmMod.MODID);

    // Blood Crop - the growing plant
    public static final DeferredBlock<Block> BLOOD_CROP = BLOCKS.register("blood_crop",
            BloodCropBlock::new);
}
