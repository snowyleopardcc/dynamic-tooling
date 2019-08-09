package net.dynamic.tooling.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EnchantsHandler {

    private static Enchantment KnockBackExtended;

    public static void init() {

        KnockBackExtended = Registry.register(
                Registry.ENCHANTMENT,
                new Identifier("dynamictooling", "knockback_extended"),
                new KnockBackExtended(
                        Enchantment.Weight.UNCOMMON,
                        EquipmentSlot.MAINHAND
                )
        );

    }
}
