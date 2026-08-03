package de.hysky.utils;

import de.hysky.skyblocker.utils.ItemUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RavengardItemUtils {
    public static final Pattern DAMAGE = Pattern.compile(RavengardIcons.DAMAGE + "(?<damage>[\\d,.]+) Damage");
    public static final Pattern DEFENSE = Pattern.compile(RavengardIcons.DEFENSE + "(?<defense>[\\d,.]+) Defense");
    public static final Pattern ATTACK_SPEED = Pattern.compile(RavengardIcons.ATTACK_SPEED + "(?<attackSpeed>[\\d,.]+) Attack Speed");

    public static Optional<Boolean> isGreyed(ItemStack stack) {
        Identifier itemModel = stack.get(DataComponents.ITEM_MODEL);
        if (itemModel == null) return Optional.empty();
        return Optional.of(itemModel.getPath().endsWith("_greyed"));
    }

    public static float getDamage(ItemStack stack) {
        return getFloatStat(stack, DAMAGE);
    }

    public static float getDefense(ItemStack stack) {
        return getFloatStat(stack, DEFENSE);
    }

    public static float getAttackSpeed(ItemStack stack) {
        return getFloatStat(stack, ATTACK_SPEED);
    }

    public static float getFloatStat(ItemStack stack, Pattern pattern) {
        Matcher matcher = ItemUtils.getLoreLineIfContainsMatch(stack, pattern);
        return matcher != null ? Float.parseFloat(matcher.group(1)) : 0;
    }
}
