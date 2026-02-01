package com.vincychannel.lootrfix.mixin.lootr;

import net.minecraft.entity.item.EntityMinecartContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.util.ChestUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(LootrChestMinecartEntity.class)
public abstract class MixinLootrChestMCartEnt extends EntityMinecartContainer {

    /**
     * @author VincyChannel
     * @reason Fixing empty cart container
     */
    @Overwrite(remap = false)
    public boolean processInitialInteract(EntityPlayer player, @Nonnull EnumHand hand) {
        if (player.isSneaking()) {
            ChestUtil.handleLootCartSneak(player.world, (LootrChestMinecartEntity) (Object)this, player);
        } else {
            ChestUtil.handleLootCart(player.world, (LootrChestMinecartEntity) (Object)this, player);
            super.processInitialInteract(player, hand);
        }

        return true;
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        IInteractionObject provider = DataStorage.getInventory(world, (LootrChestMinecartEntity)(Object)this, (EntityPlayerMP) playerIn, ((LootrChestMinecartEntity)(Object)this)::addLoot, this.getPosition());

        if (provider != null) {
            return new ContainerChest(playerInventory, (IInventory) provider, playerIn);
        } else {
            return new ContainerChest(playerInventory, this, playerIn);
        }
    }


    public MixinLootrChestMCartEnt(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getSizeInventory() {
        return 27;
    }

    @Override
    @Nonnull
    public Type getType() {
        return Type.CHEST;
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return "minecraft:chest";
    }
}
