package com.hjmmd_8.bettergold.recipe;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

/**
 * 自定义酿造配方：匹配"指定效果的普通药水 + 指定材料 → 输出药水"。
 *
 * 用于万坚金食物升级原版药水：
 * - 夜视药水 + 万坚金胡萝卜 → 16 分钟夜视药水
 * - 抗火药水 + 万坚金冰淇淋 → 16 分钟抗火药水
 * - 迅捷药水 + 万坚金甘蔗棒 → 6 分钟迅捷 3 药水
 * - 力量药水 + 万坚金钱茄 → 6 分钟力量 3 药水
 */
public class PotionUpgradeBrewingRecipe implements IBrewingRecipe {

    private final Holder<Potion> inputPotion;   // 基底药水效果（如夜视）
    private final Ingredient ingredient;          // 酿造材料（万坚金食物）
    private final Holder<Potion> outputPotion;    // 输出药水效果

    public PotionUpgradeBrewingRecipe(Holder<Potion> inputPotion, Ingredient ingredient, Holder<Potion> outputPotion) {
        this.inputPotion = inputPotion;
        this.ingredient = ingredient;
        this.outputPotion = outputPotion;
    }

    /** 是否为基底：普通药水且效果匹配指定药水 */
    @Override
    public boolean isInput(ItemStack stack) {
        if (!stack.is(Items.POTION)) {
            return false;
        }
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        return contents != null && contents.potion().isPresent()
                && contents.potion().get().is(inputPotion);
    }

    @Override
    public boolean isIngredient(ItemStack stack) {
        return ingredient.test(stack);
    }

    /** 输出：带指定效果的普通药水（保留容器为普通药水） */
    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredientStack) {
        if (!isInput(input) || !isIngredient(ingredientStack)) {
            return ItemStack.EMPTY;
        }
        ItemStack out = new ItemStack(Items.POTION);
        out.set(DataComponents.POTION_CONTENTS, new PotionContents(outputPotion));
        return out;
    }
}
