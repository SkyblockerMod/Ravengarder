package de.hysky.utils.container;

import de.hysky.ravengard.items.GearHelper;
import de.hysky.skyblocker.mixins.accessors.AbstractContainerScreenAccessor;
import de.hysky.skyblocker.utils.container.ContainerAndInventorySolver;
import de.hysky.skyblocker.utils.container.ContainerSolver;
import de.hysky.skyblocker.utils.container.ContainerSolverManager;
import de.hysky.skyblocker.utils.container.RegexContainerMatcher;
import de.hysky.skyblocker.utils.render.gui.ColorHighlight;
import de.hysky.utils.RavengardInfo;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.List;

/// Copy of Skyblocker ContainerSolverManager
public class RavengarderContainerSolverManager {
    private static final ContainerSolver[] solvers = new ContainerSolver[]{
            new GearHelper()
    };
    private static @Nullable ContainerSolver currentSolver;
    private static @Nullable List<ColorHighlight> highlights;
    private static int screenId;

    public static @Nullable ContainerSolver getCurrentSolver() {
        return currentSolver;
    }

    public static void init() {
        ScreenEvents.BEFORE_INIT.register((_, screen, _, _) -> {
            if (RavengardInfo.isOnRavengard() && screen instanceof AbstractContainerScreen<?> containerScreen) {
                ScreenEvents.remove(screen).register(_ -> clearScreen());
                onSetScreen(containerScreen);
            } else {
                clearScreen();
            }
        });
    }

    public static void onSetScreen(AbstractContainerScreen<?> screen) {
        String screenName = screen.getTitle().getString();
        for (ContainerSolver solver : solvers) {
            if (solver.isEnabled()) {
                //Ignore the result of instanceof being always true.
                //This only happens because all solvers in the `solvers` array are SimpleContainerSolvers, which extend RegexContainerMatcher.
                //This may not be the case as more and more solvers are added.
                //Also don't merge this with the above `if`, the parenthesis mess gets hard to read. (java:S1066 for sonarlint users)
                if ((solver instanceof RegexContainerMatcher containerMatcher && containerMatcher.test(screenName)) || solver.test(screen)) {
                    ++screenId;
                    currentSolver = solver;
                    // Dirty hack to make container solvers work in the inventory screen, which is not a container screen
                    if (screen instanceof ContainerScreen containerScreen) currentSolver.start(containerScreen);
                    markHighlightsDirty();
                    return;
                }
            }
        }
        clearScreen();
    }

    public static void clearScreen() {
        if (currentSolver != null) {
            currentSolver.reset();
            currentSolver = null;
        }
    }

    public static void markHighlightsDirty() {
        highlights = null;

        if (currentSolver != null) {
            currentSolver.markDirty();
        }
    }

    /**
     * @return Whether the click should be disallowed.
     */
    public static boolean onSlotClick(int slot, ItemStack stack, int button) {
        return currentSolver != null && currentSolver.onClickSlot(slot, stack, screenId, button);
    }

    public static void onExtract(GuiGraphicsExtractor context, AbstractContainerScreen<?> handledScreen, List<Slot> slots) {
        if (currentSolver == null) return;

        context.pose().pushMatrix();
        context.pose().translate(((AbstractContainerScreenAccessor) handledScreen).getX(), ((AbstractContainerScreenAccessor) handledScreen).getY());

        if (!(currentSolver instanceof ContainerAndInventorySolver) && handledScreen.getMenu() instanceof ChestMenu chestMenu) {
            slots = slots.subList(0, chestMenu.getRowCount() * 9);
        }

        if (highlights == null) highlights = currentSolver.getColors(ContainerSolverManager.slotMap(slots));
        for (ColorHighlight highlight : highlights) {
            Slot slot = slots.get(highlight.slot());
            int color = highlight.color();
            context.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, color);
        }

        context.pose().popMatrix();
    }
}
