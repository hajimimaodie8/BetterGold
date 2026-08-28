package com.hjmmd_8.bettergold;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

/**
 * 金钱贝模具配方：模具（未损坏）+ 1 金锭 → 1 金钱贝，模具耐久 -1。
 * 模具耐久耗尽（64）后消失。
 */
public class GoldenCowrieMoldRecipe extends CustomRecipe {

    public GoldenCowrieMoldRecipe(CraftingBookCategory category) {
        super(category);
    }

    /** 允许在配方书中显示（默认 CustomRecipe 是特殊配方，不出现在配方书） */
    @Override
    public boolean isSpecial() {
        return false;
    }

    /** 提供输入物品列表，供 JEI/配方书展示 */
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY,
                Ingredient.of(AllItems.GOLDEN_COWRIE_MOLD.get()), // 金钱贝模具
                Ingredient.of(Items.GOLD_INGOT)                  // 金锭
        );
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasMold = false;
        boolean hasIngot = false;
        boolean hasOther = false;
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.is(AllItems.GOLDEN_COWRIE_MOLD.get()) && stack.getDamageValue() < stack.getMaxDamage()) {
                hasMold = true;
            } else if (stack.is(Items.GOLD_INGOT)) {
                hasIngot = true;
            } else {
                hasOther = true;
            }
        }
        return hasMold && hasIngot && !hasOther;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        return new ItemStack(AllItems.GOLDEN_COWRIE.get());
    }

    /** 提供输出物品，供 JEI/配方书展示（CustomRecipe 默认返回空） */
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(AllItems.GOLDEN_COWRIE.get());
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.is(AllItems.GOLDEN_COWRIE_MOLD.get())) {
                ItemStack worn = stack.copy();
                worn.setDamageValue(worn.getDamageValue() + 1);
                if (worn.getDamageValue() >= worn.getMaxDamage()) {
                    remaining.set(i, ItemStack.EMPTY); // 模具耐久耗尽
                } else {
                    remaining.set(i, worn);            // 模具耐久 -1 后返还
                }
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AllRecipes.GOLDEN_COWRIE_MOLD_RECIPE.get();
    }
}
