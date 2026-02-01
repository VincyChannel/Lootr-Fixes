package com.vincychannel.lootrfix.mixin.lootr;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.networking.CloseCart;
import noobanidus.mods.lootr.networking.PacketHandler;
import noobanidus.mods.lootr.util.ChestUtil;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.UUID;

@Mixin(ChestUtil.class)
public class MixinChestUtil {

    /**
     * @author VincyChannel
     * @reason Fixing empty cart container
     */
    @Overwrite(remap = false)
    public static void handleLootCart(World world, LootrChestMinecartEntity cart, EntityPlayer player) {
        if (!world.isRemote) {
            if (player.isSpectator()) {
                player.closeScreen();
            } else {
                UUID tileId = cart.getUniqueID();
                if (DataStorage.isDecayed(tileId)) {
                    cart.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
                    DataStorage.removeDecayed(tileId);
                    player.sendStatusMessage((new TextComponentTranslation("lootr.message.decayed", new Object[0])).setStyle((new Style()).setColor(TextFormatting.RED).setBold(true)), true);
                    return;
                }

                int decayValue = DataStorage.getDecayValue(tileId);
                if (decayValue > 0 && ConfigManager.shouldNotify(decayValue)) {
                    player.sendStatusMessage((new TextComponentTranslation("lootr.message.decay_in", new Object[]{decayValue / 20})).setStyle((new Style()).setColor(TextFormatting.RED).setBold(true)), true);
                } else if (decayValue == -1 && ConfigManager.isDecaying(world, cart)) {
                    DataStorage.setDecaying(tileId, ConfigManager.getDecayValue());
                    player.sendStatusMessage((new TextComponentTranslation("lootr.message.decay_start", new Object[]{ConfigManager.getDecayValue() / 20})).setStyle((new Style()).setColor(TextFormatting.RED).setBold(true)), true);
                }

                if (!cart.getOpeners().contains(player.getUniqueID())) {
                    cart.addOpener(player);
                }

                if (DataStorage.isRefreshed(tileId)) {
                    DataStorage.refreshInventory(world, cart, (EntityPlayerMP)player, cart.getPosition());
                    DataStorage.removeRefreshed(tileId);
                    player.sendStatusMessage((new TextComponentTranslation("lootr.message.refreshed", new Object[0])).setStyle((new Style()).setColor(TextFormatting.BLUE).setBold(true)), true);
                }

                decayValue = DataStorage.getRefreshValue(tileId);
                if (decayValue > 0 && ConfigManager.shouldNotify(decayValue)) {
                    player.sendStatusMessage((new TextComponentTranslation("lootr.message.refresh_in", new Object[]{decayValue / 20})).setStyle((new Style()).setColor(TextFormatting.BLUE).setBold(true)), true);
                } else if (decayValue == -1 && ConfigManager.isRefreshing(world, cart)) {
                    DataStorage.setRefreshing(tileId, ConfigManager.getRefreshValue());
                    player.sendStatusMessage((new TextComponentTranslation("lootr.message.refresh_start", new Object[]{ConfigManager.getRefreshValue() / 20})).setStyle((new Style()).setColor(TextFormatting.BLUE).setBold(true)), true);
                }
            }
        }
    }

    /**
     * @author VincyChannel
     * @reason Fixing showing to everyone the un-opened state of the cart (only visual) [now it shows only to the player that shift + right click on it]
     */
    @Overwrite(remap = false)
    public static void handleLootCartSneak(World world, LootrChestMinecartEntity cart, EntityPlayer player) {
        if (!world.isRemote) {
            if (!player.isSpectator()) {
                cart.getOpeners().remove(player.getUniqueID());
                CloseCart open = new CloseCart(cart.getEntityId());
                EntityPlayerMP playerMP = (EntityPlayerMP)player;
                PacketHandler.sendToInternal(open, playerMP);
            }
        }
    }
}