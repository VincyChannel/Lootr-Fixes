package com.vincychannel.lootrfix.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;

import noobanidus.mods.lootr.block.TrophyBlock;

import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;

@Mixin(TrophyBlock.class)
public class MixinTrophyBlock extends Block {
    public MixinTrophyBlock(Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean causesSuffocation(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(@Nonnull IBlockState state) {
        return false;
    }
}
