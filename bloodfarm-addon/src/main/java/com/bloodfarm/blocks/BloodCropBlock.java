package com.bloodfarm.blocks;

import com.bloodfarm.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * Blood Crop - A dark, vampiric plant that grows blood-rich fruits
 * Grows in 5 stages (faster than wheat's 8)
 */
public class BloodCropBlock extends CropBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),   // Stage 0: tiny sprout
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),   // Stage 1: small plant
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),  // Stage 2: growing
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D),  // Stage 3: budding
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)   // Stage 4: mature with fruits
    };

    public BloodCropBlock() {
        super(Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .pushReaction(PushReaction.DESTROY)
                .sound(SoundType.CROP));
    }

    @Override
    public int getMaxAge() {
        return 4; // 5 stages: 0-4
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ModItems.BLOOD_FRUIT.get();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }
}
