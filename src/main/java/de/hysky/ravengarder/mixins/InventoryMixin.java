package de.hysky.ravengarder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import de.hysky.ravengarder.utils.container.RavengarderContainerSolverManager;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
	@Inject(method = "setItem", at = @At("RETURN"))
	private void setItem(int slot, ItemStack itemStack, CallbackInfo ci) {
		RavengarderContainerSolverManager.markHighlightsDirty();
	}
}
