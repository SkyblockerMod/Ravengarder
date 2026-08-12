package de.hysky.ravengarder.mixins;

import de.hysky.ravengarder.utils.container.RavengarderContainerSolverManager;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/// Copied from Skyblocker ChestMenuMixin, but made to also work in {@link net.minecraft.client.gui.screens.inventory.InventoryScreen InventoryScreen}.
@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
	@Shadow
	public abstract void broadcastChanges();

	@Inject(method = "setItem", at = @At("RETURN"))
	public void setItem(int slot, int stateId, ItemStack itemStack, CallbackInfo ci) {
		RavengarderContainerSolverManager.markHighlightsDirty();
		broadcastChanges();
	}

	@Inject(method = "initializeContents", at = @At("RETURN"))
	public void initializeContents(int stateId, List<ItemStack> items, ItemStack carried, CallbackInfo ci) {
		RavengarderContainerSolverManager.markHighlightsDirty();
		broadcastChanges();
	}
}
