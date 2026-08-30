package com.hjmmd_8.bettergold;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

/**
 * 酿造配方注册（NeoForge 支持任意物品作为酿造基底/材料）。
 *
 * 只保留热可可酿造（万坚金食物本身直接提供强化效果，不再通过酿造升级药水）：
 * 1. 金酿热可可：金钱巧克力棒（基底） + 玻璃瓶（材料） → 金酿热可可
 * 2. 万坚金酿热可可：金酿热可可（基底） + 万坚金巧克力棒（材料） → 万坚金酿热可可（16 分钟抗寒）
 */
public class ModBrewing {

    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();

        // 1. 金酿热可可：金钱巧克力棒 + 玻璃瓶 → 金酿热可可
        builder.addRecipe(
                Ingredient.of(AllItems.GOLDEN_CHOCOLATE_BAR.get()),
                Ingredient.of(Items.GLASS_BOTTLE),
                new ItemStack(AllItems.BREWED_HOT_COCOA.get()));

        // 2. 万坚金酿热可可：金酿热可可 + 万坚金巧克力棒 → 万坚金酿热可可
        builder.addRecipe(
                Ingredient.of(AllItems.BREWED_HOT_COCOA.get()),
                Ingredient.of(AllItems.STURDYGOLD_CHOCOLATE_BAR.get()),
                new ItemStack(AllItems.STURDYGOLD_BREWED_HOT_COCOA.get()));
    }
}
