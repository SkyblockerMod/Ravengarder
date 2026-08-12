package de.hysky.ravengarder.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import de.hysky.ravengarder.utils.RavengardInfo;
import de.hysky.ravengarder.utils.container.RavengarderContainerSolverManager;
import de.hysky.skyblocker.utils.container.ContainerSolver;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/// Copied from Skyblocker
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> extends Screen {
    @Shadow
    @Final
    protected T menu;

    protected AbstractContainerScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "extractTooltip", at = @At("HEAD"))
    private void beforeTooltipExtracted(CallbackInfo ci, @Local(name = "graphics", argsOnly = true) GuiGraphicsExtractor graphics) {
        RavengarderContainerSolverManager.onExtract(graphics, (AbstractContainerScreen<?>) (Object) this, menu.slots);
    }

    @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;handleContainerInput(IIILnet/minecraft/world/inventory/ContainerInput;Lnet/minecraft/world/entity/player/Player;)V"), cancellable = true)
    private void onSlotClick(@Nullable Slot slot, int slotId, int buttonNum, ContainerInput containerInput, CallbackInfo ci) {
        if (!RavengardInfo.isOnRavengard()) return;
        if (slot == null) return;

        ContainerSolver currentSolver = RavengarderContainerSolverManager.getCurrentSolver();
        ItemStack stack = slot.getItem();

        if (currentSolver != null) {
            boolean disallowed = RavengarderContainerSolverManager.onSlotClick(slotId, stack, buttonNum);

            if (disallowed) ci.cancel();
        }
    }
}
