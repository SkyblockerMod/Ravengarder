package de.hysky.mixins;

import de.hysky.utils.container.RavengarderContainerSolverManager;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @Inject(method = "setItem", at = @At("RETURN"))
    private void setItem(int slot, ItemStack itemStack, CallbackInfo ci) {
        RavengarderContainerSolverManager.markHighlightsDirty();
    }
}
