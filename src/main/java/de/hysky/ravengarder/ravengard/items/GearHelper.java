package de.hysky.ravengarder.ravengard.items;

import de.hysky.ravengarder.utils.RavengardItemUtils;
import de.hysky.skyblocker.utils.container.ContainerAndInventorySolver;
import de.hysky.skyblocker.utils.container.SimpleContainerSolver;
import de.hysky.skyblocker.utils.render.gui.ColorHighlight;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ToFloatFunction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.ArrayList;
import java.util.List;

/// Highlights better gear in your inventory and containers
public class GearHelper extends SimpleContainerSolver implements ContainerAndInventorySolver {
    public GearHelper() {
        super(".*");
    }

    @Override
    public List<ColorHighlight> getColors(Int2ObjectMap<ItemStack> int2ObjectMap) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return List.of();

        ItemStack hand = player.getMainHandItem();
        ItemStack head = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        ItemStack legs = player.getItemBySlot(EquipmentSlot.LEGS);
        ItemStack feet = player.getItemBySlot(EquipmentSlot.FEET);

        List<ColorHighlight> highlights = new ArrayList<>();
        for (Int2ObjectMap.Entry<ItemStack> entry : int2ObjectMap.int2ObjectEntrySet()) {
            ItemStack stack = entry.getValue();
            if (RavengardItemUtils.isGreyed(stack).orElse(false)) continue;

            if (stack.get(DataComponents.EQUIPPABLE) instanceof Equippable equippable) {
                if (equippable.slot() == EquipmentSlot.HEAD && isBetter(head, stack, RavengardItemUtils::getDefense)
                        || equippable.slot() == EquipmentSlot.CHEST && isBetter(chest, stack, RavengardItemUtils::getDefense)
                        || equippable.slot() == EquipmentSlot.LEGS && isBetter(legs, stack, RavengardItemUtils::getDefense)
                        || equippable.slot() == EquipmentSlot.FEET && isBetter(feet, stack, RavengardItemUtils::getDefense)) {
                    highlights.add(ColorHighlight.green(entry.getIntKey()));
                }
            } else if (hand.get(DataComponents.WEAPON) != null && stack.get(DataComponents.WEAPON) != null) {
                if (isBetter(hand, stack, RavengardItemUtils::getDamage)) {
                    if (isBetter(hand, stack, RavengardItemUtils::getAttackSpeed)) {
                        highlights.add(ColorHighlight.green(entry.getIntKey()));
                    } else {
                        highlights.add(ColorHighlight.yellow(entry.getIntKey()));
                    }
                }
            }
        }

        return highlights;
    }

    private boolean isBetter(ItemStack current, ItemStack newStack, ToFloatFunction<ItemStack> keyExtractor) {
        return keyExtractor.applyAsFloat(newStack) > keyExtractor.applyAsFloat(current);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
