package com.hjmmd_8.bettergold;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

/**
 * 饮品物品：使用动画为 DRINK（像喝药水一样，冒饮品粒子而不是食物粒子）；
 * 饮用后返还玻璃瓶（仿原版药水逻辑）。
 */
public class DrinkItem extends Item {

    public DrinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32; // 饮用时长（与药水一致）
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // 应用食物效果（饥饿值 + 抗寒等 FoodProperties effects）并消耗物品
        ItemStack result = super.finishUsingItem(stack, level, entity);
        // 返还玻璃瓶（仿原版药水：非创造模式喝完给回空瓶）
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            if (result.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (!player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE))) {
                player.drop(new ItemStack(Items.GLASS_BOTTLE), false);
            }
        }
        return result;
    }
}
