package com.vincychannel.lootrfix.mixin.lootr;

import com.vincychannel.lootrfix.access.LootrChestTEAccessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import noobanidus.mods.lootr.block.tile.LootrChestTileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootrChestTileEntity.class)
public abstract class MixinLootrChestTileEntity implements LootrChestTEAccessor {

    @Unique
    private boolean lootrFixes$openedOnce = false;

    @Override
    public boolean lootrFixes$hasBeenOpenedOnce() {
        return this.lootrFixes$openedOnce;
    }

    @Inject(method = "readFromNBT", at = @At("RETURN"))
    private void lootrFixes$readOpenedTag(NBTTagCompound compound, CallbackInfo ci) {
        if (compound.hasKey("LootrOpenedOnce")) {
            this.lootrFixes$openedOnce = compound.getBoolean("LootrOpenedOnce");
        }
    }

    @Inject(method = "writeToNBT", at = @At("RETURN"), cancellable = true)
    private void lootrFixes$writeOpenedTag(NBTTagCompound compound, CallbackInfoReturnable<NBTTagCompound> cir) {
        compound.setBoolean("LootrOpenedOnce", this.lootrFixes$openedOnce);
        cir.setReturnValue(compound);
    }

    @Inject(method = "openInventory", at = @At("RETURN"))
    public void lootrFixes$openChestInventory(EntityPlayer player, CallbackInfo ci) {
        if (!this.lootrFixes$openedOnce) {
            this.lootrFixes$openedOnce = true;
        }
    }
}
