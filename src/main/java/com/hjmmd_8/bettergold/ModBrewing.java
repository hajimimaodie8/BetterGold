package com.hjmmd_8.bettergold;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

/**
 * 酿造配方注册。
 *
 * 1. 金酿热可可：金钱巧克力棒 + 玻璃瓶 → 金酿热可可（清除负面 + 3 分钟抗寒性）
 * 2. 万坚金酿热可可：万坚金巧克力棒 + 玻璃瓶 → 万坚金酿热可可（清除负面 + 16 分钟抗寒性）
 * 3. 万坚金胡萝卜：金酿热可可 + 万坚金胡萝卜 → 16 分钟夜视药水
 * 4. 万坚金冰淇淋：金酿热可可 + 万坚金冰淇淋 → 16 分钟抗火药水
 * 5. 万坚金甘蔗棒：金酿热可可 + 万坚金甘蔗棒 → 6 分钟迅捷 3 药水
 * 6. 万坚金钱茄：金酿热可可 + 万坚金钱茄 → 6 分钟力量 3 药水
 *
 * 注意：这些是物品级酿造（输入物品 + 材料 → 输出物品），通过 NeoForge 的
 * {@code RegisterBrewingRecipesEvent} 注册（MOD 总线）。
 */
public class ModBrewing {

    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        var builder = event.getBuilder();

        // 金酿热可可：金钱巧克力棒 + 玻璃瓶（容器）→ 金酿热可可
        // 容器配方：把玻璃瓶作为"基底"不行（玻璃瓶是原版药水容器），
        // 这里用 addRecipe(输入物品, 酿造材料, 输出)：金钱巧克力棒 + 玻璃瓶 → 金酿热可可
        builder.addRecipe(
                Ingredient.of(AllItems.GOLDEN_CHOCOLATE_BAR.get()),
                Ingredient.of(Items.GLASS_BOTTLE),
                new ItemStack(AllItems.BREWED_HOT_COCOA.get()));

        // 万坚金酿热可可：万坚金巧克力棒 + 玻璃瓶 → 万坚金酿热可可
        builder.addRecipe(
                Ingredient.of(AllItems.STURDYGOLD_CHOCOLATE_BAR.get()),
                Ingredient.of(Items.GLASS_BOTTLE),
                new ItemStack(AllItems.STURDYGOLD_BREWED_HOT_COCOA.get()));

        // 万坚金胡萝卜：以金酿热可可为基底，加万坚金胡萝卜 → 16 分钟夜视药水
        builder.addRecipe(
                Ingredient.of(AllItems.BREWED_HOT_COCOA.get()),
                Ingredient.of(AllItems.STURDYGOLD_CARROT.get()),
                makePotion(net.minecraft.world.item.alchemy.Potions.LONG_NIGHT_VISION));

        // 万坚金冰淇淋：以金酿热可可为基底，加万坚金冰淇淋 → 16 分钟抗火药水
        builder.addRecipe(
                Ingredient.of(AllItems.BREWED_HOT_COCOA.get()),
                Ingredient.of(AllItems.STURDYGOLD_ICE_CREAM.get()),
                makePotion(net.minecraft.world.item.alchemy.Potions.LONG_FIRE_RESISTANCE));

        // 万坚金甘蔗棒：以金酿热可可为基底，加万坚金甘蔗棒 → 6 分钟迅捷 3 药水
        builder.addRecipe(
                Ingredient.of(AllItems.BREWED_HOT_COCOA.get()),
                Ingredient.of(AllItems.STURDYGOLD_SUGAR_CANE_STICK.get()),
                makePotion(net.minecraft.world.item.alchemy.Potions.STRONG_SWIFTNESS));

        // 万坚金钱茄：以金酿热可可为基底，加万坚金钱茄 → 6 分钟力量 3 药水
        builder.addRecipe(
                Ingredient.of(AllItems.BREWED_HOT_COCOA.get()),
                Ingredient.of(AllItems.STURDYGOLD_EGGPLANT.get()),
                makePotion(net.minecraft.world.item.alchemy.Potions.STRONG_STRENGTH));
    }

    /** 构造原版药水物品 */
    private static ItemStack makePotion(net.minecraft.core.Holder<net.minecraft.world.item.alchemy.Potion> potion) {
        ItemStack stack = new ItemStack(Items.POTION);
        stack.set(net.minecraft.core.component.DataComponents.POTION_CONTENTS,
                new net.minecraft.world.item.alchemy.PotionContents(potion));
        return stack;
    }
}
