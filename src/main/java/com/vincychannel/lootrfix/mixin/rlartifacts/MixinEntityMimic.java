package com.vincychannel.lootrfix.mixin.rlartifacts;

import artifacts.common.ModConfig;

import com.vincychannel.lootrfix.access.LootrChestTEAccessor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import noobanidus.mods.lootr.block.tile.LootrChestTileEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
        targets = "artifacts.common.entity.EntityMimic$MimicEventHandler",
        remap = false
)
public abstract class MixinEntityMimic {

    @Inject(
            method = "onRightClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void lootrFixes$onRightClick(
            PlayerInteractEvent.RightClickBlock event,
            CallbackInfo ci
    ) {
        if (event.getUseBlock() == Event.Result.DENY || event.getWorld().isRemote || event.getEntityPlayer() == null
                || ModConfig.general.unlootedChestMimicRatio <= (double)0.0F) {
            return;
        }

        TileEntity te = event.getWorld().getTileEntity(event.getPos());
        if (!(te instanceof TileEntityChest)) return;

        EntityPlayer player = event.getEntityPlayer();
        if (player == null) return;

        TileEntityChest chest = (TileEntityChest) te;

        if (lootrFixes$isLootrChestAlreadyOpened(chest)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "onBlockBreak",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void lootrFixes$onBlockBreak(
            BlockEvent.BreakEvent event, CallbackInfo ci
    ) {
        if (event.getWorld().isRemote && event.getPlayer() == null
                && ModConfig.general.unlootedChestMimicRatio <= (double)0.0F) {
            return;
        }

        TileEntity te = event.getWorld().getTileEntity(event.getPos());
        if (!(te instanceof TileEntityChest)) return;

        EntityPlayer player = event.getPlayer();
        if (player == null) return;

        TileEntityChest chest = (TileEntityChest) te;

        if (lootrFixes$isLootrChestAlreadyOpened(chest)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean lootrFixes$isLootrChestAlreadyOpened(TileEntityChest chest) {
        if (!(chest instanceof LootrChestTileEntity)) return false;

        LootrChestTileEntity lootrChest = (LootrChestTileEntity) chest;
        if (!(lootrChest instanceof LootrChestTEAccessor)) return false;

        return ((LootrChestTEAccessor) lootrChest).lootrFixes$hasBeenOpenedOnce();
    }
}
