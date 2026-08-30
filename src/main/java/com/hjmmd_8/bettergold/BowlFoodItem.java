package com.hjmmd_8.bettergold;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * 碗装食物（金冰淇淋 / 万坚金冰淇淋）：
 * 饥饿值满格也能吃（alwaysEdible），吃完返还木碗（仿原版蘑菇煲逻辑）。
 */
public class BowlFoodItem extends Item {

    public BowlFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 应用食物效果（饥饿值 + 抗火等）并消耗物品
        ItemStack result = super.finishUsingItem(stack, level, entity);
        // 返还木碗（非创造模式）
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            if (result.isEmpty()) {
                return new ItemStack(Items.BOWL);
            }
            if (!player.getInventory().add(new ItemStack(Items.BOWL))) {
                player.drop(new ItemStack(Items.BOWL), false);
            }
        }
        return result;
    }
}
