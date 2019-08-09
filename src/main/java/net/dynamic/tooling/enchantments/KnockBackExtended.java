package net.dynamic.tooling.enchantments;

import net.minecraft.enchantment.KnockbackEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;

public class KnockBackExtended extends KnockbackEnchantment {

    public KnockBackExtended(Weight enchantmentWeight, EquipmentSlot... equipmentSlots) {
        super(enchantmentWeight, equipmentSlots);
    }


    @Override
    public int getMinimumLevel() {
        return 3;
    }

    @Override
    public int getMaximumLevel() {
        return 6;
    }


    @Override
    public void onTargetDamaged(LivingEntity livingEntity, Entity target, int level) {

        if(target instanceof LivingEntity) {

//            (double) Math.pow(2.0, (double)level); //

            double distance = (3 * level);

            ((LivingEntity)target).takeKnockback(target, (float) distance, target.getBlockPos().getX(), target.getBlockPos().getZ());
        }

        super.onTargetDamaged(livingEntity, target, level);
    }
}
